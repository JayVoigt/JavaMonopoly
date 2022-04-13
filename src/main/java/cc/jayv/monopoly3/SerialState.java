package cc.jayv.monopoly3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerialState implements Serializable {
    Board board;
    GameLogicController controller;
    LogHelper logHelper;
    Player currentPlayer;
    GameLogicSwitchboard switchboard;

    public SerialState() {

    }

    public void updateData(Board board, GameLogicController controller, LogHelper logHelper, Player currentPlayer, GameLogicSwitchboard switchboard) {
        this.board = board;
        this.controller = controller;
        this.logHelper = logHelper;
        this.currentPlayer = currentPlayer;
        this.switchboard = switchboard;
    }

    public Board getBoard() {
        return board;
    }

    public GameLogicController getController() {
        return controller;
    }

    public LogHelper getLogHelper() {
        return logHelper;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameLogicSwitchboard getSwitchboard() {
        return switchboard;
    }

}
