import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class GameEvent extends Space implements Serializable {
	public enum gameEventTypeKeys {
		unspecified,
		drawCard,
		updateBalance,
		teleport,
		jailPlayer
	}
	gameEventTypeKeys gameEventType;
	
	GameEvent(int inputSpaceID, String inputFriendlyName) {
		id = inputSpaceID;
		friendlyName = inputFriendlyName;
		
		spaceType = Space.spaceTypeKeys.gameEvent;
		gameEventType = gameEventTypeKeys.unspecified;
	}
	
	GameEvent(gameEventTypeKeys inputGameEventType) {
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
	// </editor-fold>
}
