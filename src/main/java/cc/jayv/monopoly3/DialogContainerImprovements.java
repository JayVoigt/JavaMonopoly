package cc.jayv.monopoly3;

import javax.swing.*;

public class DialogContainerImprovements extends DialogContainer {

    JDialog dialog;

    JButton buttonBuildHouse;
    JButton buttonSellHouse;
    JButton buttonBuildHotel;
    JButton buttonSellHotel;

    @Override
    protected void initComponents() {
        initLabelTitle("Improvements", "/improvements.png");
        dialog = new JDialog();
    }

    @Override
    protected void arrangeComponents() {

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

    @Override
    public JDialog getDialog() {
        return dialog;
    }
}
