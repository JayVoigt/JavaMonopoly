package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.Serializable;

public class DialogContainerAbout extends DialogContainer implements Serializable {
    JLabel logoLabel;
    JLabel authorLabel;
    JLabel infoLabel;

    public DialogContainerAbout() {
        initComponents();
        arrangeComponents();
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        // Logo
        logoLabel = new JLabel();
        logoLabel.setIcon(SwingHelper.getImageIconFromResource("/logo.png"));

        // Author tag with icon
        authorLabel = new JLabel();
        SwingHelper.formatLabel(authorLabel, "by Jay Voigt", SwingHelper.LabelStyles.TITLE_BOLD);
        authorLabel.setIcon(SwingHelper.getImageIconFromResource("/robot2.gif"));
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // General information about the project
        infoLabel = new JLabel();
        String infoText = "<html>Created as a final project for CSCI 24000, Spring 2022.<br>Art assets created with Aesprite.</html>";
        SwingHelper.formatLabel(infoLabel, infoText, SwingHelper.LabelStyles.REGULAR);
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(logoLabel, "cell 0 0, spanx, al center center");
        dialog.add(authorLabel, "cell 0 1, spanx, al center center");
        dialog.add(infoLabel, "cell 0 2, spanx");

        dialog.pack();
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        // About dialog has no JButtons
    }

    @Override
    public JDialog getDialog() {
        return dialog;
    }
}
