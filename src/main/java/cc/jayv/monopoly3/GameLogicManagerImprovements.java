package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.Objects;

public class GameLogicManagerImprovements extends GameLogicModule implements Serializable {
    
    public GameLogicManagerImprovements(GameLogicController gameLogicController) {
    }

    public void improvementsManager(int spaceID, DialogContainerImprovements.ActionsImprovements.ImprovementsActions inputAction) {
        Space localSpace = board.spaces.get(spaceID);
        Color localColor = null;

        if (localSpace instanceof Color) {
            localColor = (Color) localSpace;
        } else {
            System.err.println("Improvements: property not Color.");
        }

        // Build house
        if (inputAction.equals(DialogContainerImprovements.ActionsImprovements.ImprovementsActions.BUILD_HOUSE)) {
            if (Objects.requireNonNull(localColor).getIsEligibleForImprovements()) {
                // Maximum houses on space
                if (localColor.getHouseCount() == 4) {
                    controller.appendToGameLog(localColor.getFriendlyName() + " already has the maximum number of houses.");
                } else if (localColor.getHotelCount() == 1) {
                    controller.appendToGameLog(localColor.getFriendlyName() + " cannot have "
                            + "houses constructed when a hotel is already present.");
                } else if (currentPlayer.getCurrentBalance() >= localColor.getHouseCost()) {
                    if (board.getBankHouseCount() > 0) {
                        localColor.buildHouse();

                        // Debit player
                        currentPlayer.updateCurrentBalance(-1 * localColor.getHouseCost());
                    } else {
                        controller.appendToGameLog("There are no available houses to purchase from the Bank.");
                    }
                } else {
                    controller.appendToGameLog("You do not have sufficient funds to construct "
                            + "a house on this property.");
                }

            }
        }

        // Sell house
        else if (inputAction.equals(DialogContainerImprovements.ActionsImprovements.ImprovementsActions.SELL_HOUSE)) {
            if (Objects.requireNonNull(localColor).getHouseCount() > 0) {
                localColor.sellHouse();
                currentPlayer.updateCurrentBalance((int) (localColor.getHouseCost() * 0.5));
            } else {
                controller.appendToGameLog(localColor.getFriendlyName() + " does not have any houses to sell.");
            }
        }

        // Build hotel
        else if (inputAction.equals(DialogContainerImprovements.ActionsImprovements.ImprovementsActions.BUILD_HOTEL)) {
            if (Objects.requireNonNull(localColor).getIsEligibleForImprovements()) {
                if (localColor.getHouseCount() == 4) {
                    localColor.buildHotel();
                } else {
                    controller.appendToGameLog(localColor.getFriendlyName() + " does not have enough houses "
                            + "for a hotel to be constructed.");
                }
            } else {
                controller.appendToGameLog("You do not have sufficient funds to construct "
                        + "a hotel on this property.");
            }
        }

        // Sell hotel
        else if (inputAction.equals(DialogContainerImprovements.ActionsImprovements.ImprovementsActions.SELL_HOTEL)) {
            if (Objects.requireNonNull(localColor).getHotelCount() > 0) {
                localColor.sellHotel();
                currentPlayer.updateCurrentBalance((int) (localColor.getHotelCost() * 0.5));
            } else {
                controller.appendToGameLog(localColor.getFriendlyName() + " does not have any hotels to sell.");
            }
        }
    }
}