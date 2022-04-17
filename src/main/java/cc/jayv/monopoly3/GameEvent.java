package cc.jayv.monopoly3;

import java.io.Serializable;

/**
 * @author jay
 */
public class GameEvent extends Space implements Serializable {

    GameEventTypeKeys gameEventType;

    /**
     * Parameterized constructor
     *
     * @param inputGameEventType The type of game event that landing on this space will trigger.
     * @param inputSpaceID The ID of the game event space.
     * @param inputFriendlyName The friendly name of the space.
     */
    GameEvent(GameEventTypeKeys inputGameEventType, int inputSpaceID, String inputFriendlyName) {
        id = inputSpaceID;
        friendlyName = inputFriendlyName;

        spaceType = Space.spaceTypeKeys.gameEvent;
        gameEventType = inputGameEventType;
    }

    public GameEventTypeKeys getGameEventType() {
        return gameEventType;
    }

    /**
     * The type of GameEvent that describes this object.<br>
     * The default value is <code>unspecified</code>.
     */
    public enum GameEventTypeKeys {
        UNSPECIFIED,
        DRAW_CARD,
        UPDATE_BALANCE,
        TELEPORT,
        JAIL_PLAYER,
        TAX,
        JAIL_SPACE,
        FREE_PARKING
    }
}
