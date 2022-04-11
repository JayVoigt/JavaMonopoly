package cc.jayv.monopoly3;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jay
 */
public abstract class Property extends Space implements Serializable {

	// <editor-fold desc="Attributes">
	public enum propertyTypeKeys {
		unspecified,
		color,
		railroad,
		utility
	}
	propertyTypeKeys propertyType;

	public enum QueryRentStates {
		NO_FULL_SET,
		FULL_SET,
		HOUSE_1,
		HOUSE_2,
		HOUSE_3,
		HOUSE_4,
		HOTEL,

		RAILROAD_1,
		RAILROAD_2,
		RAILROAD_3,
		RAILROAD_4
	}

	boolean isOwned,
		isMortgaged,
		isFullSetOwned;

	int ownerID;

	// Note that rentHouse<n> variables are used for both Color and Railroad properties.
	int rentBase,
		rentHouse1,
		rentHouse2,
		rentHouse3,
		rentHouse4,
		rentHotel;

	int purchaseCost,
		mortgageValue,
		houseCost,
		hotelCost;
	// </editor-fold>

	// <editor-fold desc="Constructor">
	protected Property(HashMap<String, Integer> propertyAttributes) {
		spaceType = spaceTypeKeys.property;
		propertyType = propertyTypeKeys.unspecified;

		isOwned = false;
		isMortgaged = false;
		isFullSetOwned = false;

		ownerID = 0;
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Setters and getters">
	public propertyTypeKeys getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(propertyTypeKeys inputPropertyType) {
		propertyType = inputPropertyType;
	}

	public boolean getIsOwned() {
		return isOwned;
	}

	public void setIsOwned(boolean inputIsOwned) {
		isOwned = inputIsOwned;
	}

	public boolean getIsMortgaged() {
		return isMortgaged;
	}

	public void setIsMortgaged(boolean inputIsMortgaged) {
		isMortgaged = inputIsMortgaged;
	}

	public boolean getIsFullSetOwned() {
		return isFullSetOwned;
	}

	public void setIsFullSetOwned(boolean inputIsFullSetOwned) {
		if (ownerID != 0) {
			isFullSetOwned = inputIsFullSetOwned;
		}
		else {
			isFullSetOwned = false;
		}
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int inputOwnerID) {
		ownerID = inputOwnerID;
	}

	public int getPurchaseCost() {
		return purchaseCost;
	}
	// </editor-fold>

	/**
	 * Calculate the rent that needs to be paid if a player lands on this space.
	 *
	 * @return The rent that needs to be paid.
	 */
	abstract int calculateRent();

	public String queryRentValue(QueryRentStates rentState) {
		int rentValue = 0;

		switch (rentState) {
			case FULL_SET -> rentValue = 2 * rentBase;
			case NO_FULL_SET, RAILROAD_1 -> rentValue = rentBase;
			case HOUSE_1, RAILROAD_2 -> rentValue = rentHouse1;
			case HOUSE_2, RAILROAD_3 -> rentValue = rentHouse2;
			case HOUSE_3, RAILROAD_4 -> rentValue = rentHouse3;
			case HOUSE_4 -> rentValue = rentHouse4;
			case HOTEL -> rentValue = rentHotel;
		}

		return "$" + String.valueOf(rentValue);
	}
}
