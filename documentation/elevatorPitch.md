#### Elevator pitch

The goal of the project is to implement a fully playable version of Monopoly in Java. At the core of the project is the data structure, which consists mostly of the Board. The Board contains information about the spaces and properties, as well as the players and their current state.

When looking at the spaces in the game, they can be organized into a tree-like structure. All spaces in the game are either properties, or game events. For example, the properties are further categorized into color, railroad, and utility property types.

appx. 30 sec

The players consist of a set of values that determine their state. These are manipulated by a controller according to their current values and the game rules.

A controller effectively acts as a medium between the board, and what the user is able to see and interact with.
