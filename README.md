# Chicken Invaders: Echo Squadron

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
- Day 12 initially used system beeps; Day 15 replaces them with generated digital audio

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

## Day 15 - Final Version

Added the project's original feature and completed final delivery:

- `PlayerSnapshot` records the player's recent position and shooting actions
- `EchoPlane` replays the last five seconds as a holographic wingmate
- Press `E` to activate Echo Squadron after it finishes recording
- Echo bullets damage regular enemies and bosses
- Echo has a visible 15-second cooldown in the HUD
- Echo plane and bullets have a separate holographic visual style
- Replaced system beeps with generated digital sound effects
- Added looping background music, shot, explosion, Echo, win, and game-over sounds
- All four saved sound switches continue to work independently
- Added `ChickenInvaders.jar` and `run.bat` for easy execution

## Final Rubric Fixes

- Refactored `Enemy` into an abstract base class with `NormalEnemy`, `FastEnemy`, `ZigzagEnemy`, and `ShooterEnemy`
- Refactored `Boss` into an abstract base class with `BossLevel4` and `BossLevel8`
- Zigzag replacement enemies now fly along a wave path into their cells
- Fast enemies move horizontally at twice the formation speed around their cells
- Shooter enemies fire horizontal projectiles at 5 pixels per frame
- Final boss now accelerates, changes direction occasionally, and moves vertically across a 100-pixel range
- Background music now starts in the initial menu before login and follows saved user settings after login
- Added a shared Echo Squadron UI theme for menus, forms, settings, scores, and help screens

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

Requirement: Java Development Kit (JDK) 17 or newer. No external library is required.

Easiest method on Windows:

1. Double-click `run.bat`.

Or run the JAR from a terminal:

```bash
java -jar ChickenInvaders.jar
```

To compile the source code manually:

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
- E: activate Echo Squadron
- P: pause/resume
- Esc: return to menu

## GitHub

Repository: https://github.com/ayliinzarrin-cmd/Chicken-invaders-AP
