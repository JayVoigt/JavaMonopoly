package cc.jayv.monopoly3;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardFrame {

    JInternalFrame frame;

    SpaceSelectionArea spaceSelectionArea;
    ArrayList<SpaceButton> spaceButtons;
    ArrayList<JLabel> playerIndicators;

    JScrollPane gameLogScrollPane;
    JTextArea gameLogTextArea;

    LogHelper logHelper;

    ArrayList<ImageIcon> improvementIconsNorth;
    ArrayList<ImageIcon> improvementIconsEast;
    ArrayList<ImageIcon> improvementIconsSouth;
    ArrayList<ImageIcon> improvementIconsWest;

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
            b.getButton().addMouseListener(new CosmeticSpaceActionListener());

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

    private void initImprovementIcons() {
        improvementIconsNorth = new ArrayList<>();
        improvementIconsEast = new ArrayList<>();
        improvementIconsSouth = new ArrayList<>();
        improvementIconsWest = new ArrayList<>();

        improvementIconsNorth.add(SwingHelper.getImageIconFromResource("/hotel-label-north.png"));
        improvementIconsNorth.add(SwingHelper.getImageIconFromResource("/house-label-1-north.png"));
        improvementIconsNorth.add(SwingHelper.getImageIconFromResource("/house-label-2-north.png"));
        improvementIconsNorth.add(SwingHelper.getImageIconFromResource("/house-label-3-north.png"));
        improvementIconsNorth.add(SwingHelper.getImageIconFromResource("/house-label-4-north.png"));

        improvementIconsEast.add(SwingHelper.getImageIconFromResource("/hotel-label-east.png"));
        improvementIconsEast.add(SwingHelper.getImageIconFromResource("/house-label-1-east.png"));
        improvementIconsEast.add(SwingHelper.getImageIconFromResource("/house-label-2-east.png"));
        improvementIconsEast.add(SwingHelper.getImageIconFromResource("/house-label-3-east.png"));
        improvementIconsEast.add(SwingHelper.getImageIconFromResource("/house-label-4-east.png"));

        improvementIconsSouth.add(SwingHelper.getImageIconFromResource("/hotel-label-south.png"));
        improvementIconsSouth.add(SwingHelper.getImageIconFromResource("/house-label-1-south.png"));
        improvementIconsSouth.add(SwingHelper.getImageIconFromResource("/house-label-2-south.png"));
        improvementIconsSouth.add(SwingHelper.getImageIconFromResource("/house-label-3-south.png"));
        improvementIconsSouth.add(SwingHelper.getImageIconFromResource("/house-label-4-south.png"));

        improvementIconsWest.add(SwingHelper.getImageIconFromResource("/hotel-label-west.png"));
        improvementIconsWest.add(SwingHelper.getImageIconFromResource("/house-label-1-west.png"));
        improvementIconsWest.add(SwingHelper.getImageIconFromResource("/house-label-2-west.png"));
        improvementIconsWest.add(SwingHelper.getImageIconFromResource("/house-label-3-west.png"));
        improvementIconsWest.add(SwingHelper.getImageIconFromResource("/house-label-4-west.png"));
    }

    private enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        CORNER
    }

    public class SpaceButton {
        JButton button;
        int id;
        Direction direction;

        public SpaceButton(int id) {
            this.id = id;
            button = new JButton();

            if (id == 0 || id == 10 || id == 20 || id == 30) {
                direction = Direction.CORNER;
            }
            else if (id > 0 && id < 10) {
                direction = Direction.SOUTH;
            }
            else if (id > 10 && id < 20) {
                direction = Direction.WEST;
            }
            else if (id > 20 && id < 30) {
                direction = Direction.NORTH;
            }
            else if (id > 30 && id < 40) {
                direction = Direction.EAST;
            }
        }

        public JButton getButton() {
            return button;
        }

        public int getID() {
            return id;
        }

        public Direction getDirection() {
            return direction;
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
        int spaceID;
        ArrayList<ImageIcon> directionalImprovementIcons;

        for (Space s : board.spaces) {
            // Only iterate on Color spaces
            if (s instanceof Color c) {
                spaceID = c.getID();
                SpaceButton spaceButton = spaceButtons.get(spaceID);

                // Determine which directional set to obtain icons from
                switch (spaceButton.getDirection()) {
                    case NORTH -> directionalImprovementIcons = improvementIconsNorth;
                    case EAST -> directionalImprovementIcons = improvementIconsEast;
                    case SOUTH -> directionalImprovementIcons = improvementIconsSouth;
                    default -> directionalImprovementIcons = improvementIconsWest;
                }

                // Set icon depending on improvement level
                if (c.getHotelCount() == 1) {
                    spaceButton.getButton().setIcon(directionalImprovementIcons.get(0));
                }
                else {
                    switch (c.getHouseCount()) {
                        case 1 -> spaceButton.getButton().setIcon(directionalImprovementIcons.get(1));
                        case 2 -> spaceButton.getButton().setIcon(directionalImprovementIcons.get(2));
                        case 3 -> spaceButton.getButton().setIcon(directionalImprovementIcons.get(3));
                        case 4 -> spaceButton.getButton().setIcon(directionalImprovementIcons.get(4));
                        default -> spaceButton.getButton().setIcon(null);
                    }   // end switch
                }   // end else

            }   // end if
        }   // end for
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

    private static class CosmeticSpaceActionListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            ((JButton) e.getSource()).setBorderPainted(true);
            ((JButton) e.getSource()).setBorder(SwingHelper.createBorderStyleHighlight(java.awt.Color.black, true));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ((JButton) e.getSource()).setBorder(null);
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
