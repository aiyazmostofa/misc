package main

import (
	"encoding/json"
	"fmt"
	"io"
	"os"
	"os/exec"
	"sort"
	"text/template"
)

// Panic if error
func check(err error) {
	if err != nil {
		panic(err)
	}
}

type Master struct {
	Title    string
	Subtitle string
	Problems []Problem
}

type Problem struct {
	Index   int
	Title   string
	Alias   string
	Credits string
	Content string
}

func main() {
	// Read main info file
	bytes, err := os.ReadFile("info.json")
	check(err)

	// Parse main info file json
	var info Master
	err = json.Unmarshal(bytes, &info)
	check(err)

	// Read the current directory
	files, err := os.ReadDir(".")
	check(err)

	// For each file
	for _, file := range files {
		// It needs to be a directory
		if !file.IsDir() {
			continue
		}

		// Read the directory in question
		files, err := os.ReadDir(file.Name())
		check(err)

		// See if it's a problem, a solution, and sample/judge data
		isProblem := false
		hasSolution := false
		hasSample := false
		hasJudge := false
		for _, file := range files {
			if file.Name() == "info.json" {
				isProblem = true
			}

			if file.Name() == "Solution.java" {
				hasSolution = true
			}

			if file.Name() == "sample.dat" {
				hasSample = true
			}

			if file.Name() == "judge.dat" {
				hasJudge = true
			}
		}

		// Needs to be problem
		if !isProblem {
			continue
		}

		// Test code
		if hasSolution {
			if hasSample {
				fmt.Printf("%s:%s - %s\n", file.Name(), "sample", judgeCode(file.Name(), "sample"))
			}

			if hasJudge {
				fmt.Printf("%s:%s - %s\n", file.Name(), "judge", judgeCode(file.Name(), "judge"))
			}
		}

		// Read the problem info file
		bytes, err := os.ReadFile(file.Name() + "/info.json")
		check(err)

		// Parse the problem file info
		var problem Problem
		err = json.Unmarshal(bytes, &problem)
		check(err)

		// Execute the pandoc md -> tex command
		cmd := exec.Command("pandoc", "--metadata-file", file.Name()+"/info.json", file.Name()+"/statement.md", "-o", "temp.tex", "--from", "markdown", "--template", "templates/problem.tex")
		stdout, err := cmd.CombinedOutput()
		check(err)
		fmt.Print(string(stdout))

		// Take the output of the pandoc command
		bytes, err = os.ReadFile("temp.tex")
		check(err)

		// Delete the output of the pandoc command
		err = os.Remove("temp.tex")
		check(err)

		// Add to our problemset
		problem.Content = string(bytes)
		info.Problems = append(info.Problems, problem)
	}

	// Sort our problems by index
	sort.Slice(info.Problems, func(i, j int) bool {
		return info.Problems[i].Index < info.Problems[j].Index
	})

	// Parse our master template file
	tmpl := template.Must(template.ParseGlob("templates/master.tex"))

	// Create our new tex file
	texFile, err := os.Create("main.tex")
	check(err)

	// Execute template and write to new tex file
	err = tmpl.Execute(texFile, info)
	check(err)
}

// Runs code and compares output exactly.
func judgeCode(name string, prefix string) string {
	// Opens a stream to the source file
	fromFile, err := os.Open(name + "/" + prefix + ".dat")
	check(err)
	defer fromFile.Close()

	// Opens a stream to the destination file
	toFile, err := os.Create(name + ".dat")
	check(err)
	defer toFile.Close()
	defer os.Remove(name + ".dat")

	// Copies the file
	_, err = io.Copy(toFile, fromFile)
	check(err)

	// Runs the code
	cmd := exec.Command("java", name+"/Solution.java")
	actualOutput, err := cmd.CombinedOutput()
	if err != nil {
		return "!"
	}

	// Gets the expected output
	expectedOutput, err := os.ReadFile(name + "/" + prefix + ".out")
	check(err)

	// Comparison
	if len(actualOutput) != len(expectedOutput) {
		return "X"
	}
	for i := range len(actualOutput) {
		if actualOutput[i] != expectedOutput[i] {
			return "X"
		}
	}
	return "*"
}
