package cc.jayv.monopoly3;

import java.io.Serializable;

/**
 * Controls game logic and updates data within the Board accordingly.
 *
 * @author jay
 */
public class GameLogicController implements Serializable {

    Board board;

    Player currentPlayer;

    Space currentSpace;
    GameEvent currentGameEvent;
    Property currentProperty;

    boolean isGameActive;

    boolean useExtraTextPadding;

    int playersCount,
            turnCounter;

    boolean playerCanBuildImprovements;

    LogHelper logHelper;

    boolean victoryConditionMet;
    private final GameLogicEvaluatorDrawCard GameLogicEvaluatorDrawCard = new GameLogicEvaluatorDrawCard();
    private final GameLogicManagerImprovements gameLogicManagerImprovements = new GameLogicManagerImprovements(this);
    private final GameLogicTools gameLogicTools = new GameLogicTools();

    public GameLogicController(Board inputBoard, LogHelper inputLogHelper) {
        board = inputBoard;
        logHelper = inputLogHelper;

        useExtraTextPadding = true;

        playerCanBuildImprovements = false;
        victoryConditionMet = false;
    }

    // <editor-fold desc="Setters and getters">
    public boolean getIsGameActive() {
        return isGameActive;
    }

    public void setIsGameActive(boolean input) {
        isGameActive = input;
    }

    public void setPlayersCount(int input) {
        playersCount = input;
    }

    public void setPlayerCanBuildImprovements(boolean input) {
        playerCanBuildImprovements = input;
    }

    public boolean getIsVictoryConditionMet() {
        return victoryConditionMet;
    }

    /**
     * Append the input to the game log as a formatted line.<br>
     * Entries are prefixed with the current date and time, as well as the
     * current turn number.<br>
     *
     * @param input The contents of the message.
     */
    public void appendToGameLog(String input) {
        logHelper.appendToGameLog(input, turnCounter);
    }

    /**
     * Send a message to the game log when the game has started, including
     * information about the number of players specified.
     */
    public void sendInitGameMessage() {
        appendToGameLog("A new game has been started with " + playersCount + " players.");
    }

    /**
     * Evaluator which is run at the beginning of a player's turn.<br>
     * Implements which decision path to execute given the player's condition.
     */
    public void initialEvaluator() {
        logHelper.appendToDebugLog("-> executing initialEvaluator");
        turnCounter++;
        currentPlayer = board.players.get(board.getCurrentPlayerID());

        board.forceBoardSelfCheck();
        logHelper.addNewLine();

        appendToGameLog("It is now " + currentPlayer.getCustomName() + "'s turn.");
        currentPlayer.evaluateState();

        if (currentPlayer.getIsJailed()) {
            currentPlayer.incrementConsecutiveTurnsJailed();
            jailStateEvaluator();
        } else if (currentPlayer.getIsBankrupt()) {
            bankruptStateEvaluator();
        } else {
            normalTurnEvaluator();
        }
    }

    /**
     * Evaluator which is run when the current player is currently in Jail.
     */
    private void jailStateEvaluator() {
        logHelper.appendToDebugLog("-> executing jailStateEvaluator");
        appendToGameLog(currentPlayer.getCustomName() + " is jailed!");

    }

    private void bankruptStateEvaluator() {
        currentPlayer.setActionLockedEndTurn(true);
        currentPlayer.setActionLockedRollDice(true);

        logHelper.appendToGameLog(currentPlayer.getCustomName() + " is bankrupt!");
    }

    /**
     * Evaluator which is run if no special conditions apply to the current
     * player.
     */
    private void normalTurnEvaluator() {
        logHelper.appendToDebugLog("-> executing normalTurnEvaluator");
        if (currentPlayer.hasRolledDice) {
            currentPlayer.setIsInMAEState(true);
            maeStateEvaluator();
        } else {
            logHelper.appendToDebugLog("\t Suggested action: unlock rollDice");
            currentPlayer.setActionLockedRollDice(false);
        }
    }

