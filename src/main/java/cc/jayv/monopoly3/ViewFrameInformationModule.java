package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.*;

public class ViewFrameInformationModule {

    JPanel panelPlayerStatus;
    JPanel panelPlayerAssets;
    JPanel compositePanel;

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

    JLabel labelOwnedColorBrown;
    JLabel labelOwnedColorLightBlue;
    JLabel labelOwnedColorMagenta;
    JLabel labelOwnedColorOrange;
    JLabel labelOwnedColorRed;
    JLabel labelOwnedColorYellow;
    JLabel labelOwnedColorGreen;
    JLabel labelOwnedColorDarkBlue;

    JLabel labelOwnedRailroad;
    JLabel labelOwnedUtility;

    Map<Color.colorGroupKeys, JLabel> ownedPropertyGroupLabels;

    JLabel labelOwnedHouses;
    JLabel labelOwnedHotels;

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
        panelPlayerStatus.setLayout(new MigLayout("ins 0, fill"));

        labelName = new JLabel();
        labelName.setToolTipText("Name of the player.");

        staticLabelBalance = new JLabel();
        staticLabelBalance.setText("");
        staticLabelBalance.setIcon(SwingHelper.getImageIconFromResource("/money.png"));
        staticLabelBalance.setToolTipText("Current balance of the player.");
        labelBalance = new JLabel();

        labelStatusTurn = new JLabel(SwingHelper.getImageIconFromResource("/dice-icon.png"));
        labelStatusTurn.setToolTipText("Indicates if the current turn belongs to this player.");

        labelStatusJailed = new JLabel(SwingHelper.getImageIconFromResource("/jail.png"));
        labelStatusJailed.setToolTipText("Indicates if the player is jailed.");

        labelStatusBankrupt = new JLabel(SwingHelper.getImageIconFromResource("/piggy.png"));
        labelStatusBankrupt.setToolTipText("Indicates if the current player is bankrupt.");

