
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
public abstract class Property extends Space implements Serializable {
	public enum propertyTypeKeys {
		unspecified,
		color,
		railroad,
		utility
	}
	propertyTypeKeys propertyType;

	boolean isOwned, isMortgaged;
	boolean isFullSetOwned;
	int ownerID;
	
	int rentBase, rentHouse1, rentHouse2, rentHouse3, rentHouse4, rentHotel;
	int purchaseCost, mortgageValue, houseCost, hotelCost;
	
	Property(Map<String, Integer> propertyAttributes) {
		spaceType = spaceTypeKeys.property;
		propertyType = propertyTypeKeys.unspecified;
		
		isOwned = false;
		isMortgaged = false;
		
		ownerID = 0;
		
		rentBase = staticValues[0];
		rentHouse1 = staticValues[1];
		rentHouse2 = staticValues[2];
		rentHouse3 = staticValues[3];
		rentHouse4 = staticValues[4];
		rentHotel = staticValues[5];
		
		purchaseCost = staticValues[6];
		mortgageValue = staticValues[7];
		houseCost = staticValues[8];
		hotelCost = staticValues[9];
	}
	
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
		isFullSetOwned = inputIsFullSetOwned;
	}
	
	public int getOwnerID() {
		return ownerID;
	}
	
	public void setOwnerID(int inputOwnerID) {
		ownerID = inputOwnerID;
	}
	// </editor-fold>
	
	/**
	 * Calculate the rent that needs to be paid if a player lands on this space.
	 * @return The rent that needs to be paid.
	 */
	abstract int calculateRent();
}
