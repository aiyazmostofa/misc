from flask import Flask, redirect, render_template, g, request
import sqlite3
import threading
import os
import re
import music_tag
from pytube import YouTube

app = Flask(__name__, template_folder='.')

def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect("storage.db")
    return db

@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, '_database', None)
    if db is not None:
        db.close()

@app.route("/", methods=['GET'])
def edit():
    db = get_db()
    cur = db.cursor()
    pieces = cur.execute("SELECT * FROM pieces").fetchall()
    return render_template('template.html', piece=None, pieces=pieces)

@app.route("/<int:piece_id>", methods=['GET'])
def edit_piece(piece_id):
    db = get_db()
    cur = db.cursor()
    pieces = cur.execute("SELECT * FROM pieces").fetchall()
    piece = cur.execute("SELECT * FROM pieces WHERE id = ?", (piece_id,)).fetchone()
    return render_template('template.html', piece=piece, pieces=pieces)

@app.route("/", methods=['POST'])
def new_piece():
    db = get_db()
    cur = db.cursor()
    old_id = request.form['old_id']
    piece_id = 0
    if old_id != '':
        cur.execute("DELETE FROM pieces WHERE id = ?", (old_id,))
        os.remove(f"music/{old_id}.mp3")
        piece_id = int(old_id)
    title = request.form['title']
    composer = request.form['composer']
    links = request.form['links']
    if piece_id == 0:
        cur.execute("INSERT INTO pieces (title, composer, links, finished) VALUES (?, ?, ?, ?)", (title,composer, links,0))
        piece_id = cur.lastrowid
    else:
        cur.execute("INSERT INTO pieces (id, title, composer, links, finished) VALUES (?,?, ?, ?, ?)", (piece_id,title,composer, links,0))
    db.commit()
    threading.Thread(target=lambda: download_piece(piece_id)).start()
    return redirect("/", 303)

def download_piece(piece_id):
    db = sqlite3.connect("storage.db")
    cur = db.cursor()
    piece = cur.execute("SELECT * FROM pieces WHERE id = ?", (piece_id,)).fetchone()
    links = re.split("\\s+", piece[3].strip())
    f = open(f"music/{piece_id}.txt", "w")
    for i in range(len(links)):
        yt = YouTube(links[i])
        yt.streams.filter(only_audio=True)[0].download("music", f"{piece_id}-{i}.webm")
        os.system(f"ffmpeg -i music/{piece_id}-{i}.webm music/{piece_id}-{i}.mp3")
        os.remove(f"music/{piece_id}-{i}.webm")
        f.write(f"file '{piece_id}-{i}.mp3'\n")
    f.close()
    os.system(f"ffmpeg -f concat -safe 0 -i music/{piece_id}.txt -c copy music/{piece_id}.mp3")
    for i in range(len(links)):
        os.remove(f"music/{piece_id}-{i}.mp3")
    os.remove(f"music/{piece_id}.txt")
    f = music_tag.load_file(f"music/{piece_id}.mp3")
    f['title'] = piece[1]
    f['artist'] = piece[2]
    f.save()
    cur.execute("UPDATE pieces SET finished = 1 WHERE id = ?", (piece_id,))
    db.commit()
    return

@app.route("/<int:piece_id>", methods=['POST'])
def delete_piece(piece_id):
    db = get_db()
    cur = db.cursor()
    cur.execute("DELETE FROM pieces WHERE id = ?", (piece_id,))
    db.commit()
    os.remove(f"music/{piece_id}.mp3")
    return redirect("/", 303) 
