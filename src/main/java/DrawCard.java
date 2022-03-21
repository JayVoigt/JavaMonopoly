
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

	public enum destinationRelativeTypeKeys {
		unspecified,
		railroad,
		utility,
		backThreeSpaces
	}
	destinationRelativeTypeKeys destinationRelativeType;

	String message;
	int destinationSpaceID,
		movementQuantity;

	public DrawCard() {
		drawCardType = drawCardTypeKeys.unspecified;
		destinationRelativeType = destinationRelativeTypeKeys.unspecified;

		message = "";
		destinationSpaceID = 0;
		movementQuantity = 0;
	}

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
