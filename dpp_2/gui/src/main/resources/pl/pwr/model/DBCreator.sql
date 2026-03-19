--========= usun stara baze
PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS joke_tags;
DROP TABLE IF EXISTS jokes;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS users;

-- Ponownie włączamy klucze obce
PRAGMA foreign_keys = ON;


--====== Użytkownicy   =========
-- 1. Tabela reprezentuje użytkowników
CREATE TABLE IF NOT EXISTS users (
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     username TEXT UNIQUE NOT NULL,
     passwordHash TEXT NOT NULL,
     creationDate date DEFAULT (date('now'))
);
--====== TAGI   =========
-- 2. Tabela tagów dla żartów
CREATE TABLE IF NOT EXISTS tags (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL
);
-- 3. Tabela łączy tagi i żarty(Relacja M:N)
CREATE TABLE IF NOT EXISTS joke_tags
(
    joke_id INTEGER,
    tag_id  INTEGER,
    PRIMARY KEY (joke_id, tag_id),
    FOREIGN KEY (joke_id) REFERENCES jokes (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);
--====== Żarty   =========
-- 4. Tabela żartów (relacje jokes N:1 user, jokes 1:N comments, jokes N:M tags)
CREATE TABLE IF NOT EXISTS jokes (
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     title TEXT NOT NULL,
     content TEXT NOT NULL,
     creationDate date DEFAULT (date('now')),
     displayCount INTEGER DEFAULT 0,
     status INTEGER DEFAULT 0,
     user_id INTEGER,   --for N:1 with user
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
--====== Komentarze   =========
-- 5. Tabela komentarzy (Relacja 1:N z jokes i users)
CREATE TABLE IF NOT EXISTS comments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    text TEXT,
    rating INTEGER NOT NULL,
    joke_id INTEGER,
    user_id INTEGER,
    FOREIGN KEY (joke_id) REFERENCES jokes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);