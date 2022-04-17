package cc.jayv.monopoly3;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Dialog, Start Game: An optional dialog that allows the user to configure basic game settings,
 * and start the game when ready.<br>
 *
 * Provides options for player count and custom player names; allows starting the game.
 */
public class DialogContainerStartGame extends DialogContainer implements Serializable {

    int playerCount;
    JLabel staticLabelPlayerCount;
    JComboBox<Integer> comboBoxPlayerCount;

    ArrayList<String> playerCustomNames;
    JTextField playerCustomName1;
    JTextField playerCustomName2;
    JTextField playerCustomName3;
    JTextField playerCustomName4;

    JLabel staticLabelPlayer1;
    JLabel staticLabelPlayer2;
    JLabel staticLabelPlayer3;
    JLabel staticLabelPlayer4;

    JButton buttonStartGame;

    DynamicView.StartGameButtonActionListener startGameButtonActionListener;

    public DialogContainerStartGame() {
        initComponents();
        arrangeComponents();
    }

    public static void main(String[] args) {
        DialogContainerStartGame startGameDialog = new DialogContainerStartGame();
        JDialog dialog = startGameDialog.getDialog();

        dialog.setVisible(true);
    }

    @Override
    protected void initComponents() {
        dialog = new JDialog();
        dialog.setLayout(new MigLayout());

        // Title
        initLabelTitle("New Game", "/board.png");

        // Label and combo box for player counts
        staticLabelPlayerCount = new JLabel("Players:");
        staticLabelPlayerCount.setIcon(SwingHelper.getImageIconFromResource("/player-generic.png"));

        comboBoxPlayerCount = new JComboBox<>();
        comboBoxPlayerCount.addItem(1);
        comboBoxPlayerCount.addItem(2);
        comboBoxPlayerCount.addItem(3);
        comboBoxPlayerCount.addItem(4);
        comboBoxPlayerCount.setSelectedItem(4);

        comboBoxPlayerCount.addActionListener(e -> {
            playerCount = (int) comboBoxPlayerCount.getSelectedItem();

            if (playerCount == 1) {
                playerCustomName1.setEnabled(true);
                playerCustomName2.setEnabled(false);
                playerCustomName3.setEnabled(false);
                playerCustomName4.setEnabled(false);
            } else if (playerCount == 2) {
                playerCustomName1.setEnabled(true);
                playerCustomName2.setEnabled(true);
                playerCustomName3.setEnabled(false);
                playerCustomName4.setEnabled(false);
            } else if (playerCount == 3) {
                playerCustomName1.setEnabled(true);
                playerCustomName2.setEnabled(true);
                playerCustomName3.setEnabled(true);
                playerCustomName4.setEnabled(false);
            } else {
                playerCustomName1.setEnabled(true);
                playerCustomName2.setEnabled(true);
                playerCustomName3.setEnabled(true);
                playerCustomName4.setEnabled(true);
            }
        });

        // Strings containing player's desired name
        playerCustomNames = new ArrayList<>();

        // Fields for name entry
        playerCustomName1 = new JTextField("Player 1");
        playerCustomName2 = new JTextField("Player 2");
        playerCustomName3 = new JTextField("Player 3");
        playerCustomName4 = new JTextField("Player 4");

        // Labels to indicate player index
        staticLabelPlayer1 = new JLabel("Player 1 name:");
        staticLabelPlayer1.setHorizontalAlignment(SwingConstants.RIGHT);

        staticLabelPlayer2 = new JLabel("Player 2 name:");
        staticLabelPlayer2.setHorizontalAlignment(SwingConstants.RIGHT);

        staticLabelPlayer3 = new JLabel("Player 3 name:");
        staticLabelPlayer3.setHorizontalAlignment(SwingConstants.RIGHT);

        staticLabelPlayer4 = new JLabel("Player 4 name:");
        staticLabelPlayer4.setHorizontalAlignment(SwingConstants.RIGHT);

        // Button to start game
        buttonStartGame = new JButton();
        buttonStartGame.setText("Start Game");
        buttonStartGame.setIcon(SwingHelper.getImageIconFromResource("/newgame.png"));
    }

    @Override
    protected void arrangeComponents() {
        dialog.add(labelTitle, "cell 0 0, wrap");

        dialog.add(staticLabelPlayerCount, "cell 0 2");
        dialog.add(comboBoxPlayerCount, "cell 1 2, wrap");

        dialog.add(staticLabelPlayer1, "cell 0 4");
        dialog.add(playerCustomName1, "cell 1 4, width 150, wrap");

        dialog.add(staticLabelPlayer2, "cell 0 5");
        dialog.add(playerCustomName2, "cell 1 5, width 150, wrap");

        dialog.add(staticLabelPlayer3, "cell 0 6");
        dialog.add(playerCustomName3, "cell 1 6, width 150, wrap");

        dialog.add(staticLabelPlayer4, "cell 0 7");
        dialog.add(playerCustomName4, "cell 1 7, width 150, wrap");

        dialog.add(buttonStartGame, "cell 0 9, width 240, height 40, spanx 2, al center center");

        dialog.pack();
    }

    @Override
    public void setStateOfActionButton(Actions action, boolean isEnabled) {
        if (action == Actions.NEWGAME_STARTGAME) {
            buttonStartGame.setEnabled(isEnabled);
        }
    }

    public void attachStartGameActionListener(DynamicView.StartGameButtonActionListener listener) {
        startGameButtonActionListener = listener;
        buttonStartGame.addActionListener(startGameButtonActionListener);
        buttonStartGame.addActionListener(new LocalActionListener());
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String getPlayerCustomName(int playerID) {
        String playerCustomName = "";

        switch (playerID) {
            case 1 -> playerCustomName = playerCustomNames.get(0);
            case 2 -> playerCustomName = playerCustomNames.get(1);
            case 3 -> playerCustomName = playerCustomNames.get(2);
            case 4 -> playerCustomName = playerCustomNames.get(3);
        }

        return playerCustomName;
    }

    private class LocalActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            playerCount = (int) comboBoxPlayerCount.getSelectedItem();

            playerCustomNames.clear();
            playerCustomNames.trimToSize();
            playerCustomNames.add(playerCustomName1.getText());
            playerCustomNames.add(playerCustomName2.getText());
            playerCustomNames.add(playerCustomName3.getText());
            playerCustomNames.add(playerCustomName4.getText());

            startGameButtonActionListener.setPlayerCustomNames(playerCustomNames);
            startGameButtonActionListener.setPlayerCount(playerCount);
        }
    }

}
