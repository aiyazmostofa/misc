package main

import "time"

type User struct {
	ID int `db:"id"`
	Username string `db:"username"`
	Password string `db:"password"`
	Email string `db:"email"`
	AuthLevel string `db:"auth_level"`
}

type Packet struct {
	ID int `db:"id"`
	Title string `db:"title"`
	Zip bool `db:"zip"`
}

type Problem struct {
	ID int `db:"id"`
	Title string `db:"title"`
	InputFileName string `db:"input_file_name"`
	PacketID int `db:"packet_id"`
}

type Submission struct {
	ID int `db:"id"`
	CodeFileName string `db:"code_file_name"`
	SubmittedAt time.Time `db:"submitted_at"`
	Status string `db:"status"`
	ProblemID int `db:"problem_id"`
	UserID int `db:"user_id"`
}

type Invitation struct {
	ID int `db:"id"`
	Expiration time.Time `db:"expiration"`
	Token string `db:"token"`
}

type Session struct {
	ID int `db:"id"`
	Expiration time.Time `db:"expiration"`
	Token string `db:"token"`
	UserID int `db:"user_id"`
}
