package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class PropertySelectionArea implements ViewComponent {

    JPanel selectionInfoArea;

    JLabel staticLabelID;
    JLabel staticLabelName;
    JLabel staticLabelIsOwned;
    JLabel staticLabelSpaceType;
    JLabel staticLabelOwnedBy;
    JLabel staticLabelLanded;
    JLabel staticLabelPrice;

    JLabel staticLabelRent;
    JLabel staticLabelRentHouse1;
    JLabel staticLabelRentHouse2;
    JLabel staticLabelRentHouse3;
    JLabel staticLabelRentHouse4;
    JLabel staticLabelRentHotel;

    public PropertySelectionArea() {
        selectionInfoArea = new JPanel();
        selectionInfoArea.setLayout(new MigLayout());

        selectionInfoArea.setBounds(150, 140, 660, 220);

        initLabels();

        selectionInfoArea.setEnabled(true);
        selectionInfoArea.setVisible(true);
    }

    private void initLabels() {
        String baseMigSpec = "";

        // Left side, static labels
        generateLabel(staticLabelID, LabelAlignment.RIGHT, true, "ID", "cell 0 0, align left");
        generateLabel(staticLabelName, LabelAlignment.RIGHT, true, "Name", "cell 0 1, align left");
        generateLabel(staticLabelIsOwned, LabelAlignment.RIGHT,  true, "Is owned", "cell 0 2, align left");
        generateLabel(staticLabelOwnedBy, LabelAlignment.RIGHT, true, "Owned by", "cell 0 3, align left");
        generateLabel(staticLabelSpaceType, LabelAlignment.RIGHT, true, "Space type", "cell 0 4, align left");
        generateLabel(staticLabelLanded, LabelAlignment.RIGHT, true, "Landed", "cell 0 5, align left");
        generateLabel(staticLabelPrice, LabelAlignment.RIGHT, true, "Price", "cell 0 6, align left");
    }

    private enum LabelAlignment {
        LEFT,
        RIGHT,
        CENTER
    }

    private void generateLabel(JLabel label, boolean isTitle, String text, String migSpec) {
        generateLabel(label, LabelAlignment.LEFT, isTitle, text, migSpec);
    }

    private void generateLabel(JLabel label, LabelAlignment alignment, boolean isTitle, String text, String migSpec) {
        label = new JLabel();
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

        selectionInfoArea.add(label, customMigSpec);
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
