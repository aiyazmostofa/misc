package main

import (
	"encoding/json"
	"fmt"
	"html/template"
	"io"
	"net/http"
	"os"
	"sort"
)

type Problem struct {
	ContestID int
	Index     string
	Name      string
	Type      string
	Points    float64
	Rating    int
	Tags      []string
}

type ProblemsJSON struct {
	Result struct {
		Problems []Problem
	}
}

type SubmissionsJSON struct {
	Result []struct {
		Problem Problem
		Verdict string
	}
}

func main() {
	tmpl := template.Must(template.ParseGlob("tmpl.html"))
	url := "https://codeforces.com/api/user.status?handle=motorsponge"
	res, err := http.Get(url)
	if err != nil {
		panic(err)
	}
	defer res.Body.Close()
	bytes, err := io.ReadAll(res.Body)
	if err != nil {
		panic(err)
	}
	var submissions SubmissionsJSON
	err = json.Unmarshal(bytes, &submissions)
	if err != nil {
		panic(err)
	}
	contestsDone := make(map[int]bool)
	completedProblems := make(map[string]bool)
	for _, v := range submissions.Result {
		contestsDone[v.Problem.ContestID] = true
		if v.Verdict == "OK" {
			completedProblems[fmt.Sprintf("%d%s", v.Problem.ContestID, v.Problem.Index)] = true
		}
	}
	url = "https://codeforces.com/api/problemset.problems"
	res, err = http.Get(url)
	bytes, err = io.ReadAll(res.Body)
	defer res.Body.Close()
	if err != nil {
		panic(err)
	}
	var problemsJSON ProblemsJSON
	err = json.Unmarshal(bytes, &problemsJSON)
	if err != nil {
		panic(err)
	}
	var filteredProblems []Problem
	for _, v := range problemsJSON.Result.Problems {
		_, ok := contestsDone[v.ContestID]
		if ok {
			_, ok := completedProblems[fmt.Sprintf("%d%s", v.ContestID, v.Index)]
			if v.Rating <= 2400 && !ok {
				filteredProblems = append(filteredProblems, v)
			}
		}
	}
	sort.SliceStable(filteredProblems, func(i, j int) bool {
		if filteredProblems[i].Rating != filteredProblems[j].Rating {
			return filteredProblems[i].Rating < filteredProblems[j].Rating
		}
		if filteredProblems[i].ContestID != filteredProblems[j].ContestID {
			return filteredProblems[i].ContestID < filteredProblems[j].ContestID
		}
		return filteredProblems[i].Index < filteredProblems[j].Index
	})
	file, err := os.Create("grind.html")
	// file.Chmod(0755)
	if err != nil {
		panic(err)
	}
	defer file.Close()
	tmpl.ExecuteTemplate(file, "tmpl.html", filteredProblems)
}
