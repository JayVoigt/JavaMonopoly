package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ViewAreaSpaceSelection implements ViewComponent, Serializable {

    JPanel selectionInfoArea;
    JPanel leftArea;
    JPanel rightArea;

    // Left area, static
    JLabel staticLabelID;
    JLabel staticLabelName;
    JLabel staticLabelSpaceType;
    JLabel staticLabelOwnedBy;
    JLabel staticLabelLanded;
    JLabel staticLabelPrice;
    JLabel staticLabelIsMortgaged;

    // Left area, dynamic
    JLabel labelID;
    JLabel labelName;
    JLabel labelSpaceType;
    JLabel labelOwnedBy;
    JLabel labelLanded;
    JLabel labelPrice;
    JLabel labelIsMortgaged;

    // Right area, static
    JLabel staticLabelRent;
    JLabel staticLabelRentHouse1;
    JLabel staticLabelRentHouse2;
    JLabel staticLabelRentHouse3;
    JLabel staticLabelRentHouse4;
    JLabel staticLabelRentHotel;
    JLabel staticLabelCurrentRent;

    // Right area, dynamic
    JLabel labelRent;
    JLabel labelRentHouse1;
    JLabel labelRentHouse2;
    JLabel labelRentHouse3;
    JLabel labelRentHouse4;
    JLabel labelRentHotel;
    JLabel labelCurrentRent;

    public ViewAreaSpaceSelection() {
        selectionInfoArea = new JPanel();
        selectionInfoArea.setLayout(new MigLayout("ins 8"));

        leftArea = new JPanel();
        leftArea.setLayout(new MigLayout("ins 0"));
        rightArea = new JPanel();
        rightArea.setLayout(new MigLayout("ins 0"));

        // The board view (currently) uses a null layout - setBounds is required
        selectionInfoArea.setBounds(150, 140, 660, 220);
        selectionInfoArea.setBackground(java.awt.Color.WHITE);
        leftArea.setBackground(java.awt.Color.WHITE);
        rightArea.setBackground(java.awt.Color.WHITE);

        initLabels();

        selectionInfoArea.add(leftArea, "cell 0 0, width 330, height 220");
        selectionInfoArea.add(rightArea, "cell 1 0, width 330, height 220");

        selectionInfoArea.setEnabled(true);
        selectionInfoArea.setVisible(true);
    }

    private void initLabels() {
        String baseMigSpec = "";

        // Left area, static labels
        staticLabelID = generateLabel(leftArea, LabelAlignment.RIGHT, true, "ID", "cell 0 0, align right" + baseMigSpec);
        staticLabelName = generateLabel(leftArea, LabelAlignment.RIGHT, true, "Name", "cell 0 1, align right" + baseMigSpec);
        staticLabelOwnedBy = generateLabel(leftArea, LabelAlignment.RIGHT, true, "Owned by", "cell 0 2, align right" + baseMigSpec);
        staticLabelSpaceType = generateLabel(leftArea, LabelAlignment.RIGHT, true, "Space type", "cell 0 3, align right" + baseMigSpec);
        staticLabelLanded = generateLabel(leftArea, LabelAlignment.RIGHT, true, "Landed", "cell 0 4, align right" + baseMigSpec);
        staticLabelPrice = generateLabel(leftArea, LabelAlignment.RIGHT, true, "Price", "cell 0 5, align right" + baseMigSpec);
        staticLabelIsMortgaged = generateLabel(leftArea, LabelAlignment.RIGHT, true, "Mortgaged", "cell 0 6, align right" + baseMigSpec);

        // Left area, dynamic labels
        labelID = generateLabel(leftArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 0, align left" + baseMigSpec);
        labelName = generateLabel(leftArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 1, align left" + baseMigSpec);
        labelOwnedBy = generateLabel(leftArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 2, align left" + baseMigSpec);
        labelSpaceType = generateLabel(leftArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 3, align left" + baseMigSpec);
        labelLanded = generateLabel(leftArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 4, align left" + baseMigSpec);
        labelPrice = generateLabel(leftArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 5, align left" + baseMigSpec);
        labelIsMortgaged = generateLabel(leftArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 6, align left" + baseMigSpec);

        // Right area, static labels
        staticLabelRent = generateLabel(rightArea, LabelAlignment.RIGHT, true, "Base rent", "cell 0 0, align right" + baseMigSpec);
        staticLabelRentHouse1 = generateLabelWithIcons(rightArea, LabelAlignment.RIGHT, true, "+", "cell 0 1, align right" + baseMigSpec, "/house-label-1-horiz-rightalign.png");
        staticLabelRentHouse2 = generateLabelWithIcons(rightArea, LabelAlignment.RIGHT, true, "+", "cell 0 2, align right" + baseMigSpec, "/house-label-2-horiz-rightalign.png");
        staticLabelRentHouse3 = generateLabelWithIcons(rightArea, LabelAlignment.RIGHT, true, "+", "cell 0 3, align right" + baseMigSpec, "/house-label-3-horiz-rightalign.png");
        staticLabelRentHouse4 = generateLabelWithIcons(rightArea, LabelAlignment.RIGHT, true, "+", "cell 0 4, align right" + baseMigSpec, "/house-label-4-horiz-rightalign.png");
        staticLabelRentHotel = generateLabelWithIcons(rightArea, LabelAlignment.RIGHT, true, "+", "cell 0 5, align right" + baseMigSpec, "/hotel-label-1-horiz-rightalign.png");
        staticLabelCurrentRent = generateLabel(rightArea, LabelAlignment.RIGHT, true, "Current rent", "cell 0 6, align right" + baseMigSpec);

        // Right area, dynamic labels
        labelRent = generateLabel(rightArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 0, align left" + baseMigSpec);
        labelRentHouse1 = generateLabel(rightArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 1, align left" + baseMigSpec);
        labelRentHouse2 = generateLabel(rightArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 2, align left" + baseMigSpec);
        labelRentHouse3 = generateLabel(rightArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 3, align left" + baseMigSpec);
        labelRentHouse4 = generateLabel(rightArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 4, align left" + baseMigSpec);
        labelRentHotel = generateLabel(rightArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 5, align left" + baseMigSpec);
        labelCurrentRent = generateLabel(rightArea, LabelAlignment.LEFT, false, "(no selection)", "cell 1 6, align left" + baseMigSpec);

    }

    private JLabel generateLabelWithIcons(JPanel targetPanel, LabelAlignment alignment, boolean isTitle, String text, String migSpec, String icon) {
        JLabel label = generateLabel(targetPanel, alignment, isTitle, text, migSpec);
        label.setIcon(SwingHelper.getImageIconFromResource(icon));
        label.setHorizontalTextPosition(SwingConstants.LEFT);

        return label;
    }

    private JLabel generateLabel(JPanel targetPanel, LabelAlignment alignment, boolean isTitle, String text, String migSpec) {
        JLabel label = new JLabel();
        SwingHelper.LabelStyles style = SwingHelper.LabelStyles.TITLE_REGULAR;
        String customMigSpec = migSpec + ", width 100";

        if (isTitle) {
            style = SwingHelper.LabelStyles.TITLE_BOLD;
        }

        switch (alignment) {
            case LEFT -> label.setHorizontalAlignment(SwingConstants.LEFT);
            case RIGHT -> label.setHorizontalAlignment(SwingConstants.RIGHT);
            case CENTER -> label.setHorizontalAlignment(SwingConstants.CENTER);
        }

        SwingHelper.formatLabel(label, text, style);
        label.setName(text);

        targetPanel.add(label, customMigSpec);

        return label;
    }

    private JLabel getLabel(String name) {
        for (Component j : selectionInfoArea.getComponents()) {
            if (j instanceof JLabel) {
                if (j.getName().equals(name)) {
                    return (JLabel) j;
                }
            }
        }
        return null;
    }

    public void update(Board board, int spaceSelectionID) {

        Space localSpace = board.spaces.get(spaceSelectionID);

        // ID
        SwingHelper.formatLabel(labelID, String.valueOf(localSpace.getID()), SwingHelper.LabelStyles.TITLE_REGULAR);

        // Name
        SwingHelper.formatLabel(labelName, localSpace.getFriendlyName());

        // Is owned; owned by
        if (localSpace instanceof Property p) {
            // If property, check if owned
            if (p.getIsOwned()) {

                String mortgagedString;

                if (p.getIsMortgaged()) {
                    mortgagedString = "yes";
                } else {
                    mortgagedString = "no";
                }

                SwingHelper.formatLabel(labelOwnedBy, board.players.get(p.getOwnerID()).getCustomName(), SwingHelper.LabelStyles.TITLE_REGULAR);
                SwingHelper.formatLabel(labelIsMortgaged, mortgagedString, SwingHelper.LabelStyles.TITLE_REGULAR);
            } else {
                SwingHelper.formatLabel(labelOwnedBy, true);
                labelOwnedBy.setText("not owned");

                SwingHelper.formatLabel(labelIsMortgaged, true);
                labelIsMortgaged.setText("not owned");
            }
        } else {
            // If not property, cannot be owned
            SwingHelper.formatLabel(labelOwnedBy, true);
        }

        // Space type
        if (localSpace instanceof Property p) {
            String spaceType = "";
            if (p instanceof Color) {
                spaceType = "Color";
            } else if (p instanceof Utility) {
                spaceType = "Utility";
            } else if (p instanceof Railroad) {
                spaceType = "Railroad";
            }
            SwingHelper.formatLabel(labelSpaceType, spaceType, SwingHelper.LabelStyles.TITLE_REGULAR);
        } else {
            SwingHelper.formatLabel(labelSpaceType, "Game event", SwingHelper.LabelStyles.TITLE_REGULAR);
        }

        // Times landed
        String landedSuffix;
        if (localSpace.getTimesLanded() == 1) {
            landedSuffix = " time";
        } else {
            landedSuffix = " times";
        }
        SwingHelper.formatLabel(labelLanded, localSpace.getTimesLanded() + landedSuffix, SwingHelper.LabelStyles.TITLE_REGULAR);

        // Price
        if (localSpace instanceof Property p) {
            SwingHelper.formatLabel(labelPrice, "$" + p.getPurchaseCost(), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelCurrentRent, "$" + p.calculateRent(), SwingHelper.LabelStyles.TITLE_REGULAR);
        } else {
            SwingHelper.formatLabel(labelPrice, true);
        }

        // Rent values
        if (localSpace instanceof Color p) {
            SwingHelper.formatLabel(labelRentHouse1, p.queryRentValue(Property.QueryRentStates.HOUSE_1), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHouse2, p.queryRentValue(Property.QueryRentStates.HOUSE_2), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHouse3, p.queryRentValue(Property.QueryRentStates.HOUSE_3), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHouse4, p.queryRentValue(Property.QueryRentStates.HOUSE_4), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHotel, p.queryRentValue(Property.QueryRentStates.HOTEL), SwingHelper.LabelStyles.TITLE_REGULAR);
        } else if (localSpace instanceof Railroad p) {
            SwingHelper.formatLabel(labelRentHouse1, p.queryRentValue(Property.QueryRentStates.RAILROAD_1), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHouse2, p.queryRentValue(Property.QueryRentStates.RAILROAD_2), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHouse3, p.queryRentValue(Property.QueryRentStates.RAILROAD_3), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHouse4, p.queryRentValue(Property.QueryRentStates.RAILROAD_4), SwingHelper.LabelStyles.TITLE_REGULAR);
            SwingHelper.formatLabel(labelRentHotel, true);
        } else {
            SwingHelper.formatLabel(labelRentHouse1, true);
            SwingHelper.formatLabel(labelRentHouse2, true);
            SwingHelper.formatLabel(labelRentHouse3, true);
            SwingHelper.formatLabel(labelRentHouse4, true);
            SwingHelper.formatLabel(labelRentHotel, true);
        }
    }

    @Override
    public void update(Board board) {
    }

    @Override
    public void setStateOfActionButton(ActionsGUI action, boolean isEnabled) {
    }

    @Override
    public JComponent getComponent() {
        return null;
    }

    public JPanel getJPanel() {
        return selectionInfoArea;
    }

    private enum LabelAlignment {
        LEFT,
        RIGHT,
        CENTER
    }

}
