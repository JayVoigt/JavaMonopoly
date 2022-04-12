package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.Serializable;

public class DialogContainerAbout implements Serializable {
    JDialog dialog;

    public DialogContainerAbout() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        // Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(SwingHelper.getImageIconFromResource("/logo.png"));

        // Author tag with icon
        JLabel authorLabel = new JLabel();
        SwingHelper.formatLabel(authorLabel, "by Jay Voigt", SwingHelper.LabelStyles.TITLE_BOLD);
        authorLabel.setIcon(SwingHelper.getImageIconFromResource("/robot2.gif"));
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // General information about the project
        JLabel infoLabel = new JLabel();
        String infoText = "<html>Created as a final project for CSCI 24000, Spring 2022.<br>Art assets created with Aesprite.</html>";
        SwingHelper.formatLabel(infoLabel, infoText, SwingHelper.LabelStyles.REGULAR);

        dialog.add(logoLabel, "cell 0 0, spanx, al center center");
        dialog.add(authorLabel, "cell 0 1, spanx, al center center");
        dialog.add(infoLabel, "cell 0 2, spanx");

        dialog.pack();
    }

    public JDialog getDialog() {
        return dialog;
    }
}
