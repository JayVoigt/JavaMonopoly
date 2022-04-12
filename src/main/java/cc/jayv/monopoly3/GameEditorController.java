package cc.jayv.monopoly3;

import java.io.Serializable;

public class GameEditorController implements Serializable {
    Board board;
    GameLogicController controller;
    Player player;
    LogHelper logHelper;

    public GameEditorController(Board board, GameLogicController controller, LogHelper logHelper) {
        this.board = board;
        this.controller = controller;
        this.logHelper = logHelper;
    }

    public void give1000(Player player) {
        this.player = player;
        int previousBalance = player.getCurrentBalance();
        player.updateCurrentBalance(1000);

        appendToGameLogGameEditor("Balance +1000: " + previousBalance + " -> " + player.getCurrentBalance());
    }

    public void deduct1000(Player player) {
        this.player = player;
        int previousBalance = player.getCurrentBalance();
        player.updateCurrentBalance(-1000);

        appendToGameLogGameEditor("Balance -1000: " + previousBalance + " -> " + player.getCurrentBalance());
    }

    public void jailPlayer(Player player) {
        this.player = player;
        controller.gameEditorJailPlayer(player);

        appendToGameLogGameEditor("Jailed.");
    }

    public void unjailPlayer(Player player) {

        appendToGameLogGameEditor("Released from jail.");
    }

    public void giveAllProperties(Player player) {
        this.player = player;
        controller.debugToolsGiveAllProperties(player.getPlayerID());

        appendToGameLogGameEditor("Gave ownership of all properties.");
    }

    private void appendToGameLogGameEditor(String input) {
        logHelper.appendToGameLog("[Game Editor: " + player.getCustomName() + "] " + input);
    }
}
