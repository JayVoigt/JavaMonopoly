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
import java.util.Scanner;

public class Board implements Serializable {
	List<Space> spaces = new ArrayList<>();
	List<Player> players = new ArrayList<>();
	
	int currentPlayerID;
	
	int bankHouseCount, bankHotelCount;
	
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
	
		// Read from CSV file for Space data
		String lineBuffer = "";
		int staticValues[] = null;
		Map<String, Integer> propertyAttributes = new HashMap();
		BufferedReader spacesConfig = new BufferedReader(new FileReader("spaces.csv"));
		
		spacesConfig.readLine();
		
		while ((lineBuffer = spacesConfig.readLine()) != null) {
			String[] configLine = lineBuffer.split(",", -1);
			
			int localID = parseIntHandler(configLine[0]);
			
			String localFriendlyName =	configLine[1];
			String localSpaceType =		configLine[2];
			String localPropertyType =	configLine[3];
			String localColorGroup =	configLine[4];
			
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
				spaces.add(localID, new GameEvent());
				spaces.get(localID).setID(localID);
				spaces.get(localID).setFriendlyName(localFriendlyName);
			}
			else if (localSpaceType.equals("property")) {
				
				if (localPropertyType.equals("color")) {
					spaces.add(localID, new Color(propertyAttributes, localID, localFriendlyName));
				}
				else if (localPropertyType.equals("railroad")) {
					
				}
				else if (localPropertyType.equals("utility")) {
					spaces.add(localID, new Utility(propertyAttributes, localID, localFriendlyName));
				}
			}
		}
		spacesConfig.close();
		// End CSV reading
	}	// end Board()
	
	private int parseIntHandler(String inputString) {
		if (inputString.isEmpty()) {
			return 0;
		}
		else {
			return Integer.parseInt(inputString);
		}
	}	// end parseIntHandler()
}