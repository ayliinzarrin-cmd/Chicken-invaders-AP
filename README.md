# Chicken Invaders AP Project

Student: Write your full name here.

## Day 1

Created the first Java Swing window and main menu skeleton.

## Day 2

Separated the user interface into different panel classes:

- Main menu
- Login screen
- Register screen
- Placeholder pages for High Scores, Settings, and How to Play

This prepares the project for user management and database work in the next days.

## Day 3

Added a simple text-file database for users:

- `User` model class
- `DatabaseManager` class
- Real register with duplicate username check
- Real login with password check
- User data is saved in `data/users.txt`

## How to Run

Compile:

```bash
javac -encoding UTF-8 -d out src/com/ap/chickeninvaders/*.java src/com/ap/chickeninvaders/ui/*.java src/com/ap/chickeninvaders/model/*.java src/com/ap/chickeninvaders/db/*.java
```

Run:

```bash
java -cp out com.ap.chickeninvaders.GameMain
```

## Controls

Game controls will be added after the game panel is created.

## GitHub

Repository link: paste your GitHub link here.
