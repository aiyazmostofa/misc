#!/bin/sh
go run main.go
scp grind.html ut:~/public_html/
ssh ut -t "chmod 755 ~/public_html/grind.html"
rm grind.html
