#### Elevator pitch

The goal of the project is to implement a fully playable version of Monopoly in Java. At the core of the project is the data structure, which consists mostly of the Board. The Board contains information about the spaces and properties, as well as the players and their current state.

When looking at the spaces in the game, they can be organized into a tree-like structure. All spaces in the game are either properties, or game events. For example, the properties are further categorized into color, railroad, and utility property types.

appx. 30 sec here

The player data consists of a set of values that determine the player state. These are analyzed and manipulated by a controller according to the game rules. This controller effectively acts as a medium between the board, and what the user is able to see and interact with. This is where vast majority of the game logic exists.

The application also supports saving and loading the game state.
