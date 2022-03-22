package cc.jayv.monopoly3;


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

	// <editor-fold desc="Constructor">
	public Utility(Map<String, Integer> propertyAttributes, int inputSpaceID, String inputFriendlyName) {
		super(null);

		id = inputSpaceID;
		friendlyName = inputFriendlyName;

		purchaseCost = propertyAttributes.get("purchaseCost");
	}
	// </editor-fold>

	@Override
	public int calculateRent() {
		int diceSum = (int) (1 + Math.random() * 6);
		if (isFullSetOwned == true) {
			return 10 * diceSum;
		}
		else {
			return 4 * diceSum;
		}
	}
}