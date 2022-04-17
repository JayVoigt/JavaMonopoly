package cc.jayv.monopoly3;

/**
 * Indicates possible actions that the player can perform through the GUI.
 */
public enum Actions {
    CONTROLS_ROLLDICE,
    CONTROLS_ENDTURN,

    // Show dialogs and other information
    CONTROLS_SHOW_MORTGAGE,
    CONTROLS_SHOW_PROPERTIES,
    CONTROLS_SHOW_IMPROVEMENTS,
    CONTROLS_SHOW_STATISTICS,
    CONTROLS_SHOW_TRADE,
    CONTROLS_SHOW_FORFEIT,

    // Game-driven GUI events, such as mandatory jail or property purchase decisions.
    GAME_SHOW_JAIL,
    GAME_SHOW_PURCHASE,

    // Property purchase dialog
    PROPERTY_PURCHASE,
    PROPERTY_AUCTION,

    // Mortgaging dialog
    PROPERTY_MORTGAGE,
    PROPERTY_UNMORTGAGE,

    // Improvements dialog
    IMPROVEMENTS_BUILD_HOUSE,
    IMPROVEMENTS_SELL_HOUSE,
    IMPROVEMENTS_BUILD_HOTEL,
    IMPROVEMENTS_SELL_HOTEL,

    // Jail dialog
    JAIL_POSTBAIL,
    JAIL_ROLLDOUBLES,
    JAIL_USEGOOJFC,

    NEWGAME_STARTGAME,

    // Forfeit dialog
    FORFEIT_CONFIRM,
    FORFEIT_CANCEL
}