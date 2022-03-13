
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class Utility extends Property {
	public Utility(Map<String, Integer> propertyAttributes, int inputSpaceID, String inputFriendlyName) {
		super(null);
		
		id = inputSpaceID;
		friendlyName = inputFriendlyName;
		
		purchaseCost = propertyAttributes.get("purchaseCost");
	}
	
	@Override
	public int calculateRent() {
		if (isFullSetOwned == true) {
			return 0;
		}
		else {
			return 0;
		}
	}
}
