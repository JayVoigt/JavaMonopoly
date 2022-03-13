
import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class Player implements Serializable {
	int playerID;
	
	// customName allows players to use a name other than the default
	// "Player n".
	String customName;
	
	int currentBalance,
		currentPosition;
	
	boolean isPlayerActive,
		isComputerControlled,
		hasRequestedEndTurn,
		hasRolledDice,
		isInMandatoryActionsExhaustedState,
		isJailed;
	
	boolean madeDecisionPostedBail,
		madeDecisionPropertyAction;
	
	// How many times the player has rolled doubles on a single turn.
	int consecutiveDoublesCount;
	
	// Sets of boolean values whose indices match to the corresponding
	// attribute of the space sharing the same ID.
	boolean ownedPropertyIDs[];
	boolean mortgagedPropertyIDs[];
	
	// Number that appears on the face of the dice
	int die1, die2;

	Player() {
		playerID = 0;
		customName = "";

		// The default balance for players is $1500.
		currentBalance = 1500;

		// Players start at GO, with space ID 0
		currentPosition = 0;
		
		isPlayerActive = false;
		isComputerControlled = false;
		hasRequestedEndTurn = false;
		hasRolledDice = false;
		isInMandatoryActionsExhaustedState = false;
		isJailed = false;
		
		madeDecisionPostedBail = false;
		madeDecisionPropertyAction = false;
		
		consecutiveDoublesCount = 0;
		
		for ( int i = 0 ; i < 40 ; i++ ) {
			ownedPropertyIDs[i] = false;
			mortgagedPropertyIDs[i] = false;
		}
		
		die1 = 0;
		die2 = 0;
	}

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
	
	public boolean getIsComputerControlled() {
		return isComputerControlled;
	}
	// </editor-fold>
	
}