    /**
     * Evaluator which is run when the player is in mandatory actions exhausted
     * (MAE) state.<br>
     * This occurs when the player has no remaining mandatory actions, such as
     * rolling the dice or ending their turn, but they are still permitted to
     * perform optional actions, such as mortgaging a property or building
     * improvements.
     */
    private void maeStateEvaluator() {
        logHelper.appendToDebugLog("-> executing maeStateEvaluator");
        currentPlayer.setActionLockedEndTurn(false);
        currentPlayer.setActionLockedRollDice(true);

        if (currentPlayer.getHasRolledDoubles()) {
            currentPlayer.setActionLockedRollDice(false);
            currentPlayer.setActionLockedEndTurn(true);
        }

        if (currentPlayer.getRequiredDecisionPropertyAction()) {
            if (currentPlayer.getMadeDecisionPropertyAction()) {
                logHelper.appendToDebugLog("\t Suggested action: unlock endTurn");
                logHelper.appendToDebugLog("\t\t Reason: Player has made property decision.");
                currentPlayer.setActionLockedEndTurn(false);
            } else {
                //propertyPurchaseManager();
            }
        } else {
            if (currentPlayer.getHasRolledDice()) {
                logHelper.appendToDebugLog("\t Suggested action: unlock endTurn");
                logHelper.appendToDebugLog("\t\t Reason: No property decision necessary.");
                currentPlayer.setActionLockedEndTurn(false);
            } else {
                logHelper.appendToDebugLog("\t Suggested action: unlock rollDice");
                logHelper.appendToDebugLog("\t\t Reason: Player has not rolled dice.");
                currentPlayer.setActionLockedRollDice(false);
            }
        }
    }

    /**
     * Manager which is executed when the player decides to purchase a property.
     */
    private void propertyPurchaseManager() {
        logHelper.appendToDebugLog("-> executing propertyPurchaseManager");
        currentPlayer.setRequiredDecisionPropertyAction(false);
        currentPlayer.setMadeDecisionPropertyAction(true);
    }

    /**
     * Manager which is executed when the current player rolls the dice.<br>
     * Note: this does not execute for special dice roll conditions, such as
     * rolling for doubles when jailed, or rolling to determine the rent paid
     * when landing on an owned Utility.
     */
    public void diceRollManager() {
        logHelper.appendToDebugLog("-> executing diceRollManager");
        currentPlayer = board.players.get(board.getCurrentPlayerID());

        currentPlayer.rollDice();

        String doublesSuffix;
        if (currentPlayer.getHasRolledDoubles()) {
            doublesSuffix = " Doubles!";
        } else {
            doublesSuffix = "";
        }

        appendToGameLog(currentPlayer.getCustomName() + " rolled (" + currentPlayer.getDie1() + "," + currentPlayer.getDie2() + ")." + doublesSuffix);

        // Reset roll dice condition for doubles
        currentPlayer.setHasRolledDice(!currentPlayer.getHasRolledDoubles());

        if (currentPlayer.getConsecutiveDoublesCount() >= 3) {
            appendToGameLog(currentPlayer.getCustomName() + " has rolled doubles 3 times, and is now jailed for speeding!");
            jailPlayer();
        } else {
            movementEvaluator();
        }
    }

    /**
     * Ready the state of the current player to be jailed; jail the player.
     */
    void jailPlayer() {
        currentPlayer.setIsJailed(true);
        currentPlayer.setHasRolledDice(true);
        currentPlayer.setActionLockedEndTurn(false);
        currentPlayer.setActionLockedRollDice(true);
        currentPlayer.setInitialJailTurn(true);
    }

    /**
     * Ready the state of the current player to be jailed; jail the player.<br>
     * Jailing a player through the Game Editor requires the special condition that
     * "initialJailTurn" must be false.
     */
    public void gameEditorJailPlayer(Player player) {
        gameLogicTools.attachLogHelper(logHelper);
        gameLogicTools.attachReferences(board, this, currentSpace, currentPlayer);
        gameLogicTools.gameEditorJailPlayer(player);
    }

    /**
     * Evaluator which executes when the current player rolls the dice, and a
     * roll quantity is determined.<br>
     * Determines where the player should move, and then executes the
     * appropriate evaluator for the destination space.
     */
    private void movementEvaluator() {
        logHelper.appendToDebugLog("-> executing movementEvaluator");
        int diceSum = currentPlayer.getDiceSum();

        boolean playerPassedGo = currentPlayer.advancePosition(diceSum);
        currentPlayer.setAnimateMovement(true);

        // Issue GO bonus
        if (playerPassedGo) {
            currentPlayer.updateCurrentBalance(200);
            appendToGameLog(currentPlayer.getCustomName() + " has passed or landed on GO, and is rewarded a bonus of $200.");
        }

        currentSpace = board.spaces.get(currentPlayer.getCurrentPosition());
        currentSpace.incrementTimesLanded();

        appendToGameLog(currentPlayer.getCustomName() + " has advanced to " + currentSpace.getFriendlyName() + ".");

        if (currentSpace.getSpaceType().equals(Space.spaceTypeKeys.gameEvent)) {
            gameEventEvaluator();
        } else {
            propertyEvaluator();
        }
        maeStateEvaluator();
    }

