package cc.jayv.monopoly3;

import javax.swing.*;

public abstract class DialogContainer {
    JDialog dialog;
    JLabel labelTitle;

    /**
     * Initialize GUI components for the dialog.
     */
    protected abstract void initComponents();

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
     * @param action The action which the corresponding button will have its state set.
     * @param isEnabled The state of the button.
     */
    public abstract void setStateOfActionButton(Actions action, boolean isEnabled);

    /**
     * Obtain the JDialog instantiated by the container class.
     * @return The JDialog within the container.
     */
    public JDialog getDialog() {
        return dialog;
    }
}