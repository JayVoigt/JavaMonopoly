package cc.jayv.monopoly3;


import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
	int timesLanded;

	Space() {
		id = -1;
		friendlyName = "";
		spaceType = spaceTypeKeys.unspecified;
		timesLanded = 0;
	}

	Space(int inputID, spaceTypeKeys inputSpaceType, String inputFriendlyName) {
		id = inputID;
		spaceType = inputSpaceType;
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

}
