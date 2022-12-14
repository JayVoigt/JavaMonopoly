package cc.jayv.monopoly3;


import javax.swing.*;
import java.io.Serializable;

/**
 * @author jay
 */
public class Player implements Serializable {
    // <editor-fold desc="Attributes">

    int playerID;

    // customName allows players to use a name other than the default
    // "Player n".
    String customName;
    ImageIcon preferredIcon;

    int currentBalance;

    public int getPreviousBalance() {
        return previousBalance;
    }

    int previousBalance;
    int currentPosition;
    int previousPosition;
    boolean isPlayerActive,
            isComputerControlled;
    // How many times the player has rolled doubles on a single turn.
    int consecutiveDoublesCount;
    int consecutiveTurnsJailed;
    // Sets of boolean values whose indices match to the corresponding
    // attribute of the space sharing the same ID.
    boolean[] ownedPropertyIDs;
    boolean[] mortgagedPropertyIDs;
    // Number that appears on the face of the dice
    int die1, die2;
    int getOutOfJailFreeCardCount;
    boolean animateMovement;
    // In a bankruptcy state, the player who is owed money/assets
    int creditorPlayerID;
    boolean isBankrupt;
    // <editor-fold desc="State attributes">
    // Basic state parameters
    boolean hasRequestedEndTurn,
            hasRolledDice,
            isInMandatoryActionsExhaustedState,
            isJailed,
            initialJailTurn;
    // Decisions given by GUI prompts
    boolean requiredDecisionPostedBail,
            madeDecisionJail,
            resultDecisionJail,
            requiredDecisionPropertyAction,
            madeDecisionPropertyAction,
            resultDecisionPropertyAction;
    boolean hasRolledDoubles;
    // Required state of GUI elements during player's turn
    boolean actionLockedRollDice,
            actionLockedEndTurn;

    public int getOwnedRailroads() {
        return ownedRailroads;
    }

    public void setOwnedRailroads(int ownedRailroads) {
        this.ownedRailroads = ownedRailroads;
    }

    int ownedRailroads;
    // <editor-fold desc="Constructor">
    Player() {
        playerID = 0;
        customName = "";
        preferredIcon = SwingHelper.getImageIconFromResource("/player-generic.png");

        // The default balance for players is $1500.
        currentBalance = 1500;
        previousBalance = 1500;

        // Players start at GO, with space ID 0
        currentPosition = 0;
        previousPosition = 0;
        ownedRailroads = 0;

        isPlayerActive = false;
        isComputerControlled = false;
        isBankrupt = false;
        creditorPlayerID = 0;

        consecutiveDoublesCount = 0;

        ownedPropertyIDs = new boolean[40];
        mortgagedPropertyIDs = new boolean[40];
        for (int i = 0; i < 40; i++) {
            ownedPropertyIDs[i] = false;
            mortgagedPropertyIDs[i] = false;
        }

        die1 = 0;
        die2 = 0;

        animateMovement = false;
    }
    // </editor-fold>

    public boolean getIsActive() {
        return isPlayerActive;
    }

    public boolean isAnimateMovement() {
        return animateMovement;
    }

    public void setAnimateMovement(boolean animateMovement) {
        this.animateMovement = animateMovement;
    }

    public int getCreditorPlayerID() {
        return creditorPlayerID;
    }

    // </editor-fold>

    public void setCreditorPlayerID(int creditorPlayerID) {
        this.creditorPlayerID = creditorPlayerID;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters and getters">
    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int inputPlayerID) {
        playerID = inputPlayerID;
    }

    public void setIsPlayerActive(boolean input) {
        isPlayerActive = input;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String inputName) {
        customName = inputName;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int inputCurrentBalance) {
        previousBalance = currentBalance;
        currentBalance = inputCurrentBalance;
    }

