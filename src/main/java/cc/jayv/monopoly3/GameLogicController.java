package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Controls game logic and updates data within the Board accordingly.
 * @author jay
 */
public class GameLogicController implements Serializable {

	Board board;

	Player currentPlayer;
	Player currentDebugPlayer;

	Space currentSpace;
	GameEvent currentGameEvent;
	Property currentProperty;

	DrawCard currentDrawCard;

	boolean isGameActive;

	boolean useExtraTextPadding;

	int playersCount,
		turnCounter;

	boolean playerCanBuildImprovements;

	LogHelper logHelper;

	boolean victoryConditionMet;

	/**
	 * Possible actions that a player can execute when modifying improvements on
	 * a Color space.
	 */
	public enum ImprovementsActions {
		buildHouse,
		sellHouse,
		buildHotel,
		sellHotel
	}

	public enum MortgageActions {
		MORTGAGE,
		UNMORTGAGE
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

	public void setExtraTextPadding(boolean input) {
		useExtraTextPadding = input;
	}

	public void setPlayerCanBuildImprovements(boolean input) {
		playerCanBuildImprovements = input;
	}
	// </editor-fold>

	public boolean getIsVictoryConditionMet() {
		return victoryConditionMet;
	}

	// <editor-fold desc="Constructor">
	public GameLogicController(Board inputBoard, LogHelper inputLogHelper) {
		board = inputBoard;
		logHelper = inputLogHelper;
		
		useExtraTextPadding = true;

		playerCanBuildImprovements = false;
		victoryConditionMet = false;
	}
	// </editor-fold>

	// <editor-fold desc="Misc helpers">

	/**
	 * Append the input to the game log as a formatted line.<br>
	 * Entries are prefixed with the current date and time, as well as the
	 * current turn number.<br>
	 *
	 * @param input The contents of the message.
	 */
	public void appendToGameLog(String input) {
		logHelper.appendToGameLog(input, turnCounter);
	}

	/**
	 * Send a message to the game log when the game has started, including
	 * information about the number of players specified.
	 */
	public void sendInitGameMessage() {
		appendToGameLog("A new game has been started with " + playersCount + " players.");
	}
	// </editor-fold>

	/**
	 * Evaluator which is run at the beginning of a player's turn.<br>
	 * Implements which decision path to execute given the player's condition.
	 */
	public void initialEvaluator() {
		logHelper.appendToDebugLog("-> executing initialEvaluator");
		turnCounter++;
		currentPlayer = board.players.get(board.getCurrentPlayerID());

		board.forceBoardSelfCheck();

		for (Space s : board.spaces) {
			s.setButtonAppearance(Space.buttonAppearanceKeys.none);
		}

		logHelper.addNewLine();

		appendToGameLog("It is now " + currentPlayer.getCustomName() + "'s turn.");
		currentPlayer.evaluateState();

		if (currentPlayer.getIsJailed()) {
			currentPlayer.incrementConsecutiveTurnsJailed();
			jailStateEvaluator();
		}
		else if (currentPlayer.getIsBankrupt()) {
			bankruptStateEvaluator();
		}
		else {
			normalTurnEvaluator();
		}
	}

	/**
	 * Evaluator which is run when the current player is currently in Jail.
	 */
	private void jailStateEvaluator() {
		logHelper.appendToDebugLog("-> executing jailStateEvaluator");
		appendToGameLog(currentPlayer.getCustomName() + " is jailed!");

	}

	private void bankruptStateEvaluator() {
		currentPlayer.setActionLockedEndTurn(true);
		currentPlayer.setActionLockedRollDice(true);

		logHelper.appendToGameLog(currentPlayer.getCustomName() + " is bankrupt!");
	}

	/**
	 * Evaluator which is run if no special conditions apply to the current
	 * player.
	 */
	private void normalTurnEvaluator() {
		logHelper.appendToDebugLog("-> executing normalTurnEvaluator");
		if (currentPlayer.hasRolledDice == true) {
			currentPlayer.setIsInMAEState(true);
			maeStateEvaluator();
		}
		else {
			logHelper.appendToDebugLog("\t Suggested action: unlock rollDice");
			currentPlayer.setActionLockedRollDice(false);
		}
	}

