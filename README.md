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

## Day 4

Added the first playable game screen:

- `GamePanel`
- `Plane`
- `Bullet`
- 60 FPS Swing timer
- Keyboard movement with WASD / arrow keys
- Shooting with Space
- Pause with P
- Return to menu with Esc

## Day 5

Added the first enemy system:

- `Enemy` class
- 5 row by 8 column enemy grid
- Enemies move left and right together
- Enemies move down when they touch the screen edge
- Bullet-enemy collision
- Score increases by 10 when an enemy is destroyed

## How to Run

Compile:

```bash
javac -encoding UTF-8 -d out src/com/ap/chickeninvaders/*.java src/com/ap/chickeninvaders/ui/*.java src/com/ap/chickeninvaders/model/*.java src/com/ap/chickeninvaders/db/*.java src/com/ap/chickeninvaders/game/*.java
```

Run:

```bash
java -cp out com.ap.chickeninvaders.GameMain
```

## Controls

- Arrow keys / WASD: move
- Space: shoot
- P: pause/resume
- Esc: return to menu

## GitHub

Repository link: paste your GitHub link here.
