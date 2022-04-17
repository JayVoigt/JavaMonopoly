package cc.jayv.monopoly3;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author jay
 */
public class SwingHelper implements Serializable {

    static java.awt.Color highlightYellowShade;
    JInternalFrame frameBoard;
    Font titleFont;

    public SwingHelper(JInternalFrame frameBoardInput) {
        frameBoard = frameBoardInput;

        highlightYellowShade = new java.awt.Color(255, 224, 102);
    }

    /**
     * Format a JLabel that contains content.<br>
     * The text of the label is set to the standard style, and its value is set
     * to the contents of <code>inputContent</code>.
     *
     * @param inputLabel   The JLabel to modify
     * @param inputContent The contents of the label message
     */
    public static void formatLabel(JLabel inputLabel, String inputContent) {
        inputLabel.setForeground(java.awt.Color.black);
        inputLabel.setText(inputContent);
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

    public static void formatLabel(JLabel inputLabel, boolean isTitle) {
        if (isTitle) {
            formatLabel(inputLabel, "n/a", LabelStyles.TITLE_NO_CONTENT);
        } else {
            formatLabel(inputLabel, "n/a", LabelStyles.NO_CONTENT);

        }
    }

    // <editor-fold desc="JLabel formatting">

    /**
     * @param label   The label to be formatted.
     * @param content The text to be contained in the label.
     * @param style   The style of the label.
     */
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
        } else if (style.equals(LabelStyles.TITLE_BOLD)) {
            labelFont = new Font(labelFont.getName(), Font.BOLD, 18);
        } else if (style.equals(LabelStyles.TITLE_REGULAR)) {
            labelFont = new Font(labelFont.getName(), Font.PLAIN, 18);
        } else if (style.equals(LabelStyles.TITLE_NO_CONTENT)) {
            labelFont = new Font(labelFont.getName(), Font.ITALIC, 18);
            labelColor = java.awt.Color.gray;
            labelText = "n/a";
        }

