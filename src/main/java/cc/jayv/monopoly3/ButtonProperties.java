package cc.jayv.monopoly3;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ButtonProperties extends JButton {
    String customMigLayoutSpec;

    /**
     *
     * @param buttonText The text to be displayed inside the button.
     * @param buttonIconResource A string indicating a path where an ImageIcon resource can be loaded from.
     * @param actionListener An enum indicating which action should be executed when the button is pressed.
     * @param customMigLayoutSpec A string which is either blank, or can contain commands which are then
     *                            passed onto MigLayout for custom layout properties for this button.
     */
    public ButtonProperties(String buttonText, String buttonIconResource, ActionListener actionListener, String customMigLayoutSpec) {
        this.setText(buttonText);
        this.customMigLayoutSpec = customMigLayoutSpec;

        this.addActionListener(actionListener);

        if (buttonIconResource.isEmpty() == false) {
            this.setIcon(new ImageIcon(getClass().getResource(buttonIconResource)));
        }
        else {
            this.setIcon(new ImageIcon(getClass().getResource("/error-icon.png")));
        }

        this.setVisible(true);
    }

    public String getMigLayoutSpec() {
        return customMigLayoutSpec;
    }
}