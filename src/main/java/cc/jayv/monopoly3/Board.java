package cc.jayv.monopoly3;

/**
 *
 * @author jay
 */
import java.util.*;
import java.io.*;

/**
 * The primary data class for the game.
 * @author jay
 */
public class Board implements Serializable {

	public List<Space> spaces = new ArrayList<>();
	public List<Player> players = new ArrayList<>();

	public List<DrawCard> chanceCards = new ArrayList<>();
	public List<DrawCard> communityChestCards = new ArrayList<>();

	int currentPlayerID;
	int bankHouseCount,
		bankHotelCount;
	
	int repairCostHouse,
		repairCostHotel;

	ArrayList<Color> spacesByColorGroup = new ArrayList<>();
	ArrayList<Property> spacesByOwnerID = new ArrayList<>();

	/**
	 * Call all methods which evaluate the state of the board, and update
	 * relationships between Spaces.
	 */
	public void forceBoardSelfCheck() {
		updatePropertyOwnershipRelationships();
		updateImprovementEligibility();
	}

	// <editor-fold desc="Constructor">
	/**
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	Board() throws FileNotFoundException, IOException {
		// Using defaults from standard rules
		bankHouseCount = 32;
		bankHotelCount = 12;
		repairCostHouse = 25;
		repairCostHotel = 100;

		players.add(0, new Player());	// null player
		players.add(1, new Player());
		players.add(2, new Player());
		players.add(3, new Player());
		players.add(4, new Player());

		currentPlayerID = 1;

		players.get(1).setPlayerID(1);
		players.get(2).setPlayerID(2);
		players.get(3).setPlayerID(3);
		players.get(4).setPlayerID(4);

		// <editor-fold desc="Read CSV for Space data">
		// Read from CSV file for Space data
		String lineBuffer;
		HashMap<String, Integer> propertyAttributes = new HashMap();

		try ( BufferedReader spacesConfig = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("spaces.csv"))))) {
			spacesConfig.readLine();

			while ((lineBuffer = spacesConfig.readLine()) != null) {
				String[] configLine = lineBuffer.split(",", -1);

				int localID = parseIntHandler(configLine[0]);

				String localFriendlyName = configLine[1];
				String localSpaceType = configLine[2];
				String localPropertyType = configLine[3];
				String localColorGroup = configLine[4];

				propertyAttributes.put("purchaseCost", parseIntHandler(configLine[5]));
				propertyAttributes.put("rentBase", parseIntHandler(configLine[6]));
				propertyAttributes.put("rentHouse1", parseIntHandler(configLine[7]));
				propertyAttributes.put("rentHouse2", parseIntHandler(configLine[8]));
				propertyAttributes.put("rentHouse3", parseIntHandler(configLine[9]));
				propertyAttributes.put("rentHouse4", parseIntHandler(configLine[10]));
				propertyAttributes.put("rentHotel", parseIntHandler(configLine[11]));
				propertyAttributes.put("mortgageValue", parseIntHandler(configLine[12]));
				propertyAttributes.put("houseCost", parseIntHandler(configLine[13]));
				propertyAttributes.put("hotelCost", parseIntHandler(configLine[14]));

				if (localSpaceType.equals("gameEvent")) {
					spaces.add(localID, new GameEvent(GameEvent.gameEventTypeKeys.valueOf(localPropertyType), localID, localFriendlyName));
				}
				else if (localSpaceType.equals("property")) {

					if (localPropertyType.equals("color")) {
						spaces.add(localID, new Color(propertyAttributes, localID, localFriendlyName));
						Color localColor = (Color) spaces.get(localID);
						localColor.setColorGroup(Color.colorGroupKeys.valueOf(localColorGroup));
					}
					else if (localPropertyType.equals("railroad")) {
						spaces.add(localID, new Railroad(propertyAttributes, localID, localFriendlyName));
					}
					else if (localPropertyType.equals("utility")) {
						spaces.add(localID, new Utility(propertyAttributes, localID, localFriendlyName));
					}
				}	// end else if
			}	// end while
		} // end try
		// </editor-fold>

		// <editor-fold desc="Read CSV for chanceCards data">
		try ( BufferedReader chanceCardsConfig = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("chanceCards.csv")))) {
			chanceCardsConfig.readLine();

			while ((lineBuffer = chanceCardsConfig.readLine()) != null) {
				String[] configLine = lineBuffer.split(",", -1);

				int localID = parseIntHandler(configLine[0]);
				DrawCard.drawCardTypeKeys localDrawCardType = DrawCard.drawCardTypeKeys.valueOf(configLine[1]);
				String localDescription = configLine[2];
				int localDestination = parseIntHandler(configLine[3]);
				int localQuantity = parseIntHandler(configLine[4]);
				DrawCard.destinationRelativeTypeKeys localDestinationRelativeType;

				if (configLine[5] != "") {
					localDestinationRelativeType = DrawCard.destinationRelativeTypeKeys.valueOf(configLine[5]);
				}
				else {
					localDestinationRelativeType = DrawCard.destinationRelativeTypeKeys.unspecified;
				}

				chanceCards.add(localID, new DrawCard(localDrawCardType, localDestinationRelativeType,
					localDescription, localDestination, localQuantity));
			}
		}
		// </editor-fold>

		// <editor-fold desc="Read CSV for communityChestCards data">
		try ( BufferedReader communityChestCardsConfig = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("communityChestCards.csv")))) {
			communityChestCardsConfig.readLine();

			while ((lineBuffer = communityChestCardsConfig.readLine()) != null) {
				String[] configLine = lineBuffer.split(",", -1);

				int localID = parseIntHandler(configLine[0]);
				DrawCard.drawCardTypeKeys localDrawCardType = DrawCard.drawCardTypeKeys.valueOf(configLine[1]);
				String localDescription = configLine[2];
				int localDestination = parseIntHandler(configLine[3]);
				int localQuantity = parseIntHandler(configLine[4]);
				DrawCard.destinationRelativeTypeKeys localDestinationRelativeType;

				localDestinationRelativeType = DrawCard.destinationRelativeTypeKeys.unspecified;

				communityChestCards.add(localID, new DrawCard(localDrawCardType, localDestinationRelativeType,
					localDescription, localDestination, localQuantity));
			}
		}
		// </editor-fold>

	}	// end Board()
	// </editor-fold>

	public int getCurrentPlayerID() {
		return currentPlayerID;
	}

	public void setCurrentPlayerID(int inputCurrentPlayerID) {
		currentPlayerID = inputCurrentPlayerID;
	}

	public int getBankHouseCount() {
		bankHouseCount = 32;
		
		for (Space s : spaces) {
			if (s instanceof Color) {
				Color localColor = (Color) s;

				bankHouseCount -= (localColor.getHouseCount());
			}
		}
		return bankHouseCount;
	}

	public int getBankHotelCount() {
		bankHotelCount = 12;
		
		for (Space s : spaces) {
			if (s instanceof Color) {
				Color localColor = (Color) s;

				bankHotelCount -= (localColor.getHotelCount());
			}
		}
		return bankHotelCount;
	}
	
	public int getRepairCostHouse() {
		return repairCostHouse;
	}
	
	public int getRepairCostHotel() {
		return repairCostHotel;
	}

	/**
	 * Convert a string to an integer, where the returned value is set to 0
	 * if the string is empty.
	 * @param inputString A string to be converted to an integer.
	 * @return An integer value.
	 */
	private int parseIntHandler(String inputString) {
		if (inputString.isEmpty()) {
			return 0;
		}
		else {
			return Integer.parseInt(inputString);
		}
	}	// end parseIntHandler()

