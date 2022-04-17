package cc.jayv.monopoly3;

import java.io.Serializable;

/**
 * @author jay
 */
public class GameEvent extends Space implements Serializable {

    gameEventTypeKeys gameEventType;

    /**
     * Parameterized constructor
     *
     * @param inputGameEventType
     * @param inputSpaceID
     * @param inputFriendlyName
     */
    GameEvent(gameEventTypeKeys inputGameEventType, int inputSpaceID, String inputFriendlyName) {
        id = inputSpaceID;
        friendlyName = inputFriendlyName;

        spaceType = Space.spaceTypeKeys.gameEvent;
        gameEventType = inputGameEventType;
    }

    // <editor-fold defaultstate="collapsed" desc="Setters and getters">
    public gameEventTypeKeys getGameEventType() {
        return gameEventType;
    }

    public void setGameEventType(gameEventTypeKeys inputGameEventType) {
        gameEventType = inputGameEventType;
    }

    /**
     * The type of GameEvent that describes this object.<br>
     * The default value is <code>unspecified</code>.
     */
    public enum gameEventTypeKeys {
        unspecified,
        drawCard,
        updateBalance,
        teleport,
        jailPlayer,
        tax,
        jail,
        freeParking
    }
    // </editor-fold>
}
