/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cc.jayv.monopoly3;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 *
 * @author jay
 */
public class SwingHelper {

	JInternalFrame frameBoard;
	java.awt.Color highlightYellowShade;
	Font titleFont;

	public SwingHelper(JInternalFrame frameBoardInput) {
		frameBoard = frameBoardInput;

		highlightYellowShade = new java.awt.Color(255, 224, 102);
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
	public static void formatLabel(JLabel inputLabel, String inputContent) {
		inputLabel.setForeground(java.awt.Color.black);
		inputLabel.setText(inputContent);
	}

	public enum LabelStyles {
		REGULAR,
		NO_CONTENT,
		TITLE_BOLD,
		TITLE_REGULAR,
		TITLE_NO_CONTENT
	}
	/**
	 * Format a JLabel that contains no content.<br>
	 * The text of the label is stylized and set to indicate it has no value.
	 *
	 * @param inputLabel The JLabel to modify
	 */
	public static void formatLabel(JLabel inputLabel) {
		formatLabel(inputLabel, "n/a", LabelStyles.NO_CONTENT);
	}
	// </editor-fold>

	public static void formatLabel(JLabel label, String content, LabelStyles style) {
		// Obtain default font from JEditorPane component
		// Ref: https://alvinalexander.com/java/how-to-get-default-system-font-java-swing/
		Font labelFont = new JEditorPane().getFont();
		java.awt.Color labelColor = java.awt.Color.black;
		String labelText = content;

		if (style.equals(LabelStyles.NO_CONTENT)) {
			labelFont = new Font(labelFont.getName(), Font.ITALIC, labelFont.getSize());
			labelColor = java.awt.Color.gray;
			labelText = "n/a";
		}
		else if (style.equals(LabelStyles.TITLE_BOLD)) {
			labelFont = new Font(labelFont.getName(), Font.BOLD, 18);
		}
		else if (style.equals(LabelStyles.TITLE_REGULAR)) {
			labelFont = new Font(labelFont.getName(), Font.PLAIN, 18);
		}
		else if (style.equals(LabelStyles.TITLE_NO_CONTENT)) {
			labelFont = new Font(labelFont.getName(), Font.ITALIC, 18);
			labelColor = java.awt.Color.gray;
			labelText = "n/a";
		}

		label.setFont(labelFont);
		label.setForeground(labelColor);
		label.setText(labelText);
	}

	// <editor-fold desc="Highlights and borders">
	public Border createBorderStyleHighlight(java.awt.Color baseShade, boolean lighten) {
		Border highlightBorder, innerBorder, outerBorder;
		Border highlightInnerShadow;
		Border nestedOuter, nestedInner, finalBorder;

		Border mainHighlight, fadeLevel1, fadeLevel2, fadeLevel3;

		innerBorder = BorderFactory.createLineBorder(baseShade, 1, false);
		outerBorder = BorderFactory.createLineBorder(java.awt.Color.black, 1, false);
		mainHighlight = BorderFactory.createCompoundBorder(outerBorder, innerBorder);

		java.awt.Color fade1, fade2, fade3;
		
		if (lighten == true) {
			fade1 = getHighlightModifiedColor(baseShade);
			fade2 = getHighlightModifiedColor(fade1);
			fade3 = getHighlightModifiedColor(fade2);
		}
		else {
			fade1 = getShadowModifiedColor(baseShade);
			fade2 = getShadowModifiedColor(fade1);
			fade3 = getShadowModifiedColor(fade2);
		}
		
		fadeLevel1 = BorderFactory.createLineBorder(fade1, 1);
		fadeLevel2 = BorderFactory.createLineBorder(fade2, 1);
		fadeLevel3 = BorderFactory.createLineBorder(fade3, 1);

		nestedOuter = BorderFactory.createCompoundBorder(mainHighlight, fadeLevel1);
		nestedInner = BorderFactory.createCompoundBorder(fadeLevel2, fadeLevel3);
		finalBorder = BorderFactory.createCompoundBorder(nestedOuter, nestedInner);

		return finalBorder;
	}

	public Border createBorderStyleHighlight() {
		return createBorderStyleHighlight(highlightYellowShade, true);
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

	public static ImageIcon getImageIconFromResource(String inputResource) {
		if (inputResource.isEmpty() == false) {
			return new ImageIcon(SwingHelper.class.getResource(inputResource));
		}
		else {
			return new ImageIcon(SwingHelper.class.getResource("/error-icon.png"));
		}
	}
}
