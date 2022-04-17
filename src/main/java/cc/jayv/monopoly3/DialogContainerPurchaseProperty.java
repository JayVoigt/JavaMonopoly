package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Dialog, Purchase Property: A mandatory dialog that prompts the user to either purchase or auction
 * a property they land on.<br>
 *
 * Provides options for purchasing and auctioning a property.
 */
public class DialogContainerPurchaseProperty extends DialogContainer implements ViewComponent, Serializable {

    JTextArea infoArea;
    JLabel staticLabelDisclaimer;

    JLabel staticLabelPropertyName;
    JLabel staticLabelPropertyType;
    JLabel staticLabelPropertyCost;
    JLabel staticLabelTimesLanded;

    JLabel labelPropertyName;
    JLabel labelPropertyType;
    JLabel labelPropertyCost;
    JLabel labelTimesLanded;

    JButton buttonPurchase;
    JButton buttonAuction;

    public DialogContainerPurchaseProperty() {
        initComponents();
        arrangeComponents();
    }

    public static void main(String[] args) throws IOException {
        DialogContainerPurchaseProperty propertyPurchaseDialog = new DialogContainerPurchaseProperty();

        Board board = new Board();

        propertyPurchaseDialog.update(board, 9);
        propertyPurchaseDialog.getDialog().setVisible(true);
    }

    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        // Title
        initLabelTitle("Property Decision", "/properties.png");

        String disclaimerMessage =
                "You must decide to either purchase or auction this property." +
                        " If you are unable to afford this property, it will automatically" +
                        " be sent to auction where all players may bid on it.";

        // Static labels
        // Text area for general information
        infoArea = new JTextArea();
        SwingHelper.formatInfoArea(infoArea);
        infoArea.setText(disclaimerMessage);

        staticLabelPropertyName = new JLabel("Property:");
        staticLabelPropertyName.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));

        staticLabelPropertyType = new JLabel("Type:");
        staticLabelPropertyType.setIcon(SwingHelper.getImageIconFromResource("/i.png"));

        staticLabelPropertyCost = new JLabel("Cost:");
        staticLabelPropertyCost.setIcon(SwingHelper.getImageIconFromResource("/money.png"));

        staticLabelTimesLanded = new JLabel("Times landed:");
        staticLabelTimesLanded.setIcon(SwingHelper.getImageIconFromResource("/arrow.png"));

        staticLabelDisclaimer = new JLabel(disclaimerMessage);

        // Labels
        labelPropertyName = new JLabel();
        labelPropertyType = new JLabel();
        labelPropertyCost = new JLabel();
        labelTimesLanded = new JLabel();

        // Action buttons
        buttonPurchase = new JButton("Purchase", SwingHelper.getImageIconFromResource("/money.png"));
        buttonAuction = new JButton("Auction", SwingHelper.getImageIconFromResource("/auction.png"));

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(staticLabelPropertyName, "cell 0 2");
        dialog.add(labelPropertyName, "cell 1 2, wrap");

        dialog.add(staticLabelPropertyType, "cell 0 3");
        dialog.add(labelPropertyType, "cell 1 3, wrap");

        dialog.add(staticLabelPropertyCost, "cell 0 4");
        dialog.add(labelPropertyCost, "cell 1 4, wrap");

        dialog.add(staticLabelTimesLanded, "cell 0 5");
        dialog.add(labelTimesLanded, "cell 1 5, wrap");

        dialog.add(infoArea, "cell 0 7, wrap, grow, span");

        dialog.add(buttonPurchase, "cell 0 9, align left");
        dialog.add(buttonAuction, "cell 1 9, align right");
    }

    public void attachActionListeners(ArrayList<DynamicView.ButtonActionListener> listeners) {
        for (DynamicView.ButtonActionListener l : listeners) {
            switch (l.getAction()) {
                case PROPERTY_PURCHASE -> buttonPurchase.addActionListener(l);
                case PROPERTY_AUCTION -> buttonAuction.addActionListener(l);
            }
        }
    }

    public void update(Board board, int spaceID) {
        // Defaults in case update method is called on non-property space
        String propertyName = "";
        String propertyType = "";
        String propertyCost = "";
        String propertyTimesLanded = "";

        if (board.spaces.get(spaceID) instanceof Property p) {
            propertyName = p.getFriendlyName();
            propertyCost = "$" + p.getPurchaseCost();

            // Get property type
            if (p instanceof Color c) {
                // Specify color group
                propertyType = "Color, " + c.getColorGroup();
            } else if (p instanceof Railroad) {
                propertyType = "Railroad";
            } else if (p instanceof Utility) {
                propertyType = "Utility";
            }

            // Set appropriate grammar
            if (p.getTimesLanded() == 1) {
                propertyTimesLanded = p.getTimesLanded() + " time";
            } else {
                propertyTimesLanded = p.getTimesLanded() + " times";
            }
        }

        labelPropertyName.setText(propertyName);
        labelPropertyType.setText(propertyType);
        labelPropertyCost.setText(propertyCost);
        labelTimesLanded.setText(propertyTimesLanded);

        dialog.pack();
    }

    @Override
    public void update(Board board) {

    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        switch (action) {
            case PROPERTY_AUCTION -> buttonAuction.setEnabled(isEnabled);
            case PROPERTY_PURCHASE -> buttonPurchase.setEnabled(isEnabled);
        }
    }

    @Override
    public JComponent getComponent() {
        return null;
    }

}
