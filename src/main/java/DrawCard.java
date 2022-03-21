
import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jay
 */
public class DrawCard implements Serializable {

	/**
	 * The type of DrawCard event that describes this object.<br>
	 * The default value is <code>unspecified</code>.
	 */
	public enum drawCardTypeKeys {
		unspecified,
		teleport,
		teleportRelative,
		teleportWithRentModifier,
		balanceUpdate,
		getOutOfJailFreeCard,
		jailPlayer,
		improvementRepairs,
		distributedBalanceUpdate
	}
	drawCardTypeKeys drawCardType;

	/**
	 * For DrawCard objects of type <code>teleportRelative</code>, this describes the target teleport destination,
	 * relative to the player, to be calculated given the player's position.<br>
	 * The default value is <code>unspecified</code>.
	 */
	public enum destinationRelativeTypeKeys {
		unspecified,
		railroad,
		utility,
		backThreeSpaces
	}
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
		drawCardType = drawCardTypeKeys.unspecified;
		destinationRelativeType = destinationRelativeTypeKeys.unspecified;

		message = "";
		destinationSpaceID = 0;
		movementQuantity = 0;
	}

	/**
	 * Parameterized constructor
	 * @param inputDrawCardType
	 * @param inputDestinationRelativeType
	 * @param inputMessage
	 * @param inputDestinationSpaceID
	 * @param inputMovementQuantity 
	 */
	public DrawCard(drawCardTypeKeys inputDrawCardType, destinationRelativeTypeKeys inputDestinationRelativeType,
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

	public drawCardTypeKeys getDrawCardType() {
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
}
