package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class GameEditorDialog {
    JDialog dialog;

    public GameEditorDialog() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        JLabel labelTitle = new JLabel();
        SwingHelper.formatLabel(labelTitle, "Game Editor", SwingHelper.LabelStyles.TITLE_BOLD);
        labelTitle.setIcon(SwingHelper.getImageIconFromResource("/matrix-anim.gif"));
        dialog.add(labelTitle, "cell 0 0");

        JLabel staticLabelPlayerSelection = new JLabel("Select player:", SwingHelper.getImageIconFromResource("/player-generic.png"), SwingConstants.LEFT);
        dialog.add(staticLabelPlayerSelection, "cell 0 1");

        JSpinner spinnerPlayerSelection = new JSpinner();
        dialog.add(spinnerPlayerSelection, "cell 1 1");

        dialog.pack();
    }

    public JDialog getDialog() {
        return dialog;
    }
}