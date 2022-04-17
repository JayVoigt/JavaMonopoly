package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.Objects;

public class GameLogicEvaluatorDrawCard implements Serializable {
    Board board;
    Player currentPlayer;
    Space currentSpace;
    DrawCard currentDrawCard;

    private final GameLogicController gameLogicController;

    public void updateReferences(Board board, Player currentPlayer, Space currentSpace) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.currentSpace = currentSpace;
    }

    public GameLogicEvaluatorDrawCard(GameLogicController gameLogicController, Board board, Player currentPlayer) {
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

        if (Objects.equals(currentSpace.getFriendlyName(), "Chance")) {
            currentDrawCard = board.chanceCards.get(randomCardID);
            gameLogDrawPrefix = "The Chance card reads: ";
        } else {
            currentDrawCard = board.communityChestCards.get(randomCardID);
            gameLogDrawPrefix = "The Community Chest card reads: ";
        }

        gameLogicController.appendToGameLog(gameLogDrawPrefix + currentDrawCard.getMessage());

        switch (currentDrawCard.getDrawCardType()) {
            case TELEPORT -> {
                int forwardMovementQuantity = (currentDrawCard.getDestinationSpace() - currentPlayer.getCurrentPosition());
                gameLogicController.movementEvaluatorAdvanced(true, forwardMovementQuantity);
            }
            case TELEPORT_RELATIVE -> eventTeleportRelative();
            case TELEPORT_WITH_RENT_MODIFIER -> eventTeleportWithRentModifier();
            case IMPROVEMENT_REPAIRS -> eventImprovementRepairs();
            case BALANCE_UPDATE -> currentPlayer.updateCurrentBalance(currentDrawCard.getQuantity());
            case GET_OUT_OF_JAIL_FREE_CARD -> currentPlayer.setGetOutOfJailFreeCardCount(currentPlayer.getGetOutOfJailFreeCardCount() + 1);
            case JAIL_PLAYER -> gameLogicController.jailPlayer();
            case DISTRIBUTED_BALANCE_UPDATE -> eventDistributedBalanceUpdate();
        }

    }

    private void eventImprovementRepairs() {
        int ownedHouses = 0, ownedHotels = 0;
        int paymentQuantity;

        // Determine number of improvements the player owns
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

            // Deduct payment
            currentPlayer.updateCurrentBalance(-1 * paymentQuantity);
        }
    }

    private void eventDistributedBalanceUpdate() {
        int distributionCount = 0;

        // Hacky solution, really not ideal
        if (currentDrawCard.getMessage().contains("Chairman")) {
            for (Player p : board.players) {
                if (p.getIsActive()) {
                    gameLogicController.appendToGameLog(p.getCustomName() + " has been paid $50.");
                    p.updateCurrentBalance(50);
                    distributionCount++;
                }
            }

            gameLogicController.appendToGameLog(currentPlayer.getCustomName() + " has paid a total of " + distributionCount * 50 + " to all other players..");
            currentPlayer.updateCurrentBalance(distributionCount * -50);
        }
        else {
            for (Player p : board.players) {
                if (p.getIsActive()) {
                    gameLogicController.appendToGameLog(p.getCustomName() + " has paid $10 to " + currentPlayer.getCustomName() + ".");
                    p.updateCurrentBalance(-10);
                    distributionCount++;
                }
            }

            gameLogicController.appendToGameLog(currentPlayer.getCustomName() + " has received a total of " + distributionCount * 10 + " from all other players.");
            currentPlayer.updateCurrentBalance(distributionCount * 10);
        }
    }

    private void eventTeleportWithRentModifier() {

    }

    private void eventTeleportRelative() {
        switch (currentDrawCard.getDestinationRelativeType()) {
            case BACK_THREE_SPACES -> {
                currentPlayer.advancePosition(-3);
            }   // end case BACK_THREE_SPACES

            case RAILROAD -> {
                Railroad destinationRailroad = null;
                for (Space s : board.spaces) {
                    if ((s.getID() > currentSpace.getID()) && (s instanceof Railroad)) {
                        destinationRailroad = (Railroad) currentSpace;
                        break;
                    }
                }
                if (destinationRailroad != null) {
                    currentPlayer.advancePosition(destinationRailroad.getID() - currentSpace.getID());
                } else {
                    // Reading Railroad is the first railroad on the board, ID 5
                    currentPlayer.advancePosition(board.spaces.get(5).getID() - currentSpace.getID());
                }
            }   // end case RAILROAD

            case UTILITY -> {
                Utility destinationUtility = null;
                for (Space s : board.spaces) {
                    if ((s.getID() > currentSpace.getID()) && (s instanceof Utility)) {
                        destinationUtility = (Utility) currentSpace;
                        break;
                    }
                }
                if (destinationUtility != null) {
                    currentPlayer.advancePosition(destinationUtility.getID() - currentSpace.getID());
                } else {
                    // Electric Company is the first utility on the board, ID 12
                    currentPlayer.advancePosition(board.spaces.get(12).getID() - currentSpace.getID());
                }
            }   // end case UTILITY

        }   // end switch
    }
}