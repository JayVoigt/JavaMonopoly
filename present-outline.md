My project was to create an implementation of Monopoly in Java.

---

<mark>I began by thinking about the data structure I would need.

The entire game state of Monopoly can be stored with spaces and players.

---

Each player has the same attributes, such as balance, position, jailed or not jailed, and so on.

The game supports up to 4 players.

---

The board has 40 spaces, and these are put in to one of two groups: properties, and game events.

---

Properties are any spaces that players can own.

These include color properties, railroads, and utilities.

---

Game events are any spaces that are not properties, such as GO, Jail, and Chance.

---

<mark>The game is packaged in a Swing GUI.

This allows the player to see the status of the game and make decisions accordingly.

---

The GUI has three main components - the board, information, and control panes.

Extra functionality is implemented through dialogs.

---

**The board pane shows the position of all players, and has buttons for each space.**

**Spaces can be clicked for more information.**

**It also contains a game log, for detailed history of the current game.**

---

<mark>The information pane shows the status and owned properties of all active players.

---

<mark>The control pane provides buttons to show dialogs, and for rolling the dice or ending your turn.

---

**Finally, Swing has some versatile features for extra visual flair, such as border highlights and animations.**

**For example, the border animates when a player moves.**

**Or when they win the game.**
