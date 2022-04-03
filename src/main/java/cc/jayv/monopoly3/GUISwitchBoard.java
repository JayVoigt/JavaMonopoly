package cc.jayv.monopoly3;

public class GUISwitchBoard {

    GameLogicController controller;

    public GUISwitchBoard(GameLogicController controller) {
        this.controller = controller;
    }


    public enum Actions {
        CONTROLS_ROLLDICE,
        CONTROLS_ENDTURN,

        // Property purchase dialog
        PROPERTY_PURCHASE,
        PROPERTY_AUCTION,

        // Mortgaging dialog
        PROPERTY_MORTGAGE,
        PROPERTY_UNMORTGAGE,

        // Show owned properties
        VIEW_SHOWPROPERTIES,

        // Improvements dialog
        IMPROVEMENTS_BUILD_HOUSE,
        IMPROVEMENTS_SELL_HOUSE,
        IMPROVEMENTS_BUILD_HOTEL,
        IMPROVEMENTS_SELL_HOTEL,

        // Jail dialog
        JAIL_POSTBAIL,
        JAIL_ROLLDOUBLES,
        JAIL_USEGOOJFC,


    }

    public void actionHandler(Actions trigger) {
        System.out.println(trigger);
        switch (trigger) {
            case PROPERTY_MORTGAGE: {}
            case PROPERTY_UNMORTGAGE: {}
        }
    }
}
