package cc.jayv.monopoly3;

import java.io.Serializable;

/**
 *
 */
public class DrawCard implements Serializable {

    DrawCardTypeKeys drawCardType;
    DestinationRelativeTypeKeys destinationRelativeType;
    /**
     * The message which will be displayed in the game log when a player draws this card.
     */
    String message;
    int destinationSpaceID;
    int movementQuantity;

    /**
     * Default constructor
     */
    public DrawCard() {
        drawCardType = DrawCardTypeKeys.UNSPECIFIED;
        destinationRelativeType = DestinationRelativeTypeKeys.UNSPECIFIED;

        message = "";
        destinationSpaceID = 0;
        movementQuantity = 0;
    }

    /**
     * Construct a DrawCard event for Community Chest and Chance GameEvent spaces.
     *
     * @param inputDrawCardType The type of DrawCard event.
     * @param inputDestinationRelativeType If type is of TELEPORT_RELATIVE, the type of destination to teleport to.
     * @param inputMessage The message to be displayed in the game log when a player draws this card.
     * @param inputDestinationSpaceID If type is of TELEPORT, the space ID to teleport to.
     * @param inputMovementQuantity If of type TELEPORT_RELATIVE and BACK_THREE_SPACES, the number of spaces to move the player.
     */
    public DrawCard(DrawCardTypeKeys inputDrawCardType, DestinationRelativeTypeKeys inputDestinationRelativeType,
                    String inputMessage, int inputDestinationSpaceID, int inputMovementQuantity) {

        drawCardType = inputDrawCardType;
        destinationRelativeType = inputDestinationRelativeType;

        message = inputMessage;
        destinationSpaceID = inputDestinationSpaceID;
        movementQuantity = inputMovementQuantity;
    }

    public String getMessage() {
        return message;
    }

    public DrawCardTypeKeys getDrawCardType() {
        return drawCardType;
    }

    public DestinationRelativeTypeKeys getDestinationRelativeType() {
        return destinationRelativeType;
    }

    public int getDestinationSpace() {
        return destinationSpaceID;
    }

    public int getQuantity() {
        return movementQuantity;
    }

    /**
     * The type of DrawCard event that describes this object.<br>
     * The default value is <code>unspecified</code>.
     */
    public enum DrawCardTypeKeys {
        UNSPECIFIED,
        TELEPORT,
        TELEPORT_RELATIVE,
        TELEPORT_WITH_RENT_MODIFIER,
        BALANCE_UPDATE,
        GET_OUT_OF_JAIL_FREE_CARD,
        JAIL_PLAYER,
        IMPROVEMENT_REPAIRS,
        DISTRIBUTED_BALANCE_UPDATE
    }

    /**
     * For DrawCard objects of type <code>teleportRelative</code>, this describes the target teleport destination,
     * relative to the player, to be calculated given the player's position.<br>
     * The default value is <code>unspecified</code>.
     */
    public enum DestinationRelativeTypeKeys {
        UNSPECIFIED,
        RAILROAD,
        UTILITY,
        BACK_THREE_SPACES
    }
}
