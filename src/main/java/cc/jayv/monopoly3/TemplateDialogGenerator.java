package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author jay
 */
public class TemplateDialogGenerator implements Serializable {

    String dialogTitle;
    Icon dialogIcon;
    ArrayList<JButton> dialogButtonList;
    ArrayList<TemplateDialogButtonProperties> dialogButtonContentList;

    JDialog userPrompt;
    JTextArea infoArea;

    public TemplateDialogGenerator(String dialogTitle, String dialogIconResource) {
        this.dialogTitle = dialogTitle;
        dialogIcon = getImageIconFromResource(dialogIconResource);

        dialogButtonList = new ArrayList<>();
        dialogButtonContentList = new ArrayList<>();
    }

    public static void initDialogForView(JDialog inputDialog) {
        inputDialog.pack();
        inputDialog.setVisible(true);
    }

    public ImageIcon getImageIconFromResource(String inputResource) {
        return SwingHelper.getImageIconFromResource(inputResource);
    }

    /**
     * @param dialogButtonContentList The list of information for buttons to be created within the dialog.
     * @param infoAreaContents        A string which will be display as the contents for the main text pane within the dialog.
     * @return A JDialog object.
     */
    public JDialog createDialogUserPrompt(ArrayList<TemplateDialogButtonProperties> dialogButtonContentList, String infoAreaContents) {
        userPrompt = new JDialog();
        MigLayout promptMigLayout = new MigLayout("fill");
        userPrompt.setLayout(promptMigLayout);

        // Label for dialog
        JLabel titleLabel = new JLabel();
        titleLabel.setIcon(dialogIcon);
        titleLabel.setText(dialogTitle);

        // Padding for label
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        titleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));

        // Text area for general information
        infoArea = new JTextArea();
        infoArea.setName("infoArea");

        infoArea.setText(infoAreaContents);
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        infoArea.setBackground(null);
        infoArea.setEditable(false);

        userPrompt.add(titleLabel, "wrap, span");
        String centerSpanQuantity = Integer.toString(dialogButtonContentList.size());
        userPrompt.add(infoArea, "wrap, grow, span " + centerSpanQuantity);

        // Initialize buttons with contents from list
        for (TemplateDialogButtonProperties p : dialogButtonContentList) {
            int index = dialogButtonContentList.indexOf(p);

            // Add new JButton to list; create reference as localButton
            dialogButtonList.add(index, new JButton());
            JButton localButton = dialogButtonList.get(index);

            localButton.setText(p.getText());
            localButton.setIcon(p.getIcon());
            localButton.setFocusable(false);
        }

        // Add buttons
        for (TemplateDialogButtonProperties p : dialogButtonContentList) {
            if (p.getMigLayoutSpec().isEmpty()) {
                userPrompt.add(p);
            } else {
                userPrompt.add(p, p.getMigLayoutSpec());
            }
        }

        userPrompt.setAlwaysOnTop(true);
        userPrompt.setResizable(false);

        return userPrompt;
    }

}
