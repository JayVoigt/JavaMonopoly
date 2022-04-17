package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author jay
 */
public abstract class Property extends Space implements Serializable {

    propertyTypeKeys propertyType;
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

    // <editor-fold desc="Constructor">
    protected Property(HashMap<String, Integer> propertyAttributes) {
        spaceType = spaceTypeKeys.property;
        propertyType = propertyTypeKeys.unspecified;

        isOwned = false;
        isMortgaged = false;
        isFullSetOwned = false;

        ownerID = 0;
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and getters">
    public propertyTypeKeys getPropertyType() {
        return propertyType;
    }
    // </editor-fold>

    public void setPropertyType(propertyTypeKeys inputPropertyType) {
        propertyType = inputPropertyType;
    }
    // </editor-fold>

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
        } else {
            isFullSetOwned = false;
        }
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public int getUnmortgageCost() {
        // Properties can be unmortgaged for 110% of the mortgage cost
        return (int) (1.1 * mortgageValue);
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

        return "$" + rentValue;
    }
    // </editor-fold>

    // <editor-fold desc="Attributes">
    public enum propertyTypeKeys {
        unspecified,
        color,
        railroad,
        utility
    }

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
}
