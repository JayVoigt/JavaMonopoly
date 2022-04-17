package cc.jayv.monopoly3;

import java.io.Serializable;

/**
 * @author jay
 */
public abstract class Space implements Serializable {

    int id;
    String friendlyName;
    spaceTypeKeys spaceType;
    int timesLanded;

    Space() {
        id = -1;
        friendlyName = "";
        spaceType = spaceTypeKeys.unspecified;
        timesLanded = 0;
    }

    public int getID() {
        return id;
    }

    public void setID(int inputID) {
        id = inputID;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String inputFriendlyName) {
        friendlyName = inputFriendlyName;
    }

    public int getTimesLanded() {
        return timesLanded;
    }

    public void incrementTimesLanded() {
        timesLanded++;
    }

    public spaceTypeKeys getSpaceType() {
        return spaceType;
    }

    public enum spaceTypeKeys {
        unspecified,
        property,
        gameEvent
    }


}
