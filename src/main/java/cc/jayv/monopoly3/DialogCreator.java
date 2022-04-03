package cc.jayv.monopoly3;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
public class DialogCreator {

	String dialogTitle;
	Icon dialogIcon;
	ArrayList<JButton> dialogButtonList;
	ArrayList<ButtonContents> dialogButtonContentList;
	GUISwitchBoard guiSwitchBoard;
	
	JDialog userPrompt;
	
	public DialogCreator(String dialogTitle, Icon dialogIcon) {
		this.dialogTitle = dialogTitle;
		this.dialogIcon = dialogIcon;

		dialogButtonList = new ArrayList<>();
		dialogButtonContentList = new ArrayList<>();
	}

	public DialogCreator(String dialogTitle, String dialogIconResource, GUISwitchBoard guiSwitchBoard) {
		this.dialogTitle = dialogTitle;
		this.dialogIcon = new ImageIcon(getClass().getResource(dialogIconResource));
		this.guiSwitchBoard = guiSwitchBoard;

		dialogButtonList = new ArrayList<>();
		dialogButtonContentList = new ArrayList<>();
	}

	/**
	 *
	 * @param dialogButtonContentList The list of information for buttons to be created within the dialog.
	 * @param infoAreaContents A string which will be display as the contents for the main text pane within the dialog.
	 * @return A JDialog object.
	 */
	public JDialog createDialogUserPrompt(ArrayList<ButtonContents> dialogButtonContentList, String infoAreaContents) {
		userPrompt = new JDialog();
		MigLayout promptMigLayout = new MigLayout("fill");
		userPrompt.setLayout(promptMigLayout);
		//userPrompt.setUndecorated(true);

		// Label for dialog
		JLabel titleLabel = new JLabel();
		titleLabel.setIcon(dialogIcon);
		titleLabel.setText(dialogTitle);
		// Padding for label
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		titleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));

		// Text area for general information
		JTextArea infoArea = new JTextArea();

		infoArea.setText(infoAreaContents);
		infoArea.setWrapStyleWord(true);
		infoArea.setLineWrap(true);
		infoArea.setBackground(null);
		infoArea.setEditable(false);

		userPrompt.add(titleLabel, "wrap, span");
		String centerSpanQuantity = Integer.toString(dialogButtonContentList.size());
		userPrompt.add(infoArea, "wrap, grow, span " + centerSpanQuantity);

		// Initialize buttons with contents from list
		for (ButtonContents c : dialogButtonContentList) {
			int index = dialogButtonContentList.indexOf(c);
			
			// Add new JButton to list; create reference as localButton
			dialogButtonList.add(index, new JButton());
			JButton localButton = dialogButtonList.get(index);

			localButton.setText(c.getText());
			localButton.setIcon(c.getIcon());
		}

		// Add buttons
		for (ButtonContents bc : dialogButtonContentList) {
			bc.addActionListener(testListener());
			if (bc.getMigLayoutSpec().isEmpty()) {
				userPrompt.add(bc);
			}
			else {
				userPrompt.add(bc, bc.getMigLayoutSpec());
			}
		}
		
		userPrompt.setAlwaysOnTop(true);
		userPrompt.setResizable(false);

		return userPrompt;
	}
	
	public ActionListener testListener() {
		ActionListener testListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonContents localButton = (ButtonContents) e.getSource();
				guiSwitchBoard.actionHandler(localButton.getBCAction());
			}
		};
		return testListener;
	}
	
	public ComponentListener autoPack() {
		ComponentListener packListener = new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				
			}

			@Override
			public void componentResized(ComponentEvent e) {
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				
			}
		};
		
		return packListener;
	}

	public ImageIcon getImageIconFromResource(String inputResource) {
		if (inputResource.isEmpty() == false) {
			return new ImageIcon(getClass().getResource(inputResource));
		}
		else {
			return new ImageIcon(getClass().getResource("/error-icon.png"));
		}
	}

	public void initDialogForView(JDialog inputDialog) {
		inputDialog.pack();
		inputDialog.setVisible(true);
	}
	
	public static class ButtonContents extends JButton {
		GUISwitchBoard.Actions buttonAction;
		String customMigLayoutSpec;

		/**
		 *
		 * @param buttonText The text to be displayed inside the button.
		 * @param buttonIconResource A string indicating a path where an ImageIcon resource can be loaded from.
		 * @param buttonAction An enum indicating which action should be executed when the button is pressed.
		 * @param customMigLayoutSpec A string which is either blank, or can contain commands which are then
		 *                            passed onto MigLayout for custom layout properties for this button.
		 */
		public ButtonContents(String buttonText, String buttonIconResource, GUISwitchBoard.Actions buttonAction, String customMigLayoutSpec) {
			this.setText(buttonText);
			this.customMigLayoutSpec = customMigLayoutSpec;

			if (buttonIconResource.isEmpty() == false) {
				this.setIcon(new ImageIcon(getClass().getResource(buttonIconResource)));
			}
			else {
				this.setIcon(new ImageIcon(getClass().getResource("/error-icon.png")));
			}
			this.buttonAction = buttonAction;

			this.setVisible(true);
		}
		
		public GUISwitchBoard.Actions getBCAction() {
			return buttonAction;
		}
		
		public String getMigLayoutSpec() {
			return customMigLayoutSpec;
		}
	}
	
	// Testing method
	public static void main(String args[]) {
//		DialogCreator creator = new DialogCreator("test dialog", "");
//		DialogCreator creator2 = new DialogCreator("test dialog", "");
//
//		ArrayList<ButtonContents> contentList = new ArrayList<>();
//		ArrayList<ButtonContents> contentList2 = new ArrayList<>();
//
//		for ( int i = 0 ; i < 2 ; i++ ) {
//			String customMigSpec = "cell " + i + " 1";
//			ButtonContents bc = new ButtonContents(Integer.toString(i), "", "none", customMigSpec);
//			contentList.add(bc);
//		}
//
//		JDialog localDialog = creator.createDialogUserPrompt(contentList, "");
//		localDialog.pack();
//		localDialog.setVisible(true);
//		localDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
//		ButtonContents bc = new ButtonContents("Example text", "/red-x.png", "close", "");
//		contentList2.add(bc);
//		bc = new ButtonContents("short", "/money.png", "money", "wrap");
//		contentList2.add(bc);
//
//		bc = new ButtonContents("longer", "/red-x.png", "long", "span, pushx, growx");
//		contentList2.add(bc);
//
//		JDialog localDialog2 = creator2.createDialogUserPrompt(contentList2, "The quick brown fox jumps over the lazy dog.");
//		localDialog2.pack();
//
//
//		localDialog2.setVisible(true);
	}
}
