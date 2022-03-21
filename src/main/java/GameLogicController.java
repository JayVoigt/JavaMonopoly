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
import java.util.Date;

public class GameLogicController implements Serializable {

	Board board;

	Player currentPlayer;
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
			currentPlayer.incrementConsecutiveTurnsJailed();
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

	private void readyPlayerForJailRelease() {
		currentPlayer.initializePlayerForNewTurn();

		currentPlayer.setRequiredDecisionPostedBail(false);
		currentPlayer.setIsJailed(false);

		currentPlayer.setPosition(10);

		currentPlayer.setActionLockedEndTurn(true);
		currentPlayer.setActionLockedRollDice(false);
	}

	public void forfeitManager() {

	}
}
