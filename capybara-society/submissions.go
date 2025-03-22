package main

import (
	"net/http"
	"strconv"

	sq "github.com/Masterminds/squirrel"
	"github.com/go-chi/chi/v5"
)

func SubmissionsRouter() http.Handler {
	r := chi.NewRouter()
	r.Get("/", submissionsGetHandler)
	r.Get("/row/{id:[0-9]+}", submissionRowHandler)
	return r
}

func submissionRowHandler(w http.ResponseWriter, r *http.Request) {
	var submission struct {
		Submission
		ProblemTitle string `db:"p_title"`
		PacketTitle  string `db:"ps_title"`
		PacketID     int    `db:"ps_id"`
		Username     string `db:"username"`
	}
	id := chi.URLParam(r, "id")
	err := get(&submission, sq.Select("s.id", "s.problem_id", "s.status", "s.submitted_at", "p.title as p_title", "ps.title as ps_title", "ps.id as ps_id", "username").From("submissions s").InnerJoin("problems p ON s.problem_id = p.id").InnerJoin("packets ps on ps.id = p.packet_id").InnerJoin("users u on u.id = s.user_id").Where(sq.Eq{"s.id": id}))
	if err != nil {
		tmpl.Execute(w, r, "internal_sever", J{})
		return
	}
	submission.SubmittedAt = submission.SubmittedAt.In(loc)
	if r.URL.Query().Get("exclude") != "" {
		submission.Username = ""
	}
	tmpl.Execute(w, r, "submission_row_handler", J{
		"Submission": submission,
	})
}

func submissionsGetHandler(w http.ResponseWriter, r *http.Request) {
	var submissions []struct {
		Submission
		ProblemTitle string `db:"p_title"`
		PacketTitle  string `db:"ps_title"`
		PacketID     int    `db:"ps_id"`
		Username     string `db:"username"`
	}

	var count int
	query := sq.Select("COUNT(*)").From("submissions s")
	if r.URL.Query().Get("username") != "" {
		query = query.InnerJoin("users u ON s.user_id = u.id").Where(sq.Eq{"u.username": r.URL.Query().Get("username")})
	}

	if r.URL.Query().Get("problem") != "" {
		query = query.InnerJoin("problems p ON p.id = s.problem_id").Where(sq.Eq{"p.id": r.URL.Query().Get("problem")})
	}

	if r.URL.Query().Get("accepted") != "" {
		query = query.Where(sq.Eq{"status": "Accepted"})
	}

	err := get(&count, query)
	if err != nil {
		tmpl.Execute(w, r, "internal_server", J{})
		return
	}

	offset, err := strconv.Atoi(r.URL.Query().Get("offset"))
	if err != nil || offset < 0 || offset >= count {
		offset = 0
	}

	query = sq.Select("s.id", "s.problem_id", "s.status", "s.submitted_at", "p.title as p_title", "ps.title as ps_title", "ps.id as ps_id", "username").From("submissions s").InnerJoin("problems p ON s.problem_id = p.id").InnerJoin("packets ps on ps.id = p.packet_id").InnerJoin("users u on u.id = s.user_id").OrderBy("s.id DESC").Limit(20).Offset(uint64(offset))
	if r.URL.Query().Get("username") != "" {
		query = query.Where(sq.Eq{"u.username": r.URL.Query().Get("username")})
	}

	if r.URL.Query().Get("problem") != "" {
		query = query.Where(sq.Eq{"p.id": r.URL.Query().Get("problem")})
	}

	if r.URL.Query().Get("accepted") != "" {
		query = query.Where(sq.Eq{"status": "Accepted"})
	}
	err = sel(&submissions, query)

	if err != nil {
		tmpl.Execute(w, r, "internal_server", J{})
		return
	}

	for i := 0; i < len(submissions); i++ {
		submissions[i].SubmittedAt = submissions[i].SubmittedAt.In(loc)
	}

	before := max(offset-20, -1)
	after := offset + 20
	if after >= count {
		after = -1
	}

	tmpl.Execute(w, r, "submissions", J{
		"Submissions": submissions,
		"Before":      before,
		"After":       after,
	})
}