	/**
	 * Evaluator which is run when the player is in mandatory actions exhausted
	 * (MAE) state.<br>
	 * This occurs when the player has no remaining mandatory actions, such as
	 * rolling the dice or ending their turn, but they are still permitted to
	 * perform optional actions, such as mortgaging a property or building
	 * improvements.
	 */
	private void maeStateEvaluator() {
		logHelper.appendToDebugLog("-> executing maeStateEvaluator");
		currentPlayer.setActionLockedEndTurn(false);
		currentPlayer.setActionLockedRollDice(true);

		if (currentPlayer.getHasRolledDoubles() == true) {
			currentPlayer.setActionLockedRollDice(false);
			currentPlayer.setActionLockedEndTurn(true);
		}

		if (currentPlayer.getRequiredDecisionPropertyAction() == true) {
			if (currentPlayer.getMadeDecisionPropertyAction() == true) {
				logHelper.appendToDebugLog("\t Suggested action: unlock endTurn");
				logHelper.appendToDebugLog("\t\t Reason: Player has made property decision.");
				currentPlayer.setActionLockedEndTurn(false);
			}
			else {
				//propertyPurchaseManager();
			}
		}
		else {
			if (currentPlayer.getHasRolledDice() == true) {
				logHelper.appendToDebugLog("\t Suggested action: unlock endTurn");
				logHelper.appendToDebugLog("\t\t Reason: No property decision necessary.");
				currentPlayer.setActionLockedEndTurn(false);
			}
			else {
				logHelper.appendToDebugLog("\t Suggested action: unlock rollDice");
				logHelper.appendToDebugLog("\t\t Reason: Player has not rolled dice.");
				currentPlayer.setActionLockedRollDice(false);
			}
		}
	}

	/**
	 * Manager which is executed when the player decides to purchase a property.
	 */
	private void propertyPurchaseManager() {
		logHelper.appendToDebugLog("-> executing propertyPurchaseManager");
		currentPlayer.setRequiredDecisionPropertyAction(false);
		currentPlayer.setMadeDecisionPropertyAction(true);
	}

