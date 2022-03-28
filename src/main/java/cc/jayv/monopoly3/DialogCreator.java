package cc.jayv.monopoly3;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;

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
	
	public DialogCreator() {
		dialogTitle = "";
		dialogIcon = null;
		
		dialogButtonList = new ArrayList<>();
		dialogButtonContentList = new ArrayList<>();
	}
	
	public JDialog createDialogUserPrompt(ArrayList<ButtonContents> dialogButtonContentList) {
		JDialog userPrompt = new JDialog();
		userPrompt.setLayout(new GridLayout());
		userPrompt.setSize(512, 512);
		
		for (ButtonContents c : dialogButtonContentList) {
			int index = dialogButtonContentList.indexOf(c);
			
			// Add new JButton to list; create reference as localButton
			dialogButtonList.add(index, new JButton());
			JButton localButton = dialogButtonList.get(index);

			localButton.setText(c.getText());
			localButton.setIcon(c.getIcon());
		}
		
		for (JButton b : dialogButtonList) {
			b.setVisible(true);
			b.setSize(20, 20);
			userPrompt.add(b);
		}
		
		return userPrompt;
	}
	
	public static class ButtonContents {
		String buttonText;
		Icon buttonIcon;
		String buttonAction;
		
		public ButtonContents(String buttonText, Icon buttonIcon, String buttonAction) {
			this.buttonText = buttonText;
			this.buttonIcon = buttonIcon;
			this.buttonAction = buttonAction;
		}
		
		public String getText() {
			return buttonText;
		}
		
		public Icon getIcon() {
			return buttonIcon;
		}
		
		public String getAction() {
			return buttonAction;
		}
	}
	
	// Testing method
	public static void main(String args[]) {
		DialogCreator creator = new DialogCreator();
		ArrayList<ButtonContents> contentList = new ArrayList<>();

		for ( int i = 0 ; i < 32 ; i++ ) {
			ButtonContents bc = new ButtonContents(Integer.toString(i), null, "none");
			contentList.add(bc);
		}
		
		ButtonContents buttonYes = new ButtonContents("Yes", null, "actionYes");
		ButtonContents buttonNo = new ButtonContents("No", null, "actionNo");
		
		contentList.add(buttonYes);
		contentList.add(buttonNo);
		
		JDialog localDialog = creator.createDialogUserPrompt(contentList);
		localDialog.setVisible(true);
	}
}
