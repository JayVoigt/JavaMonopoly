package cc.jayv.monopoly3;

import java.awt.Font;
import java.util.ArrayList;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class ViewDialog {

	String dialogTitle;
	Icon dialogIcon;
	ArrayList<JButton> dialogButtonList;
	ArrayList<ButtonProperties> dialogButtonContentList;

	JDialog userPrompt;
	JTextArea infoArea;

	public ViewDialog(String dialogTitle, String dialogIconResource) {
		this.dialogTitle = dialogTitle;
		this.dialogIcon = getImageIconFromResource(dialogIconResource);

		dialogButtonList = new ArrayList<>();
		dialogButtonContentList = new ArrayList<>();
	}

	public ImageIcon getImageIconFromResource(String inputResource) {
		if (inputResource.isEmpty() == false) {
			return new ImageIcon(getClass().getResource(inputResource));
		}
		else {
			return new ImageIcon(getClass().getResource("/error-icon.png"));
		}
	}

	/**
	 *
	 * @param dialogButtonContentList The list of information for buttons to be created within the dialog.
	 * @param infoAreaContents A string which will be display as the contents for the main text pane within the dialog.
	 * @return A JDialog object.
	 */
	public JDialog createDialogUserPrompt(ArrayList<ButtonProperties> dialogButtonContentList, String infoAreaContents) {
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
		for (ButtonProperties p : dialogButtonContentList) {
			int index = dialogButtonContentList.indexOf(p);
			
			// Add new JButton to list; create reference as localButton
			dialogButtonList.add(index, new JButton());
			JButton localButton = dialogButtonList.get(index);

			localButton.setText(p.getText());
			localButton.setIcon(p.getIcon());
		}

		// Add buttons
		for (ButtonProperties p : dialogButtonContentList) {
			if (p.getMigLayoutSpec().isEmpty()) {
				userPrompt.add(p);
			}
			else {
				userPrompt.add(p, p.getMigLayoutSpec());
			}
		}
		
		userPrompt.setAlwaysOnTop(true);
		userPrompt.setResizable(false);

		return userPrompt;
	}

	public void setInfoAreaContents(String text) {
		try {
			infoArea.setText(text);
		} catch (NullPointerException e) {
			System.err.println("Text area not initialized.");
		}
	}

	public void initDialogForView(JDialog inputDialog) {
		inputDialog.pack();
		inputDialog.setVisible(true);
	}

}
