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

	Player currentPlayer;
	Space currentSpace;
	GameEvent currentGameEvent;
	Property currentProperty;

	String debugLogContents;

	boolean isGameActive;

	int playersCount,
		turnCounter;

	public GameLogicController(Board inputBoard) {
		board = inputBoard;
		debugLogContents = "";
	}

	public String getDebugLogContents() {
		return debugLogContents;
	}

	public void appendToDebugLog(String input) {
		String formattedPrefix = Integer.toString(turnCounter);
		formattedPrefix = formattedPrefix.concat(": ");
		debugLogContents = debugLogContents.concat(formattedPrefix + input + "\n");
	}

	// <editor-fold desc="Setters and getters">
	public int getTurnCounter() {
		return turnCounter;
	}

	public boolean getIsGameActive() {
		return isGameActive;
	}

	public void setIsGameActive(boolean input) {
		isGameActive = input;
	}

	public void setPlayersCount(int input) {
		playersCount = input;
	}
	// </editor-fold>

	public void initialEvaluator() {
		appendToDebugLog("-> executing initialEvaluator");
		currentPlayer = board.players.get(board.getCurrentPlayerID());

		if (currentPlayer.getIsJailed() == true) {
			jailStateEvaluator();
		}
		else {
			normalTurnEvaluator();
		}
	}

	private void jailStateEvaluator() {
		appendToDebugLog("-> executing jailStateEvaluator");
	}

	private void normalTurnEvaluator() {
		appendToDebugLog("-> executing normalTurnEvaluator");
		if (currentPlayer.hasRolledDice == true) {
			currentPlayer.setIsInMAEState(true);
			maeStateEvaluator();
		}
		else {
			appendToDebugLog("\t Suggested action: unlock rollDice");
			currentPlayer.setActionLockedRollDice(false);
		}
	}

	private void maeStateEvaluator() {
		appendToDebugLog("-> executing maeStateEvaluator");
		if (currentPlayer.getIsInMAEState() == true) {
			currentPlayer.setActionLockedEndTurn(false);
		}
		else {

			if (currentPlayer.getHasRolledDice() == true) {

				if (currentPlayer.getRequiredDecisionPropertyAction() == true) {
					if (currentPlayer.getMadeDecisionPropertyAction() == true) {
						appendToDebugLog("\t Suggested action: unlock endTurn");
						appendToDebugLog("\t\t Reason: Player has made property decision.");
						currentPlayer.setActionLockedEndTurn(false);
					}
					else {
						propertyPurchaseManager();
					}
				}
				else {
					appendToDebugLog("\t Suggested action: unlock endTurn");
					appendToDebugLog("\t\t Reason: No property decision necessary.");
					currentPlayer.setActionLockedEndTurn(false);
				}
			}
			else {

				appendToDebugLog("\t Suggested action: unlock rollDice");
				appendToDebugLog("\t\t Reason: Player has not rolled dice.");
				currentPlayer.setActionLockedRollDice(false);

			}
		}
	}

	private void propertyPurchaseManager() {
		appendToDebugLog("-> executing propertyPurchaseManager");
		currentPlayer.setRequiredDecisionPropertyAction(false);
		currentPlayer.setMadeDecisionPropertyAction(true);
	}

	public void diceRollManager() {
		appendToDebugLog("-> executing diceRollManager");
		currentPlayer = board.players.get(board.getCurrentPlayerID());

		currentPlayer.rollDice();

		// Reset roll dice condition for doubles
		if (currentPlayer.getHasRolledDoubles() == true) {
			currentPlayer.setHasRolledDice(false);
		}
		else {
			currentPlayer.setHasRolledDice(true);
		}

		movementEvaluator();
	}

	private void movementEvaluator() {
		appendToDebugLog("-> executing movementEvaluator");
		int diceSum = currentPlayer.getDiceSum();
		boolean playerPassedGo = currentPlayer.advancePosition(diceSum);

		// Issue GO bonus
		if (playerPassedGo == true) {
			currentPlayer.updateCurrentBalance(200);
		}

		currentSpace = board.spaces.get(currentPlayer.getCurrentPosition());
		currentSpace.incrementTimesLanded();

		if (currentSpace.getSpaceType().equals(Space.spaceTypeKeys.gameEvent)) {
			gameEventEvaluator();
		}
		else {
			propertyEvaluator();
		}
	}

	private void gameEventEvaluator() {
		appendToDebugLog("-> executing gameEventEvaluator");
		maeStateEvaluator();
	}

	private void propertyEvaluator() {
		appendToDebugLog("-> executing propertyEvaluator");
		currentProperty = (Property) currentSpace;
		if (currentProperty.getIsOwned() == false) {
			currentPlayer.setRequiredDecisionPropertyAction(true);
			currentPlayer.setMadeDecisionPropertyAction(false);
		}
		else {
			currentPlayer.setRequiredDecisionPropertyAction(false);
			currentPlayer.setMadeDecisionPropertyAction(true);
		}

		maeStateEvaluator();
	}

	public void endTurnManager() {
		appendToDebugLog("-> executing endTurnManager");
		currentPlayer = board.players.get(board.getCurrentPlayerID());
		currentPlayer.setActionLockedEndTurn(true);

		if (board.getCurrentPlayerID() == playersCount) {
			board.setCurrentPlayerID(1);
			appendToDebugLog("\t Rolling over turn to Player 1");
		}
		else {
			board.setCurrentPlayerID(board.getCurrentPlayerID() + 1);
			appendToDebugLog("\t Incrementing turn to Player " + board.getCurrentPlayerID());
		}

		currentPlayer = board.players.get(board.getCurrentPlayerID());

		appendToDebugLog("\t currentPlayerID: " + board.getCurrentPlayerID());
		currentPlayer.initializePlayerForNewTurn();
		turnCounter++;
		initialEvaluator();
	}
	
	public void playerDecisionPurchaseProperty() {
		currentPlayer.setMadeDecisionPropertyAction(true);
		currentPlayer.setResultDecisionPropertyAction(true);
		
		if (currentPlayer.getCurrentBalance() >= currentProperty.getPurchaseCost()) {
			currentProperty.setOwnerID(currentPlayer.getPlayerID());
			currentPlayer.updateCurrentBalance(-1 * currentProperty.getPurchaseCost());
		}
		else {
			
		}
	}
	
	public void playerDecisionAuction() {
		
	}
}
