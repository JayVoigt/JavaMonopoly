![](https://img.shields.io/badge/-NOTICE-FF0000?&style=for-the-badge)

Images may not load correctly in the README version of the documentation.

Please refer to `documentation/MainDocumentation.md` instead.

# Final Project: Java Monopoly Prototype

<div align="center">
<figure>
    <img src="documentation/logo.png">
</figure>
</div>
<br>

#### Table of Contents
1.  [Introduction](#introduction) 
2.  [Data structures](#data-structures) 
    1. [Spaces, properties, and game events](#spaces%2C-properties%2C-and-game-events) 
    2. [Players](#players) 
    3. [Aggregating spaces and players](#aggregating-spaces-and-players) 
3.  [Code organization](#code-organization)
4.  [Manipulation of data](#manipulation-of-data)
5.  [User interface - main components](#user-interface---main-components)
    1.  [Inspiration](#inspiration) 
    2.  [General structure](#general-structure) 
    3.  [Board view](#board-view)
    4.  [Information pane](#information-pane) 
    5.  [Control pane](#control-pane) 
    6.  [Animations and extra features](#animations-and-extra-features) 
6.  [User interface - dialogs](#dialogs) 
    1.  [Property purchase](#dialog-purchase-property) 
    2.  [Jail](#jail) 
    3.  [Game editor](#game-editor) 
    4.  [Improvements](#improvements) 
    5.  [Mortgage](#mortgage) 
    6.  [Start game](#start-game) 
7.  [Future features and ideas](#future-features-and-ideas) 

&nbsp;
### Introduction
*Created by Jay Voigt as a final project for CSCI 24000, Spring 2022.*

This project seeks to implement a playable version of the board game, Monopoly. With the physical version of the board game, players are responsible for interpreting and enforcing the game rules, while the software versions typically perform this automatically.

The primary objective for this Java prototype of is to demonstrate how the game can be logically organized, such that it is fully playable with no need to reference a rule book.

Note the use of the word "prototype" -- this implementation is not fully compliant with the original game rules. For example, auctions and trading are not implemented, and there are inevitably some bugs to be encountered while playing normally.

&nbsp;

![](documentation/important.svg)

**See the directory `/src/main/java/cc/jayv/monopoly3` for all Java code** utilized in the final build for this project.

The latest executable `.jar` file is available as `/JavaMonopolyPrototype.jar`.

For more details on the codebase, please see the JavaDoc -- it is currently available online at [GitHub Pages](), but the offline version is available under the `/javadoc` directory.

Primary documentation is available as `/documentation/MainDocumentation.md`, along with supplemental diagrams and screenshots in this directory.

Legacy and unused code files are stored under `/src/main/legacy`. These files are **not** used in the final version of the main project, but are stored for reference.

Art assets and helper files, such as icons and .csv-formatted space data, are stored under `/src/main/resources`. All non-reference images were created with Aseprite and Adobe Illustrator.

&nbsp;
##### Third-party libraries and dependencies
This project utilizes:
- [MigLayout](http://miglayout.com/) as the primary Swing layout manager. Licensed under BSD or GPL.
- [FlatLaf](https://github.com/JFormDesigner/FlatLaf) for the Swing look and feel. Licensed under Apache License 2.0.

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
    <img src="documentation/data-diagram.png">
<figcaption><br>A simplified representation of the Space data.<figcaption>
</figure>
</div>
<br>

#### Players

The data for each player is largely independent, not requiring the same degree of intra-object dependence such as that which may exist across space groups.

Each player must contain data for the following attributes:
- name
- current balance
- current position
- is jailed
- values of dice
- number of owned Get Out of Jail Free Cards

As well as several "flags" that are accessed by the Controller class group, examples:
- suggested lock state of end turn, roll dice buttons
- is bankrupt
- is currently in the initial turn of being jailed
- has rolled doubles
- needs to make a decision for property purchase, jail

&nbsp;
#### Aggregating spaces and players
The primary data element is the board, containing information about all players, spaces, and game events. However, this largely acts as a container for the state of the game, with some inputs and outputs that implement simple validation and manipulation. More advanced manipulation and analysis of this data is delegated to the Controller class group.

A UML diagram illustrating the game state data structure is included:

![](data-uml.svg)

Additionally, some data is not as cleanly organized into the "space" or "player" domain - namely, the ownership of a property. This data is useful when held in the property object itself, but there are scenarios where finding the properties owned by a specific player is required. The aggregation of space and player data into a single "board" domain proves useful for this reason.

A positive consequence of composing this data as a single class is the ability to query information across domains. As an example: suppose some representation of the game state must be updated, after a player has purchased the final property in the green color group, while owning the others within the set.

If we wish to determine if a player owns this green color group monopoly, there are two general approaches to query this data:

**1 - Query the player set**
- Create a set of values indicating the total number of green properties owned by each player.
    - For each player in the game, check if they own a property with the following attributes:
        - belongs to the *green* color group
    - If this matches, add to the respective total.
- Compare the totals. If only one of the totals is non-zero, all *owned properties* within that color set belong to a single player.

A problem arises when considering that a player may own 2 out of 3 spaces in a single color group, where all other players own none. This necessitates further querying of the space data, determining how many spaces exist in the group total.

&nbsp;

**2 - Query the space set**
- Create a set of values indicating the owners of each green property.
    - For each space in the game, check if the following conditions apply:
        - belongs to the *green* color group
        - is owned by a player
    - If this matches, set the appropriate owner value for the property.
- Compare the values. If all values are equal, the full set is owned by the player.

&nbsp;

The general algorithm for determining this data is similar across the domains, but becomes messy when it is frequently access across multiple classes and contexts. An alternative approach exists - the board itself can be queried specifically, through some specialized methods:

**1 - Obtain the set of all spaces owned by player *n***
 - For each space in the game, check if the following conditions apply:
    - owned by player *n*
- If this matches, add the space to the set.
- Return the set.

&nbsp;

**2 - Obtain the set of all spaces belonging to color group *g***
- For each space in the game, check if the following conditions apply:
    - within color group *g*
- If this matches add the space to the set.
- Return the set.

&nbsp;

The latter approaches of board-domain querying allow for cleaner access to attributes of the game state. Additionally, much of the need for this querying is eliminated as the board class can automatically self-update the relationships between its data members when the game state changes, using similar methods internally.

&nbsp;

### Code organization

Components of the application are roughly grouped into three categories:

**View**: displays the current game state to the user, and allows them to interact with it in a controlled manner

**Data**: contains information about the game state

**Controller**: modifies the game state according to specific rules and user input

An important note is that these categories are not occupied by single classes or objects, rather, responsibilities are delegated into smaller classes with some overlap.


<div align="center">
<figure>
    <img src="documentation/simple-main.svg">
    <figcaption><br>A simplified view of the data and access structure.</figcaption>
</figure>
</div>
<br>

&nbsp;
### Manipulation of data

Classes belonging to the **Controller** group are responsible for manipulating the game state, enforcing the game rules, and regulating the flow of available interactions. An intermediary class, `GameLogicSwitchboard`, receives action descriptions from the View group, and forwards them appropriately to other objects in the Controller group.

This enables more concise control expressions in the GUI class, along with adding additional routines universally to all usages of a particular message. For example, a dialog may invoke the `CONTROLS_ROLLDICE` action -- *only this value* needs to be passed to the "switchboard" for the sequence of roll-dice routines to occur.

Below is a UML diagram demonstrating the relationship between classes within this group. Note the inclusion of the symbolic `DynamicView` object to display its connection into the rest of the application.
<div align="center">
<img src="documentation/control-uml.svg">
</div>

&nbsp;

The Controller group contains the vast majority of game logic, aside from some context enforcement of GUI actions, such as enabling/disabling buttons based on game state. This contains the primary class, `GameLogicController`, with some delegate classes to handle more complex actions, such as evaluating a Community Chest card or building improvements on Color properties.

&nbsp;
### User interface - main components

Classes belonging to the **View** group are responsible for displaying the state of the game to the user, and routing user input to any corresponding actions within Controller group classes.

Below is a UML diagram indicating the structure and relationships of the objects which comprise the view group. These relationships are further described in later sections.

Three primary subgroups exist in the View group:
- primary view elements, non-dialog
- dialog view elements
- ActionListener implementations

<div align="center">
<img src="documentation/gui-uml.svg">
</div>

&nbsp;

*See the below sections for more information on the *primary view* and *dialog* subgroups.*

The ActionListener implementation subgroup contains internal classes of the primary View class -- `DynamicView`. These can be attached to any arbitrary Swing element that supports them. The function of these classes is similar to that of the "switchboard," i.e., to reduce code duplication and to allow simpler expressions of actions.

Example:

An instance of `GenericButtonActionListener` can be created with the action `CONTROLS_SHOW_IMPROVEMENTS` specified in the constructor call:

```
listener = new GenericButtonActionListener(CONTROLS_SHOW_IMPROVEMENTS);
```

This new instance can then be attached to an existing `JButton`, and the appropriate action will be forwarded to the switchboard object without needing any additional expressions:

```
JButton button;
...
button.addActionListener(listener);
```

&nbsp;
#### Inspiration

The user interface for this application is inspired and informed by [a commercial implementation](https://archive.org/details/MonopolyMacPlay) of the game - <i>Monopoly</i> (1993) by MacPlay, for the original Macintosh platform.
<div align="center">
<figure>
    <img src="documentation/gui-inspiration.png">
    <figcaption><br>A screenshot of <i>Monopoly</i> on the Macintosh.
</figure>
</div>
<br>

A key attribute of this implementation is the simplicity of the user interface - given that the Macintosh has a resolution of 512x342 with 2 colors, this serves as a good template for a simpler design.

&nbsp;
#### General structure

Visually, the user interface is comprised of a main window, with a set of dialogs that hide/show when appropriate. This main window consists of three sub-windows: the board, information, and control sections.

<div align="center">
<figure>
    <img src="documentation/view-areas-all.png">
</figure>
</div>
<br>

A more abstract diagram of the View group components, and how they interact with the Controller and Data groups, is given:

<div align="center">
<img src="documentation/gui-diagram.png">
</div>

&nbsp;
#### Board view

The board view provides a visualization for the spaces on the board, acting as a proxy for a physical printed board. Each space is a button that, when pressed, updates a space information area. 

<div align="center">
<figure>
    <img src="documentation/board-view.gif">
</figure>
</div>
<br>

&nbsp;

#### Information pane

The information pane provides a single visual area for any player to assess the state of the game.

<div align="center">
<figure>
    <img src="documentation/gui-info-pane.gif">
</figure>
</div>
<br>

It consists of four information modules, each assigned to one player. These modules are then subdivided into **status** and **asset** views.

The **status** view, on the left, contains the following icons that indicate
- ![](res/dice-icon.png)  - if it is currently the player's turn
- ![](res/jail.png)  - if the player is jailed
- ![](res/piggy.png)  - if the player is bankrupt

as well as labels to indiate:
- ![](res/player-generic.png) [ *Player n* ] - the player's name
- ![](res/money.png) [ *$____* ] - the current balance of the player
- ![](res/position.png) [ *space* ] - the name of the space that the player's game piece is currently on

&nbsp;

The **asset** view, on the right, contains icons with an associated numerical value. These include:

- ![](res/properties.png) - the total number of properties owned by the player
- ![](res/goojfc.png) - the number of Get Out of Jail Free Cards owned by the player
- ![](res/property-single-lightblue.png), ![](res/property-single-orange.png), etc. - the number of properties belonging to each group owned by the player

The text of the property group labels additionally changes colors depending on the state: gray when no properties are owned, and red when all properties are owned. (i.e., a monopoly)

&nbsp;
#### Control pane

The control pane provides important information about the current player, buttons to access optional dialogs (see section [Optional dialogs](#optional-dialogs)), and buttons to roll the dice and end their turn.

<div align="center">

![](control.gif)

</div>

Note how the "Roll dice" and "End turn" buttons lock and unlock in response to the game state. Each player has an attribute, manipulated by the controller, that indicates if a player is permitted to perform either of these actions.

For example, it is not legal within official game rules for a player to end their turn if they have not rolled the dice; this also applies when rolling doubles.

&nbsp;

The six buttons in the center of the dialog allow the user to perform *optional* actions, i.e., any actions that are not strictly required by the game rules. These include:
- mortgaging properties
- viewing owned properties
- managing improvements
- viewing statistics (not yet implemented)
- trading with other players (not yet implemented)
- forfeiting the game

&nbsp;
#### Animations and extra features

Some spaces support animated assets that, when the mouse enters the space area, will replace the standard button appearance until the mouse exits. These spaces include:
- Electric Company
- Water Works
- Free Parking
- Go To Jail
- Luxury Tax

<div align="center">

![](electric-company-anim.gif) ![](waterworks-anim.gif) ![](free-parking-anim.gif) ![](go-to-jail-anim.gif) ![](luxury-tax-anim.gif)

</div>

<a name="dialogs"></a>
&nbsp;
### User interface - dialogs

Some user interface elements have specific contexts where their visibility is required, in contrast to the always-shown board, information, and control views of the main window. Such user interface elements are implemented with **dialogs**, and these are split into two categories: **mandatory**, which allow responses to required actions, and **optional**, which can be summoned by the user in nearly any game state context.

**Mandatory** dialogs include the property purchase and jail dialogs. A user cannot dismiss these, as a decision is required to continue the game.

&nbsp;
<a name="dialog-purchase-property"></a>

The **Property Decision** mandatory dialog appears when a player lands on a property that is not owned. They must then decide to either purchase, or sent the property to auction. *Note that auctions are not currently implemented.*

<div align="center">
<img src="documentation/dialog-property-purchase.png" width=300>
</div>

&nbsp;
<a name="jail"></a>

The **Jail** mandatory dialog appears at the beginning of a player's turn if they are jailed, only if it is *not* the first turn they have been jailed. For example, if a player lands on *Go To Jail*, their only option to continue will be to end their turn. The dialog will then appear on their *next* turn.

Three options are provided through the dialog:
- Post Bail: Pay $50 to be released from jail immediately.
- Roll for Doubles: Roll the dice, and be released from jail if the values of both dice are the same.
- Use Get Out of Jail Free Card: Consume a GOOJFC card and be released from jail immediately.
<div align="center">

<img src="documentation/jail-1turn-nogoojfc.png" width=300>
<img src="documentation/jail-1turn-goojfc.png" width=300>
<br>
<img src="documentation/jail-3turns.png" width=300>
</div>

&nbsp;
<a name="game-editor"></a>
<a name="optional-dialogs"></a>

The **Game Editor** optional dialog is available under the *Edit* menu, and provides the user with options to directly manipulate the game state. This is very useful for debugging, and can also "repair" the game if a bug renders it unplayable.

<div align="center">
<img src="documentation/dialog-game-editor.png" width=500>
</div>

&nbsp;
<a name="improvements"></a>

The **Improvements** optional dialog allows the player to construct and sell houses and hotels on their color properties.

<div align="center">
<img src="documentation/dialog-improvements-1.png" width=300>
<img src="documentation/dialog-improvements-2.png" width=300>
<br>
<img src="documentation/dialog-improvements-3.png" width=300>
</div>

&nbsp;
<a name="mortgage"></a>

The **Mortgage Properties** optional dialog allows the player to mortgage and lift the mortgage of their owned properties.

<div align="center">
<img src="documentation/dialog-mortgage-1.png" width=300>
<img src="documentation/dialog-mortgage-2.png" width=300>
<br>
<img src="documentation/dialog-mortgage-3.png" width=300>
</div>

&nbsp;
<a name="start-game"></a>

The **New Game** optional dialog allows the user to start a new game, select the number of players, and set custom names for these players.

<div align="center">
<img src="documentation/dialog-newgame-1.png" width=300>
<img src="documentation/dialog-newgame-2.png" width=300>
<br>
<img src="documentation/dialog-newgame-3.png" width=300>
<img src="documentation/dialog-newgame-3-corresponding.png" width=300>
</div>

&nbsp;
### Future features and ideas

A standard implementation of Monopoly has several features which are not present in this version. Namely, auctions and trading between players. The basic GUI elements exist for these within the codebase, but time constraints simply moved these features to a lower priority. Auctions particularly require more advanced GUI design, requiring timers and an array of simultaneous inputs for each player. 

Additional non-implemented ideas that exist outside the scope of a standard Monopoly game include:

**Statistics view**

An area to view various statistics gathered over a single game, e.g.:
- total money issued by the bank
- total money spent by each player
- frequency graphs for landing on specific areas of the board
- return on investment for improvements on color properties

**Dice probability view**

Dynamically highlighting spaces on the board such that their appearance reflects the probability that a player may land on them. For example, spaces 7 units away from the current player have the highest probability of landing, and would be the most visually prominent highlight.

**Player icon selection**

This feature existed in the prior `MainWindow.java` GUI implementation, but has not yet been re-implemented in the `DynamicView.java` implementation.

**Saving/loading game state**

The GUI does not contain necessary routines to update correctly after serialized game state objects are loaded. The board and player data have successfully been saved and loaded, but the interface becomes unusable after this.

**CPU players**

A simple implementation of computer-controlled player.

&nbsp;

<div align="center">
<img src="documentation/res/bug-log-anim.gif">
<br>
<i>Thank you for your interest in (or grading of) my project!
<br>
</i>
<br>
</div>