	/**
	 * Call all methods that evaluate the state of Properties.
	 */
	public void updatePropertyOwnershipRelationships() {
		updateColorPropertyOwnershipRelationships();
		updateRailroadPropertyOwnershipRelationships();
	}

	/**
	 * Return all spaces which match the color group given as a parameter.
	 * @param inputColorGroup The color group to return all Color spaces belonging to.
	 * @return An ArrayList of Color spaces, where all elements belong to the
	 * color group specified as a parameter.
	 */
	public ArrayList<Color> getSpacesByColorGroup(Color.colorGroupKeys inputColorGroup) {
		Color localColor;
		spacesByColorGroup.clear();

		for (Space s : spaces) {
			if (s instanceof Color) {
				localColor = (Color) s;

				if (localColor.getColorGroup().equals(inputColorGroup)) {
					spacesByColorGroup.add(localColor);
				}
			}
		}

		return spacesByColorGroup;
	}

	/**
	 * Update the relationships between Color spaces, including setting an
	 * indicator within the Color object if all spaces are owned by the same player.
	 */
	private void updateColorPropertyOwnershipRelationships() {
		for (Color.colorGroupKeys colorGroup : Color.colorGroupKeys.values()) {
			boolean isFullSetOwnedBySinglePlayer = false;

			if (colorGroup != Color.colorGroupKeys.unspecified) {
				getSpacesByColorGroup(colorGroup);
				int localColorOwnerID = spacesByColorGroup.get(0).getOwnerID();

				for (Color localColor : spacesByColorGroup) {
					if ((localColor.getOwnerID() != localColorOwnerID) && (localColor.getOwnerID() != 0)) {
						isFullSetOwnedBySinglePlayer = false;
					}
					else {
						isFullSetOwnedBySinglePlayer = true;
					}
				}	// end for
			

				if (isFullSetOwnedBySinglePlayer) {
					for (Color localColor : spacesByColorGroup) {
						int localOwnerID = localColor.getOwnerID();
						if (localOwnerID != 0) {
							localColor.setOwnerID(localOwnerID);
						}
						localColor.setIsFullSetOwned(true);
					}	// end for
				}	// end if
			}	// end if
		}	// end for
	}

