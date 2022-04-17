# Final Project: Java Monopoly Prototype

<div align="center">
<figure>
    <img src="logo.png">
</figure>
</div>
<br>

#### Table of Contents
1.  [Introduction](#introduction)
    1.  [Description](#description)
2.  [Data structures](#data-structures)
    1. [Spaces, properties, and game events](#spaces%2C-properties%2C-and-game-events)
3.  [Code organization](#code-organization)
4.  [Manipulation of data](#manipulation-of-data-structures)
5.  [User interface](#user-interface)

&nbsp;
### Introduction
This project seeks to implement a playable version of the board game, Monopoly.

&nbsp;
### Data structures

#### Spaces, properties, and game events
A game of Monopoly can be decomposed into a single, large data structure, and this comprises the core of this project.

There are 40 spaces in a standard game of Monopoly. These can be categorized into several groups according to their behavior - the two primary groups are **properties** and **game events**.

&nbsp;

**Properties** are any spaces that a player may purchase. For example, *Mediterranean Avenue* and *Electric Company* are both properties, but *Free Parking* is not. All properties share some common attributes; players can purchase, mortgage, and trade them - but they can also charge rent when another player lands on them. The calculation of this rent is not uniform across properties, and is a key reason for further subcategorization.

Some properties belong to a **Color** group, such as *Park Place* and *Boardwalk* from the dark blue group. The only non-color properties on the board are **Railroads** and **Utilities**.

Each of these property groups have specific rules that determine the amount of rent that a player must pay when they land on them. Additionally, these calculations are dependent on the state of other spaces within the group. For example, a player must pay more rent if the owner possesses more than one railroad, or if a house is developed on a color property.

&nbsp;

**Game events** are any spaces that a player is not able to purchase. These include *GO*, *Jail*, and any draw-card spaces such as *Chance* or *Community Chest*.

These spaces also have specific rules that govern how the game state changes if a player lands on them. Examples: landing or passing on *GO* grants the player a $200 bonus, landing on *Chance* executes a random event from a predetermined set, and landing on *Free Parking* does nothing.

Similar to properties, game event spaces are also subcategorized into several groups. A key differentiating factor of game events, however, is that there exist several categories that are occupied by only one space. For example, there is only a single *Jail* space.

Some game event spaces are grouped together - these include **draw card** and **tax** spaces.


<div align="center">
<figure>
    <img src="data-diagram.png">
<figcaption><br>A simplified representation of the Space data.<figcaption>
</figure>
</div>
<br>

#### Players

The data for each player is largely independent, not requiring intra-object structures as complex as those for spaces.

Additionally, some data is not as cleanly organized into the "space" or "player" domain - namely, the ownership of a property. This data is useful when held in the property object itself, but there are scenarios where finding the properties owned by a specific player is required. The aggregation of space and player data into a single "board" domain proves useful for this reason.

#### Aggregating spaces and players
The primary data element is the board, containing information about all players, spaces, and game events. However, this largely acts as a container for the state of the game, with some inputs and outputs that are validated and manipulated slightly. The manipulation of this data is delegated to the Controller class group.

A positive consequence of composing this data as a single class is the ability to query information across domains. As an example: suppose some representation of the game state must be updated, after a player has purchased the final property in the *green* color group after owning the others within the set.

If we wish to determine if a player owns this green color group monopoly, there are two general approaches to query this data:

1. Query the player set.
- Create a set of values indicating the total number of green properties owned by each player.
    - For each player in the game, check if they own a property with the following attributes:
        - belongs to the *green* color group
    - If this matches, add to the total.
- Compare the totals. If only one of the totals is non-zero, all *owned properties* within that color set belong to a single player.

A problem arises when considering that a player may own 2 out of 3 spaces in a single color group, where all other players own none. This necessitates further querying of the space data, determining how many spaces exist in the group total.


2. Query the space set.
- For each space in the game, check if the following conditions apply:
- belongs to the *green* color group
    - owned by 

However, an alternative approach exists - the board itself can be queried specifically, through some specialized methods:

1. Query the set of all spaces owned by player *n*.
    1. 

2. Query the set of all spaces belonging to the *green* color group.

&nbsp;
### Code organization

&nbsp;
### Manipulation of data
