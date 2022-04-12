package cc.jayv.monopoly3;

import javax.swing.*;

public class DialogContainerJail extends DialogContainer {

    JButton buttonPostBail;
    JButton buttonRollForDoubles;
    JButton buttonUseGOOJFC;

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        initLabelTitle("Jail", "/jail.png");
    }

    @Override
    protected void arrangeComponents() {

    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        switch(action) {
            case JAIL_POSTBAIL -> buttonPostBail.setEnabled(isEnabled);
            case JAIL_ROLLDOUBLES -> buttonRollForDoubles.setEnabled(isEnabled);
            case JAIL_USEGOOJFC -> buttonUseGOOJFC.setEnabled(isEnabled);
        }
    }

}
