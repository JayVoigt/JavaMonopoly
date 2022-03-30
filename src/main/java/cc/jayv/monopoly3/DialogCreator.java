package cc.jayv.monopoly3;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

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
	
	public DialogCreator(String dialogTitle, Icon dialogIcon) {
		this.dialogTitle = dialogTitle;
		this.dialogIcon = dialogIcon;
		
		dialogButtonList = new ArrayList<>();
		dialogButtonContentList = new ArrayList<>();
	}
	
	public JDialog createDialogUserPrompt(ArrayList<ButtonContents> dialogButtonContentList) {
		JDialog userPrompt = new JDialog();
		userPrompt.setLayout(new GridBagLayout());
		userPrompt.setSize(512, 512);
		userPrompt.setUndecorated(false);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		JLabel titleLabel = new JLabel();
		titleLabel.setText(dialogTitle);
		
		// padding for label
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		titleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
		
		Icon testIcon = new ImageIcon(getClass().getResource("/robot2.gif"));
		titleLabel.setIcon(testIcon);
		
		gbc.fill = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		userPrompt.add(titleLabel, gbc);
		
		for (ButtonContents c : dialogButtonContentList) {
			int index = dialogButtonContentList.indexOf(c);
			
			// Add new JButton to list; create reference as localButton
			dialogButtonList.add(index, new JButton());
			JButton localButton = dialogButtonList.get(index);

			localButton.setText(c.getText());
			localButton.setIcon(c.getIcon());
		}
		
		
		for (JButton b : dialogButtonList) {
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 0;
			gbc.gridx = dialogButtonList.indexOf(b);
			gbc.gridy = 1;
			b.addActionListener(testListener());
			userPrompt.add(b, gbc);
			b.setVisible(true);
		}
		
		return userPrompt;
	}
	
	public ActionListener testListener() {
		ActionListener testListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton localButton = (JButton) e.getSource();
				
				localButton.setIcon(new ImageIcon(getClass().getResource("/money.png")));
				System.out.println(localButton.getText());
			}
		};
		return testListener;
	}
	
	public static class ButtonContents {
		String buttonText;
		Icon buttonIcon;
		String buttonAction;
		
		public ButtonContents(String buttonText, String buttonIconResource, String buttonAction) {
			this.buttonText = buttonText;
			
			URL iconResouce = getClass().getResource(buttonIconResource);
			if (buttonIconResource.isEmpty() == false) {
				this.buttonIcon = new ImageIcon(getClass().getResource(buttonIconResource));
			}
			else {
				this.buttonIcon = null;
			}
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
		DialogCreator creator = new DialogCreator("test dialog", null);
		ArrayList<ButtonContents> contentList = new ArrayList<>();

		for ( int i = 0 ; i < 4 ; i++ ) {
			ButtonContents bc = new ButtonContents(Integer.toString(i), "", "none");
			contentList.add(bc);
		}
		
		JDialog localDialog = creator.createDialogUserPrompt(contentList);
		localDialog.setVisible(true);
		localDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
