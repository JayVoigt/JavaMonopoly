//package cc.jayv.monopoly3;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.util.ArrayList;
//
//public class ComputerPlayerBasic {
//    Board board;
//    GameLogicController controller;
//    Player player;
//    ArrayList<DynamicView.ButtonActionListener> listeners;
//    int iter;
//
//    public ComputerPlayerBasic(Board board, Player player, GameLogicController controller, ArrayList<DynamicView.ButtonActionListener> listeners) {
//        this.board = board;
//        this.controller = controller;
//        this.player = player;
//        this.listeners = listeners;
//    }
//
//    public void evaluate() {
//        if (board.getCurrentPlayerID() == player.getPlayerID()) {
//            boolean actionPerformed = false;
//            controller.initialEvaluator();
//            if (!player.getHasRolledDice() && !player.getActionLockedRollDice()) {
//                triggerEvent(Actions.CONTROLS_ROLLDICE);
//                actionPerformed = true;
//            }
//
//            if (player.getIsInMAEState() || !player.getActionLockedEndTurn()) {
//                triggerEvent(Actions.CONTROLS_ENDTURN);
//                actionPerformed = true;
//            }
//
//            if (player.getRequiredDecisionPropertyAction()) {
//                triggerEvent(Actions.PROPERTY_PURCHASE);
//                actionPerformed = true;
//            }
//
//            if (player.getRequiredDecisionPostedBail()) {
//                triggerEvent(Actions.JAIL_POSTBAIL);
//                actionPerformed = true;
//            }
//
//            if (!actionPerformed) {
//                controller.endTurnManager();
//            }
//        }
//    }
//
//    public int getAssociatedPlayerID() {
//        return player.getPlayerID();
//    }
//
//    private void triggerEvent(Actions action) {
//        for (DynamicView.ButtonActionListener l : listeners) {
//            if (l.getAction() == action) {
//                // Ref: https://stackoverflow.com/questions/3079524/how-do-i-manually-invoke-an-action-in-swing
//                l.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
//            }
//        }
//    }
//}
