package main

import (
	"database/sql"
	"fmt"
	"net/http"
	"os"
	"time"

	sq "github.com/Masterminds/squirrel"
	"github.com/go-chi/chi/v5"
	"github.com/golang-migrate/migrate/v4"
	"github.com/golang-migrate/migrate/v4/database/sqlite3"
	_ "github.com/golang-migrate/migrate/v4/source/file"
	"github.com/jmoiron/sqlx"
	_ "github.com/mattn/go-sqlite3"
)

type J map[string]interface{}

var db *sqlx.DB
var tmpl TemplateEngine
var loc *time.Location

func main() {
	fmt.Println("Start migration...")
	startMigration()
	fmt.Println("Loading time zone data...")
	var err error
	loc, err = time.LoadLocation("America/Chicago")
	if err != nil {
		panic(err)
	}

	fmt.Println("Starting template engine...")
	if len(os.Getenv("DEV")) > 0 {
		fmt.Println("Using development environment...")
		tmpl = &DevTemplateEngine{}
	} else {
		tmpl = &FastTemplateEngine{}
	}
	tmpl.Init()

	fmt.Println("Connecting to database...")
	db = sqlx.MustOpen("sqlite3", "storage.db")

	r := chi.NewRouter()
	r.Use(Userware)
	r.Get("/", func(w http.ResponseWriter, r *http.Request) {
		tmpl.Execute(w, r, "index", J{})
	})

	r.NotFound(func(w http.ResponseWriter, r *http.Request) {
		tmpl.Execute(w, r, "not_found", J{})
	})

	r.Get("/signout", SignOut)
	r.Handle("/static/*", http.StripPrefix("/static/", http.FileServer(http.Dir("static"))))
	r.Mount("/accounts", AccountsRouter())
	r.Mount("/login", LoginRouter())
	r.Mount("/packets", PacketsRouter())
	r.Mount("/submissions", SubmissionsRouter())
	fmt.Println("Website available at port 3000.")
	http.ListenAndServe(":3000", r)
}

func startMigration() {
	db, err := sql.Open("sqlite3", "storage.db")
	if err != nil {
		panic(err)
	}
	driver, err := sqlite3.WithInstance(db, &sqlite3.Config{})
	if err != nil {
		panic(err)
	}
	m, err := migrate.NewWithDatabaseInstance(
		"file://./migrations",
		"sqlite3", driver,
	)
	if err != nil {
		panic(err)
	}
	err = m.Up()
	if err != nil && err != migrate.ErrNoChange {
		panic(err)
	}
}

func exec(query sq.Sqlizer) error {
	expr, args, err := query.ToSql()
	if err != nil {
		return err
	}
	_, err = db.Exec(expr, args...)
	return err
}

func sel(dest interface{}, query sq.Sqlizer) error {
	expr, args, err := query.ToSql()
	if err != nil {
		return err
	}
	return db.Select(dest, expr, args...)
}

func get(dest interface{}, query sq.Sqlizer) error {
	expr, args, err := query.ToSql()
	if err != nil {
		return err
	}
	return db.Get(dest, expr, args...)
}
