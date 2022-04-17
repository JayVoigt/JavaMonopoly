package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public class DialogContainerImprovements extends DialogContainer {

    JButton buttonBuildHouse;
    JButton buttonSellHouse;
    JButton buttonBuildHotel;
    JButton buttonSellHotel;

    JLabel labelPropertySelection;
    JTextArea infoArea;

    public DialogContainerImprovements() {
        initComponents();
        arrangeComponents();
    }

    public static void main(String[] args) {
        DialogContainerImprovements dialogContainerImprovements = new DialogContainerImprovements();
        JDialog dialog = dialogContainerImprovements.getDialog();
        dialog.setVisible(true);
    }

    @Override
    protected void initComponents() {
        initLabelTitle("Improve Properties", "/improvements.png");
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        labelPropertySelection = new JLabel();
        labelPropertySelection.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));

        infoArea = new JTextArea();
        infoArea.setText("No property is selected.");
        SwingHelper.formatInfoArea(infoArea);

        buttonBuildHouse = new JButton("Build a house", SwingHelper.getImageIconFromResource("/house.png"));
        buttonSellHouse = new JButton("Sell a house", SwingHelper.getImageIconFromResource("/house.png"));
        buttonBuildHotel = new JButton("Build a hotel", SwingHelper.getImageIconFromResource("/hotel.png"));
        buttonSellHotel = new JButton("Sell a hotel", SwingHelper.getImageIconFromResource("/hotel.png"));

        setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOUSE, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOUSE, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOTEL, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOTEL, false);
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(labelPropertySelection, "cell 0 2, split 2, align left, wrap");
        dialog.add(infoArea, "cell 0 3, grow, span, wrap");

        dialog.add(buttonBuildHouse, "cell 0 5, grow");
        dialog.add(buttonBuildHotel, "cell 1 5, grow, wrap");
        dialog.add(buttonSellHouse, "cell 0 6, grow");
        dialog.add(buttonSellHotel, "cell 1 6, grow, wrap");
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

    // Passing through controller is a hacky solution that could be improved later
    public void update(Board board, Player player, int spaceSelectionID, GameLogicController controller) {
        Space currentSpace = board.spaces.get(spaceSelectionID);
        ImprovementEligibilityStatus status;

        labelPropertySelection.setText(currentSpace.getFriendlyName());

        setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOUSE, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOUSE, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_BUILD_HOTEL, false);
        setStateOfActionButton(Actions.IMPROVEMENTS_SELL_HOTEL, false);

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
                } else {
                    status = ImprovementEligibilityStatus.CANT_BUILD_FULL_SET_NOT_OWNED;
                }   // end check: does player own full set

            } else {
                status = ImprovementEligibilityStatus.CANT_BUILD_DO_NOT_OWN_PROPERTY;
            }   // end check: is owned
        } else {
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
            case CANT_BUILD_NOT_A_COLOR_PROPERTY -> m = "The selected space is not a Color property.";
            case CANT_BUILD_DO_NOT_OWN_PROPERTY -> m = "You do not own the selected property.";
            case CANT_IMPROVE_UNKNOWN ->
                    m = "Unknown disqualification.";   // fall back for any edge cases not considered
        }

        return m;
    }

    private enum ImprovementEligibilityStatus {
        CAN_IMPROVE,
        CANT_BUILD_FULL_SET_NOT_OWNED,
        CANT_BUILD_NOT_A_COLOR_PROPERTY,
        CANT_BUILD_DO_NOT_OWN_PROPERTY,
        CANT_IMPROVE_UNKNOWN
    }

}
