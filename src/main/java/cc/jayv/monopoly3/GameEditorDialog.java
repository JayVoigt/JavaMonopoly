package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEditorDialog {
    JDialog dialog;
    JComboBox<Integer> comboBoxPlayerSelection;

    JLabel labelTitle;
    JLabel staticLabelPlayerSelection;

    JButton buttonGive1000;
    JButton buttonDeduct1000;
    JButton buttonJailPlayer;
    JButton buttonUnjailPlayer;
    JButton buttonUnlockRollDiceAction;
    JButton buttonUnlockEndTurnAction;
    JButton buttonGiveAllProperties;

    int playerID;

    public GameEditorDialog() {
        initComponents();
        arrangeComponents();
    }

    private void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        // Title label
        labelTitle = new JLabel();
        SwingHelper.formatLabel(labelTitle, "Game Editor", SwingHelper.LabelStyles.TITLE_BOLD);
        labelTitle.setIcon(SwingHelper.getImageIconFromResource("/matrix-anim.gif"));
        dialog.add(labelTitle, "cell 0 0");

        staticLabelPlayerSelection = new JLabel("Select player:", SwingHelper.getImageIconFromResource("/player-generic.png"), SwingConstants.LEFT);
        dialog.add(staticLabelPlayerSelection, "cell 0 1");

        // Combo box for selecting what player should be affected by actions
        comboBoxPlayerSelection = new JComboBox<>();
        comboBoxPlayerSelection.addItem(1);
        comboBoxPlayerSelection.addItem(2);
        comboBoxPlayerSelection.addItem(3);
        comboBoxPlayerSelection.addItem(4);

        comboBoxPlayerSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerID = (int) comboBoxPlayerSelection.getSelectedItem();
            }
        });

        buttonGive1000 = new JButton("Give $1000", SwingHelper.getImageIconFromResource("/money.png"));
        buttonDeduct1000 = new JButton("Deduct $1000", SwingHelper.getImageIconFromResource("/money.png"));
        buttonJailPlayer = new JButton("Jail player", SwingHelper.getImageIconFromResource("/jail.png"));
        buttonUnjailPlayer = new JButton("Release player", SwingHelper.getImageIconFromResource("/jail.png"));
        buttonUnlockRollDiceAction = new JButton("Unlock roll dice action", SwingHelper.getImageIconFromResource("/dice-icon.png"));
        buttonUnlockEndTurnAction = new JButton("Unlock end turn action", SwingHelper.getImageIconFromResource("/arrow.png"));
        buttonGiveAllProperties = new JButton("Give all properties", SwingHelper.getImageIconFromResource("/properties.png"));
    }

    private void arrangeComponents() {
        dialog.add(comboBoxPlayerSelection, "cell 1 1, wrap");

        dialog.add(buttonGive1000, "cell 0 2, grow");
        dialog.add(buttonDeduct1000, "cell 1 2, grow, wrap");
        dialog.add(buttonJailPlayer, "cell 0 3, grow");
        dialog.add(buttonUnjailPlayer, "cell 1 3, grow, wrap");
        dialog.add(buttonUnlockRollDiceAction, "cell 0 4, grow");
        dialog.add(buttonUnlockEndTurnAction, "cell 1 4, grow, wrap");
        dialog.add(buttonGiveAllProperties, "cell 0 5, grow");

        dialog.pack();
    }

    public int getSelectedPlayer() {
        return playerID;
    }

    public JDialog getDialog() {
        return dialog;
    }

    public static void main(String args[]) {
        JDialog dialog;

        GameEditorDialog gameEditorDialog = new GameEditorDialog();
        dialog = gameEditorDialog.getDialog();
        dialog.setVisible(true);
    }
}