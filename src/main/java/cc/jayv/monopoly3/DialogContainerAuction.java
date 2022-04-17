package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Color;
import java.io.IOException;

public class DialogContainerAuction extends DialogContainer {

    JPanel bidView;

    BidControls bidControlsPlayer1;
    BidControls bidControlsPlayer2;
    BidControls bidControlsPlayer3;
    BidControls bidControlsPlayer4;

    JPanel bidControlsPanelPlayer1;
    JPanel bidControlsPanelPlayer2;
    JPanel bidControlsPanelPlayer3;
    JPanel bidControlsPanelPlayer4;

    int winningBidPlayerID;
    int winningBidCost;

    Board board;

    public DialogContainerAuction(Board board) {
        this.board = board;

        initComponents();
        arrangeComponents();
    }

    public static void main(String[] args) {
        try {
            DialogContainerAuction dialogContainerAuction = new DialogContainerAuction(new Board());
            JDialog dialog = dialogContainerAuction.getDialog();
            dialog.pack();
            dialog.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        bidControlsPlayer1 = new BidControls(1);
        bidControlsPlayer2 = new BidControls(2);
        bidControlsPlayer3 = new BidControls(3);
        bidControlsPlayer4 = new BidControls(4);

        bidControlsPanelPlayer1 = bidControlsPlayer1.getPanel();
        bidControlsPanelPlayer2 = bidControlsPlayer2.getPanel();
        bidControlsPanelPlayer3 = bidControlsPlayer3.getPanel();
        bidControlsPanelPlayer4 = bidControlsPlayer4.getPanel();

        bidControlsPanelPlayer1.setBackground(Color.red);
        bidControlsPanelPlayer2.setBackground(Color.red);
        bidControlsPanelPlayer3.setBackground(Color.red);
        bidControlsPanelPlayer4.setBackground(Color.red);

        initLabelTitle("Auction", "/auction.png");

        bidView = new JPanel();
        bidView.setBackground(Color.magenta);
        bidView.setSize(400, 200);
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");
        dialog.add(bidView, "cell 0 2, wrap, width 400, height 200, al center center");

        dialog.add(bidControlsPanelPlayer1, "cell 0 4, width 150, height 100, split 4");
        dialog.add(bidControlsPanelPlayer2, "cell 1 4, width 150, height 100");
        dialog.add(bidControlsPanelPlayer3, "cell 2 4, width 150, height 100");
        dialog.add(bidControlsPanelPlayer4, "cell 3 4, width 150, height 100");
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {

    }

    private class BidControls {
        int playerID;
        int bidQuantity;

        JPanel panel;
        JLabel labelPlayerName;
        JLabel labelPlayerBalance;

        JButton bid1;
        JButton bid10;
        JButton bid100;

        public BidControls(int playerID) {
            this.playerID = playerID;
            bidQuantity = 0;

            labelPlayerName = new JLabel(SwingHelper.getImageIconFromResource("/player-generic.png"));
            labelPlayerBalance = new JLabel(SwingHelper.getImageIconFromResource("/money.png"));

            panel = new JPanel();
            panel.setLayout(new MigLayout());
            panel.setSize(150, 100);

            bid1 = new JButton("Bid $1");
            bid10 = new JButton("Bid $10");
            bid100 = new JButton("Bid $100");

            bid1.addActionListener(e -> bidQuantity += 1);
            bid10.addActionListener(e -> bidQuantity += 10);
            bid100.addActionListener(e -> bidQuantity += 100);

            panel.add(labelPlayerName, "cell 0 0");
            panel.add(labelPlayerBalance, "cell 0 1");
            panel.add(bid1, "cell 0 2, grow, al center center");
            panel.add(bid10, "cell 0 3, grow, al center center");
            panel.add(bid100, "cell 0 4, grow, al center center");
        }

        public void update() {
            if (board.players.get(playerID).getCurrentBalance() < 1) {
                bid1.setEnabled(false);
            } else if (board.players.get(playerID).getCurrentBalance() < 10) {
                bid10.setEnabled(false);
            } else if (board.players.get(playerID).getCurrentBalance() < 100) {
                bid100.setEnabled(false);
            }
        }

        public int getPlayerID() {
            return playerID;
        }

        public JPanel getPanel() {
            return panel;
        }
    }
}
