package cc.jayv.monopoly3;

import javax.swing.*;

/**
 * A template for dialog creation. Provides abstract methods for obtaining the JDialog object
 * that is shown to the user, and infrastructure for modifying contents of the dialog according
 * to game state.
 */
public abstract class DialogContainer {
    JDialog dialog;
    JLabel labelTitle;

    /**
     * Initialize GUI components for the dialog.
     */
    protected abstract void initComponents();

    /**
     * Initialize the label used for dialogs. Ensures that dialogs have a more standardized
     * appearance.
     * @param title The content of the title label.
     * @param iconResource A string indicating a resource that will be loaded as the icon for the title label.
     */
    protected void initLabelTitle(String title, String iconResource) {
        labelTitle = new JLabel();
        SwingHelper.formatLabel(labelTitle, title, SwingHelper.LabelStyles.TITLE_BOLD);
        labelTitle.setIcon(SwingHelper.getImageIconFromResource(iconResource));
    }

    /**
     * Arrange the GUI components within the dialog using class-specific MigSpec
     */
    protected abstract void arrangeComponents();

    /**
     * Set a particular button to be enabled or disabled, by providing the corresponding Action key
     * implemented by the ButtonActionListener.
     *
     * @param action    The action which the corresponding button will have its state set.
     * @param isEnabled The state of the button.
     */
    public abstract void setStateOfActionButton(ActionsGUI action, boolean isEnabled);

    /**
     * Obtain the JDialog instantiated by the container class.
     *
     * @return The JDialog within the container.
     */
    public JDialog getDialog() {
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        return dialog;
    }
}