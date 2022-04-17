package cc.jayv.monopoly3;

import java.io.Serializable;
import java.util.ArrayList;

public class GameLogicTools extends GameLogicModule implements Serializable {
    LogHelper logHelper;
    Player debugPlayer;

    public GameLogicTools() {
    }

    public void attachLogHelper(LogHelper logHelper) {
        this.logHelper = logHelper;
    }

    /**
     * Ready the state of the current player to be jailed; jail the player.<br>
     * Jailing a player through the Game Editor requires the special condition that
     * "initialJailTurn" must be false.
     */
    public void gameEditorJailPlayer(Player player) {
        player.setIsJailed(true);
        player.setHasRolledDice(true);
        player.setActionLockedEndTurn(false);
        player.setActionLockedRollDice(true);
        player.setInitialJailTurn(false);
    }

    public void debugToolsGiveAllProperties(int playerID) {
        debugPlayer = board.players.get(playerID);
        logHelper.appendToDebugLog("Giving " + debugPlayer.getCustomName() + " all properties.");

        Property localProperty;
        for (Space s : board.spaces) {
            if (s instanceof Property) {
                localProperty = (Property) s;

                localProperty.setIsOwned(true);
                localProperty.setOwnerID(playerID);
                currentPlayer.setPropertyOwnedState(true, localProperty.getID());
            }
        }

        board.forceBoardSelfCheck();
    }

    public void debugToolsRandomlyDistributeAllProperties() {
        ArrayList<Player> activePlayers = new ArrayList<>();

        for (Player p : board.players) {
            if (p.getIsActive()) {
                activePlayers.add(p);
            }
        }

        for (Space s : board.spaces) {
            if (s instanceof Property p) {
                int randomPlayerID = 1 + (int) (Math.random() * activePlayers.size());
                p.setIsOwned(true);
                p.setOwnerID(randomPlayerID);
            }
        }
    }
}