package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SpaceSelectionArea implements ViewComponent {

    JPanel selectionInfoArea;

    // Left area, static
    JLabel staticLabelID;
    JLabel staticLabelName;
    JLabel staticLabelIsOwned;
    JLabel staticLabelSpaceType;
    JLabel staticLabelOwnedBy;
    JLabel staticLabelLanded;
    JLabel staticLabelPrice;

    // Left area, dynamic
    JLabel labelID;
    JLabel labelName;
    JLabel labelIsOwned;
    JLabel labelSpaceType;
    JLabel labelOwnedBy;
    JLabel labelLanded;
    JLabel labelPrice;

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

    public SpaceSelectionArea() {
        selectionInfoArea = new JPanel();
        selectionInfoArea.setLayout(new MigLayout());

        selectionInfoArea.setBounds(150, 140, 660, 220);

        initLabels();

        selectionInfoArea.setEnabled(true);
        selectionInfoArea.setVisible(true);
    }

    private void initLabels() {
        String baseMigSpec = "";

        // Left area, static labels
        staticLabelID = generateLabel(LabelAlignment.RIGHT, true, "ID", "cell 0 0, align right");
        staticLabelName = generateLabel(LabelAlignment.RIGHT, true, "Name", "cell 0 1, align right");
        staticLabelIsOwned = generateLabel(LabelAlignment.RIGHT,  true, "Is owned", "cell 0 2, align right");
        staticLabelOwnedBy = generateLabel(LabelAlignment.RIGHT, true, "Owned by", "cell 0 3, align right");
        staticLabelSpaceType = generateLabel(LabelAlignment.RIGHT, true, "Space type", "cell 0 4, align right");
        staticLabelLanded = generateLabel(LabelAlignment.RIGHT, true, "Landed", "cell 0 5, align right");
        staticLabelPrice = generateLabel(LabelAlignment.RIGHT, true, "Price", "cell 0 6, align right");

        // Left area, dynamic labels
        labelID = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 1 0, align left");
        labelName = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 1 1, align left");
        labelIsOwned = generateLabel(LabelAlignment.LEFT,  false, "(no selection)", "cell 1 2, align left");
        labelOwnedBy = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 1 3, align left");
        labelSpaceType = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 1 4, align left");
        labelLanded = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 1 5, align left");
        labelPrice = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 1 6, align left");

        // Right area, static labels
        staticLabelRent = generateLabel(LabelAlignment.RIGHT, true, "Base rent", "cell 2 0, align right");
        staticLabelRentHouse1 = generateLabel(LabelAlignment.RIGHT, true, "+", "cell 2 1, align right", "/house-label-1-horiz-rightalign.png");
        staticLabelRentHouse2 = generateLabel(LabelAlignment.RIGHT, true, "+", "cell 2 2, align right", "/house-label-2-horiz-rightalign.png");
        staticLabelRentHouse3 = generateLabel(LabelAlignment.RIGHT, true, "+", "cell 2 3, align right", "/house-label-3-horiz-rightalign.png");
        staticLabelRentHouse4 = generateLabel(LabelAlignment.RIGHT, true, "+", "cell 2 4, align right", "/house-label-4-horiz-rightalign.png");
        staticLabelRentHotel = generateLabel(LabelAlignment.RIGHT, true, "+", "cell 2 5, align right", "/hotel-label-1-horiz-rightalign.png");
        staticLabelCurrentRent = generateLabel(LabelAlignment.RIGHT, true, "Current rent", "cell 2 6, align right");

        // Right area, dynamic labels
        labelRent = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 3 0, align left");
        labelRentHouse1 = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 3 1, align left");
        labelRentHouse2 = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 3 2, align left");
        labelRentHouse3 = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 3 3, align left");
        labelRentHouse4 = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 3 4, align left");
        labelRentHotel = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 3 5, align left");
        labelCurrentRent = generateLabel(LabelAlignment.LEFT, false, "(no selection)", "cell 3 6, align left");

    }

    private enum LabelAlignment {
        LEFT,
        RIGHT,
        CENTER
    }

    private JLabel generateLabel(LabelAlignment alignment, boolean isTitle, String text, String migSpec, String icon) {
        JLabel label = generateLabel(alignment, isTitle, text, migSpec);
        label.setIcon(SwingHelper.getImageIconFromResource(icon));

        return label;
    }

    private JLabel generateLabel(LabelAlignment alignment, boolean isTitle, String text, String migSpec) {
        JLabel label = new JLabel();
        SwingHelper.LabelStyles style = SwingHelper.LabelStyles.TITLE_REGULAR;
        String customMigSpec = migSpec + ", width 150";

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

        selectionInfoArea.add(label, customMigSpec);

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
    }

    @Override
    public void update(Board board) {
        // ID

    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {

    }

    @Override
    public JComponent getComponent() {
        return null;
    }

    public JPanel getJPanel() {
        return selectionInfoArea;
    }

}
