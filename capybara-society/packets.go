package main

import (
	"net/http"

	sq "github.com/Masterminds/squirrel"
	"github.com/go-chi/chi/v5"
)

func PacketsRouter() http.Handler {
	r := chi.NewRouter()
	r.Get("/", packetsGetHandler)
	r.Get("/{id:[0-9]+}", packetGetHandler)
	return r
}

func packetGetHandler(w http.ResponseWriter, r *http.Request) {
	idString := chi.URLParam(r, "id")
	var packet Packet
	err := get(&packet, sq.Select("*").From("packets").Where(sq.Eq{"id": idString}))
	if err != nil {
		tmpl.Execute(w, r, "not_found", J{})
		return
	}

	var problems []struct {
		Problem
		TotalSubmissions    int `db:"total_submissions"`
		AcceptedSubmissions int `db:"accepted_submissions"`
	}

	userID := 0
	if r.Context().Value("user") != nil {
		userID = r.Context().Value("user").(User).ID
	}

	err = db.Select(&problems, "SELECT *, (SELECT COUNT(*) FROM submissions WHERE submissions.problem_id = problems.id AND submissions.user_id = ?) AS total_submissions, (SELECT COUNT(*) FROM submissions WHERE submissions.problem_id = problems.id AND submissions.user_id = ? AND submissions.status = ? AND submissions.status != ?) AS accepted_submissions FROM problems WHERE packet_id = ? ORDER BY title ASC", userID, userID, "Accepted", "TBD", packet.ID)
	if err != nil {
		tmpl.Execute(w, r, "internal_server", J{})
		return
	}

	tmpl.Execute(w, r, "packet", J{
		"Packet":   packet,
		"Problems": problems,
	})
}

func packetsGetHandler(w http.ResponseWriter, r *http.Request) {
	var packets []struct {
		Packet
		ProblemCount  int `db:"problem_count"`
		ProblemSolved int `db:"problem_solved"`
	}

	userID := 0
	if r.Context().Value("user") != nil {
		userID = r.Context().Value("user").(User).ID
	}

	err := db.Select(&packets, "SELECT *, (SELECT COUNT(*) FROM problems WHERE problems.packet_id = packets.id) AS problem_count, (SELECT COUNT(DISTINCT problem_id) FROM submissions INNER JOIN problems ON submissions.problem_id = problems.id WHERE submissions.user_id = ? AND problems.packet_id = packets.id AND submissions.status = ?) AS problem_solved FROM packets ORDER BY title ASC", userID, "Accepted")
	if err != nil {
		tmpl.Execute(w, r, "internal_server", J{})
		return
	}

	tmpl.Execute(w, r, "packets", J{
		"Packets": packets,
	})
}
