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
	
	PlayerState state;
	
	int	
		currentBalance,
		currentPosition;
	
	boolean
		isPlayerActive,
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
	boolean
		hasRequestedEndTurn,
		hasRolledDice,
		isInMandatoryActionsExhaustedState,
		isJailed;
	
	// Decisions given by GUI prompts
	boolean
		madeDecisionPostedBail,
		resultDecisionPostedBail,
		madeDecisionPropertyAction,
		resultDecisionPropertyAction;
	
	boolean
		hasRolledDoubles;

	// Required state of GUI elements during player's turn
	boolean
		actionLockedRollDice,
		actionLockedEndTurn;
	// </editor-fold>
	
	public void initializePlayerStateForNewTurn() {
		hasRequestedEndTurn = false;
		hasRolledDice = false;
		isInMandatoryActionsExhaustedState = false;
		
		madeDecisionPostedBail = false;
		madeDecisionPropertyAction = false;
		
		hasRolledDoubles = false;
		
		// Actions are locked by default, and later unlocked when appropriate.
		actionLockedRollDice = true;
		actionLockedEndTurn = true;
	}

	// <editor-fold desc="Constructor">
	Player() {
		playerID = 0;
		customName = "";
		
		state = new PlayerState();

		// The default balance for players is $1500.
		currentBalance = 1500;

		// Players start at GO, with space ID 0
		currentPosition = 0;
		
		isPlayerActive = false;
		isComputerControlled = false;
		
		consecutiveDoublesCount = 0;
		
		ownedPropertyIDs = new boolean[40];
		mortgagedPropertyIDs = new boolean[40];
		for ( int i = 0 ; i < 40 ; i++ ) {
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
	
	public boolean getMadeDecisionPostedBail() {
		return madeDecisionPostedBail;
	}
	
	public boolean getMadeDecisionPropertyAction() {
		return madeDecisionPropertyAction;
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
	// </editor-fold>
	
	public void initializePlayerForNewTurn() {
		initializePlayerStateForNewTurn();
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
		}
		else {
			currentPosition += spacesCount;
			return false;
		}
	}
	
	public int rollDice() {
		die1 = (int) (Math.random() * 6);
		die2 = (int) (Math.random() * 6);
		
		if (die1 == die2) {
			hasRolledDoubles = true;
		}
		else {
			hasRolledDoubles = false;
			hasRolledDice = true;
		}
		
		return die1 + die2;
	}
}	// end class