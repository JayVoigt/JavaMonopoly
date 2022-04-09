package cc.jayv.monopoly3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class BoardFrame {

    JInternalFrame frame;

    SpaceSelectionArea spaceSelectionArea;
    ArrayList<SpaceButton> spaceButtons;

    JScrollPane gameLogScrollPane;
    JTextArea gameLogTextArea;

    LogHelper logHelper;

    int spaceSelectionID;

    public BoardFrame(LogHelper logHelper, ArrayList<DynamicView.spaceButtonActionHandler> listener) {
        frame = new JInternalFrame();
        frame.setFrameIcon(SwingHelper.getImageIconFromResource("/board.png"));

        this.logHelper = logHelper;

        spaceButtons = new ArrayList<>();

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
    }

    public ArrayList<SpaceButton> getSpaceButtonArrayList() {
        return spaceButtons;
    }

    private void updateGameLog() {
        gameLogTextArea.setText(logHelper.getAllGameLogContents());
    }
}
