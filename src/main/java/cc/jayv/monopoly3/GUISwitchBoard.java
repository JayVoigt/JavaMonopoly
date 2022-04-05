package cc.jayv.monopoly3;

public class GUISwitchBoard {

    GameLogicController controller;

    public GUISwitchBoard(GameLogicController controller) {
        this.controller = controller;
    }

    public void actionHandler(DynamicView.Actions trigger) {
        System.out.println(trigger);
    }
}
