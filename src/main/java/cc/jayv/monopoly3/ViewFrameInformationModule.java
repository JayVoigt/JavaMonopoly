package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.IOException;

public class ViewFrameInformationModule {

    JPanel panelPlayerStatus;
    JPanel panelPlayerAssets;

    JLabel labelName;

    // Status
    // Balance
    JLabel staticLabelBalance;
    JLabel labelBalance;

    // Status icons
    JLabel labelStatusTurn;
    JLabel labelStatusJailed;
    JLabel labelStatusBankrupt;

    JLabel staticLabelPosition;
    JLabel labelPosition;

    // Assets
    JLabel staticLabelOwnedPropertyCount;
    JLabel labelOwnedPropertyCount;

    JLabel staticLabelGOOJFCCount;
    JLabel labelGOOJFCCount;

    Player player;
    Board board;

    public ViewFrameInformationModule(Board board, Player player) {
        this.board = board;
        this.player = player;

        initComponentsPlayerStatus();
        initComponentsPlayerAssets();
        arrangeComponentsPlayerStatus();
        arrangeComponentsPlayerAssets();
    }

    private void initComponentsPlayerStatus() {
        panelPlayerStatus = new JPanel();
        panelPlayerStatus.setSize(200, 150);
        panelPlayerStatus.setLayout(new MigLayout("ins 0"));

        labelName = new JLabel();
        staticLabelBalance = new JLabel();
        labelBalance = new JLabel();

        labelStatusTurn = new JLabel(SwingHelper.getImageIconFromResource("/dice-icon.png"));
        labelStatusJailed = new JLabel(SwingHelper.getImageIconFromResource("/jail.png"));
        labelStatusBankrupt = new JLabel(SwingHelper.getImageIconFromResource("/i.png"));

        staticLabelPosition = new JLabel();
        staticLabelPosition.setIcon(SwingHelper.getImageIconFromResource("/position.png"));
        labelPosition = new JLabel();
    }

    private void initComponentsPlayerAssets() {
        panelPlayerAssets = new JPanel();
        panelPlayerAssets.setSize(200, 150);
        panelPlayerAssets.setLayout(new MigLayout("ins 0"));

        staticLabelOwnedPropertyCount = new JLabel("Owned:");
        staticLabelOwnedPropertyCount.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));
        labelOwnedPropertyCount = new JLabel();

        staticLabelGOOJFCCount = new JLabel("Owned:");
        staticLabelGOOJFCCount.setIcon(SwingHelper.getImageIconFromResource("/goojfc.png"));
        labelGOOJFCCount = new JLabel();
    }

    private void arrangeComponentsPlayerStatus() {
        String iconMigSpec = ", width 18, height 18";
        panelPlayerStatus.add(labelStatusTurn, "cell 0 0, split 4" + iconMigSpec);
        panelPlayerStatus.add(labelStatusJailed, "cell 1 0" + iconMigSpec);
        panelPlayerStatus.add(labelStatusBankrupt, "cell 2 0, " + iconMigSpec);

        panelPlayerStatus.add(labelName, "cell 3 0, skip");
        panelPlayerStatus.add(staticLabelBalance, "cell 0 1, split 2");
        panelPlayerStatus.add(labelBalance, "cell 1 1, skip");

        panelPlayerStatus.add(staticLabelPosition, "cell 0 2, split 2");
        panelPlayerStatus.add(labelPosition, "cell 1 2, skip");
    }

    private void arrangeComponentsPlayerAssets() {
        panelPlayerAssets.add(staticLabelOwnedPropertyCount, "cell 0 0");
        panelPlayerAssets.add(labelOwnedPropertyCount, "cell 1 0");

        panelPlayerAssets.add(staticLabelGOOJFCCount, "cell 0 1");
        panelPlayerAssets.add(labelGOOJFCCount, "cell 1 1");
    }

    public JPanel getPanelPlayerStatus() {
        return panelPlayerStatus;
    }

    public JPanel getPanelPlayerAssets() {
        return panelPlayerAssets;
    }

    public void update() {
        labelName.setText(player.getCustomName());

        staticLabelBalance.setText("Balance:");
        staticLabelBalance.setIcon(SwingHelper.getImageIconFromResource("/money.png"));
        labelBalance.setText("$" + player.getCurrentBalance());

        labelStatusTurn.setEnabled((board.getCurrentPlayerID() == player.getPlayerID()));
        labelStatusJailed.setEnabled(player.getIsJailed());

        String playerPosition = (board.spaces.get(player.getCurrentPosition()).getFriendlyName());
        labelPosition.setText(playerPosition);

        // Assets
        int ownedPropertyCount = 0;
        for (boolean b : player.getOwnedPropertyIDs()) {
            if (b) {
                ownedPropertyCount++;
            }
        }

        labelOwnedPropertyCount.setText(String.valueOf(ownedPropertyCount));
        labelGOOJFCCount.setText(String.valueOf(player.getGetOutOfJailFreeCardCount()));
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setLayout(new MigLayout("ins 4, width 400, height 150"));
        frame.setSize(400, 150);

        Player player = new Player();
        player.setCustomName("Player 1");
        player.setCurrentBalance(10001);

        Board board = null;
        try {
            board = new Board();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ViewFrameInformationModule viewFrameInformationModule = new ViewFrameInformationModule(board, player);
        viewFrameInformationModule.update();

        frame.add(viewFrameInformationModule.getPanelPlayerStatus(), "cell 0 0, width 200, height 150");
        frame.add(viewFrameInformationModule.getPanelPlayerAssets(), "cell 1 0, width 200, height 150");

        frame.setVisible(true);
    }

}
