package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class PropertyPurchaseDialog {

    JDialog dialog;

    JLabel labelTitle;

    JTextArea infoArea;
    JLabel staticLabelDisclaimer;

    JLabel staticLabelPropertyName;
    JLabel staticLabelPropertyCost;
    JLabel staticLabelTimesLanded;

    JLabel labelPropertyName;
    JLabel labelPropertyCost;
    JLabel labelTimesLanded;

    JButton buttonPurchase;
    JButton buttonAuction;

    public PropertyPurchaseDialog() {
        initComponents();
        arrangeComponents();
    }

    private void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        // Title
        labelTitle = new JLabel();
        SwingHelper.formatLabel(labelTitle, "Property purchase", SwingHelper.LabelStyles.TITLE_BOLD);
        labelTitle.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));

        String disclaimerMessage =
                "You must decide to either purchase or auction this property." +
                " If you are unable to afford this property, it will automatically" +
                " be sent to auction where all players may bid on it.";

        // Static labels
        // Text area for general information
        infoArea = new JTextArea();

        infoArea.setText(disclaimerMessage);
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        infoArea.setBackground(null);
        infoArea.setEditable(false);

        staticLabelPropertyName = new JLabel("Property:");
        staticLabelPropertyName.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));

        staticLabelPropertyCost = new JLabel("Cost:");
        staticLabelPropertyCost.setIcon(SwingHelper.getImageIconFromResource("/money.png"));

        staticLabelTimesLanded = new JLabel("Times landed:");
        staticLabelTimesLanded.setIcon(SwingHelper.getImageIconFromResource("/arrow.png"));

        staticLabelDisclaimer = new JLabel(disclaimerMessage);

        // Labels
        labelPropertyName = new JLabel();
        labelPropertyCost = new JLabel();
        labelTimesLanded = new JLabel();

        // Action buttons
        buttonPurchase = new JButton("Purchase", SwingHelper.getImageIconFromResource("/money.png"));
        buttonAuction = new JButton("Auction", SwingHelper.getImageIconFromResource("/auction.png"));
    }

    private void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(staticLabelPropertyName, "cell 0 2");
        dialog.add(labelPropertyName, "cell 1 2, wrap");

        dialog.add(staticLabelPropertyCost, "cell 0 3");
        dialog.add(labelPropertyCost, "cell 1 3, wrap");

        dialog.add(staticLabelTimesLanded, "cell 0 4");
        dialog.add(labelTimesLanded, "cell 1 4, wrap");

        dialog.add(infoArea, "cell 0 6, wrap, grow, span");

        dialog.add(buttonPurchase, "cell 0 9, align left");
        dialog.add(buttonAuction, "cell 1 9, align right");
    }

    public void attachActionListeners(ArrayList<DynamicView.ButtonActionListener> listeners) {
        for (DynamicView.ButtonActionListener l : listeners) {
            switch (l.getAction()) {
            }
        }
    }

    public void update(Board board, int spaceID) {
        String propertyName = "";
        String propertyCost = "";
        String propertyTimesLanded = "";

        Space localSpace = board.spaces.get(spaceID);
        if (localSpace instanceof Property p) {
            propertyName = p.getFriendlyName();
            propertyCost = "$" + String.valueOf(p.getPurchaseCost());

            // Set appropriate grammar
            if (p.getTimesLanded() == 1) {
                propertyTimesLanded = String.valueOf(p.getTimesLanded()) + " time";
            }
            else {
                propertyTimesLanded = String.valueOf(p.getTimesLanded()) + " times";
            }
        }

        labelPropertyName.setText(propertyName);
        labelPropertyCost.setText(propertyCost);
        labelTimesLanded.setText(propertyTimesLanded);

        dialog.pack();
    }

    public JDialog getDialog() {
        return dialog;
    }

    public static void main(String args[]) throws IOException {
        PropertyPurchaseDialog propertyPurchaseDialog = new PropertyPurchaseDialog();

        Board board = new Board();
        propertyPurchaseDialog.update(board, 12);
        propertyPurchaseDialog.getDialog().setVisible(true);
    }

}
