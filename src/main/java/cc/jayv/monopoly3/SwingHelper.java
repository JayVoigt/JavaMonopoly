/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cc.jayv.monopoly3;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author jay
 */
public class SwingHelper {

	JInternalFrame frameBoard;
	
	public SwingHelper(JInternalFrame frameBoardInput) {
		frameBoard = frameBoardInput;
	}
	
	private void drawBorderJDialog(JDialog inputDialog) {
		if (inputDialog.getName().equals("gameEditorDialog")) {
			inputDialog.getRootPane().setBorder(BorderFactory.createLineBorder(java.awt.Color.RED));
		}
		else {
			inputDialog.getRootPane().setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
		}
	}

	public void centerJDialog(JDialog inputDialog) {
		int referenceX = frameBoard.getLocationOnScreen().x;
		int referenceY = frameBoard.getLocationOnScreen().y;
		int referenceWidth = frameBoard.getWidth();
		int referenceHeight = frameBoard.getHeight();
		
		int innerWidth = inputDialog.getWidth();
		int innerHeight = inputDialog.getHeight();
		
		int targetX = (int) (0.5 * (referenceWidth - innerWidth));
		int targetY = (int) (0.5 * (referenceHeight - innerHeight));
		
		targetX += referenceX;
		targetY += referenceY;
		
		inputDialog.setLocation(targetX, targetY);
	}
	
	public void setCustomAppearanceJDialog(JDialog inputDialog) {
		centerJDialog(inputDialog);
		drawBorderJDialog(inputDialog);
	}
	
	// <editor-fold desc="JLabel formatting">
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

	/**
	 * Format a JLabel that contains no content.<br>
	 * The text of the label is stylized and set to indicate it has no value.
	 *
	 * @param inputLabel The JLabel to modify
	 */
	public void formatLabel(JLabel inputLabel) {
		inputLabel.setForeground(java.awt.Color.gray);
		inputLabel.setText("<html><i>n/a</i></html>");
	}
	// </editor-fold>
	
	// <editor-fold desc="Highlights and borders">
	public Border createBorderStyleHighlight(java.awt.Color baseShade) {
		Border highlightBorder, innerBorder, outerBorder;
		Border highlightInnerShadow;
		Border nestedOuter, nestedInner, finalBorder;

		Border mainHighlight, fadeLevel1, fadeLevel2, fadeLevel3;

		innerBorder = BorderFactory.createLineBorder(baseShade, 1, false);
		outerBorder = BorderFactory.createLineBorder(java.awt.Color.black, 1, false);
		mainHighlight = BorderFactory.createCompoundBorder(outerBorder, innerBorder);

		java.awt.Color yellowFade1 = getHighlightModifiedColor(baseShade);
		java.awt.Color yellowFade2 = getHighlightModifiedColor(yellowFade1);
		java.awt.Color yellowFade3 = getHighlightModifiedColor(yellowFade2);

		fadeLevel1 = BorderFactory.createLineBorder(yellowFade1, 1);
		fadeLevel2 = BorderFactory.createLineBorder(yellowFade2, 1);
		fadeLevel3 = BorderFactory.createLineBorder(yellowFade3, 1);

		nestedOuter = BorderFactory.createCompoundBorder(mainHighlight, fadeLevel1);
		nestedInner = BorderFactory.createCompoundBorder(fadeLevel2, fadeLevel3);
		finalBorder = BorderFactory.createCompoundBorder(nestedOuter, nestedInner);

		return finalBorder;
	}
	
	public void resetBorder(JButton inputButton) {
		inputButton.setBorder(null);
	}
	
	public java.awt.Color getHighlightModifiedColor(java.awt.Color inputAWTColor) {
		int newR, newG, newB, newA;
		float highightFactor = 0.8f;

		newR = (int) (((256 - inputAWTColor.getRed()) * highightFactor) + inputAWTColor.getRed());
		newG = (int) (((256 - inputAWTColor.getGreen()) * highightFactor) + inputAWTColor.getGreen());
		newB = (int) (((256 - inputAWTColor.getBlue()) * highightFactor) + inputAWTColor.getBlue());
		newA = (int) ((inputAWTColor.getAlpha()) * highightFactor);

		java.awt.Color newColor = new java.awt.Color(newR, newG, newB, newA);
		return newColor;
	}

	public java.awt.Color getShadowModifiedColor(java.awt.Color inputAWTColor) {
		int newR, newG, newB, newA;
		float shadowFactor = 0.8f;

		newR = (int) (shadowFactor * inputAWTColor.getRed());
		newG = (int) (shadowFactor * inputAWTColor.getGreen());
		newB = (int) (shadowFactor * inputAWTColor.getBlue());
		newA = (int) (shadowFactor * inputAWTColor.getAlpha());

		java.awt.Color newColor = new java.awt.Color(newR, newG, newB, newA);
		return newColor;
	}
	// </editor-fold>
}
