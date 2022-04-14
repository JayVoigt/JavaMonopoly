//package cc.jayv.monopoly3;
//
//import net.miginfocom.swing.MigLayout;
//
//import javax.swing.*;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class DialogContainerTrade extends DialogContainer {
//
//    Board board;
//    Player sendingPlayer;
//    Player receivingPlayer;
//
//    ArrayList<Property> sendingPlayerOwnedProperties;
//    ArrayList<Property> receivingPlayerOwnedProperties;
//
//    int sendingPlayerBalance;
//    int sendingPlayerGOOJFCcount;
//    int receivingPlayerBalance;
//    int receivingPlayerGOOJFCcount;
//
//    PlayerAssetView sendingPlayerAssetView;
//    PlayerAssetView receivingPlayerAssetView;
//
//    public DialogContainerTrade(Board board) {
//        this.board = board;
//
//        sendingPlayerOwnedProperties = new ArrayList<>();
//        receivingPlayerOwnedProperties = new ArrayList<>();
//
//        sendingPlayerBalance = 0;
//        sendingPlayerGOOJFCcount = 0;
//        receivingPlayerBalance = 0;
//        receivingPlayerGOOJFCcount = 0;
//
//        initComponents();
//        arrangeComponents();
//    }
//
//    private class PlayerAssetView {
//        JPanel panel;
//        Player assetViewPlayer;
//
//        JList<String> ownedAssetList;
//        JList<String> tradeOfferList;
//
//        public PlayerAssetView() {
//            initComponents();
//            arrangeComponents();
//        }
//
//        private void initComponents() {
//            panel = new JPanel();
//            panel.setLayout(new MigLayout());
//
//            DefaultListModel<String> ownedAssetModel = new DefaultListModel<>();
//            DefaultListModel<String> tradeOfferModel = new DefaultListModel<>();
//
//            ownedAssetList = new JList<>(ownedAssetModel);
//            tradeOfferList = new JList<>(tradeOfferModel);
//        }
//
//        private void arrangeComponents() {
//
//        }
//
//        public void update(Player player) {
//            assetViewPlayer = player;
//
//            for (Property p : board.getSpacesByOwnerID(assetViewPlayer.getPlayerID())) {
//                ownedProperties.add(p);
//                ownedPropertyNames.add(p.getFriendlyName());
//            }
//
//        }
//
//        public JPanel getPanel() {
//            return panel;
//        }
//    }
//
//    @Override
//    protected void initComponents() {
//        dialog = new JDialog();
//        dialog.setLayout(new MigLayout());
//        initLabelTitle("Trade", "/trade.png");
//
//    }
//
//    @Override
//    protected void arrangeComponents() {
//        dialog.add(labelTitle, "cell 0 0, wrap");
//    }
//
//    @Override
//    public void setStateOfActionButton(Actions action, boolean isEnabled) {
//
//    }
//
//    public void setPlayersForTrade(int sendingPlayerID, int receivingPlayerID) {
//        sendingPlayer = board.players.get(sendingPlayerID);
//        receivingPlayer = board.players.get(receivingPlayerID);
//
//        // IDE-suggested code, better than my solution
//        sendingPlayerOwnedProperties.addAll(board.getSpacesByOwnerID(sendingPlayerID));
//        receivingPlayerOwnedProperties.addAll(board.getSpacesByOwnerID(receivingPlayerID));
//
//        sendingPlayerBalance = sendingPlayer.getCurrentBalance();
//        sendingPlayerGOOJFCcount = sendingPlayer.getGetOutOfJailFreeCardCount();
//        receivingPlayerBalance = receivingPlayer.getCurrentBalance();
//        receivingPlayerGOOJFCcount = receivingPlayer.getGetOutOfJailFreeCardCount();
//    }
//
//    public static void main (String[] args) {
//        DialogContainer dialogContainer = null;
//        try {
//            dialogContainer = new DialogContainerTrade(new Board());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        JDialog dialog = dialogContainer.getDialog();
//
//        dialog.pack();
//        dialog.setVisible(true);
//    }
//}
