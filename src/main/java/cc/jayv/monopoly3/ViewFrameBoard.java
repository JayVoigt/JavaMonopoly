package cc.jayv.monopoly3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 */
public class ViewFrameBoard implements Serializable {

    JInternalFrame frame;

    ViewAreaSpaceSelection viewAreaSpaceSelection;
    ArrayList<SpaceButton> spaceButtons;

    JLabel indicatorPlayer1;
    JLabel indicatorPlayer2;
    JLabel indicatorPlayer3;
    JLabel indicatorPlayer4;
    ArrayList<JLabel> playerIndicators;

    JScrollPane gameLogScrollPane;
    JTextArea gameLogTextArea;

    LogHelper logHelper;

    // Sets of icons for Color properties that belong to a particular cardinal direction on the board
    ArrayList<ImageIcon> improvementIconsNorth;
    ArrayList<ImageIcon> improvementIconsEast;
    ArrayList<ImageIcon> improvementIconsSouth;
    ArrayList<ImageIcon> improvementIconsWest;

    /**
     *
     * @param logHelper The logHelper object where the internal text area will read from.
     * @param listener
     */
    public ViewFrameBoard(LogHelper logHelper, ArrayList<DynamicView.spaceButtonActionHandler> listener) {
        frame = new JInternalFrame();
        frame.setFrameIcon(SwingHelper.getImageIconFromResource("/board.png"));

        this.logHelper = logHelper;

        spaceButtons = new ArrayList<>();

        // Player icons
        indicatorPlayer1 = new JLabel(SwingHelper.getImageIconFromResource("/player-icon-1-px.png"));
        indicatorPlayer2 = new JLabel(SwingHelper.getImageIconFromResource("/player-icon-2-px.png"));
        indicatorPlayer3 = new JLabel(SwingHelper.getImageIconFromResource("/player-icon-3-px.png"));
        indicatorPlayer4 = new JLabel(SwingHelper.getImageIconFromResource("/player-icon-4-px.png"));
        playerIndicators = new ArrayList<>();
        playerIndicators.add(indicatorPlayer1);
        playerIndicators.add(indicatorPlayer2);
        playerIndicators.add(indicatorPlayer3);
        playerIndicators.add(indicatorPlayer4);

        for (JLabel l : playerIndicators) {
            frame.add(l);
        }

        frame.setSize(1000, 1000);
        frame.setLayout(null);
        frame.setVisible(true);

        // Game log
        gameLogScrollPane = new JScrollPane();
        gameLogTextArea = new JTextArea();
        SwingHelper.formatGameLogArea(gameLogTextArea);

        gameLogScrollPane.setBounds(150, 380, 660, 430);
        gameLogScrollPane.setVisible(true);
        gameLogScrollPane.setViewportView(gameLogTextArea);
        frame.add(gameLogScrollPane);

        viewAreaSpaceSelection = new ViewAreaSpaceSelection();
        frame.add(viewAreaSpaceSelection.getJPanel());

        // Add space buttons
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

        // Background image for board
        JLabel boardImage = new JLabel();
        boardImage.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/board-px-template.png"))));
        boardImage.setBounds(0, 0, 960, 960);

        // Spaces that have an animated image on hover have a special MouseListener attached to them
        initMouseListenersForAnimatedSpaces();

        frame.add(boardImage);

        initImprovementIcons();

        logHelper.sendWelcomeMessage();
        updateGameLog();
    }

    /**
     * Load improvement icons into sets sorted by cardinal direction
     */
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

    /**
     *
     */
    public class SpaceButton {
        JButton button;
        int id;
        Direction direction;

        boolean isAnimated;
        ImageIcon animatedIcon;
        ImageIcon animatedIconMortgaged;

        public SpaceButton(int id, boolean isAnimated, ImageIcon animatedIcon, ImageIcon animatedIconMortgaged) {
            this.isAnimated = isAnimated;
            this.animatedIcon = animatedIcon;
            this.animatedIconMortgaged = animatedIconMortgaged;

            new SpaceButton(id);
        }
        /**
         *
         * @param id The ID of the space whose button will be created.
         */
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

        public void setAnimated(boolean isAnimated) {
            this.isAnimated = isAnimated;
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

        public void setMortgagedAppearance(boolean isMortgaged) {
            // If not animated
            if (!isAnimated) {
                if (isMortgaged) {
                    switch (direction) {
                        case NORTH, SOUTH -> button.setIcon(SwingHelper.getImageIconFromResource("/checker-80x120.png"));
                        case EAST, WEST -> button.setIcon(SwingHelper.getImageIconFromResource("/checker-120x80.png"));
                    }
                } else {
                    button.setIcon(null);
                }
            }
            // If animated
            else {
                if (isMortgaged) {
                    if (id == 12) {
                        animatedIconMortgaged = SwingHelper.getImageIconFromResource("/electric-company-mortgaged.png");
                    }
                }
                else {
                    if (id == 12) {
                    }
                }
            }
        }
    }

    public JInternalFrame getInternalFrame() {
        return frame;
    }

