package cc.jayv.monopoly3;

import java.util.ArrayList;

public class MacroController {
    Board board;
    GameLogicController controller;
    LogHelper logHelper;
    ArrayList<Player> activePlayers;

    public MacroController(Board board, GameLogicController controller, LogHelper logHelper) {
        this.board = board;
        this.controller = controller;

        activePlayers = getActivePlayers();
    }

    private void appendToGameLogMacros(String message) {
        logHelper.appendToGameLog("[Macro executed] " + message);
    }

    private ArrayList<Player> getActivePlayers() {
        ArrayList<Player> activePlayers = new ArrayList<>();

        for (Player p : board.players) {
            if (p.getIsActive()) {
                activePlayers.add(p);
            }
        }

        return activePlayers;
    }

    private Player getRandomActivePlayer() {
        return (activePlayers.get((int) (Math.random() * activePlayers.size())));
    }

    public void macroBankruptcyTest() {
        Player advantagePlayer = getRandomActivePlayer();
        appendToGameLogMacros("Bankruptcy test: Advantage player is " + advantagePlayer.getCustomName());

        controller.debugToolsGiveAllProperties(advantagePlayer.getPlayerID());

        for (Player p : activePlayers) {
            if (p != advantagePlayer) {
                p.setCurrentBalance(1);
            }
        }
    }

    public void macroDisableRandomPlayer() {
        getRandomActivePlayer().setIsPlayerActive(false);
    }
}
