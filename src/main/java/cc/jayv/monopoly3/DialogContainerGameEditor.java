package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

public class DialogContainerGameEditor extends DialogContainer implements Serializable {
    JComboBox<Integer> comboBoxPlayerSelection;

    JLabel staticLabelPlayerSelection;

    JButton buttonGive1000;
    JButton buttonDeduct1000;
    JButton buttonJailPlayer;
    JButton buttonUnjailPlayer;
    JButton buttonUnlockRollDiceAction;
    JButton buttonUnlockEndTurnAction;
    JButton buttonGiveAllProperties;
    JButton buttonRandomlyDistributeAllProperties;
    JButton buttonForceEndTurn;

    int playerID;

    public DialogContainerGameEditor() {
        initComponents();
        arrangeComponents();
    }

    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        // Title label
        initLabelTitle("Game Editor", "/matrix-anim.gif");
        dialog.add(labelTitle, "cell 0 0");

        staticLabelPlayerSelection = new JLabel("Select player:", SwingHelper.getImageIconFromResource("/player-generic.png"), SwingConstants.LEFT);
        dialog.add(staticLabelPlayerSelection, "cell 0 1");

        // Combo box for selecting what player should be affected by actions
        comboBoxPlayerSelection = new JComboBox<>();
        comboBoxPlayerSelection.addItem(1);
        comboBoxPlayerSelection.addItem(2);
        comboBoxPlayerSelection.addItem(3);
        comboBoxPlayerSelection.addItem(4);

        // Fallback value
        playerID = 1;

        comboBoxPlayerSelection.addActionListener(e -> playerID = (int) comboBoxPlayerSelection.getSelectedItem());

        buttonGive1000 = new JButton("Give $1000", SwingHelper.getImageIconFromResource("/money.png"));
        buttonDeduct1000 = new JButton("Deduct $1000", SwingHelper.getImageIconFromResource("/money.png"));
        buttonJailPlayer = new JButton("Jail player", SwingHelper.getImageIconFromResource("/jail.png"));
        buttonUnjailPlayer = new JButton("Release player", SwingHelper.getImageIconFromResource("/jail.png"));
        buttonUnlockRollDiceAction = new JButton("Unlock roll dice action", SwingHelper.getImageIconFromResource("/dice-icon.png"));
        buttonUnlockEndTurnAction = new JButton("Unlock end turn action", SwingHelper.getImageIconFromResource("/arrow.png"));
        buttonGiveAllProperties = new JButton("Give all properties", SwingHelper.getImageIconFromResource("/properties.png"));
        buttonRandomlyDistributeAllProperties = new JButton("Randomly distribute all properties", SwingHelper.getImageIconFromResource("/properties.png"));
        buttonForceEndTurn = new JButton("Forcibly end turn", SwingHelper.getImageIconFromResource("/alert.png"));
    }

    public void attachActionListeners(ArrayList<DynamicView.GameEditorActionListener> listeners) {
        for (DynamicView.GameEditorActionListener l : listeners) {
            switch (l.getAction()) {
                case JAIL -> buttonJailPlayer.addActionListener(l);
                case UNJAIL -> buttonUnjailPlayer.addActionListener(l);
                case GIVE_1000 -> buttonGive1000.addActionListener(l);
                case DEDUCT_1000 -> buttonDeduct1000.addActionListener(l);
                case UNLOCK_END_TURN -> buttonUnlockEndTurnAction.addActionListener(l);
                case UNLOCK_ROLL_DICE -> buttonUnlockRollDiceAction.addActionListener(l);
                case GIVE_ALL_PROPERTIES -> buttonGiveAllProperties.addActionListener(l);
                case RANDOMLY_DISTRIBUTE_ALL_PROPERTIES -> buttonRandomlyDistributeAllProperties.addActionListener(l);
                case FORCE_END_TURN -> buttonForceEndTurn.addActionListener(l);
            }
        }
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(comboBoxPlayerSelection, "cell 1 1, wrap");

        dialog.add(buttonGive1000, "cell 0 2, grow");
        dialog.add(buttonDeduct1000, "cell 1 2, grow, wrap");
        dialog.add(buttonJailPlayer, "cell 0 3, grow");
        dialog.add(buttonUnjailPlayer, "cell 1 3, grow, wrap");
        dialog.add(buttonUnlockRollDiceAction, "cell 0 4, grow");
        dialog.add(buttonUnlockEndTurnAction, "cell 1 4, grow, wrap");
        dialog.add(buttonGiveAllProperties, "cell 0 5, grow");
        dialog.add(buttonRandomlyDistributeAllProperties, "cell 1 5, grow");
        dialog.add(buttonForceEndTurn, "cell 0 6, grow, wrap");

        dialog.pack();
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
    }

    public int getSelectedPlayer() {
        return playerID;
    }

    public static void main(String[] args) {
        JDialog dialog;

        DialogContainerGameEditor gameEditorDialog = new DialogContainerGameEditor();
        dialog = gameEditorDialog.getDialog();
        dialog.setVisible(true);
    }
}