# Chicken Invaders AP Project

Student: Aylin Zarrin

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

## Day 6

Added early gameplay progression:

- Levels 1 to 3
- Enemy types: Normal, Fast, Zigzag
- Enemy eggs
- Plane lives
- Egg-plane collision
- Level clear bonus score

## Day 7

Added game state and saving game results:

- `GameState` enum
- Cleaner pause/game-over state
- Save each finished game in `data/game_records.txt`
- Save username, score, level reached, status, and timestamp
- Update user's high score and last level

## Day 8

Added the first boss fight:

- `Boss` class
- Level 4 boss after clearing level 3
- Boss has 50 HP
- Boss health bar
- Boss moves left/right and slightly up/down
- Boss shoots eggs in 4 directions every 1.5 seconds
- Defeating the boss gives 500 bonus score

## Day 9 + Day 10 Combined

Added the advanced late-game section:

- Levels 5, 6, and 7
- `SHOOTER` enemy type
- Harder enemy movement speed
- Faster egg dropping in later levels
- Shooter enemies can fire horizontal eggs
- Level 5 starts after defeating Boss 4
- Level 8 starts after clearing Level 7
- Final boss with 100 HP
- Final boss shoots in 8 directions
- Defeating final boss ends the game with `WIN`

## Day 11

Added power-ups and explosion effects:

- `PowerUpType`
- `PowerUp`
- `Explosion`
- 20% power-up drop chance after destroying normal enemies
- Add Fire increases simultaneous bullets
- Rapid Fire increases fire rate for 8 seconds
- Extra Life increases lives up to 5
- Shield protects from egg damage for 10 seconds
- Freeze Bomb freezes enemies and eggs for 3 seconds
- Explosion effect on enemy/boss destruction and plane hit

## Day 12

Added sound settings:

- `SoundManager`
- `SettingsPanel`
- Four saved sound settings:
  - Background Music
  - Shot Sound
  - Explosion Sound
  - Game Over / Win Sound
- Settings are saved in `data/users.txt`
- Shooting, explosions, and game end now call the sound manager
- Current implementation uses simple system beeps, so no external sound files are required yet

## Day 13

Added the required score and help screens:

- `GameRecord` model for reading saved game results
- `HighScorePanel` with rank, username, score, level, result, and date
- Only the best score for each username is displayed
- Scores are sorted from highest to lowest
- `HowToPlayPanel` with controls, mission, and power-up help
- Each game record now also saves the four active sound settings

## Day 14

Completed the required enemy cell and health system:

- Added the `Cell` class for the 5 by 8 formation
- Every cell stores its enemy type, position, and remaining enemy counter
- Cell counters follow the project table: 2, 2, 3, 3, 4, and 4
- Normal, Fast, Zigzag, and Shooter enemies now have different health values
- Enemy health increases in levels 5 to 7
- Each enemy displays a health bar and its cell counter
- Destroyed enemies respawn from a random top corner while their cell counter is above zero
- Replacement enemies can be shot while flying toward their cell
- Formation speed, vertical step, and egg delay now follow each level's values
- Shooter enemies aim their special projectiles toward the player's current position

## Project Structure

- `src/com/ap/chickeninvaders`: application entry point
- `src/com/ap/chickeninvaders/ui`: menu, login, settings, scores, and help screens
- `src/com/ap/chickeninvaders/game`: game loop and game state
- `src/com/ap/chickeninvaders/model`: users and game objects
- `src/com/ap/chickeninvaders/db`: text-file database manager
- `src/com/ap/chickeninvaders/sound`: sound settings and effects

## Database

The project uses an advanced text-file database and needs no external library.

- `data/users.txt`: username, password, high score, last level, and sound settings
- `data/game_records.txt`: username, score, level, result, timestamp, and sound settings

The `data` folder and both files are created automatically when the game starts.

## How to Run

Compile:

```bash
javac -encoding UTF-8 -d out src/com/ap/chickeninvaders/*.java src/com/ap/chickeninvaders/ui/*.java src/com/ap/chickeninvaders/model/*.java src/com/ap/chickeninvaders/db/*.java src/com/ap/chickeninvaders/game/*.java src/com/ap/chickeninvaders/sound/*.java
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

Repository: https://github.com/ayliinzarrin-cmd/Chicken-invaders-AP
