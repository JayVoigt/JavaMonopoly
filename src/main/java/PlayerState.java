/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class PlayerState {
	
	// Basic state parameters
	public boolean
		hasRequestedEndTurn,
		hasRolledDice,
		isInMandatoryActionsExhaustedState,
		isJailed;
	
	// Decisions given by GUI prompts
	private boolean
		madeDecisionPostedBail,
		resultDecisionPostedBail,
		madeDecisionPropertyAction,
		resultDecisionPropertyAction;
	
	private boolean
		hasRolledDoubles;

	// Required state of GUI elements during player's turn
	private boolean
		actionLockedRollDice,
		actionLockedEndTurn;
	
	public PlayerState() {
		initializePlayerStateForNewTurn();
	}
	
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
	
}
