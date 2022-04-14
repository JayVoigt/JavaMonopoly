package cc.jayv.monopoly3;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class GameLogicSwitchboard implements Serializable {

    GameLogicController c;
    Object origin;
    JTextArea infoArea;

    public GameLogicSwitchboard(GameLogicController controller) {
        this.c = controller;
    }

    public void actionHandler(Actions trigger, int currentSpaceSelectionID) {
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

            case PROPERTY_MORTGAGE -> c.mortgageProperty(currentSpaceSelectionID, GameLogicController.MortgageActions.MORTGAGE);
            case PROPERTY_UNMORTGAGE -> c.mortgageProperty(currentSpaceSelectionID, GameLogicController.MortgageActions.UNMORTGAGE);

            case FORFEIT_CONFIRM -> c.forfeitManager();
        }
    }

    public void setOrigin(Object origin) {
        this.origin = origin;

        infoArea = null;
        if (origin instanceof TemplateDialogButtonProperties b) {
            JRootPane rootPane = b.getRootPane();

            for (Component jc : rootPane.getContentPane().getComponents()) {
                // Null-safe equals
                if (Objects.equals(jc.getName(), "infoArea")) {
                    infoArea = (JTextArea) jc;
                }
            }
        }
    }

    private void updateInfoArea(String content) {
        if (infoArea != null) {
            infoArea.setText(content);
        }
    }
}