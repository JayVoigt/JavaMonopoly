package cc.jayv.monopoly3;

public abstract class GameLogicModule {
    Board board;
    GameLogicController controller;
    Space currentSpace;
    Player currentPlayer;

    public void attachReferences(Board board, GameLogicController controller, Space currentSpace, Player currentPlayer) {
        this.board = board;
        this.controller = controller;
        this.currentSpace = currentSpace;
        this.currentPlayer = currentPlayer;
    }
}
