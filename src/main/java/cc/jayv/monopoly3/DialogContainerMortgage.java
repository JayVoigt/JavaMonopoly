package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class DialogContainerMortgage extends DialogContainer {

    JButton buttonMortgage;
    JButton buttonUnmortgage;

    JLabel labelPropertySelection;
    JLabel staticLabelMortgagePayout;
    JLabel labelMortgagePayout;
    JLabel staticLabelUnmortgageCost;
    JLabel labelUnmortgageCost;

    JTextArea infoArea;

    public DialogContainerMortgage() {
        initComponents();
        arrangeComponents();
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());
        initLabelTitle("Mortgage Properties", "/mortgage.png");

        infoArea = new JTextArea();
        SwingHelper.formatInfoArea(infoArea);

        buttonMortgage = new JButton("Mortgage property");
        buttonMortgage.setIcon(SwingHelper.getImageIconFromResource("/mortgage.png"));

        buttonUnmortgage = new JButton("Unmortgage property");
        buttonUnmortgage.setIcon(SwingHelper.getImageIconFromResource("/money.png"));

        labelPropertySelection = new JLabel(SwingHelper.getImageIconFromResource("/properties.png"));

        staticLabelMortgagePayout = new JLabel("Mortgage payout:");
        staticLabelMortgagePayout.setIcon(SwingHelper.getImageIconFromResource("/mortgage.png"));

        labelMortgagePayout = new JLabel();

        staticLabelUnmortgageCost = new JLabel("Cost to unmortgage:");
        staticLabelUnmortgageCost.setIcon(SwingHelper.getImageIconFromResource("/money.png"));

        labelUnmortgageCost = new JLabel();
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(labelPropertySelection, "cell 0 2, align left");
        dialog.add(infoArea, "cell 0 3, grow, span, wrap");

        dialog.add(staticLabelMortgagePayout, "cell 0 5");
        dialog.add(labelMortgagePayout, "cell 1 5, wrap");

        dialog.add(staticLabelUnmortgageCost, "cell 0 6");
        dialog.add(labelUnmortgageCost, "cell 1 6, wrap");

        dialog.add(buttonMortgage, "cell 0 8, align left");
        dialog.add(buttonUnmortgage, "cell 1 8, align right");
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        switch (action) {
            case PROPERTY_MORTGAGE -> buttonMortgage.setEnabled(isEnabled);
            case PROPERTY_UNMORTGAGE -> buttonUnmortgage.setEnabled(isEnabled);
        }
    }

    private enum MortgageEligibilityStatus {
        CAN_MORTGAGE,
        CANT_MORTGAGE_NOT_OWNED,
        CANT_MORTGAGE_ALREADY_MORTGAGED,
        CANT_MORTGAGE_NOT_PROPERTY
    }

    public void update(Board board, Player player, int spaceSelectionID, GameLogicController controller) {
        Space currentSpace = board.spaces.get(spaceSelectionID);
        MortgageEligibilityStatus status;
        String mortgagePayout = "n/a";
        String unmortgageCost = "n/a";

        // Check for eligibility
        if (currentSpace instanceof Property p) {
            if (p.getIsOwned() && (p.getOwnerID() == player.getPlayerID())) {
                if (!p.getIsMortgaged()) {
                    status = MortgageEligibilityStatus.CAN_MORTGAGE;
                }
                else {
                    status = MortgageEligibilityStatus.CANT_MORTGAGE_ALREADY_MORTGAGED;
                }
                mortgagePayout = "$" + p.getMortgageValue();
                unmortgageCost = "$" + p.getUnmortgageCost();
            }
            else {
                status = MortgageEligibilityStatus.CANT_MORTGAGE_NOT_OWNED;
            }
        }
        else {
            status = MortgageEligibilityStatus.CANT_MORTGAGE_NOT_PROPERTY;
        }

        infoArea.setText(getMessage(status));
        labelPropertySelection.setText(currentSpace.getFriendlyName());

        labelMortgagePayout.setText(mortgagePayout);
        labelUnmortgageCost.setText(unmortgageCost);

        // Set button status
        if (status == MortgageEligibilityStatus.CAN_MORTGAGE) {
            setStateOfActionButton(Actions.PROPERTY_MORTGAGE, true);
            setStateOfActionButton(Actions.PROPERTY_UNMORTGAGE, false);
        }
        else if (status == MortgageEligibilityStatus.CANT_MORTGAGE_ALREADY_MORTGAGED) {
            setStateOfActionButton(Actions.PROPERTY_MORTGAGE, false);
            setStateOfActionButton(Actions.PROPERTY_UNMORTGAGE, true);
        }
        else {
            setStateOfActionButton(Actions.PROPERTY_MORTGAGE, false);
            setStateOfActionButton(Actions.PROPERTY_UNMORTGAGE, false);
        }

    }

    private String getMessage(MortgageEligibilityStatus status) {
        String m = "";

        switch (status) {
            case CAN_MORTGAGE -> m = "This property is eligible to be mortgaged.";
            case CANT_MORTGAGE_NOT_OWNED -> m = "You do not own this property.";
            case CANT_MORTGAGE_ALREADY_MORTGAGED -> m = "This property is already mortgaged.";
            case CANT_MORTGAGE_NOT_PROPERTY -> m = "This space is not a property.";
        }

        return m;
    }

    public void attachActionListeners(ArrayList<DynamicView.ButtonActionListener> listeners) {
        for (DynamicView.ButtonActionListener l : listeners) {
            switch (l.getAction()) {
                case PROPERTY_MORTGAGE -> buttonMortgage.addActionListener(l);
                case PROPERTY_UNMORTGAGE -> buttonUnmortgage.addActionListener(l);
            }
        }
    }

    public static void main(String[] args) {
        DialogContainerMortgage dialogContainerMortgage = new DialogContainerMortgage();
        try {
            Board board = new Board();
            LogHelper logHelper = new LogHelper();
            dialogContainerMortgage.update(board, new Player(), 1, new GameLogicController(board, logHelper));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JDialog dialog = dialogContainerMortgage.getDialog();
        dialog.setVisible(true);

    }

}
