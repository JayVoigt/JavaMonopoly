package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class ViewFrameControl implements ViewComponent, Serializable {
    JInternalFrame frame;

    TemplateDialogButtonProperties buttonEndTurn;
    TemplateDialogButtonProperties buttonRollDice;

    TemplateDialogButtonProperties buttonControlsMortgage;
    TemplateDialogButtonProperties buttonControlsShowProperties;
    TemplateDialogButtonProperties buttonControlsStatistics;
    TemplateDialogButtonProperties buttonControlsTrade;
    TemplateDialogButtonProperties buttonControlsForfeit;
    TemplateDialogButtonProperties buttonControlsImprovements;

    JLabel staticLabelCurrentPlayer;
    JLabel labelCurrentPlayer;
    JLabel staticLabelCurrentBalance;
    JLabel labelCurrentBalance;
    JLabel staticLabelCurrentPosition;
    JLabel labelCurrentPosition;

    JLabel labelDie1;
    JLabel labelDie2;

    ArrayList<ImageIcon> DicePips;

    public ViewFrameControl() {
        frame = new JInternalFrame();
        frame.setLayout(new MigLayout());
        frame.setSize(400, 400);
        frame.setVisible(true);

        frame.setTitle("Controls");
        frame.setFrameIcon(SwingHelper.getImageIconFromResource("/doc-with-pencil.png"));

        initLabels();
    }

    private void initLabels() {
        String standardMigSpecLabel = ", grow, width 200";
        // Current player static
        staticLabelCurrentPlayer = new JLabel();
        staticLabelCurrentPlayer.setIcon(SwingHelper.getImageIconFromResource("/player-generic.png"));
        SwingHelper.formatLabel(staticLabelCurrentPlayer, "Current Player", SwingHelper.LabelStyles.TITLE_BOLD);
        frame.add(staticLabelCurrentPlayer, "align left, cell 0 0" + standardMigSpecLabel);

        // Current player
        labelCurrentPlayer = new JLabel();
        SwingHelper.formatLabel(labelCurrentPlayer, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
        labelCurrentPlayer.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(labelCurrentPlayer, "align right, cell 2 0, wrap" + standardMigSpecLabel);

        // Current balance static
        staticLabelCurrentBalance = new JLabel();
        staticLabelCurrentBalance.setIcon(SwingHelper.getImageIconFromResource("/money.png"));
        SwingHelper.formatLabel(staticLabelCurrentBalance, "Balance", SwingHelper.LabelStyles.TITLE_BOLD);
        frame.add(staticLabelCurrentBalance, "align left, cell 0 2" + standardMigSpecLabel);

        // Current balance
        labelCurrentBalance = new JLabel();
        SwingHelper.formatLabel(labelCurrentBalance, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
        labelCurrentBalance.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(labelCurrentBalance, "align right, cell 2 2, wrap" + standardMigSpecLabel);

        // Current position static
        staticLabelCurrentPosition = new JLabel();
        staticLabelCurrentPosition.setIcon(SwingHelper.getImageIconFromResource("/properties.png"));
        SwingHelper.formatLabel(staticLabelCurrentPosition, "Position", SwingHelper.LabelStyles.TITLE_BOLD);
        frame.add(staticLabelCurrentPosition, "align left, cell 0 4" + standardMigSpecLabel);

        // Current position
        labelCurrentPosition = new JLabel();
        SwingHelper.formatLabel(labelCurrentPosition, "n/a", SwingHelper.LabelStyles.TITLE_NO_CONTENT);
        labelCurrentPosition.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(labelCurrentPosition, "align right, cell 2 4, wrap" + standardMigSpecLabel);

    }

    public void initButtons(ArrayList<DynamicView.GenericButtonActionListener> actionListeners) {

        DynamicView.GenericButtonActionListener endTurnActionListener = null;
        DynamicView.GenericButtonActionListener rollDiceActionListener = null;
        DynamicView.GenericButtonActionListener controlsMortgageActionListener = null;
        DynamicView.GenericButtonActionListener controlsPropertiesActionListener = null;
        DynamicView.GenericButtonActionListener controlsImprovementsActionListener = null;
        DynamicView.GenericButtonActionListener controlsForfeitActionListener = null;

        for (DynamicView.GenericButtonActionListener l : actionListeners) {
            switch (l.getAction()) {
                case CONTROLS_ENDTURN -> endTurnActionListener = l;
                case CONTROLS_ROLLDICE -> rollDiceActionListener = l;
                case CONTROLS_SHOW_IMPROVEMENTS -> controlsImprovementsActionListener = l;
                case CONTROLS_SHOW_MORTGAGE -> controlsMortgageActionListener = l;
                case CONTROLS_SHOW_FORFEIT -> controlsForfeitActionListener = l;
            }
        }

        try {
            // End turn
            buttonEndTurn = new TemplateDialogButtonProperties("End turn", "/arrow.png", endTurnActionListener, "cell 2 16, align right, width 108, height 60");
            buttonEndTurn.setFocusable(true);
            frame.add(buttonEndTurn, buttonEndTurn.getMigLayoutSpec());

            // Roll dice
            buttonRollDice = new TemplateDialogButtonProperties("Roll dice", "/dice-icon.png", rollDiceActionListener, "cell 0 16, align left, width 108, height 60");
            buttonRollDice.setFocusable(true);
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

            String standardMigSpecButton = ", grow";
            // Mortgage
            buttonControlsMortgage = new TemplateDialogButtonProperties("Mortgage", "/mortgage.png", controlsMortgageActionListener, "cell 0 6, align left" + standardMigSpecButton);
            buttonControlsMortgage.setFocusable(false);
            frame.add(buttonControlsMortgage, buttonControlsMortgage.getMigLayoutSpec());

            // Properties
            buttonControlsShowProperties = new TemplateDialogButtonProperties("Properties", "/properties.png", controlsPropertiesActionListener, "cell 2 6, align right" + standardMigSpecButton);
            buttonControlsShowProperties.setFocusable(false);
            frame.add(buttonControlsShowProperties, buttonControlsShowProperties.getMigLayoutSpec());

            // Improvements
            buttonControlsImprovements = new TemplateDialogButtonProperties("Improvements", "/improvements.png", controlsImprovementsActionListener, "cell 0 8, align left" + standardMigSpecButton);
            buttonControlsImprovements.setFocusable(false);
            frame.add(buttonControlsImprovements, buttonControlsImprovements.getMigLayoutSpec());

            // Statistics
            buttonControlsStatistics = new TemplateDialogButtonProperties("Statistics", "/statistics.png", null, "cell 2 8, align right" + standardMigSpecButton);
            buttonControlsStatistics.setFocusable(false);
            frame.add(buttonControlsStatistics, buttonControlsStatistics.getMigLayoutSpec());

            // Trade
            buttonControlsTrade = new TemplateDialogButtonProperties("Trade", "/trade.png", null, "cell 0 10, align left" + standardMigSpecButton);
            buttonControlsTrade.setFocusable(false);
            frame.add(buttonControlsTrade, buttonControlsTrade.getMigLayoutSpec());

            // Forfeit
            buttonControlsForfeit = new TemplateDialogButtonProperties("Forfeit", "/surrender.png", controlsForfeitActionListener, "cell 2 10, align right" + standardMigSpecButton);
            buttonControlsForfeit.setFocusable(false);
            frame.add(buttonControlsForfeit, buttonControlsForfeit.getMigLayoutSpec());

            for (ActionsGUI a : ActionsGUI.values()) {
                setStateOfActionButton(a, false);
            }

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
        SwingHelper.formatLabel(labelCurrentBalance, "$" + currentPlayer.getCurrentBalance(), SwingHelper.LabelStyles.TITLE_REGULAR);

        String currentSpaceFriendlyName = board.spaces.get(currentPlayer.getCurrentPosition()).getFriendlyName();
        SwingHelper.formatLabel(labelCurrentPosition, currentSpaceFriendlyName, SwingHelper.LabelStyles.TITLE_REGULAR);

        labelDie1.setIcon(DicePips.get(currentPlayer.getDie1()));
        labelDie2.setIcon(DicePips.get(currentPlayer.getDie2()));

        for (ActionsGUI a : ActionsGUI.values()) {
            setStateOfActionButton(a, true);
        }

        setStateOfActionButton(ActionsGUI.CONTROLS_ENDTURN, !currentPlayer.getActionLockedEndTurn());
        setStateOfActionButton(ActionsGUI.CONTROLS_ROLLDICE, !currentPlayer.getActionLockedRollDice());
    }

    @Override
    public void setStateOfActionButton(ActionsGUI action, boolean isEnabled) {
        JButton localButton = null;

        switch (action) {
            case CONTROLS_ENDTURN -> localButton = buttonEndTurn;
            case CONTROLS_ROLLDICE -> localButton = buttonRollDice;
            case CONTROLS_SHOW_IMPROVEMENTS -> localButton = buttonControlsImprovements;
            case CONTROLS_SHOW_FORFEIT -> localButton = buttonControlsForfeit;
            case CONTROLS_SHOW_MORTGAGE -> localButton = buttonControlsMortgage;
            case CONTROLS_SHOW_PROPERTIES -> localButton = buttonControlsShowProperties;
            case CONTROLS_SHOW_STATISTICS -> localButton = buttonControlsStatistics;
            case CONTROLS_SHOW_TRADE -> localButton = buttonControlsTrade;
        }

        if (localButton != null) {
            localButton.setEnabled(isEnabled);
        }
    }

    @Override
    public JComponent getComponent() {
        return null;
    }

    public void partyVisuals() {
        SwingHelper.partyModeComponent(frame);
    }
}
