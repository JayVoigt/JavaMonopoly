package cc.jayv.monopoly3;

import java.util.Map;

/**
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
        int diceSum = (int) (1 + Math.random() * 6);
        if (isFullSetOwned) {
            return 10 * diceSum;
        } else {
            return 4 * diceSum;
        }
    }
}
