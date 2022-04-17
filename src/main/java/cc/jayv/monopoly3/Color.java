package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author jay
 */
public class Color extends Property implements Serializable {

    // <editor-fold desc="Attributes">

    colorGroupKeys colorGroup;
    int houseCount,
            hotelCount;
    boolean isEligibleForImprovements;
    ArrayList<Integer> hasHouseID;
    ArrayList<Integer> hasHotelID;
    /**
     * @param propertyAttributes
     * @param inputSpaceID       The ID of the Space to become a Color type.
     * @param inputFriendlyName  The name of the Color property, e.g., "Boardwalk".
     */
    Color(Map<String, Integer> propertyAttributes, int inputSpaceID, String inputFriendlyName) {
        super(null);

        propertyType = propertyTypeKeys.color;
        colorGroup = colorGroupKeys.unspecified;
        id = inputSpaceID;
        friendlyName = inputFriendlyName;

        houseCount = 0;
        hotelCount = 0;

        rentBase = propertyAttributes.get("rentBase");
        rentHouse1 = propertyAttributes.get("rentHouse1");
        rentHouse2 = propertyAttributes.get("rentHouse2");
        rentHouse3 = propertyAttributes.get("rentHouse3");
        rentHouse4 = propertyAttributes.get("rentHouse4");
        rentHotel = propertyAttributes.get("rentHotel");

        purchaseCost = propertyAttributes.get("purchaseCost");
        mortgageValue = propertyAttributes.get("mortgageValue");
        houseCost = propertyAttributes.get("houseCost");
        hotelCost = propertyAttributes.get("hotelCost");

        isEligibleForImprovements = true;
    }

    // </editor-fold>
    // <editor-fold desc="Constructor">

    // <editor-fold defaultstate="collapsed" desc="Setters and getters">
    public colorGroupKeys getColorGroup() {
        return colorGroup;
    }
    // </editor-fold>

    public void setColorGroup(colorGroupKeys inputColorGroup) {
        colorGroup = inputColorGroup;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(int inputHouseCount) {
        houseCount = inputHouseCount;
    }

    /**
     * Update the quantity of houses as necessary when one is constructed.
     */
    public void buildHouse() {
        // houseCount: [0, 4)
        if ((houseCount < 4) && (houseCount >= 0)) {
            houseCount++;
        }
    }

    /**
     * Update the quantity of houses as necessary when one is sold.
     */
    public void sellHouse() {
        // houseCount: (0, 4]
        if ((houseCount <= 4) && (houseCount > 0)) {
            houseCount--;
        }
    }

    public int getHouseCost() {
        return houseCost;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(int inputHotelCount) {
        hotelCount = inputHotelCount;
    }

    /**
     * Update the quantity of hotels as necessary when one is constructed.
     */
    public void buildHotel() {
        if (hotelCount == 0) {
            hotelCount = 1;
            houseCount = 0;
        }
    }

    /**
     * Update the quantity of hotels as necessary when one is sold.
     */
    public void sellHotel() {
        if (hotelCount == 1) {
            hotelCount = 0;
            houseCount = 4;
        }
    }

    public int getHotelCost() {
        return hotelCost;
    }

    public boolean getIsEligibleForImprovements() {
        return isEligibleForImprovements;
    }

    public void setIsEligibleForImprovements() {

    }

    /**
     * Calculate the rent that needs to be paid if a player lands on this space.<br>
     * The calculation is dependent on:<br>
     * - if the property is mortgaged<br>
     * - if any improvements are constructed<br>
     * - if the full color set is owned<br>
     *
     * @return The amount of rent that needs to be paid.
     */
    @Override
    public int calculateRent() {
        if (isMortgaged) {
            return 0;
        } else {
            if (hotelCount == 1) {
                return rentHotel;
            } else if (houseCount == 0) {
                if (getIsFullSetOwned()) {
                    return 2 * rentBase;
                }
                return rentBase;
            } else if (houseCount == 1) {
                return rentHouse1;
            } else if (houseCount == 2) {
                return rentHouse2;
            } else if (houseCount == 3) {
                return rentHouse3;
            } else if (houseCount == 4) {
                return rentHouse4;
            }
        }

        return 0;
    }


    // </editor-fold>

    /**
     * Which color group the Color object belongs to.<br>
     * The default value is <code>unspecified</code>.
     */
    public enum colorGroupKeys {
        unspecified,
        brown,
        lightBlue,
        magenta,
        orange,
        red,
        yellow,
        green,
        darkBlue
    }
}
