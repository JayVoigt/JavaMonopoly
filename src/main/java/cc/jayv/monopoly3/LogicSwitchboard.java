package cc.jayv.monopoly3;

public class LogicSwitchboard {

    GameLogicController c;

    public LogicSwitchboard(GameLogicController controller) {
        this.c = controller;
    }

    public void actionHandler(DynamicView.Actions trigger, int currentSpaceSelectionID) {
        System.out.println("switchboard : " + trigger);

        switch (trigger) {
            case CONTROLS_ENDTURN -> c.endTurnManager();
            case CONTROLS_ROLLDICE -> c.diceRollManager();

            case IMPROVEMENTS_BUILD_HOUSE -> c.improvementsManager(currentSpaceSelectionID, GameLogicController.ImprovementsActions.buildHouse);
            case IMPROVEMENTS_BUILD_HOTEL -> c.improvementsManager(currentSpaceSelectionID, GameLogicController.ImprovementsActions.buildHotel);
            case IMPROVEMENTS_SELL_HOUSE -> c.improvementsManager(currentSpaceSelectionID, GameLogicController.ImprovementsActions.sellHouse);
            case IMPROVEMENTS_SELL_HOTEL -> c.improvementsManager(currentSpaceSelectionID, GameLogicController.ImprovementsActions.sellHotel);

            case JAIL_POSTBAIL -> c.playerDecisionJailPostBail();
            case JAIL_ROLLDOUBLES -> c.playerDecisionJailRollDoubles();
            case JAIL_USEGOOJFC -> c.playerDecisionJailUseGOOJFC();

            case PROPERTY_PURCHASE -> c.playerDecisionPurchaseProperty();
            case PROPERTY_AUCTION -> c.playerDecisionAuction();
        }
    }
}