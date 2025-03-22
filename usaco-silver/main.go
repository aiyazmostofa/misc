package main

import (
	"encoding/json"
	"log"
	"net/http"
	"os"
	"slices"
	"text/template"
	h "html"

	"github.com/gomarkdown/markdown"
	"github.com/gomarkdown/markdown/html"
	"github.com/gomarkdown/markdown/parser"
)

func mdToHTML(md []byte) []byte {
	extensions := parser.CommonExtensions | parser.AutoHeadingIDs | parser.NoEmptyLineBeforeBlock
	p := parser.NewWithExtensions(extensions)
	doc := p.Parse(md)
	htmlFlags := html.CommonFlags | html.HrefTargetBlank
	opts := html.RendererOptions{Flags: htmlFlags}
	renderer := html.NewRenderer(opts)
	return markdown.Render(doc, renderer)
}

var tmpl *template.Template

type Problem struct {
	Title string
	URL   string
	Index string
	Done  bool
}

func getDb() ([]Problem, error) {
	var problems []Problem
	bytes, err := os.ReadFile("db.json")
	if err != nil {
		return nil, err
	}

	err = json.Unmarshal(bytes, &problems)
	if err != nil {
		return nil, err
	}
	return problems, nil
}

func indexHandler(w http.ResponseWriter, r *http.Request) {
	problems, err := getDb()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	tmpl.ExecuteTemplate(w, "index", problems)
}

func solutionHandler(w http.ResponseWriter, r *http.Request) {
	index := r.URL.Path[len("/solution/"):]
	problems, err := getDb()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	idx := slices.IndexFunc(problems, func(p Problem) bool { return p.Index == index })
	if idx == -1 || !problems[idx].Done {
		http.NotFound(w, r)
		return
	}

	md, err := os.ReadFile(problems[idx].Index + "/solution.md")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	code, err := os.ReadFile(problems[idx].Index + "/Main.java")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	tmpl.ExecuteTemplate(w, "solution", struct {
		Title    string
		Code    string
		Content string
	}{problems[idx].Title, h.EscapeString(string(code)), string(mdToHTML(md))})
}

func main() {
	tmpl = template.Must(template.ParseGlob("templates.html"))
	http.HandleFunc("/", indexHandler)
	http.HandleFunc("/solution/", solutionHandler)
	log.Fatalln(http.ListenAndServe("127.0.0.1:8080", nil))
}
