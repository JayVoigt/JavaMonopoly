package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.Map;

/**
 * @author jay
 */
public class Railroad extends Property implements Serializable {

    int ownedRailroads;
    int rentRailroad2,
            rentRailroad3,
            rentRailroad4;

    public Railroad(Map<String, Integer> propertyAttributes, int inputSpaceID, String inputFriendlyName) {
        super(null);
        id = inputSpaceID;
        friendlyName = inputFriendlyName;

        purchaseCost = propertyAttributes.get("purchaseCost");
        rentBase = propertyAttributes.get("rentBase");
        rentRailroad2 = propertyAttributes.get("rentHouse1");
        rentRailroad3 = propertyAttributes.get("rentHouse2");
        rentRailroad4 = propertyAttributes.get("rentHouse3");
    }

    @Override
    public int calculateRent() {
        if (ownedRailroads == 2) {
            return rentRailroad2;
        } else if (ownedRailroads == 3) {
            return rentRailroad3;
        } else if (ownedRailroads == 4) {
            return rentRailroad4;
        } else {
            return rentBase;
        }
    }

    public int getOwnedRailroads() {
        return ownedRailroads;
    }

    public void setOwnedRailroads(int input) {
        ownedRailroads = input;
    }
}