        staticLabelPosition = new JLabel();
        staticLabelPosition.setIcon(SwingHelper.getImageIconFromResource("/position.png"));
        labelPosition = new JLabel();
    }

    private void initComponentsPlayerAssets() {
        panelPlayerAssets = new JPanel();
        panelPlayerAssets.setSize(200, 150);
        panelPlayerAssets.setLayout(new MigLayout("ins 0"));

        staticLabelOwnedPropertyCount = new JLabel("");
        staticLabelOwnedPropertyCount.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));
        staticLabelOwnedPropertyCount.setToolTipText("Properties owned by the player.");
        labelOwnedPropertyCount = new JLabel("0/28");

        staticLabelGOOJFCCount = new JLabel("");
        staticLabelGOOJFCCount.setIcon(SwingHelper.getImageIconFromResource("/goojfc.png"));
        staticLabelGOOJFCCount.setToolTipText("Get Out of Jail Free Cards owned by the player.");
        labelGOOJFCCount = new JLabel("0");

        labelOwnedColorBrown = new JLabel(SwingHelper.getImageIconFromResource("/property-single-brown.png"));
        labelOwnedColorLightBlue = new JLabel(SwingHelper.getImageIconFromResource("/property-single-lightblue.png"));
        labelOwnedColorMagenta = new JLabel(SwingHelper.getImageIconFromResource("/property-single-magenta.png"));
        labelOwnedColorOrange = new JLabel(SwingHelper.getImageIconFromResource("/property-single-orange.png"));
        labelOwnedColorRed = new JLabel(SwingHelper.getImageIconFromResource("/property-single-red.png"));
        labelOwnedColorYellow = new JLabel(SwingHelper.getImageIconFromResource("/property-single-yellow.png"));
        labelOwnedColorGreen = new JLabel(SwingHelper.getImageIconFromResource("/property-single-green.png"));
        labelOwnedColorDarkBlue = new JLabel(SwingHelper.getImageIconFromResource("/property-single-darkblue.png"));
        labelOwnedRailroad = new JLabel(SwingHelper.getImageIconFromResource("/property-railroad.png"));
        labelOwnedUtility = new JLabel(SwingHelper.getImageIconFromResource("/property-utility.png"));

        ownedPropertyGroupLabels = new HashMap<>();
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.brown, labelOwnedColorBrown);
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.lightBlue, labelOwnedColorLightBlue);
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.magenta, labelOwnedColorMagenta);
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.orange, labelOwnedColorOrange);
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.red, labelOwnedColorRed);
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.yellow, labelOwnedColorYellow);
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.green, labelOwnedColorGreen);
        ownedPropertyGroupLabels.put(Color.colorGroupKeys.darkBlue, labelOwnedColorDarkBlue);

        labelOwnedHouses = new JLabel(SwingHelper.getImageIconFromResource("/house.png"));
        labelOwnedHotels = new JLabel(SwingHelper.getImageIconFromResource("/hotel.png"));

    }

    private void arrangeComponentsPlayerStatus() {
        String iconMigSpec = ", width 18, height 18";
        panelPlayerStatus.add(labelStatusTurn, "cell 0 0, split 4" + iconMigSpec);
        panelPlayerStatus.add(labelStatusJailed, "cell 1 0" + iconMigSpec);
        panelPlayerStatus.add(labelStatusBankrupt, "cell 2 0, wrap" + iconMigSpec);

        panelPlayerStatus.add(labelName, "cell 0 1");
        panelPlayerStatus.add(staticLabelBalance, "cell 0 2, split 2");
        panelPlayerStatus.add(labelBalance, "cell 1 2, skip");

        panelPlayerStatus.add(staticLabelPosition, "cell 0 3, split 2");
        panelPlayerStatus.add(labelPosition, "cell 1 3, skip");
    }

    private void arrangeComponentsPlayerAssets() {
        panelPlayerAssets.add(staticLabelOwnedPropertyCount, "cell 0 0, split 4");
        panelPlayerAssets.add(labelOwnedPropertyCount, "cell 1 0");

        panelPlayerAssets.add(staticLabelGOOJFCCount, "cell 2 0");
        panelPlayerAssets.add(labelGOOJFCCount, "cell 3 0");

        panelPlayerAssets.add(labelOwnedColorBrown, "cell 0 3, split 4");
        panelPlayerAssets.add(labelOwnedColorLightBlue, "cell 1 3");
        panelPlayerAssets.add(labelOwnedColorMagenta, "cell 2 3");
        panelPlayerAssets.add(labelOwnedColorOrange, "cell 3 3");
        panelPlayerAssets.add(labelOwnedColorRed, "cell 0 4, split 4");
        panelPlayerAssets.add(labelOwnedColorYellow, "cell 1 4");
        panelPlayerAssets.add(labelOwnedColorGreen, "cell 2 4");
        panelPlayerAssets.add(labelOwnedColorDarkBlue, "cell 3 4");
        panelPlayerAssets.add(labelOwnedRailroad, "cell 0 5, split 4");
        panelPlayerAssets.add(labelOwnedUtility, "cell 1 5");

    }

    public JPanel getPanelPlayerStatus() {
        return panelPlayerStatus;
    }

    public JPanel getPanelPlayerAssets() {
        return panelPlayerAssets;
    }

    public JPanel getCompositePanel() {
        compositePanel = new JPanel();
        compositePanel.setLayout(new MigLayout("fill"));

        compositePanel.add(panelPlayerStatus, "cell 0 0, width 200, height 150, align left");
        compositePanel.add(panelPlayerAssets, "cell 1 0, width 200, height 150, align right");

        compositePanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0, 32), 1, true));

        return compositePanel;
    }

    public void attachMouseListener(MouseListener listener) {
        compositePanel.addMouseListener(listener);
    }

    public void update() {
        if (player.getIsActive()) {
            labelName.setText(player.getCustomName());
            labelName.setIcon(player.getPreferredIcon());

            labelBalance.setText("$" + player.getCurrentBalance());

            labelStatusTurn.setEnabled((board.getCurrentPlayerID() == player.getPlayerID()));

            labelStatusJailed.setEnabled(player.getIsJailed());

            labelStatusBankrupt.setEnabled(player.getCurrentBalance() <= 0);

            String playerPosition = (board.spaces.get(player.getCurrentPosition()).getFriendlyName());
            labelPosition.setText(playerPosition);

            // Assets
            int ownedPropertyCount = 0;
            for (Space s : board.spaces) {
                if (s instanceof Property p) {
                    if (p.getIsOwned() && p.getOwnerID() == player.getPlayerID()) {
                        ownedPropertyCount++;
                    }
                }
            }

            updatePropertyGroupOwnershipLabels();

            labelOwnedPropertyCount.setText(String.valueOf(ownedPropertyCount) + "/28");
            labelGOOJFCCount.setText(String.valueOf(player.getGetOutOfJailFreeCardCount()));

        }
        else {
            disableAllComponents();
        }
    }

    private void disableAllComponents() {
        for (Component c : panelPlayerStatus.getComponents()) {
            c.setEnabled(false);
        }

        for (Component c : panelPlayerAssets.getComponents()) {
            c.setEnabled(false);
        }
    }

    private void updatePropertyGroupOwnershipLabels() {

        JLabel label = null;

        for ( Player p : board.players ) {
            int ownedProperties = 0;
            int totalProperties = 0;

            if (p.getIsActive())  {
                for (Color.colorGroupKeys colorGroup : Color.colorGroupKeys.values()) {

                    label = ownedPropertyGroupLabels.get(colorGroup);

                    totalProperties = board.getSpacesByColorGroup(colorGroup).size();
                    for (Property property : board.getSpacesByOwnerID(p.getPlayerID())) {
                        if (property instanceof Color c) {
                            if (c.getColorGroup() == colorGroup) {
                                ownedProperties++;
                            }
                        }
                    }

                    if (label != null) {
                        label.setText(ownedProperties + "/" + totalProperties);

                        // Indicate player has monopoly over property group
                        if (ownedProperties == totalProperties) {
                            label.setForeground(java.awt.Color.red);
                        } else {
                            label.setForeground(java.awt.Color.black);
                        }
                    }
                }   // end for
            }   // end if
        }   // end for
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
