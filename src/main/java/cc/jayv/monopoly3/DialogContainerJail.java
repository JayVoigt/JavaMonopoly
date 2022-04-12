package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public class DialogContainerJail extends DialogContainer {

    JButton buttonPostBail;
    JButton buttonRollForDoubles;
    JButton buttonUseGOOJFC;

    JTextArea infoArea;

    public DialogContainerJail() {
        initComponents();
        arrangeComponents();
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());
        initLabelTitle("Jail", "/jail.png");

        infoArea = new JTextArea();
        SwingHelper.formatInfoArea(infoArea);

        buttonPostBail = new JButton("Post Bail ($50)", SwingHelper.getImageIconFromResource("/money.png"));
        buttonRollForDoubles = new JButton("Roll for Doubles", SwingHelper.getImageIconFromResource("/dice-icon.png"));
        buttonUseGOOJFC = new JButton("Use Get Out of Jail Free Card", SwingHelper.getImageIconFromResource("/goojfc.png"));

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(infoArea, "cell 0 2, grow, span, wrap");

        dialog.add(buttonPostBail, "cell 0 4");
        dialog.add(buttonRollForDoubles, "cell 1 4, wrap");
        dialog.add(buttonUseGOOJFC, "cell 0 5, grow, span, wrap");
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
        String message;
        int turnsJailed = player.getConsecutiveTurnsJailed();

        if (turnsJailed == 1) {
            message = "You have been jailed for 1 turn.";
        }
        else {
            message = "You have been jailed for " + turnsJailed + " turns.";
        }

        // Enable Get Out of Jail Free Card button if player has at least 1 card
        setStateOfActionButton(Actions.JAIL_USEGOOJFC, (player.getGetOutOfJailFreeCardCount() > 0));

        // Disable Roll for Doubles button if player has been jailed for 3 or more turns consecutively
        if (player.getConsecutiveTurnsJailed() >= 3) {
            setStateOfActionButton(Actions.JAIL_ROLLDOUBLES, false);
            message = message.concat(" You are unable to roll for doubles, and must post bail or use a Get Out" +
                    "of Jail Free card.");
        }
        else {
            setStateOfActionButton(Actions.JAIL_ROLLDOUBLES, true);
        }

        infoArea.setText(message);
        dialog.pack();
    }

    public void attachActionListeners(ArrayList<DynamicView.ButtonActionListener> listeners) {
        for (DynamicView.ButtonActionListener l : listeners) {
            switch (l.getAction()) {
                case JAIL_POSTBAIL -> buttonPostBail.addActionListener(l);
                case JAIL_ROLLDOUBLES -> buttonRollForDoubles.addActionListener(l);
                case JAIL_USEGOOJFC -> buttonUseGOOJFC.addActionListener(l);
            }
        }
    }

    public static void main(String args[]) {
        DialogContainerJail dialogContainerJail = new DialogContainerJail();
        JDialog dialog = dialogContainerJail.getDialog();
        dialog.setVisible(true);
    }
}
