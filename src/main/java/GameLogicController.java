/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class GameLogicController {
	Board board;
	MainView view;
	
	Player currentPlayer;
	
	boolean isGameActive;
	
	int playersCount,
		turnCounter;
	
	public GameLogicController(Board inputBoard, MainView inputView) {
		board = inputBoard;
		view = inputView;
	}
	
	public void initializeNextTurn() {
		currentPlayer.state.initializePlayerStateForNewTurn();
		initialEvaluator();
	}
	
	public void initialEvaluator() {
		if (currentPlayer.state.isJailed == true) {
			jailStateEvaluator();
		}
		else {
			normalTurnEvaluator();
		}
	}
	
	public void normalTurnEvaluator() {
		if (currentPlayer.state.hasRolledDice == true) {
			currentPlayer.state.isInMandatoryActionsExhaustedState = true;
			maeStateEvaluator();
		}
		else {
			currentPlayer.state.actionLockedRollDice = false;
			// Termination
		}
	}
	
	public void jailStateEvaluator() {
		
	}
	
	public void maeStateEvaluator() {
		if (currentPlayer.state.isInMandatoryActionsExhaustedState == true) {
			currentPlayer.state.actionLockedEndTurn = false;
		}
		else {
			if (currentPlayer.state.hasRolledDice == true) {
				if (currentPlayer.state.madeDecisionPropertyAction == true) {
					currentPlayer.state.actionLockedEndTurn = false;
				}
				else {
					propertyPurchaseManager();
				}
			}
			else {
				currentPlayer.state.actionLockedRollDice = false;
			}
		}
	}
	
	public void propertyPurchaseManager() {
		
	}
}