	/**
	 * Update the relationships between Railroad spaces, including setting an
	 * indicator within the Railroad object if all spaces are owned by the same player.
	 */
	private void updateRailroadPropertyOwnershipRelationships() {
		int ownedRailroads = 0;

		for (Player p : players) {
			ArrayList<Railroad> railroadsOwnedBySamePlayer = new ArrayList<>();
			railroadsOwnedBySamePlayer.clear();
			
			// Ref: https://stackoverflow.com/questions/4819635/how-to-efficiently-remove-all-null-elements-from-a-arraylist-or-string-array
			while(railroadsOwnedBySamePlayer.remove(null));
			
			if (p.getPlayerID() != 0) {
				for (Space s : spaces) {
					if (s instanceof Railroad) {
						railroadsOwnedBySamePlayer.add((Railroad) s);
					}	// end if
				}	// end for
			}	// end if

			for (Railroad r : railroadsOwnedBySamePlayer) {
				r.setOwnedRailroads(railroadsOwnedBySamePlayer.size());
			}
		}	// end for

	}

	/**
	 * Return all properties whose ownerID matches the given parameter.
	 * @param ownerID
	 * @return An ArrayList of Property objects.
	 */
	public ArrayList<Property> getSpacesByOwnerID(int ownerID) {
		Property localProperty;
		spacesByOwnerID.clear();
		while(spacesByOwnerID.remove(null));

		for (Space s : spaces) {
			if (s instanceof Property) {
				localProperty = (Property) s;

				if (localProperty.getOwnerID() == ownerID) {
					spacesByOwnerID.add(localProperty);
				}
			}
		}

		return spacesByOwnerID;
	}

	/**
	 * Update each Color space and determine if it is able to have improvements
	 * constructed on it.
	 */
	private void updateImprovementEligibility() {
		for (Color.colorGroupKeys colorGroup : Color.colorGroupKeys.values()) {
			getSpacesByColorGroup(colorGroup);

			if (colorGroup != Color.colorGroupKeys.unspecified) {

				// This logic depends on the execution of updateColorPropertyOwnershipRelationships
				if (spacesByColorGroup.get(0).getIsFullSetOwned() == true) {
					for ( int i = 0 ; i < spacesByColorGroup.size() ; i++ ) {
						
					}
				}	// end if
			}	// end if

		}	// end for

	}

}	// end class
