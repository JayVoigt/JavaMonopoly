/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
import com.formdev.flatlaf.extras.components.FlatTriStateCheckBox;
import com.sun.tools.javac.util.PropagatedException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class GameLogicController {

	Board board;

	Player currentPlayer;
	Space currentSpace;
	GameEvent currentGameEvent;
	Property currentProperty;

	String gameLogContents,
		debugLogContents;

	boolean isGameActive;

	boolean useExtraTextPadding;

	int playersCount,
		turnCounter;

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

	public void setExtraTextPadding(boolean input) {
		useExtraTextPadding = input;
	}
	// </editor-fold>

	// <editor-fold desc="Construcctor">
	public GameLogicController(Board inputBoard) {
		board = inputBoard;
		gameLogContents = "";
		debugLogContents = "";

		useExtraTextPadding = true;
	}
	// </editor-fold>
	
	// <editor-fold desc="Misc helpers">
	public String getGameLogContents() {
		return gameLogContents;
	}

	public void sendWelcomeMessage() {
		appendToGameLog("Welcome to Java Monopoly Prototype!");
		appendToGameLog("A new game can be started or loaded under the File menu.\n");
	}

	public void sendInitGameMessage() {
		appendToGameLog("A new game has been started with " + playersCount + " players.");
	}

	public void appendToGameLog(String input) {
		Date currentDate = new Date();
		SimpleDateFormat datePrefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

		String currentTurn = Integer.toString(turnCounter);
		String formattedPrefix;
		formattedPrefix = ("[" + currentDate + "] (" + currentTurn + "): ");

		gameLogContents = gameLogContents.concat(formattedPrefix + input + "\n");
	}

	public String getDebugLogContents() {
		return debugLogContents;
	}

	public void appendToDebugLog(String input) {
		String formattedPrefix = Integer.toString(turnCounter);
		formattedPrefix = formattedPrefix.concat(": ");
		debugLogContents = debugLogContents.concat(formattedPrefix + input + "\n");
	}
	// </editor-fold>
	
	public void initialEvaluator() {
		appendToDebugLog("-> executing initialEvaluator");
		turnCounter++;
		currentPlayer = board.players.get(board.getCurrentPlayerID());

		String paddingPrefix = "";
		if (useExtraTextPadding == true) {
			gameLogContents = gameLogContents.concat("\n");
		}

		appendToGameLog("It is now " + currentPlayer.getCustomName() + "'s turn.");

		if (currentPlayer.getIsJailed() == true) {
			jailStateEvaluator();
		}
		else {
			normalTurnEvaluator();
		}
	}

	private void jailStateEvaluator() {
		appendToDebugLog("-> executing jailStateEvaluator");
		appendToGameLog(currentPlayer.getCustomName() + " is jailed!");
		
		
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
		currentPlayer.setActionLockedEndTurn(false);
		currentPlayer.setActionLockedRollDice(true);

		if (currentPlayer.getHasRolledDoubles() == true) {
			currentPlayer.setActionLockedRollDice(false);
			currentPlayer.setActionLockedEndTurn(true);
		}

		if (currentPlayer.getRequiredDecisionPropertyAction() == true) {
			if (currentPlayer.getMadeDecisionPropertyAction() == true) {
				appendToDebugLog("\t Suggested action: unlock endTurn");
				appendToDebugLog("\t\t Reason: Player has made property decision.");
				currentPlayer.setActionLockedEndTurn(false);
			}
			else {
				//propertyPurchaseManager();
			}
		}
		else {
			if (currentPlayer.getHasRolledDice() == true) {
				appendToDebugLog("\t Suggested action: unlock endTurn");
				appendToDebugLog("\t\t Reason: No property decision necessary.");
				currentPlayer.setActionLockedEndTurn(false);
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

		String doublesSuffix;
		if (currentPlayer.getHasRolledDoubles() == true) {
			doublesSuffix = " Doubles!";
		}
		else {
			doublesSuffix = "";
		}

		appendToGameLog(currentPlayer.getCustomName() + " rolled (" + currentPlayer.getDie1() + "," + currentPlayer.getDie2() + ")." + doublesSuffix);

		// Reset roll dice condition for doubles
		if (currentPlayer.getHasRolledDoubles() == true) {
			currentPlayer.setHasRolledDice(false);
		}
		else {
			currentPlayer.setHasRolledDice(true);
		}

		if (currentPlayer.getConsecutiveDoublesCount() >= 3) {
			appendToGameLog(currentPlayer.getCustomName() + " has rolled doubles 3 times, and is now jailed for speeding!");
			jailPlayer();
		}
		else {
			movementEvaluator();
		}
	}

	private void jailPlayer() {
		currentPlayer.setIsJailed(true);
		currentPlayer.setHasRolledDice(true);
		currentPlayer.setActionLockedEndTurn(false);
		currentPlayer.setActionLockedRollDice(true);
		currentPlayer.setInitialJailTurn(true);
	}
	
	private void movementEvaluator() {
		appendToDebugLog("-> executing movementEvaluator");
		int diceSum = currentPlayer.getDiceSum();
		boolean playerPassedGo = currentPlayer.advancePosition(diceSum);

		// Issue GO bonus
		if (playerPassedGo == true) {
			currentPlayer.updateCurrentBalance(200);
			appendToGameLog(currentPlayer.getCustomName() + " has passed or landed on GO, and is rewarded a bonus of $200.");
		}

		currentSpace = board.spaces.get(currentPlayer.getCurrentPosition());
		currentSpace.incrementTimesLanded();

		appendToGameLog(currentPlayer.getCustomName() + " has advanced to " + currentSpace.getFriendlyName() + ".");

		if (currentSpace.getSpaceType().equals(Space.spaceTypeKeys.gameEvent)) {
			gameEventEvaluator();
		}
		else {
			propertyEvaluator();
		}
		maeStateEvaluator();
	}

	private void gameEventEvaluator() {
		appendToDebugLog("-> executing gameEventEvaluator");
		maeStateEvaluator();
	}

	private void propertyEvaluator() {
		appendToDebugLog("-> executing propertyEvaluator");
		currentProperty = (Property) currentSpace;
		if (currentProperty.getIsOwned() == false) {
			appendToGameLog(currentProperty.getFriendlyName() + " is not owned.");
			currentPlayer.setRequiredDecisionPropertyAction(true);
			currentPlayer.setMadeDecisionPropertyAction(false);
		}
		else {
			appendToGameLog(currentProperty.getFriendlyName() + " is owned by " + board.players.get(currentProperty.getOwnerID()).getCustomName() + ".");
			currentPlayer.setRequiredDecisionPropertyAction(false);
			currentPlayer.setMadeDecisionPropertyAction(true);
			
			int rentOwed = currentProperty.calculateRent();
			Player propertyOwner = board.players.get(currentProperty.getOwnerID());
			
			currentPlayer.updateCurrentBalance(-1 * rentOwed);
			propertyOwner.updateCurrentBalance(rentOwed);
			
			appendToGameLog(currentPlayer.getCustomName() + " has paid $" + rentOwed + " in rent to " + propertyOwner.getCustomName() + ".");
		}
	}

	public void endTurnManager() {
		appendToDebugLog("-> executing endTurnManager");
		currentPlayer = board.players.get(board.getCurrentPlayerID());
		currentPlayer.setActionLockedEndTurn(true);

		appendToGameLog(currentPlayer.getCustomName() + " has ended their turn.");

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
		initialEvaluator();
	}

	public void playerDecisionPurchaseProperty() {
		appendToDebugLog("-> executing playerDecisionPurchaseProperty");
		currentPlayer.setMadeDecisionPropertyAction(true);
		currentPlayer.setResultDecisionPropertyAction(true);

		if (currentPlayer.getCurrentBalance() >= currentProperty.getPurchaseCost()) {
			currentProperty.setIsOwned(true);
			currentProperty.setOwnerID(currentPlayer.getPlayerID());
			
			currentPlayer.updateCurrentBalance(-1 * currentProperty.getPurchaseCost());
			currentPlayer.setPropertyOwnedState(true, currentProperty.getID());
			
			currentPlayer.setRequiredDecisionPropertyAction(false);
			currentPlayer.setMadeDecisionPropertyAction(true);
			appendToGameLog(currentPlayer.getCustomName() + " has purchased " + currentProperty.getFriendlyName() + " for $" + currentProperty.getPurchaseCost() + ".");
		}
		else {
			appendToGameLog(currentPlayer.getCustomName() + " cannot afford to purchase " + currentSpace.getFriendlyName() + ".");
		}
	}

	public void playerDecisionAuction() {
		appendToDebugLog("-> executing playerDecisionAuction");
	}

}