    /**
     * Update all components of the BoardFrame class whose appearance depends on the
     * game state.
     * @param board Board data
     * @param spaceSelectionID The ID of the space that is currently selected.
     */
    public void update(Board board, int spaceSelectionID) {
        viewAreaSpaceSelection.update(board, spaceSelectionID);
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

    /**
     * Update the icons which indicate the position of each player on the board.
     * @param board
     */
    private void updateGuiPlayerIndicators(Board board) {
        Point labelPoint;
        JLabel label;

        class MovementAnimationListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }

        Timer movementAnimationTimer = new Timer(50, )

        for (Player p : board.players) {
            if (p.getIsActive()) {
                labelPoint = calculatePointForPlayerIndicator(p.getPlayerID(), p.getCurrentPosition(), p.getIsJailed());
                label = playerIndicators.get(p.getPlayerID() - 1);
                label.setVisible(true);
                label.setBounds(labelPoint.x, labelPoint.y, 18, 18);
            }
        }
    }

    /**
     *
     * @param playerID The player ID whose icon will have its new position calculated.
     * @param spaceID The space ID that will be used as a target for position calculation.
     * @return x and y coordinates (java.awt.Dimension) that the player's icon should be set to.
     */
    private Point calculatePointForPlayerIndicator(int playerID, int spaceID, boolean isJailed) {
        Point point = new Point();
        int x = 0, y = 0;

        int standardXOffset = 18 * (playerID - 1);
        int cornerXOffset = 10 * (playerID - 1);
        int standardYOffset = -4;

        int yReferenceNorth = 0;
        int yReferenceSouth = 942;
        int xReferenceEast = 942;
        int xReferenceWest = 0;

        int xAnchorNorth = 0;
        int xAnchorSouth = 840;
        int yAnchorEast = 108;
        int yAnchorWest = 832;

        if (isJailed) {
            if (playerID == 1) {
                x = 90;
                y = 850;
            }
            else if (playerID == 2) {
                x = 110;
                y = 850;
            }
            else if (playerID == 3) {
                x = 90;
                y = 870;
            }
            else if (playerID == 4) {
                x = 110;
                y = 870;
            }
        }
        else {
            if (spaceID == 0) {
                x = xAnchorSouth + cornerXOffset;
                y = yReferenceSouth;
            }
            else if (spaceID == 10) {
                x = 10 + cornerXOffset;
                y = yReferenceSouth;
            }
            else if (spaceID == 20) {
                x = 10 + cornerXOffset;
                y = yReferenceNorth;
            }
            else if (spaceID == 30) {
                x = xAnchorSouth + cornerXOffset;
                y = yReferenceNorth;
            }
            else if (spaceID > 0 && spaceID < 10) {
                x = xAnchorSouth - (80 * spaceID);
                y = yReferenceSouth;
            }
            else if (spaceID > 10 && spaceID < 20) {
                x = xReferenceWest;
                y = yAnchorWest + (80 * (11 - spaceID));
            }
            else if (spaceID > 20 && spaceID < 30) {
                x = 130 - (80 * (21 - spaceID));
                y = yReferenceNorth;
            }
            else if (spaceID > 30 && spaceID < 40) {
                x = xReferenceEast;
                y = 190 - (80 * (31 - spaceID));
            }
            x += standardXOffset;
            y += standardYOffset;
        }
        point.setLocation(x, y);

        return point;
    }

    /**
     * Update the icons indicating the improvements for each Color property.
     * @param board Board data
     */
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

    private void updateGuiMortgagedProperties(Board board) {

        for (Space s : board.spaces) {
            if (s instanceof Property p) {
                if (p.getIsMortgaged()) {

                }
                else {

                }
            }
        }
    }

    /**
     * Create special MouseListeners for space buttons that have animated icons on hover.
     */
    private void initMouseListenersForAnimatedSpaces() {
        JButton buttonElectricCompany = spaceButtons.get(12).getButton();
        JButton buttonFreeParking = spaceButtons.get(20).getButton();
        JButton buttonWaterWorks = spaceButtons.get(28).getButton();
        JButton buttonGoToJail = spaceButtons.get(30).getButton();
        JButton buttonLuxuryTax = spaceButtons.get(38).getButton();

        buttonElectricCompany.addMouseListener(new AnimatedSpaceActionListener("/electric-company-anim.gif", "/electric-company-mortgaged.png"));
        buttonFreeParking.addMouseListener(new AnimatedSpaceActionListener("/free-parking-anim.gif", null));
        buttonWaterWorks.addMouseListener(new AnimatedSpaceActionListener("/waterworks-anim.gif", null));
        buttonGoToJail.addMouseListener(new AnimatedSpaceActionListener("/go-to-jail-anim.gif", null));
        buttonLuxuryTax.addMouseListener(new AnimatedSpaceActionListener("/luxury-tax-anim.gif", null));
    }

    private class AnimatedSpaceActionListener implements MouseListener {

        ImageIcon animatedLabel;
        ImageIcon animatedLabelMortgaged;

        public AnimatedSpaceActionListener(String animatedLabelResource, String animatedMortgagedLabelResource) {
            this.animatedLabel = SwingHelper.getImageIconFromResource(animatedLabelResource);
            this.animatedLabelMortgaged = SwingHelper.getImageIconFromResource(animatedMortgagedLabelResource);
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
            JButton sourceButton = (JButton) e.getSource();
            sourceButton.setIcon(animatedLabel);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton sourceButton = (JButton) e.getSource();
            sourceButton.setIcon(null);
        }
    }

    /**
     * Generic MouseListener for graphical effects that apply to all space buttons.
     */
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