        label.setFont(labelFont);
        label.setForeground(labelColor);
        label.setText(labelText);
    }

    public static void formatInfoArea(JTextArea textArea) {
        // Transparent background
        textArea.setOpaque(false);

        // Disable visible text selection
        textArea.setEditable(false);
        textArea.setHighlighter(null);

        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBorder(null);
        textArea.setVisible(true);
        textArea.setEnabled(true);
    }

    public static void formatGameLogArea(JTextArea textArea) {
        formatInfoArea(textArea);

        // White background
        textArea.setBackground(new java.awt.Color(255, 255, 255, 255));
        textArea.setOpaque(true);

    }
    // </editor-fold>

    // <editor-fold desc="Highlights and borders">
    public static Border createBorderStyleHighlight(java.awt.Color baseShade, boolean lighten, int thickness) {
        Border highlightBorder, innerBorder, outerBorder;
        Border highlightInnerShadow;
        Border nestedOuter, nestedInner, finalBorder;

        Border mainHighlight, fadeLevel1, fadeLevel2, fadeLevel3;

        innerBorder = BorderFactory.createLineBorder(baseShade, thickness, false);
        outerBorder = BorderFactory.createLineBorder(java.awt.Color.black, 1, false);
        mainHighlight = BorderFactory.createCompoundBorder(outerBorder, innerBorder);

        java.awt.Color fade1, fade2, fade3;

        if (lighten) {
            fade1 = getHighlightModifiedColor(baseShade);
            fade2 = getHighlightModifiedColor(fade1);
            fade3 = getHighlightModifiedColor(fade2);
        } else {
            fade1 = getShadowModifiedColor(baseShade);
            fade2 = getShadowModifiedColor(fade1);
            fade3 = getShadowModifiedColor(fade2);
        }

        fadeLevel1 = BorderFactory.createLineBorder(fade1, thickness);
        fadeLevel2 = BorderFactory.createLineBorder(fade2, thickness);
        fadeLevel3 = BorderFactory.createLineBorder(fade3, thickness);

        nestedOuter = BorderFactory.createCompoundBorder(mainHighlight, fadeLevel1);
        nestedInner = BorderFactory.createCompoundBorder(fadeLevel2, fadeLevel3);
        finalBorder = BorderFactory.createCompoundBorder(nestedOuter, nestedInner);

        return finalBorder;
    }

    public static Border createBorderStyleHighlight(java.awt.Color baseShade, boolean lighten) {
        return createBorderStyleHighlight(baseShade, lighten, 1);
    }

    public static Border createBorderStyleHighlight() {
        return createBorderStyleHighlight(highlightYellowShade, true);
    }

    public static java.awt.Color getHighlightModifiedColor(java.awt.Color inputAWTColor) {
        int newR, newG, newB, newA;
        float highlightFactor = 0.8f;

        newR = (int) (((256 - inputAWTColor.getRed()) * highlightFactor) + inputAWTColor.getRed());
        newG = (int) (((256 - inputAWTColor.getGreen()) * highlightFactor) + inputAWTColor.getGreen());
        newB = (int) (((256 - inputAWTColor.getBlue()) * highlightFactor) + inputAWTColor.getBlue());
        newA = (int) ((inputAWTColor.getAlpha()) * highlightFactor);

        return new java.awt.Color(newR, newG, newB, newA);
    }

    public static java.awt.Color getShadowModifiedColor(java.awt.Color inputAWTColor) {
        int newR, newG, newB, newA;
        float shadowFactor = 0.8f;

        newR = (int) (shadowFactor * inputAWTColor.getRed());
        newG = (int) (shadowFactor * inputAWTColor.getGreen());
        newB = (int) (shadowFactor * inputAWTColor.getBlue());
        newA = (int) (shadowFactor * inputAWTColor.getAlpha());

        return new java.awt.Color(newR, newG, newB, newA);
    }

    public static void partyMode(ArrayList<ViewFrameBoard.SpaceButton> spaceButtons) {
        int randomR, randomG, randomB;

        for (ViewFrameBoard.SpaceButton b : spaceButtons) {
            randomR = (int) (Math.random() * 256);
            randomG = (int) (Math.random() * 256);
            randomB = (int) (Math.random() * 256);

            java.awt.Color randomColor = new java.awt.Color(randomR, randomG, randomB);

            Border partyBorder = createBorderStyleHighlight(randomColor, true);
            spaceButtons.get(b.getID()).getButton().setBorderPainted(true);
            spaceButtons.get(b.getID()).getButton().setBorder(partyBorder);
        }

    }

    public static void partyModeComponent(JComponent component) {
        int randomR, randomG, randomB;

        randomR = (int) (Math.random() * 256);
        randomG = (int) (Math.random() * 256);
        randomB = (int) (Math.random() * 256);

        java.awt.Color randomColor = new java.awt.Color(randomR, randomG, randomB);

        Border partyBorder = createBorderStyleHighlight(randomColor, true);
        if (component instanceof JButton b) {
            b.setBorderPainted(true);
        }
        component.setBorder(partyBorder);
    }

    public static void spaceButtonHighlightSpectrum(int startSpaceID, int endSpaceID, ArrayList<ViewFrameBoard.SpaceButton> spaceButtons) {
        float hueShiftFactor;

        for (int i = 0; i < spaceButtons.size(); i++) {
            hueShiftFactor = i / 40f;

            int localRGB = java.awt.Color.HSBtoRGB((hueShiftFactor), 1, 1);
            java.awt.Color localAWTColor = new java.awt.Color(localRGB);

            spaceButtons.get((startSpaceID + i) % 40).getButton().setBorderPainted(true);
            spaceButtons.get((startSpaceID + i) % 40).getButton().setBorder(createBorderStyleHighlight(localAWTColor, true, 2));
        }
    }

    public static ImageIcon getImageIconFromResource(String inputResource) {
        try {
            return new ImageIcon(Objects.requireNonNull(SwingHelper.class.getResource(inputResource)));
        } catch (NullPointerException e) {
            return new ImageIcon(Objects.requireNonNull(SwingHelper.class.getResource("/error-icon.png")));
        }
    }

    private void drawBorderJDialog(JDialog inputDialog) {
        if (inputDialog.getName().equals("gameEditorDialog")) {
            inputDialog.getRootPane().setBorder(BorderFactory.createLineBorder(java.awt.Color.RED));
        } else {
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

    public void resetBorder(JButton inputButton) {
        inputButton.setBorder(null);
    }

    // </editor-fold>

    public enum LabelStyles {
        REGULAR,
        NO_CONTENT,
        TITLE_BOLD,
        TITLE_REGULAR,
        TITLE_NO_CONTENT
    }
}
