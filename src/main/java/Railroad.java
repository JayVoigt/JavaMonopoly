import java.io.Serializable;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class Railroad extends Property implements Serializable {
	public Railroad(Map<String, Integer> propertyAttributes, int inputSpaceID, String inputFriendlyName) {
		super(null);
		id = inputSpaceID;
		friendlyName = inputFriendlyName;
		
		purchaseCost = propertyAttributes.get("purchaseCost");
		System.out.println("Railroad: " + purchaseCost);
		rentHouse1 = propertyAttributes.get("rentHouse1");
		rentHouse2 = propertyAttributes.get("rentHouse2");
		rentHouse3 = propertyAttributes.get("rentHouse3");
		rentHouse4 = propertyAttributes.get("rentHouse4");
	}
	
	@Override
	public int calculateRent() {
		return 0;
	}
}
