package main

import (
	"fmt"
	"net/http"
	"net/mail"
	"regexp"

	sq "github.com/Masterminds/squirrel"

	"github.com/go-chi/chi/v5"
	"golang.org/x/crypto/bcrypt"
)

var usernameRegex = regexp.MustCompile("^[A-Za-z0-9_-]+$")

func AccountsRouter() http.Handler {
	r := chi.NewRouter()
	r.Get("/{username:[A-Za-z0-9-_]+}", accountGetHandler)
	r.Group(func(r chi.Router) {
		r.Use(Authware)
		r.Get("/{username:[A-Za-z0-9-_]+}/edit", editAccountGetHandler)
		r.Post("/{username:[A-Za-z0-9-_]+}/edit", editAccountPostHandler)
	})
	return r
}

func editAccountGetHandler(w http.ResponseWriter, r *http.Request) {
	user := r.Context().Value("user").(User)
	username := chi.URLParam(r, "username")
	if user.Username != username {
		tmpl.Execute(w, r, "unauthorized", J{})
		return
	}
	tmpl.Execute(w, r, "account_edit", J{})
}

func editAccountPostHandler(w http.ResponseWriter, r *http.Request) {
	user := r.Context().Value("user").(User)
	username := chi.URLParam(r, "username")
	if user.Username != username {
		tmpl.Execute(w, r, "unauthorized", J{})
		return
	}

	currentPassword := r.FormValue("current_password")
	err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(currentPassword))
	if err != nil {
		tmpl.Execute(w, r, "account_edit", J{"Error": "Incorrect password"})
		return
	}

	newUsername := r.FormValue("username")
	if newUsername != user.Username {
		if !usernameRegex.MatchString(newUsername) {
			tmpl.Execute(w, r, "account_edit", J{"Error": "Username should match [A-Za-z0-9-_]+"})
			return
		}

		var id int
		err = get(&id, sq.Select("id").From("users").Where(sq.Eq{"username": newUsername}))
		if err == nil {
			tmpl.Execute(w, r, "account_edit", J{"Error": "Username already taken"})
			return
		}
	}

	newEmail := r.FormValue("email")
	if newEmail != user.Email {
		if _, err = mail.ParseAddress(newEmail); err != nil {
			tmpl.Execute(w, r, "account_edit", J{"Error": "Invalid email address"})
			return
		}

		var id int
		err = get(&id, sq.Select("id").From("users").Where(sq.Eq{"email": newEmail}))
		if err == nil {
			tmpl.Execute(w, r, "account_edit", J{"Error": "Email already taken"})
			return
		}
	}

	newPassword := r.FormValue("new_password")
	if newPassword == "" {
		newPassword = user.Password
	} else {
		bytes, err := bcrypt.GenerateFromPassword([]byte(newPassword), bcrypt.DefaultCost)
		if err != nil {
			tmpl.Execute(w, r, "account_edit", J{"Error": "Invalid password"})
			return
		}
		newPassword = string(bytes)
	}

	err = exec(sq.Update("users").Set("username", newUsername).Set("password", newPassword).Set("email", newEmail).Where(sq.Eq{"id": user.ID}))
	if err != nil {
		tmpl.Execute(w, r, "account_edit", J{"Error": "Internal server error"})
		return
	}

	http.Redirect(w, r, "/accounts/"+newUsername, 303)
}

func accountGetHandler(w http.ResponseWriter, r *http.Request) {
	var account User
	username := chi.URLParam(r, "username")
	err := get(&account, sq.Select("*").From("users").Where(sq.Eq{"username": username}))
	if err != nil {
		tmpl.Execute(w, r, "not_found", J{})
		return
	}

	var problemsSolved int
	err = get(&problemsSolved,
		sq.Select("COUNT(DISTINCT problem_id)").From("submissions").Where(sq.Eq{"user_id": account.ID}).Where(sq.Eq{"status": "Accepted"}))
	if err != nil {
		fmt.Println(err.Error())
		problemsSolved = 0
	}

	var submissions []struct {
		Submission
		ProblemTitle string `db:"p_title"`
		PacketTitle  string `db:"ps_title"`
		PacketID     int    `db:"ps_id"`
		Username     string
	}

	err = sel(&submissions, sq.Select("s.id", "s.problem_id", "s.status", "s.submitted_at", "p.title as p_title", "ps.title as ps_title", "ps.id as ps_id").From("submissions s").InnerJoin("problems p ON s.problem_id = p.id").InnerJoin("packets ps on ps.id = p.packet_id").Where(sq.Eq{"s.user_id": account.ID}).OrderBy("s.id DESC").Limit(10))
	if err != nil {
		fmt.Println(err)
		tmpl.Execute(w, r, "internal_server", J{})
		return
	}

	for i := 0; i < len(submissions); i++ {
		submissions[i].SubmittedAt = submissions[i].SubmittedAt.In(loc)
	}

	tmpl.Execute(w, r, "account", J{
		"Account":        account,
		"ProblemsSolved": problemsSolved,
		"Submissions":    submissions,
	})
}
