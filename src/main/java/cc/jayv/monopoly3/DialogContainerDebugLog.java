package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * A simple dialog that contains a JTextArea for viewing the debug log contents.
 */
public class DialogContainerDebugLog extends DialogContainer {

    LogHelper logHelper;
    JTextArea textAreaDebugLog;

    public DialogContainerDebugLog(LogHelper logHelper) {
        this.logHelper = logHelper;

        initComponents();
        arrangeComponents();
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setSize(800, 400);
        dialog.setLayout(new MigLayout());

        textAreaDebugLog = new JTextArea();

        SwingHelper.formatGameLogArea(textAreaDebugLog);
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(textAreaDebugLog, "width 800, height 400");
    }

    @Override
    public void setStateOfActionButton(ActionsGUI action, boolean isEnabled) {

    }

    public void update() {
        textAreaDebugLog.setText(logHelper.getAllDebugLogContents());
    }
}
