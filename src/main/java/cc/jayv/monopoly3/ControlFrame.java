package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ControlFrame implements ViewComponent {
        SwingHelper helper;
        JInternalFrame frame;

        ButtonProperties buttonEndTurn;
        ButtonProperties buttonRollDice;

        ButtonProperties buttonControlsMortgage;
        ButtonProperties buttonControlsShowProperties;
        ButtonProperties buttonControlsStatistics;
        ButtonProperties buttonControlsTrade;
        ButtonProperties buttonControlsForfeit;
        ButtonProperties buttonControlsImprovements;

        JLabel staticLabelCurrentPlayer;
        JLabel labelCurrentPlayer;
        JLabel staticLabelCurrentBalance;
        JLabel labelCurrentBalance;
        JLabel staticLabelCurrentPosition;
        JLabel labelCurrentPosition;

        JLabel labelDie1;
        JLabel labelDie2;

        ArrayList<ImageIcon> DicePips;

        public ControlFrame() {
                frame = new JInternalFrame();
                frame.setLayout(new MigLayout());
                frame.setSize(308, 400);
                frame.setVisible(true);

                frame.setTitle("Controls");
                frame.setFrameIcon(SwingHelper.getImageIconFromResource("/doc-with-pencil.png"));

                initLabels();
        }

        private void initLabels() {
                // Current player static
                staticLabelCurrentPlayer = new JLabel();
                staticLabelCurrentPlayer.setIcon(SwingHelper.getImageIconFromResource("/player-generic.png"));
                SwingHelper.formatLabel(staticLabelCurrentPlayer, "Current Player", SwingHelper.LabelStyles.TITLE_BOLD);
                frame.add(staticLabelCurrentPlayer, "align left, cell 0 0, width 150");

                // Current player
                labelCurrentPlayer = new JLabel();
                SwingHelper.formatLabel(labelCurrentPlayer, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
                labelCurrentPlayer.setHorizontalAlignment(SwingConstants.RIGHT);
                frame.add(labelCurrentPlayer, "align right, cell 2 0, width 150, wrap");

                // Current balance static
                staticLabelCurrentBalance = new JLabel();
                staticLabelCurrentBalance.setIcon(SwingHelper.getImageIconFromResource("/money.png"));
                SwingHelper.formatLabel(staticLabelCurrentBalance, "Balance", SwingHelper.LabelStyles.TITLE_BOLD);
                frame.add(staticLabelCurrentBalance, "align left, cell 0 2, width 150");

                // Current balance
                labelCurrentBalance = new JLabel();
                SwingHelper.formatLabel(labelCurrentBalance, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
                labelCurrentBalance.setHorizontalAlignment(SwingConstants.RIGHT);
                frame.add(labelCurrentBalance, "align right, cell 2 2, width 150, wrap");

                // Current position static
                staticLabelCurrentPosition = new JLabel();
                staticLabelCurrentPosition.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));
                SwingHelper.formatLabel(staticLabelCurrentPosition, "Position", SwingHelper.LabelStyles.TITLE_BOLD);
                frame.add(staticLabelCurrentPosition, "align left, cell 0 4");

                // Current position
                labelCurrentPosition = new JLabel();
                SwingHelper.formatLabel(labelCurrentPosition, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
                labelCurrentPosition.setHorizontalAlignment(SwingConstants.RIGHT);
                frame.add(labelCurrentPosition, "align right, cell 2 4, width 150, wrap");

        }

        public void initButtons(ArrayList<DynamicView.ButtonActionListener> actionListeners) {

                DynamicView.ButtonActionListener endTurnActionListener = null;
                DynamicView.ButtonActionListener rollDiceActionListener = null;
                DynamicView.ButtonActionListener controlsMortgageActionListener = null;
                DynamicView.ButtonActionListener controlsPropertiesActionListener = null;
                DynamicView.ButtonActionListener controlsImprovementsActionListener = null;


                for (DynamicView.ButtonActionListener a : actionListeners) {
                        switch(a.getAction()) {
                                case CONTROLS_ENDTURN -> endTurnActionListener = a;
                                case CONTROLS_ROLLDICE -> rollDiceActionListener = a;
                                case CONTROLS_SHOW_IMPROVEMENTS -> controlsImprovementsActionListener = a;
                        }
                }

                try {
                        // End turn
                        buttonEndTurn = new ButtonProperties("End turn", "/arrow.png", endTurnActionListener, "cell 2 16, align right, width 108, height 60");
                        frame.add(buttonEndTurn, buttonEndTurn.getMigLayoutSpec());

                        // Roll dice
                        buttonRollDice = new ButtonProperties("Roll dice", "/dice-icon.png", rollDiceActionListener, "cell 0 16, align left, width 108, height 60");
                        frame.add(buttonRollDice, buttonRollDice.getMigLayoutSpec());

                        // Labels for dice
                        DicePips = new ArrayList<>();
                        DicePips.add(SwingHelper.getImageIconFromResource("/die-50px-0pip.png"));
                        DicePips.add(SwingHelper.getImageIconFromResource("/die-50px-1pip.png"));
                        DicePips.add(SwingHelper.getImageIconFromResource("/die-50px-2pip.png"));
                        DicePips.add(SwingHelper.getImageIconFromResource("/die-50px-3pip.png"));
                        DicePips.add(SwingHelper.getImageIconFromResource("/die-50px-4pip.png"));
                        DicePips.add(SwingHelper.getImageIconFromResource("/die-50px-5pip.png"));
                        DicePips.add(SwingHelper.getImageIconFromResource("/die-50px-6pip.png"));

                        labelDie1 = new JLabel();
                        labelDie2 = new JLabel();
                        labelDie1.setSize(50, 50);
                        labelDie2.setSize(50, 50);

                        labelDie1.setIcon(DicePips.get(0));
                        labelDie2.setIcon(DicePips.get(0));
                        labelDie1.setVisible(true);

                        frame.add(labelDie1, "cell 0 14, split 2, align left");
                        frame.add(labelDie2, "cell 1 14, align right");

                        // Mortgage
                        buttonControlsMortgage = new ButtonProperties("Mortgage", "/mortgage.png", null, "cell 0 6, width 120, align left");
                        frame.add(buttonControlsMortgage, buttonControlsMortgage.getMigLayoutSpec());

                        // Properties
                        buttonControlsShowProperties = new ButtonProperties("Properties", "/properties.png", controlsPropertiesActionListener, "cell 2 6, width 120, align right");
                        frame.add(buttonControlsShowProperties, buttonControlsShowProperties.getMigLayoutSpec());

                        // Improvements
                        buttonControlsImprovements = new ButtonProperties("Improvements", "/improvements.png", controlsImprovementsActionListener, "cell 0 8, width 120, align left");
                        frame.add(buttonControlsImprovements, buttonControlsImprovements.getMigLayoutSpec());

                        // Statistics
                        buttonControlsStatistics = new ButtonProperties("Statistics", "/statistics.png", null, "cell 2 8, width 120, align right");
                        frame.add(buttonControlsStatistics, buttonControlsStatistics.getMigLayoutSpec());

                        // Trade
                        buttonControlsTrade = new ButtonProperties("Trade", "/trade.png", null, "cell 0 10, width 120");
                        frame.add(buttonControlsTrade, buttonControlsTrade.getMigLayoutSpec());

                        // Forfeit
                        buttonControlsForfeit = new ButtonProperties("Forfeit", "/surrender.png", null, "cell 2 10, width 120");
                        frame.add(buttonControlsForfeit, buttonControlsForfeit.getMigLayoutSpec());

                } catch (NullPointerException ignored) {

                }
        }

        public JInternalFrame getFrame() {
                return frame;
        }

        @Override
        public void update(Board board) {
                Player currentPlayer = board.players.get(board.getCurrentPlayerID());

                SwingHelper.formatLabel(labelCurrentPlayer, currentPlayer.getCustomName(), SwingHelper.LabelStyles.TITLE_REGULAR);
                SwingHelper.formatLabel(labelCurrentBalance, String.valueOf(currentPlayer.getCurrentBalance()), SwingHelper.LabelStyles.TITLE_REGULAR);

                String currentSpaceFriendlyName = board.spaces.get(currentPlayer.getCurrentPosition()).getFriendlyName();
                SwingHelper.formatLabel(labelCurrentBalance, currentSpaceFriendlyName, SwingHelper.LabelStyles.TITLE_REGULAR);

                labelDie1.setIcon(DicePips.get(currentPlayer.getDie1()));
                labelDie2.setIcon(DicePips.get(currentPlayer.getDie2()));
        }

        @Override
        public void setStateOfActionButton(Actions action, boolean isEnabled) {
                JButton localButton = null;

                switch (action) {
                        case CONTROLS_ENDTURN -> localButton = buttonEndTurn;
                        case CONTROLS_ROLLDICE -> localButton = buttonRollDice;
                }

                if (localButton != null) {
                        localButton.setEnabled(isEnabled);
                }
        }

        @Override
        public JComponent getComponent() {
                return null;
        }
}
