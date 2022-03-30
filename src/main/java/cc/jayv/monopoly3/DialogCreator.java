package cc.jayv.monopoly3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
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
	
	public DialogCreator(String dialogTitle, Icon dialogIcon) {
		this.dialogTitle = dialogTitle;
		this.dialogIcon = dialogIcon;
		
		dialogButtonList = new ArrayList<>();
		dialogButtonContentList = new ArrayList<>();
	}
	
	public JDialog createDialogUserPrompt(ArrayList<ButtonContents> dialogButtonContentList) {
		JDialog userPrompt = new JDialog();
		MigLayout promptMigLayout = new MigLayout("fill");
		userPrompt.setLayout(promptMigLayout);
		userPrompt.setUndecorated(true);
		
		JLabel titleLabel = new JLabel();
		JTextArea infoArea = new JTextArea();
		titleLabel.setText(dialogTitle);
		infoArea.setText("This label has text which is intentionally lengthy, to convey the possible situations in which a long message such as this may be added to the dialog");
		infoArea.setLineWrap(true);
		infoArea.setBackground(null);
		infoArea.setEditable(false);
		
		// padding for label
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		titleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
		
		Icon testIcon = new ImageIcon(getClass().getResource("/robot2.gif"));
		titleLabel.setIcon(testIcon);

		userPrompt.add(titleLabel, "wrap, span");
		String centerSpanQuantity = Integer.toString(dialogButtonContentList.size());
		userPrompt.add(infoArea, "wrap, grow, span " + centerSpanQuantity);
		
		for (ButtonContents c : dialogButtonContentList) {
			int index = dialogButtonContentList.indexOf(c);
			
			// Add new JButton to list; create reference as localButton
			dialogButtonList.add(index, new JButton());
			JButton localButton = dialogButtonList.get(index);

			localButton.setText(c.getText());
			localButton.setIcon(c.getIcon());
		}
		
		dialogButtonList.get(0).setText("long text long text");
		
		for (JButton b : dialogButtonList) {
			String migButtonSpec = "cell " + dialogButtonList.indexOf(b) + " 3";
			b.addActionListener(testListener());
			userPrompt.add(b, migButtonSpec);
			b.setVisible(true);
		}
		
		return userPrompt;
	}
	
	public ActionListener testListener() {
		ActionListener testListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton localButton = (JButton) e.getSource();
				//localButton.setText("long text long text");
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
		DialogCreator creator2 = new DialogCreator("test dialog", null);
		ArrayList<ButtonContents> contentList = new ArrayList<>();
		ArrayList<ButtonContents> contentList2 = new ArrayList<>();

		for ( int i = 0 ; i < 3 ; i++ ) {
			ButtonContents bc = new ButtonContents(Integer.toString(i), "", "none");
			contentList.add(bc);
		}
		
		JDialog localDialog = creator.createDialogUserPrompt(contentList);
		localDialog.pack();
		localDialog.setVisible(true);
		localDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		ButtonContents bc = new ButtonContents("Example text", "/red-x.png", "close");
		contentList2.add(bc);
		bc = new ButtonContents("short", "/money.png", "money");
		contentList2.add(bc);
		
		JDialog localDialog2 = creator2.createDialogUserPrompt(contentList2);
		localDialog2.pack();
		localDialog2.setVisible(true);
	}
}