    public void updateCurrentBalance(int change) {
        previousBalance = currentBalance;
        currentBalance += change;
        evaluateState();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public boolean getIsComputerControlled() {
        return isComputerControlled;
    }

    public void setIsComputerControlled(boolean input) {
        isComputerControlled = input;
    }

    public int getConsecutiveDoublesCount() {
        return consecutiveDoublesCount;
    }

    public int getConsecutiveTurnsJailed() {
        return consecutiveTurnsJailed;
    }

    public int getDiceSum() {
        return die1 + die2;
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

    public void setPropertyOwnedState(boolean inputOwned, int inputPropertyID) {
        ownedPropertyIDs[inputPropertyID] = inputOwned;
    }

    public void setPropertyMortgagedState(boolean inputMortgaged, int inputPropertyID) {
        mortgagedPropertyIDs[inputPropertyID] = inputMortgaged;
    }

    public boolean[] getOwnedPropertyIDs() {
        return ownedPropertyIDs;
    }

    public boolean getOwnsPropertyByID(int inputPropertyID) {
        return ownedPropertyIDs[inputPropertyID];
    }

    public boolean[] getMortgagedPropertyIDs() {
        return mortgagedPropertyIDs;
    }

    public int getGetOutOfJailFreeCardCount() {
        return getOutOfJailFreeCardCount;
    }

    public void setGetOutOfJailFreeCardCount(int inputCount) {
        getOutOfJailFreeCardCount = inputCount;
    }
    // </editor-fold>

    // <editor-fold desc="Setters and getters for state attributes">
    public boolean getHasRequestedEndTurn() {
        return hasRequestedEndTurn;
    }

    public void setHasRequestedEndTurn(boolean input) {
        hasRequestedEndTurn = input;
    }

    public boolean getHasRolledDice() {
        return hasRolledDice;
    }

    public void setHasRolledDice(boolean input) {
        hasRolledDice = input;
    }

    public boolean getIsInMAEState() {
        return isInMandatoryActionsExhaustedState;
    }

    public void setIsInMAEState(boolean input) {
        isInMandatoryActionsExhaustedState = input;
    }

    public boolean getIsJailed() {
        return isJailed;
    }

    public void setIsJailed(boolean input) {
        isJailed = input;
    }

    public boolean getInitialJailTurn() {
        return initialJailTurn;
    }

    public void setInitialJailTurn(boolean input) {
        initialJailTurn = input;
    }

    public boolean getRequiredDecisionPostedBail() {
        return requiredDecisionPostedBail;
    }

    public void setRequiredDecisionPostedBail(boolean input) {
        requiredDecisionPostedBail = input;
        if (input) {
            isInMandatoryActionsExhaustedState = false;
        }
    }

    public boolean getMadeDecisionJail() {
        return madeDecisionJail;
    }

    public void setMadeDecisionJail(boolean input) {
        madeDecisionJail = input;
    }

    public boolean getResultDecisionJail() {
        return resultDecisionJail;
    }

    public boolean getRequiredDecisionPropertyAction() {
        return requiredDecisionPropertyAction;
    }

    public void setRequiredDecisionPropertyAction(boolean input) {
        requiredDecisionPropertyAction = input;
        if (input) {
            isInMandatoryActionsExhaustedState = false;
        }
    }

    public boolean getMadeDecisionPropertyAction() {
        return madeDecisionPropertyAction;
    }

    public void setMadeDecisionPropertyAction(boolean input) {
        madeDecisionPropertyAction = input;
    }

    public boolean getResultDecisionPropertyAction() {
        return resultDecisionPropertyAction;
    }

    public void setResultDecisionPropertyAction(boolean input) {
        resultDecisionPropertyAction = input;
    }

    public boolean getHasRolledDoubles() {
        return hasRolledDoubles;
    }

    public void setHasRolledDoubles(boolean input) {
        hasRolledDoubles = input;
    }

    public boolean getActionLockedRollDice() {
        return actionLockedRollDice;
    }

    public void setActionLockedRollDice(boolean input) {
        actionLockedRollDice = input;
    }

    public boolean getActionLockedEndTurn() {
        return actionLockedEndTurn;
    }

    public void setActionLockedEndTurn(boolean input) {
        actionLockedEndTurn = input;
    }

    public void setResultDecisionPostedBail(boolean input) {
        resultDecisionJail = input;
    }
    // </editor-fold>

    public void initializePlayerForNewTurn() {
        hasRequestedEndTurn = false;
        hasRolledDice = false;
        isInMandatoryActionsExhaustedState = false;

        madeDecisionJail = false;
        madeDecisionPropertyAction = false;

        initialJailTurn = false;

        hasRolledDoubles = false;

        // ActionsGUI are locked by default, and later unlocked when appropriate.
        actionLockedRollDice = true;
        actionLockedEndTurn = true;

        consecutiveDoublesCount = 0;
        die1 = 0;
        die2 = 0;
    }

    /*
     * @return A boolean value indicating whether the player passed GO.
     */
    public boolean advancePosition(int spacesCount) {
        // If player passes GO, also wrap position
        if (currentPosition + spacesCount >= 40) {
            previousPosition = currentPosition;
            currentPosition = (currentPosition + spacesCount) % 40;
            return true;
        } else {
            previousPosition = currentPosition;
            currentPosition += spacesCount;
            return false;
        }
    }

    public void setPosition(int spaceID) {
        if ((spaceID >= 0) && (spaceID <= 39)) {
            previousPosition = currentPosition;
            currentPosition = spaceID;
        }
    }

    public void rollDice() {
        die1 = (int) (1 + Math.random() * 6);
        die2 = (int) (1 + Math.random() * 6);

        if (die1 == die2) {
            hasRolledDoubles = true;
            consecutiveDoublesCount++;
        } else {
            hasRolledDoubles = false;
            hasRolledDice = true;
        }

    }

    public void incrementConsecutiveTurnsJailed() {
        consecutiveTurnsJailed++;
    }

    public void unjailPlayer() {
        consecutiveTurnsJailed = 0;
    }

    public ImageIcon getPreferredIcon() {
        return preferredIcon;
    }

    public void setPreferredIcon(ImageIcon preferredIcon) {
        this.preferredIcon = preferredIcon;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public void setIsBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public void evaluateState() {
        isBankrupt = (currentBalance < 0);

        if (isBankrupt) {
            actionLockedEndTurn = true;
            actionLockedRollDice = true;
        }
    }
}    // end class
