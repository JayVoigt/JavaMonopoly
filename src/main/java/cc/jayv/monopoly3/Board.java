package cc.jayv.monopoly3;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * The primary data class for the game.
 */
public class Board implements Serializable {

    public final List<Space> spaces = new ArrayList<>();
    public final List<Player> players = new ArrayList<>();

    public final List<DrawCard> chanceCards = new ArrayList<>();
    public final List<DrawCard> communityChestCards = new ArrayList<>();

    int currentPlayerID;
    int bankHouseCount;
    int bankHotelCount;

    final int repairCostHouse;
    final int repairCostHotel;

    ArrayList<Color> spacesByColorGroup = new ArrayList<>();
    ArrayList<Property> spacesByOwnerID = new ArrayList<>();

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
    Board() throws FileNotFoundException, IOException {
        // Using defaults from standard rules
        bankHouseCount = 32;
        bankHotelCount = 12;
        repairCostHouse = 25;
        repairCostHotel = 100;

        players.add(0, new Player());    // null player
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

        try (BufferedReader spacesConfig = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("spaces.csv"))))) {
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

                if ("gameEvent".equals(localSpaceType)) {
                    spaces.add(localID, new GameEvent(GameEvent.GameEventTypeKeys.valueOf(localPropertyType), localID, localFriendlyName));
                } else if ("property".equals(localSpaceType)) {

                    switch (localPropertyType) {
                        case "color" -> {
                            spaces.add(localID, new Color(propertyAttributes, localID, localFriendlyName));
                            Color localColor = (Color) spaces.get(localID);
                            localColor.setColorGroup(Color.ColorGroupKeys.valueOf(localColorGroup));
                        }
                        case "railroad" -> spaces.add(localID, new Railroad(propertyAttributes, localID, localFriendlyName));
                        case "utility" -> spaces.add(localID, new Utility(propertyAttributes, localID, localFriendlyName));
                    }
                }    // end else if
            }    // end while
        } // end try
        // </editor-fold>

        // <editor-fold desc="Read CSV for chanceCards data">
        try (BufferedReader chanceCardsConfig = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("chanceCards.csv"))))) {
            chanceCardsConfig.readLine();

            while ((lineBuffer = chanceCardsConfig.readLine()) != null) {
                String[] configLine = lineBuffer.split(",", -1);

                int localID = parseIntHandler(configLine[0]);
                DrawCard.DrawCardTypeKeys localDrawCardType = DrawCard.DrawCardTypeKeys.valueOf(configLine[1]);
                String localDescription = configLine[2];
                int localDestination = parseIntHandler(configLine[3]);
                int localQuantity = parseIntHandler(configLine[4]);
                DrawCard.DestinationRelativeTypeKeys localDestinationRelativeType;

                if (!Objects.equals(configLine[5], "")) {
                    localDestinationRelativeType = DrawCard.DestinationRelativeTypeKeys.valueOf(configLine[5]);
                } else {
                    localDestinationRelativeType = DrawCard.DestinationRelativeTypeKeys.UNSPECIFIED;
                }

                chanceCards.add(localID, new DrawCard(localDrawCardType, localDestinationRelativeType,
                        localDescription, localDestination, localQuantity));
            }
        }
        // </editor-fold>

        // <editor-fold desc="Read CSV for communityChestCards data">
        try (BufferedReader communityChestCardsConfig = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("communityChestCards.csv"))))) {
            communityChestCardsConfig.readLine();

            while ((lineBuffer = communityChestCardsConfig.readLine()) != null) {
                String[] configLine = lineBuffer.split(",", -1);

                int localID = parseIntHandler(configLine[0]);
                DrawCard.DrawCardTypeKeys localDrawCardType = DrawCard.DrawCardTypeKeys.valueOf(configLine[1]);
                String localDescription = configLine[2];
                int localDestination = parseIntHandler(configLine[3]);
                int localQuantity = parseIntHandler(configLine[4]);
                DrawCard.DestinationRelativeTypeKeys localDestinationRelativeType;

                localDestinationRelativeType = DrawCard.DestinationRelativeTypeKeys.UNSPECIFIED;

                communityChestCards.add(localID, new DrawCard(localDrawCardType, localDestinationRelativeType,
                        localDescription, localDestination, localQuantity));
            }
        }
        // </editor-fold>

    }    // end Board()
    // </editor-fold>

    public int getCurrentPlayerID() {
        return currentPlayerID;
    }

    public void setCurrentPlayerID(int inputCurrentPlayerID) {
        currentPlayerID = inputCurrentPlayerID;
    }

    /**
     * @return The number of houses available for new improvements.
     */
    public int getBankHouseCount() {
        bankHouseCount = 32;

        for (Space s : spaces) {
            if (s instanceof Color localColor) {
                bankHouseCount -= (localColor.getHouseCount());
            }
        }
        return bankHouseCount;
    }

    /**
     * @return The number of hotels available for new improvements.
     */
    public int getBankHotelCount() {
        bankHotelCount = 12;

        for (Space s : spaces) {
            if (s instanceof Color localColor) {
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

    // <editor-fold desc="Constructor">

    /**
     * Call all methods which evaluate the state of the board, and update
     * relationships between Spaces.
     */
    public void forceBoardSelfCheck() {
        updatePropertyOwnershipRelationships();
        updateImprovementEligibility();
    }

    /**
     * Obtain the ID of the next active player, after the current turn.
     * @return The ID of the next player.
     */
    public int getNextActivePlayerID() {
        int nextActivePlayerID = 0;

        ArrayList<Player> activePlayers = new ArrayList<>();

        for (Player p : players) {
            if (p.getIsActive() && (p.getPlayerID() != 0)) {
                activePlayers.add(p);
            }
        }

        boolean playerFound = false;
        for (Player p : activePlayers) {
            if (!playerFound) {
                if (p.getPlayerID() > currentPlayerID) {
                    nextActivePlayerID = p.getPlayerID();
                    playerFound = true;
                }
            }
        }
        if (!playerFound) {
            nextActivePlayerID = activePlayers.get(0).getPlayerID();
        }

        return nextActivePlayerID;
    }

    /**
     * Obtain the ID of the space that occurs 1 ahead of the specified input space ID.
     * Alternatively may return a valid space ID for an input > 39.
     * @param spaceID The space ID to be checked.
     * @return The next space ID.
     */
    public int getNextSpaceID(int spaceID) {
        if (spaceID < 39) {
            return spaceID + 1;
        } else if (spaceID == 39) {
            return 0;
        } else {
            return 1 + (spaceID % 39);
        }
    }

    /**
     * Obtain the distance, in number of spaces, between any two space IDs.
     * @param spaceA The origin space ID.
     * @param spaceB The destination space ID.
     * @return The number of spaces between A and B. Always a positive integer.
     */
    public int getDistance(int spaceA, int spaceB) {
        int distance;

        if (spaceB < spaceA) {
            distance = (39 - spaceA) + spaceB;
        } else {
            distance = spaceB - spaceA;
        }

        return distance;
    }

    /**
     * Return all spaces which match the color group given as a parameter.
     *
     * @param inputColorGroup The color group to return all Color spaces belonging to.
     * @return An ArrayList of Color spaces, where all elements belong to the
     * color group specified as a parameter.
     */
    public ArrayList<Color> getSpacesByColorGroup(Color.ColorGroupKeys inputColorGroup) {
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
     * Return all properties whose ownerID matches the given parameter.
     *
     * @param ownerID The ID of the player whose properties will be returned.
     * @return An ArrayList of Property objects.
     */
    public ArrayList<Property> getSpacesByOwnerID(int ownerID) {
        Property localProperty;
        spacesByOwnerID.clear();
        while (spacesByOwnerID.remove(null)) ;

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
     * Call all methods that evaluate the state of Properties.
     */
    public void updatePropertyOwnershipRelationships() {
        updateColorPropertyOwnershipRelationships();
        updateRailroadPropertyOwnershipRelationships();
    }

    /**
     * Update the relationships between Color spaces, including setting an
     * indicator within the Color object if all spaces are owned by the same player.
     */
    private void updateColorPropertyOwnershipRelationships() {
        for (Color.ColorGroupKeys colorGroup : Color.ColorGroupKeys.values()) {
            boolean isFullSetOwnedBySinglePlayer = false;

            if (colorGroup != Color.ColorGroupKeys.UNSPECIFIED) {
                getSpacesByColorGroup(colorGroup);
                int localColorOwnerID = spacesByColorGroup.get(0).getOwnerID();

                for (Color localColor : spacesByColorGroup) {
                    isFullSetOwnedBySinglePlayer = (localColor.getOwnerID() == localColorOwnerID) || (localColor.getOwnerID() == 0);
                }    // end for


                if (isFullSetOwnedBySinglePlayer) {
                    for (Color localColor : spacesByColorGroup) {
                        int localOwnerID = localColor.getOwnerID();
                        if (localOwnerID != 0) {
                            localColor.setOwnerID(localOwnerID);
                        }
                        localColor.setIsFullSetOwned(true);
                    }    // end for
                }    // end if
            }    // end if
        }    // end for
    }

    /**
     * Update the relationships between Railroad spaces, including setting an
     * indicator within the Railroad object if all spaces are owned by the same player.
     */
    private void updateRailroadPropertyOwnershipRelationships() {
        for (Player p : players) {
            if (p.getPlayerID() != 0) {
                int railroadCounter = 0;
                for (Space s : spaces) {
                    if ((s instanceof Railroad r) && (r.getOwnerID() == p.getPlayerID())) {
                        railroadCounter++;
                    }    // end if
                }    // end for
                p.setOwnedRailroads(railroadCounter);
            }    // end if
        }    // end for

        for (Space s : spaces) {
            if (s instanceof Railroad r) {
                int ownedRailroads = players.get(r.getOwnerID()).getOwnedRailroads();
                r.setOwnedRailroads(ownedRailroads);
            }
        }
    }

    /**
     * Update each Color space and determine if it is able to have improvements
     * constructed on it.
     */
    private void updateImprovementEligibility() {
        for (Color.ColorGroupKeys colorGroup : Color.ColorGroupKeys.values()) {
            getSpacesByColorGroup(colorGroup);

            if (colorGroup != Color.ColorGroupKeys.UNSPECIFIED) {

                // This logic depends on the execution of updateColorPropertyOwnershipRelationships
                if (spacesByColorGroup.get(0).getIsFullSetOwned()) {
                    for (int i = 0; i < spacesByColorGroup.size(); i++) {
                        // need to implement
                    }
                }    // end if
            }    // end if

        }    // end for
    }

    /**
     * Convert a string to an integer, where the returned value is set to 0
     * if the string is empty.
     *
     * @param inputString A string to be converted to an integer.
     * @return An integer value.
     */
    private int parseIntHandler(String inputString) {
        if (inputString.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(inputString);
        }
    }    // end parseIntHandler()

}    // end class
