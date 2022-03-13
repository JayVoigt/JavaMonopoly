
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
	
	Space() {
		id = -1;
		friendlyName = "";
	}
	
	Space (int inputID, spaceTypeKeys inputSpaceType, String inputFriendlyName) {
		id = inputID;
		spaceType = inputSpaceType;
		friendlyName = inputFriendlyName;
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
}