	/**
	 * Manager which is executed when the current player rolls the dice.<br>
	 * Note: this does not execute for special dice roll conditions, such as
	 * rolling for doubles when jailed, or rolling to determine the rent paid
	 * when landing on an owned Utility.
	 */
	public void diceRollManager() {
		logHelper.appendToDebugLog("-> executing diceRollManager");
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

	public void gameEditorJailPlayer(Player player) {
		player.setIsJailed(true);
		player.setHasRolledDice(true);
		player.setActionLockedEndTurn(false);
		player.setActionLockedRollDice(true);
		player.setInitialJailTurn(false);
	}

	/**
	 * Evaluator which executes when the current player rolls the dice, and a
	 * roll quantity is determined.<br>
	 * Determines where the player should move, and then executes the
	 * appropriate evaluator for the destination space.
	 */
	private void movementEvaluator() {
		logHelper.appendToDebugLog("-> executing movementEvaluator");
		int diceSum = currentPlayer.getDiceSum();

		board.spaces.get(currentPlayer.getCurrentPosition()).setButtonAppearance(Space.buttonAppearanceKeys.previousSpace);
		boolean playerPassedGo = currentPlayer.advancePosition(diceSum);
		currentPlayer.setAnimateMovement(true);
		board.spaces.get(currentPlayer.getCurrentPosition()).setButtonAppearance(Space.buttonAppearanceKeys.newSpace);

		// Issue GO bonus
		if (playerPassedGo) {
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
	 * An advanced evaluator, derived from <code>movementEvaluator</code>, which
	 * executes when more precise movement of a player's position is
	 * required.<br>
	 * This occurs when a player requires a <code>teleportRelative</code> event
	 * from <code>DrawCard</code> to be executed.
	 *
	 * @param collectGoBonus Whether the player should collect a bonus if they
	 * pass or land on GO.
	 * @param movementQuantity The number of spaces to move.
	 */
	private void movementEvaluatorAdvanced(boolean collectGoBonus, int movementQuantity) {
		logHelper.appendToDebugLog("-> executing movementEvaluatorAdvanced");
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
	 * Evaluator which executes when the landed space is of
	 * <code>GameEvent</code> type.
	 */
	private void gameEventEvaluator() {
		logHelper.appendToDebugLog("-> executing gameEventEvaluator");

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
	 * Evaluator which executes when the landed space is of
	 * <code>DrawCard</code> type.
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
			if (currentDrawCard.getDestinationRelativeType().equals(DrawCard.destinationRelativeTypeKeys.backThreeSpaces)) {

			}
			else if (currentDrawCard.getDestinationRelativeType().equals(DrawCard.destinationRelativeTypeKeys.railroad)) {
				Railroad destinationRailroad = null;
				for (Space s : board.spaces) {
					if (s.getID() > currentSpace.getID()) {
						if (s instanceof Railroad) {
							destinationRailroad = (Railroad) currentSpace;
						}
					}
				}
				if (destinationRailroad != null) {
					currentPlayer.advancePosition(destinationRailroad.getID() - currentSpace.getID());
				}
				else {
					currentPlayer.advancePosition(board.spaces.get(5).getID() - currentSpace.getID());
				}
			}
			else if (currentDrawCard.getDestinationRelativeType().equals(DrawCard.destinationRelativeTypeKeys.utility)) {

			}
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
			int ownedHouses = 0, ownedHotels = 0;
			int paymentQuantity;

			for (Space s : board.spaces) {
				if (s instanceof Color) {
					Color localColor = (Color) s;

					if (currentPlayer.getOwnsPropertyByID(localColor.getID())) {
						ownedHouses += localColor.getHouseCount();
						ownedHotels += localColor.getHotelCount();
					}
				}
			}

			paymentQuantity = ((board.getRepairCostHouse() * ownedHouses) + (board.getRepairCostHotel() * ownedHotels));

			if ((ownedHouses + ownedHotels) == 0) {
				appendToGameLog(currentPlayer.getCustomName() + " owns no improvements."
					+ " No payment needs to be made.");
			}
			else {
				appendToGameLog(currentPlayer.getCustomName() + " owns " + ownedHouses
					+ " houses and " + ownedHotels + " hotels. A payment of $" + paymentQuantity
					+ " is required.");

				currentPlayer.updateCurrentBalance(-1 * paymentQuantity);
			}
		}

		else if (currentDrawCard.getDrawCardType() == DrawCard.drawCardTypeKeys.distributedBalanceUpdate) {

		}
	}

	/**
	 * Evaluator which executes when the landed space is of
	 * <code>Property</code> type.
	 */
	private void propertyEvaluator() {
		logHelper.appendToDebugLog("-> executing propertyEvaluator");
		currentProperty = (Property) currentSpace;

		// Property not owned
		if (!currentProperty.getIsOwned()) {
			appendToGameLog(currentProperty.getFriendlyName() + " is not owned.");
			currentPlayer.setRequiredDecisionPropertyAction(true);
			currentPlayer.setMadeDecisionPropertyAction(false);
		}
		// Property owned
		else {
			appendToGameLog(currentProperty.getFriendlyName() + " is owned by " + board.players.get(currentProperty.getOwnerID()).getCustomName() + ".");
			currentPlayer.setRequiredDecisionPropertyAction(false);
			currentPlayer.setMadeDecisionPropertyAction(true);

			int rentOwed = currentProperty.calculateRent();
			Player propertyOwner = board.players.get(currentProperty.getOwnerID());

			if (propertyOwner != currentPlayer) {
				// Pay rent
				if (currentPlayer.getCurrentBalance() < rentOwed) {
					currentPlayer.setIsBankrupt(true);
					currentPlayer.setCreditorPlayerID(propertyOwner.getPlayerID());
					bankruptStateEvaluator();
				}
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
		logHelper.appendToDebugLog("-> executing endTurnManager");
		currentPlayer = board.players.get(board.getCurrentPlayerID());
		currentPlayer.setActionLockedEndTurn(true);

		appendToGameLog(currentPlayer.getCustomName() + " has ended their turn.");

//		if (board.getCurrentPlayerID() == playersCount) {
//			board.setCurrentPlayerID(1);
//			logHelper.appendToDebugLog("\t Rolling over turn to Player 1");
//		}
//		else {
//			board.setCurrentPlayerID(board.getCurrentPlayerID() + 1);
//			logHelper.appendToDebugLog("\t Incrementing turn to Player " + board.getCurrentPlayerID());
//		}

		if (board.getCurrentPlayerID() == board.getNextActivePlayerID()) {
			victoryConditionMet();
		}
		 board.setCurrentPlayerID(board.getNextActivePlayerID());
		 currentPlayer = board.players.get(board.getCurrentPlayerID());

		 logHelper.appendToDebugLog("\t currentPlayerID: " + board.getCurrentPlayerID());
		 currentPlayer.initializePlayerForNewTurn();
		 initialEvaluator();
	}

	/**
	 * Decision evaluator which executes when the current player decides to
	 * purchase a property.
	 */
	public void playerDecisionPurchaseProperty() {
		logHelper.appendToDebugLog("-> executing playerDecisionPurchaseProperty");
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
	 * Decision evaluator which executes when the player decides to send a
	 * landed property to auction.<br>
	 * <b>Not implemented.</b>
	 */
	public void playerDecisionAuction() {
		logHelper.appendToDebugLog("-> executing playerDecisionAuction");
	}

	/**
	 * Decision evaluator which executes when the current player decides to post
	 * bail when jailed.
	 */
	public void playerDecisionJailPostBail() {
		currentPlayer.setMadeDecisionJail(true);
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
	 * Decision evaluator which executes when the current player decides to roll
	 * for doubles when jailed.
	 */
	public void playerDecisionJailRollDoubles() {
		currentPlayer.setMadeDecisionJail(true);
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
				logHelper.appendToDebugLog("[NOTICE]: need to implement edge case logic where balance < $50");
				currentPlayer.updateCurrentBalance(-50);
				readyPlayerForJailRelease();
			}
		}
	}

	public void playerDecisionJailUseGOOJFC() {
		currentPlayer.setMadeDecisionJail(true);

		if (currentPlayer.getGetOutOfJailFreeCardCount() > 0) {
			currentPlayer.setGetOutOfJailFreeCardCount(currentPlayer.getGetOutOfJailFreeCardCount() - 1);

			appendToGameLog(currentPlayer.getCustomName() + " has used a Get Out of Jail Free Card.");
			readyPlayerForJailRelease();
		}
		else {
			appendToGameLog(currentPlayer.getCustomName() + " does not have a Get Out of Jail Free Card.");
		}
	}

	/**
	 * Ready the state of the current player to be released from jail; release
	 * the player from jail.
	 */
	public void readyPlayerForJailRelease() {

		// Should clean up this method by moving some logic into Player.unjailPlayer()
		currentPlayer.initializePlayerForNewTurn();

		currentPlayer.setRequiredDecisionPostedBail(false);
		currentPlayer.setIsJailed(false);

		currentPlayer.setPosition(10);

		currentPlayer.unjailPlayer();

		currentPlayer.setActionLockedEndTurn(true);
		currentPlayer.setActionLockedRollDice(false);
	}

	/**
	 * Manager which is executed when the player decides to forfeit the
	 * game.<br>
	 * <b>Not implemented.</b>
	 */
	public void forfeitManager() {
		Player creditorPlayer = board.players.get(currentPlayer.getCreditorPlayerID());

		// Ensure that creditorPlayer is valid and that the forfeit-requesting player is able to forfeit
		if ((creditorPlayer.getPlayerID() != 0) && (currentPlayer.getCurrentBalance() < 0)) {
			// Transfer all property assets
			for (Property p : board.getSpacesByOwnerID(currentPlayer.getPlayerID())) {
				p.setOwnerID(creditorPlayer.getPlayerID());
			}

			// Transfer GOOJFCs
			if (currentPlayer.getGetOutOfJailFreeCardCount() > 0) {
				// goofy looking line of code
				creditorPlayer.setGetOutOfJailFreeCardCount(creditorPlayer.getGetOutOfJailFreeCardCount() + currentPlayer.getGetOutOfJailFreeCardCount());
			}

			appendToGameLog(currentPlayer.getCustomName() + " has forfeited the game, and their assets have" +
					"been transferred to " + creditorPlayer.getCustomName() + "!");
			currentPlayer.setHasRequestedEndTurn(true);
			endTurnManager();
			currentPlayer.setIsPlayerActive(false);
		}

	}

	public void debugToolsGiveAllProperties(int playerID) {
		currentDebugPlayer = board.players.get(playerID);
		logHelper.appendToDebugLog("Giving " + currentDebugPlayer.getCustomName() + " all properties.");

		Property localProperty;
		for (Space s : board.spaces) {
			if (s instanceof Property) {
				localProperty = (Property) s;

				localProperty.setIsOwned(true);
				localProperty.setOwnerID(playerID);
				currentPlayer.setPropertyOwnedState(true, localProperty.getID());
			}
		}

		board.forceBoardSelfCheck();
	}

	public void debugToolsRandomlyDistributeAllProperties() {
		ArrayList<Player> activePlayers = new ArrayList<>();

		for (Player p : board.players) {
			if (p.getIsActive()) {
				activePlayers.add(p);
			}
		}

		for (Space s : board.spaces) {
			if (s instanceof Property p) {
				int randomPlayerID = 1 + (int) (Math.random() * activePlayers.size());
				System.out.println(randomPlayerID);
				p.setIsOwned(true);
				p.setOwnerID(randomPlayerID);
			}
		}
	}

	public void improvementsManager(int spaceID, ImprovementsActions inputAction) {
		Space localSpace = board.spaces.get(spaceID);
		Color localColor = null;

		if (localSpace instanceof Color) {
			localColor = (Color) localSpace;
		}
		else {
			System.err.println("Improvements: property not Color.");
		}

		// Build house
		if (inputAction.equals(ImprovementsActions.buildHouse)) {
			if (localColor.getIsEligibleForImprovements()) {
				// Maximum houses on space
				if (localColor.getHouseCount() == 4) {
					appendToGameLog(localColor.getFriendlyName() + " already has the maximum number of houses.");
				}

				else if (localColor.getHotelCount() == 1) {
					appendToGameLog(localColor.getFriendlyName() + " cannot have "
						+ "houses constructed when a hotel is already present.");
				}

				else if (currentPlayer.getCurrentBalance() >= localColor.getHouseCost()) {
					if (board.getBankHouseCount() > 0) {
						localColor.buildHouse();

						// Debit player
						currentPlayer.updateCurrentBalance(-1 * localColor.getHouseCost());
					}
					else {
						appendToGameLog("There are no available houses to purchase from the Bank.");
					}
				}

				else {
					appendToGameLog("You do not have sufficient funds to construct "
						+ "a house on this property.");
				}

			}
		}

		// Sell house
		else if (inputAction.equals(ImprovementsActions.sellHouse)) {
			if (localColor.getHouseCount() > 0) {
				localColor.sellHouse();
				currentPlayer.updateCurrentBalance((int) (localColor.getHouseCost() * 0.5));
			}
			else {
				appendToGameLog(localColor.getFriendlyName() + " does not have any houses to sell.");
			}
		}

		// Build hotel
		else if (inputAction.equals(ImprovementsActions.buildHotel)) {
			if (localColor.getIsEligibleForImprovements()) {
				if (localColor.getHouseCount() == 4) {
					localColor.buildHotel();
				}
				else {
					appendToGameLog(localColor.getFriendlyName() + " does not have enough houses "
						+ "for a hotel to be constructed.");
				}
			}
			else {
				appendToGameLog("You do not have sufficient funds to construct "
					+ "a hotel on this property.");
			}
		}

		// Sell hotel
		else if (inputAction.equals(ImprovementsActions.sellHotel)) {
			if (localColor.getHotelCount() > 0) {
				localColor.sellHotel();
				currentPlayer.updateCurrentBalance((int) (localColor.getHotelCost() * 0.5));
			}
			else {
				appendToGameLog(localColor.getFriendlyName() + " does not have any hotels to sell.");
			}
		}
	}

	public void mortgageProperty(int spaceID, MortgageActions inputAction) {
		Space localSpace = board.spaces.get(spaceID);

		int balanceUpdate = 0;

		if (localSpace instanceof Property p) {
			if (inputAction == MortgageActions.MORTGAGE) {
				p.setIsMortgaged(true);
				balanceUpdate = p.getMortgageValue();
			}
			else {
				p.setIsMortgaged(false);
				balanceUpdate = (-1) * p.getUnmortgageCost();
			}

			// Update balance of player who owns property
			board.players.get(p.getOwnerID()).updateCurrentBalance(balanceUpdate);
		}
	}

	private void victoryConditionMet() {
		if (board.getNextActivePlayerID() == currentPlayer.getPlayerID()) {
			appendToGameLog("Congratulations! " + currentPlayer.getCustomName() + " is the only remaining" +
					" player in the game, and has won!");

			victoryConditionMet = true;
		}
	}
}
