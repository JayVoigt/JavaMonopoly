package cc.jayv.monopoly3;

public class GUISwitchBoard {

    GameLogicController controller;

    public GUISwitchBoard(GameLogicController controller) {
        this.controller = controller;
    }


    public enum Actions {
        PROPERTY_PURCHASE,
        PROPERTY_AUCTION,

        JAIL_POSTBAIL,
        JAIL_ROLLDOUBLES,
        JAIL_USEGOOJFC,

        IMPROVEMENTS_BUILD_HOUSE,
        IMPROVEMENTS_SELL_HOUSE,
        IMPROVEMENTS_BUILD_HOTEL,
        IMPROVEMENTS_SELL_HOTEL,

    }

    public void actionHandler(Actions trigger) {
        System.out.println(trigger);
        switch (trigger) {
            case PROPERTY_AUCTION:
                System.out.println("prop auction");
                controller.playerDecisionPurchaseProperty();
                break;
            default:
                System.out.println("default");
        }
    }
}
