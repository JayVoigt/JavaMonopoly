package cc.jayv.monopoly3;

import java.util.ArrayList;

public class MacroController {
    Board board;
    GameLogicController controller;
    ArrayList<Player> activePlayers;

    public MacroController(Board board, GameLogicController controller) {
        this.board = board;
        this.controller = controller;

        activePlayers = getActivePlayers();
    }

    public void macroBankruptcyTest() {
        Player advantagePlayer = getRandomActivePlayer();

        controller.debugToolsGiveAllProperties(advantagePlayer.getPlayerID());

        for (Player p : activePlayers) {
            if (p != advantagePlayer) {
                p.setCurrentBalance(1);
            }
        }
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
}
