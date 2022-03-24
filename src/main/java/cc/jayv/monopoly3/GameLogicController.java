package cc.jayv.monopoly3;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class GameLogicController implements Serializable {

	Board board;

	Player currentPlayer;
	Player currentDebugPlayer;
	
	Space currentSpace;
	GameEvent currentGameEvent;
	Property currentProperty;

	DrawCard currentDrawCard;

	String gameLogContents,
		debugLogContents;

	boolean isGameActive;

	boolean useExtraTextPadding;

	int playersCount,
		turnCounter;
	
	boolean playerCanBuildImprovements;

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
	
	public void setPlayerCanBuildImprovements(boolean input) {
		playerCanBuildImprovements = input;
	}
	// </editor-fold>

	// <editor-fold desc="Constructor">
	public GameLogicController(Board inputBoard) {
		board = inputBoard;
		gameLogContents = "";
		debugLogContents = "";

		useExtraTextPadding = true;
		
		playerCanBuildImprovements = false;
	}
	// </editor-fold>

	// <editor-fold desc="Misc helpers">
	public String getGameLogContents() {
		return gameLogContents;
	}

	/**
	 * Send a welcome message to the game log when the application is first launched.
	 */
	public void sendWelcomeMessage() {
		appendToGameLog("Welcome to Java Monopoly Prototype!");
		appendToGameLog("A new game can be started or loaded under the File menu.\n");
	}

	/**
	 * Send a message to the game log when the game has started, including information about the number of players specified.
	 */
	public void sendInitGameMessage() {
		appendToGameLog("A new game has been started with " + playersCount + " players.");
	}

	/**
	 * Append the input to the game log as a formatted line.<br>
	 * Entries are prefixed with the current date and time, as well as the current turn number.<br>
	 * @param input The contents of the message.
	 */
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

	/**
	 * Append the input to the debug log.
	 * @param input The contents of the message.
	 */
	public void appendToDebugLog(String input) {
		String formattedPrefix = Integer.toString(turnCounter);
		formattedPrefix = formattedPrefix.concat(": ");
		debugLogContents = debugLogContents.concat(formattedPrefix + input + "\n");
	}
	// </editor-fold>

	/**
	 * Evaluator which is run at the beginning of a player's turn.<br>
	 * Implements which decision path to execute given the player's condition.
	 */
	public void initialEvaluator() {
		appendToDebugLog("-> executing initialEvaluator");
		turnCounter++;
		currentPlayer = board.players.get(board.getCurrentPlayerID());

		board.forceBoardSelfCheck();
		
		String paddingPrefix = "";
		if (useExtraTextPadding == true) {
			gameLogContents = gameLogContents.concat("\n");
		}

		appendToGameLog("It is now " + currentPlayer.getCustomName() + "'s turn.");

		if (currentPlayer.getIsJailed() == true) {
			currentPlayer.incrementConsecutiveTurnsJailed();
			jailStateEvaluator();
		}
		else {
			normalTurnEvaluator();
		}
	}

	/**
	 * Evaluator which is run when the current player is currently in Jail.
	 */
	private void jailStateEvaluator() {
		appendToDebugLog("-> executing jailStateEvaluator");
		appendToGameLog(currentPlayer.getCustomName() + " is jailed!");

	}

	/**
	 * Evaluator which is run if no special conditions apply to the current player.
	 */
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

	/**
	 * Evaluator which is run when the player is in mandatory actions exhausted (MAE) state.<br>
	 * This occurs when the player has no remaining mandatory actions, such as rolling the dice
	 * or ending their turn, but they are still permitted to perform optional actions, such as
	 * mortgaging a property or building improvements.
	 */
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

	/**
	 * Manager which is executed when the player decides to purchase a property.
	 */
	private void propertyPurchaseManager() {
		appendToDebugLog("-> executing propertyPurchaseManager");
		currentPlayer.setRequiredDecisionPropertyAction(false);
		currentPlayer.setMadeDecisionPropertyAction(true);
	}

	/**
	 * Manager which is executed when the current player rolls the dice.<br>
	 * Note: this does not execute for special dice roll conditions, such as rolling
	 * for doubles when jailed, or rolling to determine the rent paid when landing on an owned Utility.
	 */
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

	/**
	 * Ready the state of the current player to be jailed; jail the player.
	 */
	private void jailPlayer() {
		currentPlayer.setIsJailed(true);
		currentPlayer.setHasRolledDice(true);
		currentPlayer.setActionLockedEndTurn(false);
		currentPlayer.setActionLockedRollDice(true);
		currentPlayer.setInitialJailTurn(true);
	}

	/**
	 * Evaluator which executes when the current player rolls the dice, and a roll quantity is determined.<br>
	 * Determines where the player should move, and then executes the appropriate evaluator for the destination space.
	 */
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

	/**
	 * An advanced evaluator, derived from <code>movementEvaluator</code>, which executes when more precise
	 * movement of a player's position is required.<br>
	 * This occurs when a player requires a <code>teleportRelative</code> event from <code>DrawCard</code> to be executed.
	 * @param collectGoBonus Whether the player should collect a bonus if they pass or land on GO.
	 * @param movementQuantity The number of spaces to move.
	 */
	private void movementEvaluatorAdvanced(boolean collectGoBonus, int movementQuantity) {
		appendToDebugLog("-> executing movementEvaluatorAdvanced");
		boolean playerPassedGo = currentPlayer.advancePosition(movementQuantity);

		// Issue GO bonus
		if ((playerPassedGo == true) && (collectGoBonus == true)) {
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

	/**
	 * Evaluator which executes when the landed space is of <code>GameEvent</code> type.
	 */
	private void gameEventEvaluator() {
		appendToDebugLog("-> executing gameEventEvaluator");

		currentGameEvent = (GameEvent) currentSpace;
		GameEvent.gameEventTypeKeys localGameEventType = currentGameEvent.getGameEventType();

		if (localGameEventType.equals(localGameEventType.drawCard)) {
			drawCardEvaluator();
		}
		else if (localGameEventType.equals(localGameEventType.updateBalance)) {

		}
		else if (localGameEventType.equals(localGameEventType.teleport)) {

		}
		else if (localGameEventType.equals(localGameEventType.jailPlayer)) {
			jailPlayer();
		}
		else if (localGameEventType.equals(localGameEventType.tax)) {
			// Using 2008 Monopoly rules that exclude 10% option
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
		maeStateEvaluator();
	}

	/**
	 * Evaluator which executes when the landed space is of <code>DrawCard</code> type.
	 */
	private void drawCardEvaluator() {
		int randomCardID = (int) (Math.random() * board.chanceCards.size());
		String gameLogDrawPrefix;

		if (currentSpace.getFriendlyName() == "Chance") {
			currentDrawCard = board.chanceCards.get(randomCardID);
			gameLogDrawPrefix = "The Chance card reads: ";
		}
		else {
			currentDrawCard = board.communityChestCards.get(randomCardID);
			gameLogDrawPrefix = "The Community Chest card reads: ";
		}

		appendToGameLog(gameLogDrawPrefix + currentDrawCard.getMessage());

		if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.teleport) {
			int forwardMovementQuantity = (currentDrawCard.getDestinationSpace() - currentPlayer.getCurrentPosition());
			movementEvaluatorAdvanced(true, forwardMovementQuantity);
		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.teleportRelative) {

		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.teleportWithRentModifier) {

		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.balanceUpdate) {
			currentPlayer.updateCurrentBalance(currentDrawCard.getQuantity());
		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.getOutOfJailFreeCard) {
			currentPlayer.setGetOutOfJailFreeCardCount(currentPlayer.getGetOutOfJailFreeCardCount() + 1);
		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.jailPlayer) {
			jailPlayer();
		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.improvementRepairs) {

		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.distributedBalanceUpdate) {

		}
	}

	/**
	 * Evaluator which executes when the landed space is of <code>Property</code> type.
	 */
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

			if (propertyOwner != currentPlayer) {
				currentPlayer.updateCurrentBalance(-1 * rentOwed);
				propertyOwner.updateCurrentBalance(rentOwed);

				appendToGameLog(currentPlayer.getCustomName() + " has paid $" + rentOwed + " in rent to " + propertyOwner.getCustomName() + ".");
			}
			else {
				appendToGameLog("No rent needs to be paid.");
			}
		}
	}

	/**
	 * Manager which executes when the current player ends their turn.
	 */
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

	/**
	 * Decision evaluator which executes when the current player decides to purchase a property.
	 */
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

	/**
	 * Decision evaluator which executes when the player decides to send a landed property to auction.<br>
	 * <b>Not implemented.</b>
	 */
	public void playerDecisionAuction() {
		appendToDebugLog("-> executing playerDecisionAuction");
	}

	/** 
	 * Decision evaluator which executes when the current player decides to post bail when jailed.
	 */
	public void playerDecisionJailPostBail() {
		currentPlayer.setMadeDecisionPostedBail(true);
		if (currentPlayer.getCurrentBalance() >= 50) {
			currentPlayer.updateCurrentBalance(-50);
			appendToGameLog(currentPlayer.getCustomName() + " has posted bail for $50.");
			readyPlayerForJailRelease();
		}
		else {
			appendToGameLog(currentPlayer.getCustomName() + " cannot afford to post bail.");
		}
	}

	/**
	 * Decision evaluator which executes when the current player decides to roll for doubles when jailed.
	 */
	public void playerDecisionJailRollDoubles() {
		currentPlayer.setMadeDecisionPostedBail(true);
		currentPlayer.rollDice();

		if (currentPlayer.getHasRolledDoubles() == true) {
			appendToGameLog(currentPlayer.getCustomName() + " has rolled doubles, and is no longer jailed!");
			readyPlayerForJailRelease();
		}
		else {
			if (currentPlayer.getConsecutiveTurnsJailed() < 3) {
				appendToGameLog(currentPlayer.getCustomName() + " has not rolled doubles, and will remain jailed.");
				currentPlayer.setRequiredDecisionPostedBail(false);
				currentPlayer.setInitialJailTurn(true);
				currentPlayer.setActionLockedEndTurn(false);
			}
			else {
				appendToGameLog(currentPlayer.getCustomName() + " has failed to roll doubles after 3 tries. Posting bail for $50 is now required.");
				appendToDebugLog("[NOTICE]: need to implement edge case logic where balance < $50");
				currentPlayer.updateCurrentBalance(-50);
				readyPlayerForJailRelease();
			}
		}
	}

	/**
	 * Ready the state of the current player to be released from jail; release the player from jail.
	 */
	private void readyPlayerForJailRelease() {
		currentPlayer.initializePlayerForNewTurn();

		currentPlayer.setRequiredDecisionPostedBail(false);
		currentPlayer.setIsJailed(false);

		currentPlayer.setPosition(10);

		currentPlayer.setActionLockedEndTurn(true);
		currentPlayer.setActionLockedRollDice(false);
	}

	/**
	 * Manager which is executed when the player decides to forfeit the game.<br>
	 * <b>Not implemented.</b>
	 */
	public void forfeitManager() {

	}
	
	public void debugToolsGiveAllProperties(int playerID) {
		currentDebugPlayer = board.players.get(playerID);
		appendToDebugLog("Giving " + currentDebugPlayer.getCustomName() + " all properties.");
		
		Property localProperty;
		for (Space s : board.spaces) {
			if (s instanceof Property) {
				localProperty = (Property) s;
				
				localProperty.setIsOwned(true);
				localProperty.setOwnerID(playerID);
			}
		}
		
		board.forceBoardSelfCheck();
	}
}
