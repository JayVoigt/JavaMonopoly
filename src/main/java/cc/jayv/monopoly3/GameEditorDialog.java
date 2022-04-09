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

    public GameEditorDialog() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        labelTitle = new JLabel();
        SwingHelper.formatLabel(labelTitle, "Game Editor", SwingHelper.LabelStyles.TITLE_BOLD);
        labelTitle.setIcon(SwingHelper.getImageIconFromResource("/matrix-anim.gif"));
        dialog.add(labelTitle, "cell 0 0");

        staticLabelPlayerSelection = new JLabel("Select player:", SwingHelper.getImageIconFromResource("/player-generic.png"), SwingConstants.LEFT);
        dialog.add(staticLabelPlayerSelection, "cell 0 1");

        comboBoxPlayerSelection = new JComboBox<>();
        comboBoxPlayerSelection.addItem(1);
        comboBoxPlayerSelection.addItem(2);
        comboBoxPlayerSelection.addItem(3);
        comboBoxPlayerSelection.addItem(4);

        comboBoxPlayerSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        dialog.add(comboBoxPlayerSelection, "cell 1 1");

        dialog.pack();
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