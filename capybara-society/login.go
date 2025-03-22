package main

import (
	"crypto/rand"
	"fmt"
	"net/http"
	"time"

	sq "github.com/Masterminds/squirrel"
	"github.com/go-chi/chi/v5"
	"golang.org/x/crypto/bcrypt"
)

func LoginRouter() http.Handler {
	r := chi.NewRouter()
	r.Get("/", loginGetHandler)
	r.Post("/", loginPostHandler)
	return r
}

func loginGetHandler(w http.ResponseWriter, r *http.Request) {
	if r.Context().Value("user") != nil {
		http.Redirect(w, r, "/", 303)
		return
	}
	tmpl.Execute(w, r, "login", J{})
}

func loginPostHandler(w http.ResponseWriter, r *http.Request) {
	username := r.FormValue("username")
	var user User
	err := get(&user, sq.Select("*").From("users").Where(sq.Eq{"username": username}))
	if err != nil {
		tmpl.Execute(w, r, "login", J{"Error": "User not found"})
		fmt.Println(err)
		return
	}

	password := r.FormValue("password")
	err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(password))
	if err != nil {
		tmpl.Execute(w, r, "login", J{"Error": "Incorrect password"})
		return
	}

	tokenBytes := make([]byte, 32)
	rand.Read(tokenBytes)
	token := fmt.Sprintf("%x", tokenBytes)

	expiration := time.Now().Add(60 * 60 * 24 * 7 * time.Second)

	http.SetCookie(w, &http.Cookie{
		Name:    "auth",
		Value:   token,
		Expires: expiration,
	})

	err = exec(sq.Insert("sessions").Columns("token", "expiration", "user_id").Values(token, expiration, user.ID))
	if err != nil {
		tmpl.Execute(w, r, "login", J{"Error": "Server error"})
		return
	}

	redirect := r.URL.Query().Get("redirect")
	if len(redirect) == 0 {
		http.Redirect(w, r, "/", 303)
	} else {
		http.Redirect(w, r, redirect, 303)
	}
}

func SignOut(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("auth")
	defer http.Redirect(w, r, "/login", 303)
	if err != nil {
		return
	}
	exec(sq.Delete("sessions").Where(sq.Eq{"token": cookie.Value}))
}
