package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public class DialogContainerForfeit extends DialogContainer {

    JButton buttonForfeit;
    JButton buttonCancel;

    JTextArea infoArea;

    public DialogContainerForfeit() {
        initComponents();
        arrangeComponents();
    }

    public static void main(String[] args) {
        DialogContainerForfeit dialogContainerForfeit = new DialogContainerForfeit();
        JDialog dialog = dialogContainerForfeit.getDialog();

        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        initLabelTitle("Forfeit", "/surrender.png");

        infoArea = new JTextArea();

        String infoAreaContents = "Are you sure you want to forfeit? You may only forfeit" +
                "when in debt to another player. If you decide to forfeit, your assets will be" +
                "transferred to the creditor, and you will be unable to play for the remainder of the game.";

        SwingHelper.formatInfoArea(infoArea);
        infoArea.setText(infoAreaContents);

        buttonForfeit = new JButton("Confirm forfeit");
        buttonForfeit.setIcon(SwingHelper.getImageIconFromResource("/surrender.png"));
        buttonCancel = new JButton("Cancel");
        buttonCancel.setIcon(SwingHelper.getImageIconFromResource("/red-x.png"));
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(infoArea, "cell 0 2, grow, span, wrap");

        dialog.add(buttonForfeit, "cell 0 4, width 150, align left");
        dialog.add(buttonCancel, "cell 1 4, width 150, align right");

        dialog.pack();
    }

    public void attachActionListeners(ArrayList<DynamicView.ButtonActionListener> listeners) {
        for (DynamicView.ButtonActionListener l : listeners) {
            switch (l.getAction()) {
                case FORFEIT_CONFIRM -> buttonForfeit.addActionListener(l);
                case FORFEIT_CANCEL -> buttonCancel.addActionListener(l);
            }
        }
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        switch (action) {
            case FORFEIT_CONFIRM -> buttonForfeit.setEnabled(isEnabled);
            case FORFEIT_CANCEL -> buttonCancel.setEnabled(isEnabled);
        }
    }
}
