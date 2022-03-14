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
}
