package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.Objects;

public class GDEDC implements Serializable {
    Board board;
    Player currentPlayer;
    Space currentSpace;

    private final GameLogicController gameLogicController;

    public void updateReferences(Board board, Player currentPlayer, Space currentSpace) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.currentSpace = currentSpace;
    }

    public GDEDC(GameLogicController gameLogicController, Board board, Player currentPlayer) {
        this.gameLogicController = gameLogicController;
        this.board = board;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Evaluator which executes when the landed space is of
     * <code>DrawCard</code> type.
     */
    void drawCardEvaluator() {
        int randomCardID = (int) (Math.random() * board.chanceCards.size());
        String gameLogDrawPrefix;
        DrawCard currentDrawCard;

        if (Objects.equals(currentSpace.getFriendlyName(), "Chance")) {
            currentDrawCard = board.chanceCards.get(randomCardID);
            gameLogDrawPrefix = "The Chance card reads: ";
        } else {
            currentDrawCard = board.communityChestCards.get(randomCardID);
            gameLogDrawPrefix = "The Community Chest card reads: ";
        }

        gameLogicController.appendToGameLog(gameLogDrawPrefix + currentDrawCard.getMessage());

        if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.TELEPORT) {
            int forwardMovementQuantity = (currentDrawCard.getDestinationSpace() - currentPlayer.getCurrentPosition());
            gameLogicController.movementEvaluatorAdvanced(true, forwardMovementQuantity);
        } else if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.TELEPORT_RELATIVE) {
            if (currentDrawCard.getDestinationRelativeType().equals(DrawCard.DestinationRelativeTypeKeys.BACK_THREE_SPACES)) {

            }
            // Advance to the nearest railroad
            else if (currentDrawCard.getDestinationRelativeType().equals(DrawCard.DestinationRelativeTypeKeys.RAILROAD)) {
                Railroad destinationRailroad = null;
                for (Space s : board.spaces) {
                    if (s.getID() > currentSpace.getID()) {
                        if (s instanceof Railroad) {
                            destinationRailroad = (Railroad) currentSpace;
                        }
                    }
                }
                if (destinationRailroad != null) {
                    currentPlayer.advancePosition(destinationRailroad.getID() - currentSpace.getID());
                } else {
                    currentPlayer.advancePosition(board.spaces.get(5).getID() - currentSpace.getID());
                }
            } else if (currentDrawCard.getDestinationRelativeType().equals(DrawCard.DestinationRelativeTypeKeys.UTILITY)) {
                Utility destinationUtility = null;
                for (Space s : board.spaces) {
                    if (s.getID() > currentSpace.getID()) {
                        if (s instanceof Utility) {
                            destinationUtility = (Utility) currentSpace;
                        }
                    }
                }
                if (destinationUtility != null) {
                    currentPlayer.advancePosition(destinationUtility.getID() - currentSpace.getID());
                } else {
                    currentPlayer.advancePosition(board.spaces.get(12).getID() - currentSpace.getID());
                }
            }
        } else if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.TELEPORT_WITH_RENT_MODIFIER) {

        } else if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.BALANCE_UPDATE) {
            currentPlayer.updateCurrentBalance(currentDrawCard.getQuantity());
        } else if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.GET_OUT_OF_JAIL_FREE_CARD) {
            currentPlayer.setGetOutOfJailFreeCardCount(currentPlayer.getGetOutOfJailFreeCardCount() + 1);
        } else if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.JAIL_PLAYER) {
            gameLogicController.jailPlayer();
        } else if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.IMPROVEMENT_REPAIRS) {
            int ownedHouses = 0, ownedHotels = 0;
            int paymentQuantity;

            for (Space s : board.spaces) {
                if (s instanceof Color localColor) {
                    if (currentPlayer.getOwnsPropertyByID(localColor.getID())) {
                        ownedHouses += localColor.getHouseCount();
                        ownedHotels += localColor.getHotelCount();
                    }
                }
            }

            paymentQuantity = ((board.getRepairCostHouse() * ownedHouses) + (board.getRepairCostHotel() * ownedHotels));

            if ((ownedHouses + ownedHotels) == 0) {
                gameLogicController.appendToGameLog(currentPlayer.getCustomName() + " owns no improvements."
                        + " No payment needs to be made.");
            } else {
                gameLogicController.appendToGameLog(currentPlayer.getCustomName() + " owns " + ownedHouses
                        + " houses and " + ownedHotels + " hotels. A payment of $" + paymentQuantity
                        + " is required.");

                currentPlayer.updateCurrentBalance(-1 * paymentQuantity);
            }
        } else if (currentDrawCard.getDrawCardType() == DrawCard.DrawCardTypeKeys.DISTRIBUTED_BALANCE_UPDATE) {

        }
    }
}