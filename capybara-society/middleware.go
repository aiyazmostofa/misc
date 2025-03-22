package main

import (
	"context"
	sq "github.com/Masterminds/squirrel"
	"net/http"
	"net/url"
	"time"
)

func Userware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		cookie, err := r.Cookie("auth")
		if err != nil {
			next.ServeHTTP(w, r.WithContext(context.WithValue(r.Context(), "user", nil)))
			return
		}

		var session Session
		err = get(&session, sq.Select("*").From("sessions").Where(sq.Eq{"token": cookie.Value}))
		if err != nil {
			next.ServeHTTP(w, r.WithContext(context.WithValue(r.Context(), "user", nil)))
			return
		}

		if session.Expiration.Before(time.Now()) {
			exec(sq.Delete("sessions").Where(sq.Eq{"token": cookie.Value}))
			next.ServeHTTP(w, r.WithContext(context.WithValue(r.Context(), "user", nil)))
			return
		}

		var user User
		err = get(&user, sq.Select("*").From("users").Where(sq.Eq{"id": session.UserID}))
		if err != nil {
			exec(sq.Delete("sessions").Where(sq.Eq{"token": cookie.Value}))
			next.ServeHTTP(w, r.WithContext(context.WithValue(r.Context(), "user", nil)))
			return
		}

		next.ServeHTTP(w, r.WithContext(context.WithValue(r.Context(), "user", user)))
	})
}

func Authware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Context().Value("user") == nil {
			http.Redirect(w, r, "/login?redirect="+url.QueryEscape(r.URL.String()), 303)
			return
		}
		next.ServeHTTP(w, r)
	})
}
