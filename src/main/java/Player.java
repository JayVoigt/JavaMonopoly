
import java.io.Serializable;
import java.math.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jay
 */
public class Player implements Serializable {
    // <editor-fold desc="Attributes">

    int playerID;

    // customName allows players to use a name other than the default
    // "Player n".
    String customName;

    int currentBalance,
        currentPosition;

    boolean isPlayerActive,
        isComputerControlled;

    // How many times the player has rolled doubles on a single turn.
    int consecutiveDoublesCount;

    // Sets of boolean values whose indices match to the corresponding
    // attribute of the space sharing the same ID.
    boolean ownedPropertyIDs[];
    boolean mortgagedPropertyIDs[];

    // Number that appears on the face of the dice
    int die1, die2;
    // </editor-fold>

    // <editor-fold desc="State attributes">
    // Basic state parameters
    boolean hasRequestedEndTurn,
        hasRolledDice,
        isInMandatoryActionsExhaustedState,
        isJailed;

    // Decisions given by GUI prompts
    boolean requiredDecisionPostedBail,
		madeDecisionPostedBail,
        resultDecisionPostedBail,
		
		requiredDecisionPropertyAction,
        madeDecisionPropertyAction,
        resultDecisionPropertyAction;

    boolean hasRolledDoubles;

    // Required state of GUI elements during player's turn
    boolean actionLockedRollDice,
        actionLockedEndTurn;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    Player() {
        playerID = 0;
        customName = "";

        // The default balance for players is $1500.
        currentBalance = 1500;

        // Players start at GO, with space ID 0
        currentPosition = 0;

        isPlayerActive = false;
        isComputerControlled = false;

        consecutiveDoublesCount = 0;

        ownedPropertyIDs = new boolean[40];
        mortgagedPropertyIDs = new boolean[40];
        for (int i = 0; i < 40; i++) {
            ownedPropertyIDs[i] = false;
            mortgagedPropertyIDs[i] = false;
        }

        die1 = 0;
        die2 = 0;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters and getters">
    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int inputPlayerID) {
        playerID = inputPlayerID;
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
        currentBalance = inputCurrentBalance;
    }

    public void updateCurrentBalance(int change) {
        currentBalance += change;
    }

    public int getCurrentPosition() {
        return currentPosition;
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
	
	public boolean[] getMortgagedPropertyIDs() {
		return mortgagedPropertyIDs;
	}
    // </editor-fold>

    // <editor-fold desc="Setters and getters for state attributes">
    public boolean getHasRequestedEndTurn() {
        return hasRequestedEndTurn;
    }

    public boolean getHasRolledDice() {
        return hasRolledDice;
    }

    public boolean getIsInMAEState() {
        return isInMandatoryActionsExhaustedState;
    }

    public boolean getIsJailed() {
        return isJailed;
    }
	
	public boolean getRequiredDecisionPostedBail() {
		return requiredDecisionPostedBail;
	}

    public boolean getMadeDecisionPostedBail() {
        return madeDecisionPostedBail;
    }
	
	public boolean getResultDecisionPostedBail() {
		return resultDecisionPostedBail;
	}
	
	public boolean getRequiredDecisionPropertyAction() {
		return requiredDecisionPropertyAction;
	}

    public boolean getMadeDecisionPropertyAction() {
        return madeDecisionPropertyAction;
    }
	
	public boolean getResultDecisionPropertyAction() {
		return resultDecisionPropertyAction;
	}

    public boolean getHasRolledDoubles() {
        return hasRolledDoubles;
    }

    public boolean getActionLockedRollDice() {
        return actionLockedRollDice;
    }

    public boolean getActionLockedEndTurn() {
        return actionLockedEndTurn;
    }

    public void setHasRequestedEndTurn(boolean input) {
        hasRequestedEndTurn = input;
    }

    public void setHasRolledDice(boolean input) {
        hasRolledDice = input;
    }

    public void setIsInMAEState(boolean input) {
        isInMandatoryActionsExhaustedState = input;
    }

    public void setIsJailed(boolean input) {
        isJailed = input;
    }

	public void setRequiredDecisionPostedBail(boolean input) {
		requiredDecisionPostedBail = input;
		if (input == true) {
			isInMandatoryActionsExhaustedState = false;
		}
	}
	
    public void setMadeDecisionPostedBail(boolean input) {
        madeDecisionPostedBail = input;
    }
	
	public void setResultDecisionPostedBail(boolean input) {
		resultDecisionPostedBail = input;
	}

	public void setRequiredDecisionPropertyAction(boolean input) {
		requiredDecisionPropertyAction = input;
		if (input == true) {
			isInMandatoryActionsExhaustedState = false;
		}
	}
	
    public void setMadeDecisionPropertyAction(boolean input) {
        madeDecisionPropertyAction = input;
    }
	
	public void setResultDecisionPropertyAction(boolean input) {
		resultDecisionPropertyAction = input;
	}

    public void setHasRolledDoubles(boolean input) {
        hasRolledDoubles = input;
    }

    public void setActionLockedRollDice(boolean input) {
        actionLockedRollDice = input;
    }

    public void setActionLockedEndTurn(boolean input) {
        actionLockedEndTurn = input;
    }
    // </editor-fold>

    public void initializePlayerForNewTurn() {
        hasRequestedEndTurn = false;
        hasRolledDice = false;
        isInMandatoryActionsExhaustedState = false;

        madeDecisionPostedBail = false;
        madeDecisionPropertyAction = false;

        hasRolledDoubles = false;

        // Actions are locked by default, and later unlocked when appropriate.
        actionLockedRollDice = true;
        actionLockedEndTurn = true;

        consecutiveDoublesCount = 0;
        die1 = 0;
        die2 = 0;
    }

    /*
	 * @return A boolean value indicating whether or not the player passed GO.
     */
    public boolean advancePosition(int spacesCount) {
        // If player passes GO, also wrap position
        if (currentPosition + spacesCount >= 40) {
            currentPosition = (currentPosition + spacesCount) % 40;
            return true;
        } else {
            currentPosition += spacesCount;
            return false;
        }
    }

    public int rollDice() {
        die1 = (int) (1 + Math.random() * 6);
        die2 = (int) (1 + Math.random() * 6);

        if (die1 == die2) {
            hasRolledDoubles = true;
            consecutiveDoublesCount++;
        } else {
            hasRolledDoubles = false;
            hasRolledDice = true;
        }

        return die1 + die2;
    }
}	// end class
