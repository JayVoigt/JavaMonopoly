package cc.jayv.monopoly3;

import java.io.Serializable;

/**
 *
 * @author jay
 */
public abstract class Space implements Serializable {

	int id;
	String friendlyName;

	public enum spaceTypeKeys {
		unspecified,
		property,
		gameEvent
	}
	spaceTypeKeys spaceType;
	
	public enum buttonAppearanceKeys {
		none,
		previousSpace,
		newSpace,
		canPerformAction
	}
	buttonAppearanceKeys buttonAppearance;

	int timesLanded;

	Space() {
		id = -1;
		friendlyName = "";
		spaceType = spaceTypeKeys.unspecified;
		buttonAppearance = buttonAppearanceKeys.none;
		timesLanded = 0;
	}

	Space(int inputID, spaceTypeKeys inputSpaceType, String inputFriendlyName) {
		id = inputID;
		spaceType = inputSpaceType;
		buttonAppearance = buttonAppearanceKeys.none;
		friendlyName = inputFriendlyName;
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
	
	public buttonAppearanceKeys getButtonAppearance() {
		return buttonAppearance;
	}
	
	public void setButtonAppearance(buttonAppearanceKeys inputAppearance) {
		buttonAppearance = inputAppearance;
	}

}