    /**
     * An advanced evaluator, derived from <code>movementEvaluator</code>, which
     * executes when more precise movement of a player's position is
     * required.<br>
     * This occurs when a player requires a <code>teleportRelative</code> event
     * from <code>DrawCard</code> to be executed.
     *
     * @param collectGoBonus   Whether the player should collect a bonus if they
     *                         pass or land on GO.
     * @param movementQuantity The number of spaces to move.
     */
    protected void movementEvaluatorAdvanced(boolean collectGoBonus, int movementQuantity) {
        logHelper.appendToDebugLog("-> executing movementEvaluatorAdvanced");
        boolean playerPassedGo = currentPlayer.advancePosition(movementQuantity);

        // Issue GO bonus
        if ((playerPassedGo) && (collectGoBonus)) {
            currentPlayer.updateCurrentBalance(200);
            appendToGameLog(currentPlayer.getCustomName() + " has passed or landed on GO, and is rewarded a bonus of $200.");
        }

        currentSpace = board.spaces.get(currentPlayer.getCurrentPosition());
        currentSpace.incrementTimesLanded();

        appendToGameLog(currentPlayer.getCustomName() + " has advanced to " + currentSpace.getFriendlyName() + ".");

        if (currentSpace.getSpaceType().equals(Space.spaceTypeKeys.gameEvent)) {
            gameEventEvaluator();
        } else {
            propertyEvaluator();
        }
        maeStateEvaluator();
    }

    /**
     * Evaluator which executes when the landed space is of
     * <code>GameEvent</code> type.
     */
    private void gameEventEvaluator() {
        logHelper.appendToDebugLog("-> executing gameEventEvaluator");

        currentGameEvent = (GameEvent) currentSpace;
        GameEvent.GameEventTypeKeys localGameEventType = currentGameEvent.getGameEventType();

        switch (localGameEventType) {
            case DRAW_CARD ->  {
                GameLogicEvaluatorDrawCard.attachReferences(board, this, currentSpace, currentPlayer);
                GameLogicEvaluatorDrawCard.drawCardEvaluator();
            }

            case JAIL_PLAYER -> jailPlayer();

            case TAX -> {
                if (currentGameEvent.getFriendlyName().contains("Income")) {
                    appendToGameLog(currentPlayer.getCustomName() + " has paid $200 in Income Tax.");
                    currentPlayer.updateCurrentBalance(-200);
                }

                // Luxury Tax is the only other tax GameEvent
                else {
                    appendToGameLog(currentPlayer.getCustomName() + " has paid $100 in Luxury Tax.");
                    currentPlayer.updateCurrentBalance(-100);
                }
            }
        }
        maeStateEvaluator();
    }

    /**
     * Evaluator which executes when the landed space is of
     * <code>Property</code> type.
     */
    private void propertyEvaluator() {
        logHelper.appendToDebugLog("-> executing propertyEvaluator");
        currentProperty = (Property) currentSpace;

        // Property not owned
        if (!currentProperty.getIsOwned()) {
            appendToGameLog(currentProperty.getFriendlyName() + " is not owned.");
            currentPlayer.setRequiredDecisionPropertyAction(true);
            currentPlayer.setMadeDecisionPropertyAction(false);
        }
        // Property owned
        else {
            appendToGameLog(currentProperty.getFriendlyName() + " is owned by " + board.players.get(currentProperty.getOwnerID()).getCustomName() + ".");
            currentPlayer.setRequiredDecisionPropertyAction(false);
            currentPlayer.setMadeDecisionPropertyAction(true);

            int rentOwed = currentProperty.calculateRent();
            Player propertyOwner = board.players.get(currentProperty.getOwnerID());

            if (propertyOwner != currentPlayer) {
                // Pay rent
                if (currentPlayer.getCurrentBalance() < rentOwed) {
                    currentPlayer.setIsBankrupt(true);
                    currentPlayer.setCreditorPlayerID(propertyOwner.getPlayerID());
                    bankruptStateEvaluator();
                }
                currentPlayer.updateCurrentBalance(-1 * rentOwed);
                propertyOwner.updateCurrentBalance(rentOwed);

                appendToGameLog(currentPlayer.getCustomName() + " has paid $" + rentOwed + " in rent to " + propertyOwner.getCustomName() + ".");
            } else {
                appendToGameLog("No rent needs to be paid.");
            }
        }
    }

