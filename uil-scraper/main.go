package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"slices"
	"strconv"
	"strings"

	"golang.org/x/net/html"
)

type CombinedResult struct {
	Name        string
	School      string
	Event       string
	Conference  int
	District    int
	IsTeam      bool
	Score       float64
	BioScore    float64
	ChemScore   float64
	PhysScore   float64
	Programming float64
}

func getClasses(node *html.Node) []string {
	for _, attr := range node.Attr {
		if attr.Key == "class" {
			return strings.Fields(attr.Val)
		}
	}
	return nil
}
func IndividualHandler(strs []string, event string, conference int, district int) CombinedResult {
	score, err := strconv.ParseFloat(strs[4], 64)
	if err != nil {
		score = -100000.0
	}
	return CombinedResult{
		Name:       strs[2],
		School:     strs[1],
		Score:      score,
		Event:      event,
		Conference: conference,
		District:   district,
	}
}
func ScienceIndividualHandler(strs []string, event string, conference int, district int) CombinedResult {
	score, err := strconv.ParseFloat(strs[7], 64)
	if err != nil {
		score = -100000.0
	}
	bio, err := strconv.ParseFloat(strs[4], 64)
	if err != nil {
		bio = -100000.0
	}
	phys, err := strconv.ParseFloat(strs[6], 64)
	if err != nil {
		phys = -100000.0
	}
	chem, err := strconv.ParseFloat(strs[5], 64)
	if err != nil {
		chem = -100000.0
	}
	return CombinedResult{
		Name:       strs[2],
		School:     strs[1],
		Score:      score,
		Event:      event,
		Conference: conference,
		District:   district,
		BioScore:   bio,
		PhysScore:  phys,
		ChemScore:  chem,
	}
}
func TeamHandler(strs []string, event string, conference int, district int) CombinedResult {
	score, err := strconv.ParseFloat(strs[2], 64)
	if err != nil {
		score = -100000.0
	}
	return CombinedResult{
		School:     strs[1],
		Score:      score,
		Event:      event,
		Conference: conference,
		District:   district,
		IsTeam:     true,
	}
}
func CSTeamHandler(strs []string, event string, conference int, district int) CombinedResult {
	score, err := strconv.ParseFloat(strs[3], 64)
	if err != nil {
		score = -100000.0
	}
	programming, err := strconv.ParseFloat(strs[2], 64)
	if err != nil {
		programming = -100000.0
	}
	return CombinedResult{
		School:      strs[1],
		Programming: programming,
		Score:       score,
		Event:       event,
		Conference:  conference,
		District:    district,
		IsTeam:      true,
	}
}
func getTables(url string) ([][]string, [][]string) {
	res, err := http.Get(url)
	if err != nil {
		return nil, nil
	}
	node, err := html.Parse(res.Body)
	if err != nil {
		return nil, nil
	}
	var table *html.Node = nil
	var traverse func(*html.Node)
	traverse = func(node *html.Node) {
		classes := getClasses(node)
		if slices.Contains(classes, "ddprint") {
			table = node
			return
		}
		for child := node.FirstChild; child != nil && table == nil; child = child.NextSibling {
			traverse(child)
		}
	}
	traverse(node)
	if table == nil {
		return nil, nil
	}
	if strings.Contains(table.PrevSibling.FirstChild.Data, "Individual") {
		var individuals [][]string
		for row := table.FirstChild.FirstChild.NextSibling; row != nil; row = row.NextSibling {
			var individual []string
			for data := row.FirstChild; data != nil; data = data.NextSibling {
				if data.FirstChild == nil {
					continue
				}
				individual = append(individual, data.FirstChild.Data)
			}
			individuals = append(individuals, individual)
		}
		table = table.NextSibling
		if table == nil {
			return individuals, nil
		}
		table = table.NextSibling
		if table == nil || !slices.Contains(getClasses(table), "ddprint") || table.FirstChild == nil || table.FirstChild.FirstChild == nil {
			return individuals, nil
		}
		var teams [][]string
		for child := table.FirstChild.FirstChild.NextSibling; child != nil; child = child.NextSibling {
			var team []string
			for data := child.FirstChild; data != nil; data = data.NextSibling {
				if data.FirstChild == nil {
					continue
				}
				team = append(team, data.FirstChild.Data)
			}
			teams = append(teams, team)
		}
		return individuals, teams
	} else if strings.Contains(table.PrevSibling.FirstChild.Data, "Team") {
		var teams [][]string
		for child := table.FirstChild.FirstChild.NextSibling; child != nil; child = child.NextSibling {
			var team []string
			for data := child.FirstChild; data != nil; data = data.NextSibling {
				if data.FirstChild == nil {
					continue
				}
				team = append(team, data.FirstChild.Data)
			}
			teams = append(teams, team)
		}
		return nil, teams
	}
	return nil, nil
}
func process() {
	var results []CombinedResult
	internal := func(id int, name string) {
		for conference := 1; conference <= 6; conference++ {
			for district := 1; district <= 32; district++ {
				individuals, teams := getTables(fmt.Sprintf("https://postings.speechwire.com/r-uil-academics.php?groupingid=%d&Submit=View+postings&region=&district=%d&state=&conference=%d&seasonid=17", id, district, conference))
				if individuals != nil {
					fmt.Printf("%s Individual %dA-%d\n", name, conference, district)
					for _, strs := range individuals {
						if name == "Science" {
							results = append(results, ScienceIndividualHandler(strs, name, conference, district))
						} else {
							results = append(results, IndividualHandler(strs, name, conference, district))
						}
					}
				}
				if teams != nil {
					fmt.Printf("%s Team %dA-%d\n", name, conference, district)
					for _, strs := range individuals {
						if name == "Computer Science" {
							results = append(results, CSTeamHandler(strs, name, conference, district))
						} else {
							results = append(results, TeamHandler(strs, name, conference, district))
						}
					}
				}
			}
		}
	}
	internal(8, "Calculator Applications")
	internal(9, "Computer Science")
	internal(10, "Mathematics")
	internal(11, "Number Sense")
	internal(12, "Science")
	bytes, err := json.Marshal(results)
	if err != nil {
		panic(err)
	}
	os.WriteFile(fmt.Sprintf("data.json"), bytes, os.ModePerm)
}
func main() {
	process()
}
