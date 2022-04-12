package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public class DialogContainerImprovements extends DialogContainer {

    JButton buttonBuildHouse;
    JButton buttonSellHouse;
    JButton buttonBuildHotel;
    JButton buttonSellHotel;

    JTextArea infoArea;

    public DialogContainerImprovements() {
        initComponents();
        arrangeComponents();
    }

    @Override
    protected void initComponents() {
        initLabelTitle("Improvements", "/improvements.png");
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        buttonBuildHouse = new JButton("Build a house", SwingHelper.getImageIconFromResource("/house.png"));
        buttonSellHouse = new JButton("Sell a house", SwingHelper.getImageIconFromResource("/house.png"));
        buttonBuildHotel = new JButton("Build a hotel", SwingHelper.getImageIconFromResource("/hotel.png"));
        buttonSellHotel = new JButton("Sell a hotel", SwingHelper.getImageIconFromResource("/hotel.png"));
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

    }

    public void attachActionListeners(ArrayList<DynamicView.ButtonActionListener> listeners) {
        for (DynamicView.ButtonActionListener l : listeners) {
            switch (l.getAction()) {
                case IMPROVEMENTS_BUILD_HOUSE -> buttonBuildHouse.addActionListener(l);
                case IMPROVEMENTS_SELL_HOUSE -> buttonSellHouse.addActionListener(l);
                case IMPROVEMENTS_BUILD_HOTEL -> buttonBuildHotel.addActionListener(l);
                case IMPROVEMENTS_SELL_HOTEL -> buttonSellHotel.addActionListener(l);
            }
        }
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        switch (action) {
            case IMPROVEMENTS_BUILD_HOUSE -> buttonBuildHouse.setEnabled(isEnabled);
            case IMPROVEMENTS_SELL_HOUSE -> buttonSellHouse.setEnabled(isEnabled);
            case IMPROVEMENTS_BUILD_HOTEL -> buttonBuildHotel.setEnabled(isEnabled);
            case IMPROVEMENTS_SELL_HOTEL -> buttonSellHotel.setEnabled(isEnabled);
        }
    }

    private enum ImprovementEligibilityStatus {
        CAN_IMPROVE,
        CANT_BUILD_FULL_SET_NOT_OWNED,
        CANT_BUILD_NOT_A_COLOR_PROPERTY,
        CANT_BUILD_DO_NOT_OWN_PROPERTY,
        CANT_IMPROVE_UNKNOWN
    }

    // Passing through controller is a hacky solution that could be improved later
    public void update(Board board, Player player, int spaceSelectionID, GameLogicController controller) {
        Space currentSpace = board.spaces.get(spaceSelectionID);
        ImprovementEligibilityStatus status = ImprovementEligibilityStatus.CANT_IMPROVE_UNKNOWN;

        setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOUSE, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOUSE, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOTEL, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOTEL, false);

        infoArea.setText("This message is deliberately lengthy such that the initial call of the .pack()" +
                "method fits the text to a message that is longer than what will be needed for this dialog.");
        dialog.pack();

        // Check: color property
        if (currentSpace instanceof Color c) {

            // Check: is owned
            if (c.getIsOwned() || (c.getOwnerID() != player.getPlayerID())) {

                // Check: does player own full set; selected property is also owned by player
                if (c.getIsFullSetOwned() && (c.getOwnerID() == player.getPlayerID())) {
                    status = ImprovementEligibilityStatus.CAN_IMPROVE;

                    setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOUSE, true);
                    setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOUSE, true);
                    setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOTEL, true);
                    setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOTEL, true);

                    controller.setPlayerCanBuildImprovements(true);
                }
                else {
                    status = ImprovementEligibilityStatus.CANT_BUILD_FULL_SET_NOT_OWNED;
                }   // end check: does player own full set

            }
            else {
                status = ImprovementEligibilityStatus.CANT_BUILD_DO_NOT_OWN_PROPERTY;
            }   // end check: is owned
        }
        else {
            status = ImprovementEligibilityStatus.CANT_BUILD_NOT_A_COLOR_PROPERTY;
        }   // end check: is color property

        infoArea.setText(getInfoAreaMessage(status));
        dialog.pack();
    }

    private String getInfoAreaMessage(ImprovementEligibilityStatus status) {
        String m = "";

        switch (status) {
            case CAN_IMPROVE -> m = "You may construct or sell improvements on this property.";
            case CANT_BUILD_FULL_SET_NOT_OWNED -> m = "You do not own all properties within this color set.";
            case CANT_BUILD_NOT_A_COLOR_PROPERTY -> m = "The selected property is not a Color property.";
            case CANT_BUILD_DO_NOT_OWN_PROPERTY -> m = "You do not own the selected property.";
            case CANT_IMPROVE_UNKNOWN -> m = "Unknown disqualification.";   // fall back for any edge cases not considered
        }

        return m;
    }

}
