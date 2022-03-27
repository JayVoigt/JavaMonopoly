/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cc.jayv.monopoly3;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author jay
 */
public class SwingHelper {

	public void drawBorderJDialog(JDialog inputDialog) {
		if (inputDialog.getName().equals("gameEditorDialog")) {
			inputDialog.getRootPane().setBorder(BorderFactory.createLineBorder(java.awt.Color.RED));
		}
		else {
			inputDialog.getRootPane().setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
		}
	}

	/**
	 * Format a JLabel that contains content.<br>
	 * The text of the label is set to the standard style, and its value is set
	 * to the contents of <code>inputContent</code>.
	 *
	 * @param inputLabel The JLabel to modify
	 * @param inputContent The contents of the label message
	 */
	public void formatLabel(JLabel inputLabel, String inputContent) {
		inputLabel.setForeground(java.awt.Color.black);
		inputLabel.setText(inputContent);
	}
	
	public void formatLabel(JLabel inputLabel) {
		inputLabel.setForeground(java.awt.Color.gray);
		inputLabel.setText("<html><i>n/a</i></html>");
	}

}
