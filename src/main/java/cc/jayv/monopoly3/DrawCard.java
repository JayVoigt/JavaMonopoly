package cc.jayv.monopoly3;

import java.io.Serializable;

/**
 * @author jay
 */
public class DrawCard implements Serializable {

    DrawCardTypeKeys drawCardType;
    destinationRelativeTypeKeys destinationRelativeType;
    /**
     * The message which will be displayed in the game log when a player "draws" this card.
     */
    String message;
    int destinationSpaceID,
            movementQuantity;

    /**
     * Default constructor
     */
    public DrawCard() {
        drawCardType = DrawCardTypeKeys.UNSPECIFIED;
        destinationRelativeType = destinationRelativeTypeKeys.UNSPECIFIED;

        message = "";
        destinationSpaceID = 0;
        movementQuantity = 0;
    }

    /**
     * Parameterized constructor
     *
     * @param inputDrawCardType
     * @param inputDestinationRelativeType
     * @param inputMessage
     * @param inputDestinationSpaceID
     * @param inputMovementQuantity
     */
    public DrawCard(DrawCardTypeKeys inputDrawCardType, destinationRelativeTypeKeys inputDestinationRelativeType,
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

    public destinationRelativeTypeKeys getDestinationRelativeType() {
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
    public enum destinationRelativeTypeKeys {
        UNSPECIFIED,
        RAILROAD,
        UTILITY,
        BACK_THREE_SPACES
    }
}
