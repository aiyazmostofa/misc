package main

import (
	"html/template"
	"io"
	"net/http"
)

type DevTemplateEngine struct {
}

type FastTemplateEngine struct {
	tmpl *template.Template
}

type TemplateEngine interface {
	Init()
	Execute(wr io.Writer, r *http.Request, name string, data J) error
}

func (e *DevTemplateEngine) Init() {
}

func (e *FastTemplateEngine) Init() {
	e.tmpl = template.Must(template.ParseGlob("templates/*.html"))
}

func (e *DevTemplateEngine) Execute(wr io.Writer, r *http.Request, name string, data J) error {
	data["User"] = r.Context().Value("user")
	return template.Must(template.ParseGlob("templates/*.html")).ExecuteTemplate(wr, name, data)
}

func (e *FastTemplateEngine) Execute(wr io.Writer, r *http.Request, name string, data J) error {
	data["User"] = r.Context().Value("user")
	return e.tmpl.ExecuteTemplate(wr, name, data)
}
