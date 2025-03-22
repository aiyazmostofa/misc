#!/bin/bash
go run scripts/main.go
pdflatex -interaction=nonstopmode main.tex > log.txt
rm main.log main.aux main.tex
