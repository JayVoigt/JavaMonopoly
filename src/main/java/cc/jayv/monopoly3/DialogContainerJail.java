package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class DialogContainerJail extends DialogContainer {

    JButton buttonPostBail;
    JButton buttonRollForDoubles;
    JButton buttonUseGOOJFC;

    public DialogContainerJail() {
        initComponents();
        arrangeComponents();
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());
        initLabelTitle("Jail", "/jail.png");

        buttonPostBail = new JButton("Post Bail ($50)", SwingHelper.getImageIconFromResource("/money.png"));
        buttonRollForDoubles = new JButton("Roll for Doubles", SwingHelper.getImageIconFromResource("/dice-icon.png"));
        buttonUseGOOJFC = new JButton("Use Get Out of Jail Free Card", SwingHelper.getImageIconFromResource("/goojfc.png"));
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(buttonPostBail, "cell 0 2");
        dialog.add(buttonRollForDoubles, "cell 1 2, wrap");
        dialog.add(buttonUseGOOJFC, "cell 0 3, grow, span, wrap");
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        switch(action) {
            case JAIL_POSTBAIL -> buttonPostBail.setEnabled(isEnabled);
            case JAIL_ROLLDOUBLES -> buttonRollForDoubles.setEnabled(isEnabled);
            case JAIL_USEGOOJFC -> buttonUseGOOJFC.setEnabled(isEnabled);
        }
    }

    public void update(Board board, Player player) {
        // Enable Get Out of Jail Free Card button if player has at least 1 card
        setStateOfActionButton(Actions.JAIL_USEGOOJFC, (player.getGetOutOfJailFreeCardCount() > 0));

        // Disable Roll for Doubles button if player has been jailed for 3 or more turns consecutively
        setStateOfActionButton(Actions.JAIL_ROLLDOUBLES, (player.getConsecutiveTurnsJailed() < 3));
    }

    public static void main(String args[]) {
        DialogContainerJail dialogContainerJail = new DialogContainerJail();
        JDialog dialog = dialogContainerJail.getDialog();
        dialog.setVisible(true);
    }
}
