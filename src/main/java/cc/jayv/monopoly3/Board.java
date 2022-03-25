package cc.jayv.monopoly3;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jay
 */
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Board implements Serializable {

	/**
	 * Spaces
	 */
	public List<Space> spaces = new ArrayList<>();
	public List<Player> players = new ArrayList<>();

	public List<DrawCard> chanceCards = new ArrayList<>();
	public List<DrawCard> communityChestCards = new ArrayList<>();

	int currentPlayerID;
	int bankHouseCount,
		bankHotelCount;

	ArrayList<Color> spacesByColorGroup = new ArrayList<>();
	ArrayList<Property> spacesByOwnerID = new ArrayList<>();

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

		try ( BufferedReader spacesConfig = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("spaces.csv")))) {
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

	private int parseIntHandler(String inputString) {
		if (inputString.isEmpty()) {
			return 0;
		}
		else {
			return Integer.parseInt(inputString);
		}
	}	// end parseIntHandler()

	public void updatePropertyOwnershipRelationships() {
		updateColorPropertyOwnershipRelationships();
		updateRailroadPropertyOwnershipRelationships();
	}

	public ArrayList<Color> getSpacesByColorGroup(Color.colorGroupKeys inputColorGroup) {
		Color localColor;

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

	private void updateColorPropertyOwnershipRelationships() {
		for (Color.colorGroupKeys colorGroup : Color.colorGroupKeys.values()) {
			getSpacesByColorGroup(colorGroup);
			boolean isFullSetOwnedBySinglePlayer = false;

			if (colorGroup != Color.colorGroupKeys.unspecified) {
				int localColorOwnerID = spacesByColorGroup.get(0).getOwnerID();

				for (Color localColor : spacesByColorGroup) {
					if (localColor.getOwnerID() != localColorOwnerID) {
						isFullSetOwnedBySinglePlayer = false;
					}
					else {
						isFullSetOwnedBySinglePlayer = true;
					}
				}	// end for
			}	// end if

			if (isFullSetOwnedBySinglePlayer == true) {
				for (Color localColor : spacesByColorGroup) {
					localColor.setOwnerID(spacesByColorGroup.get(0).getOwnerID());
					localColor.setIsFullSetOwned(true);
				}	// end for
			}	// end if
		}	// end for
	}

	private void updateRailroadPropertyOwnershipRelationships() {
		int ownedRailroads = 0;

		for (Player p : players) {
			ownedRailroads = 0;
			ArrayList<Railroad> railroadsOwnedBySamePlayer = new ArrayList<>();

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

	public ArrayList<Property> getSpacesByOwnerID(int ownerID) {
		Property localProperty;

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

	private void updateImprovementEligibility() {
		for (Color.colorGroupKeys colorGroup : Color.colorGroupKeys.values()) {
			getSpacesByColorGroup(colorGroup);

			if (colorGroup != Color.colorGroupKeys.unspecified) {

				// This logic depends on the execution of updateColorPropertyOwnershipRelationships
				if (spacesByColorGroup.get(0).getIsFullSetOwned()) {
					for (Color c : spacesByColorGroup) {

					}	// end for
				}	// end if
			}	// end if

		}	// end for

	}

}	// end class
