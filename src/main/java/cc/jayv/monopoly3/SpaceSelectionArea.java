package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

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
        generateLabel(staticLabelID, LabelAlignment.RIGHT, true, "ID", "cell 0 0, align right");
        generateLabel(staticLabelName, LabelAlignment.RIGHT, true, "Name", "cell 0 1, align right");
        generateLabel(staticLabelIsOwned, LabelAlignment.RIGHT,  true, "Is owned", "cell 0 2, align right");
        generateLabel(staticLabelOwnedBy, LabelAlignment.RIGHT, true, "Owned by", "cell 0 3, align right");
        generateLabel(staticLabelSpaceType, LabelAlignment.RIGHT, true, "Space type", "cell 0 4, align right");
        generateLabel(staticLabelLanded, LabelAlignment.RIGHT, true, "Landed", "cell 0 5, align right");
        generateLabel(staticLabelPrice, LabelAlignment.RIGHT, true, "Price", "cell 0 6, align right");

        // Left area, dynamic labels
        generateLabel(labelID, LabelAlignment.LEFT, false, "(no selection)", "cell 1 0, align left");
        generateLabel(labelName, LabelAlignment.LEFT, false, "(no selection)", "cell 1 1, align left");
        generateLabel(labelIsOwned, LabelAlignment.LEFT,  false, "(no selection)", "cell 1 2, align left");
        generateLabel(labelOwnedBy, LabelAlignment.LEFT, false, "(no selection)", "cell 1 3, align left");
        generateLabel(labelSpaceType, LabelAlignment.LEFT, false, "(no selection)", "cell 1 4, align left");
        generateLabel(labelLanded, LabelAlignment.LEFT, false, "(no selection)", "cell 1 5, align left");
        generateLabel(labelPrice, LabelAlignment.LEFT, false, "(no selection)", "cell 1 6, align left");

        // Right area, static labels
        generateLabel(staticLabelRent, LabelAlignment.RIGHT, true, "Base rent", "cell 2 0, align right");
        generateLabel(staticLabelRentHouse1, LabelAlignment.RIGHT, true, "+", "cell 2 1, align right", "/house-label-1-horiz-rightalign.png");
        generateLabel(staticLabelRentHouse2, LabelAlignment.RIGHT, true, "+", "cell 2 2, align right", "/house-label-2-horiz-rightalign.png");
        generateLabel(staticLabelRentHouse3, LabelAlignment.RIGHT, true, "+", "cell 2 3, align right", "/house-label-3-horiz-rightalign.png");
        generateLabel(staticLabelRentHouse4, LabelAlignment.RIGHT, true, "+", "cell 2 4, align right", "/house-label-4-horiz-rightalign.png");
        generateLabel(staticLabelRentHotel, LabelAlignment.RIGHT, true, "+", "cell 2 5, align right", "/hotel-label-1-horiz-rightalign.png");
        generateLabel(staticLabelCurrentRent, LabelAlignment.RIGHT, true, "Current rent", "cell 2 6, align right");

        // Right area, dynamic labels
        generateLabel(labelRent, LabelAlignment.LEFT, false, "(no selection)", "cell 3 0, align left");
        generateLabel(labelRentHouse1, LabelAlignment.LEFT, false, "(no selection)", "cell 3 1, align left");
        generateLabel(labelRentHouse2, LabelAlignment.LEFT, false, "(no selection)", "cell 3 2, align left");
        generateLabel(labelRentHouse3, LabelAlignment.LEFT, false, "(no selection)", "cell 3 3, align left");
        generateLabel(labelRentHouse4, LabelAlignment.LEFT, false, "(no selection)", "cell 3 4, align left");
        generateLabel(labelRentHotel, LabelAlignment.LEFT, false, "(no selection)", "cell 3 5, align left");
        generateLabel(labelCurrentRent, LabelAlignment.LEFT, false, "(no selection)", "cell 3 6, align left");

    }

    private enum LabelAlignment {
        LEFT,
        RIGHT,
        CENTER
    }

    private void generateLabel(JLabel label, LabelAlignment alignment, boolean isTitle, String text, String migSpec, String icon) {
        label = new JLabel();
        label.setIcon(SwingHelper.getImageIconFromResource(icon));
        label.setHorizontalTextPosition(SwingConstants.LEFT);
        generateLabel(label, alignment, isTitle, text, migSpec);
    }

    private void generateLabel(JLabel label, LabelAlignment alignment, boolean isTitle, String text, String migSpec) {
        if (java.util.Objects.isNull(label)) {
            label = new JLabel();
        }
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

    @Override
    public void update(Board board) {

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
