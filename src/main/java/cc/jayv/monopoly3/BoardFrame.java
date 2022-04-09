package cc.jayv.monopoly3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;

public class BoardFrame {

    JInternalFrame frame;

    SpaceSelectionArea spaceSelectionArea;
    ArrayList<SpaceButton> spaceButtons;
    ArrayList<JLabel> playerIndicators;

    JScrollPane gameLogScrollPane;
    JTextArea gameLogTextArea;

    LogHelper logHelper;

    public BoardFrame(LogHelper logHelper, ArrayList<DynamicView.spaceButtonActionHandler> listener) {
        frame = new JInternalFrame();
        frame.setFrameIcon(SwingHelper.getImageIconFromResource("/board.png"));

        this.logHelper = logHelper;

        spaceButtons = new ArrayList<>();
        playerIndicators = new ArrayList<>();

        playerIndicators.add(new JLabel(SwingHelper.getImageIconFromResource("/player-icon-1-px.png")));
        playerIndicators.add(new JLabel(SwingHelper.getImageIconFromResource("/player-icon-2-px.png")));
        playerIndicators.add(new JLabel(SwingHelper.getImageIconFromResource("/player-icon-3-px.png")));
        playerIndicators.add(new JLabel(SwingHelper.getImageIconFromResource("/player-icon-4-px.png")));

        frame.setSize(1000, 1000);
        frame.setLayout(null);
        frame.setVisible(true);

        gameLogScrollPane = new JScrollPane();
        gameLogTextArea = new JTextArea();

        gameLogScrollPane.setBounds(150, 380, 660, 430);
        gameLogScrollPane.setVisible(true);

        gameLogScrollPane.setViewportView(gameLogTextArea);
        for ( int i = 0 ; i < 10 ; i++ ) {
            logHelper.appendToGameLog("Test log entry " + i);
        }

        for ( String s : logHelper.getGameLogContents()) {
            gameLogTextArea.setText(gameLogTextArea.getText() + s);
        }

        frame.add(gameLogScrollPane);
        spaceSelectionArea = new SpaceSelectionArea();

        frame.add(spaceSelectionArea.getJPanel());

        for (int i = 0; i < 40; i++) {
            spaceButtons.add(new SpaceButton(i));
        }

        int posX, posY, sizeX, sizeY;

        // Determine size and position for space buttons
        for (SpaceButton b : spaceButtons) {

            b.getButton().addActionListener(listener.get(b.getID()));

            int index = spaceButtons.indexOf(b);
            int cardinalPosition = (index % 10);

            if (index == 0) {
                posX = 840;
                posY = 840;
                sizeX = 120;
                sizeY = 120;
            }
            else if (index == 10) {
                posX = 0;
                posY = 840;
                sizeX = 120;
                sizeY = 120;
            }
            else if (index == 20) {
                posX = 0;
                posY = 0;
                sizeX = 120;
                sizeY = 120;
            }
            else if (index == 30) {
                posX = 840;
                posY = 0;
                sizeX = 120;
                sizeY = 120;
            }
            else if (index > 0 && index < 10) {
                posX = 840 - (80 * cardinalPosition);
                posY = 840;
                sizeX = 80;
                sizeY = 120;
            }
            else if (index > 10 && index < 20) {
                posX = 0;
                posY = 840 - (80 * cardinalPosition);
                sizeX = 120;
                sizeY = 80;
            }
            else if (index > 20 && index < 30) {
                posX = 40 + (80 * cardinalPosition);
                posY = 0;
                sizeX = 80;
                sizeY = 120;
            }
            else if (index > 30 && index < 40) {
                posX = 840;
                posY = 40 + (80 * cardinalPosition);
                sizeX = 120;
                sizeY = 80;
            }
            else {
                posX = 0;
                posY = 0;
                sizeX = 0;
                sizeY = 0;
            }

            b.getButton().setBounds(posX, posY, sizeX, sizeY);

            // Set button to transparent - is still clickable but not visible
            java.awt.Color transparentColor = new java.awt.Color(0, 0, 0, 0);
            b.getButton().setBackground(transparentColor);

            // General button appearance
            b.getButton().setBorderPainted(false);
            b.getButton().setFocusable(false);
            b.getButton().setVisible(true);
            b.getButton().setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));

            frame.add(b.getButton());
        }	// end button creation

        JLabel boardImage = new JLabel();
        boardImage.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/board-px-template.png"))));
        boardImage.setBounds(0, 0, 960, 960);

        initMouseListenersForAnimatedSpaces();

        frame.add(boardImage);
    }

    public class SpaceButton {
        JButton button;
        int id;

        public SpaceButton(int id) {
            this.id = id;
            button = new JButton();
        }

        public JButton getButton() {
            return button;
        }

        public int getID() {
            return id;
        }
    }

    public JInternalFrame getInternalFrame() {
        return frame;
    }

    public void update(Board board, int spaceSelectionID) {
        spaceSelectionArea.update(board, spaceSelectionID);
        updateGameLog();
        updateGuiPlayerIndicators(board);
        updateGuiImprovementIcons(board);
        updateGuiSpaceHighlight(board);
    }

    public ArrayList<SpaceButton> getSpaceButtonArrayList() {
        return spaceButtons;
    }

    private void updateGameLog() {
        gameLogTextArea.setText(logHelper.getAllGameLogContents());
    }

    private void updateGuiPlayerIndicators(Board board) {
        for (Player p : board.players) {
            if (p.getIsActive()) {

            }
            else {

            }
        }
    }

    private Dimension calculateDimensionForPlayerIndicator(int playerID, int spaceID) {
        Dimension dimension = new Dimension();


        return dimension;
    }

    private void updateGuiImprovementIcons(Board board) {

    }

    private void updateGuiSpaceHighlight(Board board) {

    }

    private void initMouseListenersForAnimatedSpaces() {
        JButton buttonElectricCompany = spaceButtons.get(12).getButton();
        JButton buttonFreeParking = spaceButtons.get(20).getButton();
        JButton buttonWaterWorks = spaceButtons.get(28).getButton();
        JButton buttonGoToJail = spaceButtons.get(30).getButton();
        JButton buttonLuxuryTax = spaceButtons.get(38).getButton();

        buttonElectricCompany.addMouseListener(new AnimatedSpaceActionListener(SwingHelper.getImageIconFromResource("/electric-company-anim.gif")));
        buttonFreeParking.addMouseListener(new AnimatedSpaceActionListener(SwingHelper.getImageIconFromResource("/free-parking-anim.gif")));
        buttonWaterWorks.addMouseListener(new AnimatedSpaceActionListener(SwingHelper.getImageIconFromResource("/waterworks-anim.gif")));
        buttonGoToJail.addMouseListener(new AnimatedSpaceActionListener(SwingHelper.getImageIconFromResource("/go-to-jail-anim.gif")));
        buttonLuxuryTax.addMouseListener(new AnimatedSpaceActionListener(SwingHelper.getImageIconFromResource("/luxury-tax-anim.gif")));
    }

    private class AnimatedSpaceActionListener implements MouseListener {

        ImageIcon animatedLabel;

        public AnimatedSpaceActionListener(ImageIcon animatedLabel) {
            this.animatedLabel = animatedLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ((JButton) e.getSource()).setIcon(animatedLabel);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JButton) e.getSource()).setIcon(null);
        }
    }
}