    /**
     * Manager which executes when the current player ends their turn.
     */
    public void endTurnManager() {
        logHelper.appendToDebugLog("-> executing endTurnManager");
        currentPlayer = board.players.get(board.getCurrentPlayerID());
        currentPlayer.setActionLockedEndTurn(true);

        appendToGameLog(currentPlayer.getCustomName() + " has ended their turn.");

        if (board.getCurrentPlayerID() == board.getNextActivePlayerID()) {
            victoryConditionMet();
        }
        board.setCurrentPlayerID(board.getNextActivePlayerID());
        currentPlayer = board.players.get(board.getCurrentPlayerID());

        logHelper.appendToDebugLog("\t currentPlayerID: " + board.getCurrentPlayerID());
        currentPlayer.initializePlayerForNewTurn();
        initialEvaluator();
    }

    /**
     * Decision evaluator which executes when the current player decides to
     * purchase a property.
     */
    public void playerDecisionPurchaseProperty() {
        logHelper.appendToDebugLog("-> executing playerDecisionPurchaseProperty");
        currentPlayer.setMadeDecisionPropertyAction(true);
        currentPlayer.setResultDecisionPropertyAction(true);

        if (!currentPlayer.getIsJailed()) {
            if (currentPlayer.getCurrentBalance() >= currentProperty.getPurchaseCost()) {
                currentProperty.setIsOwned(true);
                currentProperty.setOwnerID(currentPlayer.getPlayerID());

                currentPlayer.updateCurrentBalance(-1 * currentProperty.getPurchaseCost());
                currentPlayer.setPropertyOwnedState(true, currentProperty.getID());

                currentPlayer.setRequiredDecisionPropertyAction(false);
                currentPlayer.setMadeDecisionPropertyAction(true);
                appendToGameLog(currentPlayer.getCustomName() + " has purchased " + currentProperty.getFriendlyName() + " for $" + currentProperty.getPurchaseCost() + ".");
            } else {
                appendToGameLog(currentPlayer.getCustomName() + " cannot afford to purchase " + currentSpace.getFriendlyName() + ".");
            }
        }
        else {
            appendToGameLog("You cannot purchase property from within jail!");
            currentPlayer.setRequiredDecisionPropertyAction(false);
            currentPlayer.setMadeDecisionPropertyAction(true);
        }
    }

    /**
     * Decision evaluator which executes when the player decides to send a
     * landed property to auction.<br>
     * <b>Not implemented.</b>
     */
    public void playerDecisionAuction() {
        logHelper.appendToDebugLog("-> executing playerDecisionAuction");
    }

    /**
     * Decision evaluator which executes when the current player decides to post
     * bail when jailed.
     */
    public void playerDecisionJailPostBail() {
        currentPlayer.setMadeDecisionJail(true);
        if (currentPlayer.getCurrentBalance() >= 50) {
            currentPlayer.updateCurrentBalance(-50);
            appendToGameLog(currentPlayer.getCustomName() + " has posted bail for $50.");
            readyPlayerForJailRelease();
        } else {
            appendToGameLog(currentPlayer.getCustomName() + " cannot afford to post bail.");
        }
    }

    /**
     * Decision evaluator which executes when the current player decides to roll
     * for doubles when jailed.
     */
    public void playerDecisionJailRollDoubles() {
        currentPlayer.setMadeDecisionJail(true);
        currentPlayer.rollDice();

        if (currentPlayer.getHasRolledDoubles()) {
            appendToGameLog(currentPlayer.getCustomName() + " has rolled doubles, and is no longer jailed!");
            readyPlayerForJailRelease();
        } else {
            if (currentPlayer.getConsecutiveTurnsJailed() < 3) {
                appendToGameLog(currentPlayer.getCustomName() + " has not rolled doubles, and will remain jailed.");
                currentPlayer.setRequiredDecisionPostedBail(false);
                currentPlayer.setInitialJailTurn(true);
                currentPlayer.setActionLockedEndTurn(false);
            } else {
                appendToGameLog(currentPlayer.getCustomName() + " has failed to roll doubles after 3 tries. Posting bail for $50 is now required.");
                logHelper.appendToDebugLog("[NOTICE]: need to implement edge case logic where balance < $50");
                currentPlayer.updateCurrentBalance(-50);
                readyPlayerForJailRelease();
            }
        }
    }

    public void playerDecisionJailUseGOOJFC() {
        currentPlayer.setMadeDecisionJail(true);

        if (currentPlayer.getGetOutOfJailFreeCardCount() > 0) {
            currentPlayer.setGetOutOfJailFreeCardCount(currentPlayer.getGetOutOfJailFreeCardCount() - 1);

            appendToGameLog(currentPlayer.getCustomName() + " has used a Get Out of Jail Free Card.");
            readyPlayerForJailRelease();
        } else {
            appendToGameLog(currentPlayer.getCustomName() + " does not have a Get Out of Jail Free Card.");
        }
    }

    /**
     * Ready the state of the current player to be released from jail; release
     * the player from jail.
     */
    public void readyPlayerForJailRelease() {

        // Should clean up this method by moving some logic into Player.unjailPlayer()
        currentPlayer.initializePlayerForNewTurn();

        currentPlayer.setRequiredDecisionPostedBail(false);
        currentPlayer.setIsJailed(false);

        currentPlayer.setPosition(10);

        currentPlayer.unjailPlayer();

        currentPlayer.setActionLockedEndTurn(true);
        currentPlayer.setActionLockedRollDice(false);
    }

    /**
     * Manager which is executed when the player decides to forfeit the
     * game.<br>
     * <b>Not implemented.</b>
     */
    public void forfeitManager() {
        Player creditorPlayer = board.players.get(currentPlayer.getCreditorPlayerID());

        // Ensure that creditorPlayer is valid and that the forfeit-requesting player is able to forfeit
        if ((creditorPlayer.getPlayerID() != 0) && (currentPlayer.getCurrentBalance() < 0)) {
            // Transfer all property assets
            for (Property p : board.getSpacesByOwnerID(currentPlayer.getPlayerID())) {
                p.setOwnerID(creditorPlayer.getPlayerID());
            }

            // Transfer GOOJFCs
            if (currentPlayer.getGetOutOfJailFreeCardCount() > 0) {
                // goofy looking line of code
                creditorPlayer.setGetOutOfJailFreeCardCount(creditorPlayer.getGetOutOfJailFreeCardCount() + currentPlayer.getGetOutOfJailFreeCardCount());
            }

            appendToGameLog(currentPlayer.getCustomName() + " has forfeited the game, and their assets have" +
                    "been transferred to " + creditorPlayer.getCustomName() + "!");
            currentPlayer.setHasRequestedEndTurn(true);
            endTurnManager();
            currentPlayer.setIsPlayerActive(false);
        }

    }

    public void debugToolsGiveAllProperties(int playerID) {
        gameLogicTools.attachLogHelper(logHelper);
        gameLogicTools.attachReferences(board, this, currentSpace, currentPlayer);
        gameLogicTools.debugToolsGiveAllProperties(playerID);
    }

    public void debugToolsRandomlyDistributeAllProperties() {
        gameLogicTools.attachLogHelper(logHelper);
        gameLogicTools.attachReferences(board, this, currentSpace, currentPlayer);
        gameLogicTools.debugToolsRandomlyDistributeAllProperties();
    }

    public void improvementsManager(int spaceID, DialogContainerImprovements.ActionsImprovements.ImprovementsActions inputAction) {
        gameLogicManagerImprovements.attachReferences(board, this, currentSpace, currentPlayer);
        gameLogicManagerImprovements.improvementsManager(spaceID, inputAction);
    }

    public void mortgageProperty(int spaceID, DialogContainerMortgage.ActionsMortgage.MortgageActions inputAction) {
        Space localSpace = board.spaces.get(spaceID);

        int balanceUpdate;

        if (localSpace instanceof Property p) {
            if (inputAction == DialogContainerMortgage.ActionsMortgage.MortgageActions.MORTGAGE) {
                p.setIsMortgaged(true);
                balanceUpdate = p.getMortgageValue();
            } else {
                p.setIsMortgaged(false);
                balanceUpdate = (-1) * p.getUnmortgageCost();
            }

            // Update balance of player who owns property
            board.players.get(p.getOwnerID()).updateCurrentBalance(balanceUpdate);
        }
    }

    private void victoryConditionMet() {
        if (board.getNextActivePlayerID() == currentPlayer.getPlayerID()) {
            appendToGameLog("Congratulations! " + currentPlayer.getCustomName() + " is the only remaining" +
                    " player in the game, and has won!");

            victoryConditionMet = true;
        }
    }

}
