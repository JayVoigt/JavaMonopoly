package cc.jayv.monopoly3;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jay
 */
public class DynamicView implements Serializable {

    // Reference variables
    static int currentSpaceButtonSelection;

    // Core components - GUI
    private JFrame mainFrame;
    private ViewFrameBoard viewFrameBoard;
    private ViewFrameControl viewFrameControl;
    private ViewFrameInformation viewFrameInformation;
    private ArrayList<ViewFrameBoard.SpaceButton> spaceButtons;
    private ArrayList<GenericButtonActionListener> genericButtonActionListeners;

    // Core components - logic and control
    private GameLogicSwitchboard switchboard;
    private GameLogicController controller;
    private Board board;
    private LogHelper logHelper;

    // Dialog containers
    private DialogContainerGameEditor dialogContainerGameEditor;
    private DialogContainerImprovements dialogContainerImprovements;
    private DialogContainerJail dialogContainerJail;
    private DialogContainerMortgage dialogContainerMortgage;
    private DialogContainerPurchaseProperty dialogContainerPurchaseProperty;
    private DialogContainerStartGame dialogContainerStartGame;
    private DialogContainerForfeit dialogContainerForfeit;
    private DialogContainerDebugLog dialogContainerDebugLog;
    private GameEditorController gameEditorController;

    // Dialogs
    private JDialog dialogJail;
    private JDialog dialogPurchaseProperty;
    private JDialog dialogImprovements;
    private JDialog dialogStartGame;
    private JDialog dialogGameEditor;
    private JDialog dialogAbout;
    private JDialog dialogMortgage;
    private JDialog dialogForfeit;
    private JDialog dialogDebugLog;

    // Other
    private Timer partyModeTimer;
    private MacroController macroController;
    private Player currentPlayer;
    private int playerCount;
    private ArrayList<String> playerCustomNames;

    public DynamicView() {

        // Set to FlatLaf appearance
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (UnsupportedLookAndFeelException e) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     UnsupportedLookAndFeelException ex) {
                Logger.getLogger(DynamicView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Initialize board
        try {
            board = new Board();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Initialize logic and helper components
        logHelper = new LogHelper();
        controller = new GameLogicController(board, logHelper);
        switchboard = new GameLogicSwitchboard(controller);
        gameEditorController = new GameEditorController(board, controller, logHelper);

        initButtonActionListeners();
        initGUIComponents();
        initMenuBar();

        // Initialize main frame
        mainFrame.setLayout(new MigLayout());
        initControlFrame();
        mainFrame.add(viewFrameBoard.getInternalFrame(), "cell 0 0, x 0, y 0, width 1000, height 1000");
        mainFrame.add(viewFrameInformation.getFrame(), "cell 1 0, x 1000, y 0, width 400, height 600");
        mainFrame.add(viewFrameControl.getFrame(), "cell 1 1, x 1000, y 600, width 400, height 400");
        mainFrame.pack();
        mainFrame.setVisible(true);

        initDialogs();
    }

    /**
     * Update the view in accordance with the current game state.<br>
     * Calls several methods which update specific parts of the interface.
     */
    public void update() {
        board.forceBoardSelfCheck();
        currentPlayer = board.players.get(board.getCurrentPlayerID());
        currentPlayer.evaluateState();

        viewFrameBoard.update(board, currentSpaceButtonSelection);

        viewFrameControl.update(board);
        viewFrameInformation.update(board);
        updateGuiMandatoryDialogs();
        updateGuiOptionalDialogs();

        if (controller.getIsVictoryConditionMet()) {
            partyVisuals();
        }
        if (currentPlayer.isAnimateMovement()) {
            viewFrameBoard.animatePlayerMovement(board, currentPlayer.getPlayerID());
            currentPlayer.setAnimateMovement(false);
        } else {
            viewFrameBoard.resetButtonAppearance();
        }
    }

    /**
     * Update state for mandatory dialogs.
     */
    private void updateGuiMandatoryDialogs() {
        // Jail
        if (!currentPlayer.getInitialJailTurn() && currentPlayer.getIsJailed()) {
            dialogContainerJail.update(board, currentPlayer);
            updateDialogAppearance(dialogJail, true);
        } else {
            updateDialogAppearance(dialogJail, false);
        }

        // Purchase decision
        if (currentPlayer.getRequiredDecisionPropertyAction()) {
            dialogContainerPurchaseProperty.update(board, currentPlayer.getCurrentPosition());
        }
        updateDialogAppearance(dialogPurchaseProperty, currentPlayer.getRequiredDecisionPropertyAction());

        // Disable control buttons when mandatory action dialogs visible
        // IDE-suggested code
        for (JDialog jDialog : Arrays.asList(dialogPurchaseProperty, dialogJail, dialogPurchaseProperty)) {
            if (jDialog.isVisible()) {
                disableControlButtons();
            }
        }
    }

    /**
     * Update state for optional dialogs.
     */
    private void updateGuiOptionalDialogs() {
        if (dialogImprovements.isVisible()) {
            dialogContainerImprovements.update(board, currentPlayer, currentSpaceButtonSelection, controller);
        }

        if (dialogMortgage.isVisible()) {
            dialogContainerMortgage.update(board, currentPlayer, currentSpaceButtonSelection, controller);
        }

        if (dialogDebugLog.isVisible()) {
            dialogContainerDebugLog.update();
        }
    }

    /**
     * Show the corresponding prompt for a given action.
     * @param action The action that corresponds to a dialog.
     */
    private void updatePrompt(ActionsGUI action) {
        switch (action) {
            case CONTROLS_SHOW_IMPROVEMENTS -> updateDialogAppearance(dialogImprovements);
            case CONTROLS_SHOW_MORTGAGE -> updateDialogAppearance(dialogMortgage);
            case GAME_SHOW_JAIL -> updateDialogAppearance(dialogJail);
            case GAME_SHOW_PURCHASE -> updateDialogAppearance(dialogPurchaseProperty);
            case CONTROLS_SHOW_FORFEIT -> updateDialogAppearance(dialogForfeit);
        }

        update();
    }

    /**
     * Show the given dialog.
     * @param dialog The dialog to be shown to the user.
     */
    private void updateDialogAppearance(JDialog dialog) {
        updateDialogAppearance(dialog, true);
    }

    /**
     * Show or hide the given dialog. If shown, automatically sets the dialog to be always on top,
     * and sets its position relative to the main game window.
     * @param dialog The dialog whose visibility will be set.
     * @param visible The visibility for the dialog.
     */
    private void updateDialogAppearance(JDialog dialog, boolean visible) {
        if (visible) {
            dialog.setLocationRelativeTo(viewFrameBoard.getInternalFrame());
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        } else {
            dialog.setVisible(false);
        }
    }

    /**
     * Disable roll dice and end turn control buttons.
     */
    private void disableControlButtons() {
        viewFrameControl.setStateOfActionButton(ActionsGUI.CONTROLS_ENDTURN, false);
        viewFrameControl.setStateOfActionButton(ActionsGUI.CONTROLS_ROLLDICE, false);
    }


    /**
     * Initialize GUI components.
     */
    private void initGUIComponents() {

        ArrayList<SpaceButtonActionListener> SpaceButtonActionListeners = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            SpaceButtonActionListeners.add(new SpaceButtonActionListener(i));
        }

        // Core components
        mainFrame = new JFrame();
        viewFrameBoard = new ViewFrameBoard(logHelper, SpaceButtonActionListeners);
        spaceButtons = viewFrameBoard.getSpaceButtonArrayList();
        viewFrameControl = new ViewFrameControl();
        viewFrameInformation = new ViewFrameInformation(board);
    }

    /**
     * Initialize the menu bar and its items.
     */
    private void initMenuBar() {
        // Root-level menus
        JMenuBar menuBar = new JMenuBar();

        // Root level menus
        JMenu menuFile = new JMenu();
        menuFile.setText("File");
        menuBar.add(menuFile);

        JMenu menuEdit = new JMenu();
        menuEdit.setText("Edit");
        menuBar.add(menuEdit);

        JMenu menuView = new JMenu();
        menuView.setText("View");
        menuBar.add(menuView);

        JMenu menuMacros = new JMenu();
        menuMacros.setText("Macros");
        menuBar.add(menuMacros);

        // File - New Game
        JMenuItem menuFileNewGame = new JMenuItem("New Game...", SwingHelper.getImageIconFromResource("/newgame.png"));
        menuFileNewGame.addActionListener(new MenuActionListener(MenuActions.FILE_NEW_GAME));
        menuFile.add(menuFileNewGame);

        // File - Load Game
        JMenuItem menuFileLoadGame = new JMenuItem("Load Game...", SwingHelper.getImageIconFromResource(("/floppygame.png")));
        menuFileLoadGame.addActionListener(e -> loadGame());
        menuFile.add(menuFileLoadGame);

        // File - Save Game
        JMenuItem menuFileSaveGame = new JMenuItem("Save Game...", SwingHelper.getImageIconFromResource(("/floppygame.png")));
        menuFileSaveGame.addActionListener(e -> saveGame());
        menuFile.add(menuFileSaveGame);

        // File - Quit
        JMenuItem menuFileQuit = new JMenuItem("Quit", SwingHelper.getImageIconFromResource(("/red-x.png")));
        menuFileQuit.addActionListener(new MenuActionListener(MenuActions.FILE_QUIT));
        menuFile.add(menuFileQuit);

        // Edit - Game Editor
        JMenuItem menuEditGameEditor = new JMenuItem("Game Editor...", SwingHelper.getImageIconFromResource("/matrix-anim.gif"));
        menuEditGameEditor.addActionListener(new MenuActionListener(MenuActions.EDIT_GAME_EDITOR));
        menuEditGameEditor.setForeground(java.awt.Color.red);
        menuEdit.add(menuEditGameEditor);

        // Edit - Manually Refresh View
        JMenuItem menuEditManuallyRefresh = new JMenuItem("Manually Refresh View", SwingHelper.getImageIconFromResource("/alert.png"));
        menuEditManuallyRefresh.addActionListener(e -> update());
        menuEdit.add(menuEditManuallyRefresh);

        // View - About
        JMenuItem menuViewAbout = new JMenuItem("About", SwingHelper.getImageIconFromResource("/robot.png"));
        menuViewAbout.addActionListener(new MenuActionListener(MenuActions.VIEW_ABOUT));
        menuView.add(menuViewAbout);

        // View - Debug Log
        JMenuItem menuViewDebugLog = new JMenuItem("Debug Log", SwingHelper.getImageIconFromResource("/bug-log-anim.gif"));
        menuViewDebugLog.addActionListener(new MenuActionListener(MenuActions.VIEW_DEBUG_LOG));
        menuView.add(menuViewDebugLog);

        // View - Super Party Mode
        JMenuItem menuViewSuperPartyMode = new JMenuItem("Super Party Mode", SwingHelper.getImageIconFromResource("/robot2.gif"));
        menuViewSuperPartyMode.addActionListener(e -> partyVisuals());
        menuView.add(menuViewSuperPartyMode);

        // View - Reset Space Button Appearance
        JMenuItem menuViewResetSpaceButtonAppearance = new JMenuItem("Reset Space Button Appearance", SwingHelper.getImageIconFromResource("/red-x.png"));
        menuViewResetSpaceButtonAppearance.addActionListener(e -> resetSpaceButtonAppearance());
        menuView.add(menuViewResetSpaceButtonAppearance);

        // Macros - Bankruptcy Test
        JMenuItem menuMacrosBankruptcyTest = new JMenuItem("Bankruptcy Test", SwingHelper.getImageIconFromResource("/alert.png"));
        menuMacrosBankruptcyTest.addActionListener(e -> macroController.macroBankruptcyTest());
        menuMacros.add(menuMacrosBankruptcyTest);

        // Macros - Disable Random Player
        JMenuItem menuMacrosDisableRandomPlayer = new JMenuItem("Disable Random Player", SwingHelper.getImageIconFromResource("/player-generic.png"));
        menuMacrosDisableRandomPlayer.addActionListener(e -> macroController.macroDisableRandomPlayer());
        menuMacros.add(menuMacrosDisableRandomPlayer);

        for (Component c : menuMacros.getMenuComponents()) {
            if (c instanceof JMenuItem i) {
                i.addActionListener(e -> update());
            }
        }

        // Ready
        menuBar.setVisible(true);
        mainFrame.setJMenuBar(menuBar);
    }

    private void initButtonActionListeners() {
        // Create all possible ActionListeners for dialogs
        genericButtonActionListeners = new ArrayList<>();

        for (ActionsGUI a : ActionsGUI.values()) {

            switch (a) {
                case CONTROLS_SHOW_MORTGAGE, CONTROLS_SHOW_FORFEIT, CONTROLS_SHOW_IMPROVEMENTS -> genericButtonActionListeners.add(new GenericButtonActionListener(a, true));
                default -> genericButtonActionListeners.add(new GenericButtonActionListener(a, false));
            }

        }
    }

    /**
     * Initialize the control frame shown in the game view.
     */
    private void initControlFrame() {
        viewFrameControl.initButtons(genericButtonActionListeners);
    }

    /**
     * Initialize dialog containers and attach DynamicView references to their corresponding JDialog objects.
     */
    private void initDialogs() {
        // About
        dialogAbout = new DialogContainerAbout().getDialog();

        // Game editor
        dialogContainerGameEditor = new DialogContainerGameEditor();
        dialogGameEditor = dialogContainerGameEditor.getDialog();

        // Improvements
        dialogContainerImprovements = new DialogContainerImprovements();
        dialogContainerImprovements.attachActionListeners(genericButtonActionListeners);
        dialogImprovements = dialogContainerImprovements.getDialog();

        // Jail
        dialogContainerJail = new DialogContainerJail();
        dialogContainerJail.attachActionListeners(genericButtonActionListeners);
        dialogJail = dialogContainerJail.getDialog();

        // Mortgage properties
        dialogContainerMortgage = new DialogContainerMortgage();
        dialogContainerMortgage.attachActionListeners(genericButtonActionListeners);
        dialogMortgage = dialogContainerMortgage.getDialog();

        // Purchase property
        dialogContainerPurchaseProperty = new DialogContainerPurchaseProperty();
        dialogContainerPurchaseProperty.attachActionListeners(genericButtonActionListeners);
        dialogPurchaseProperty = dialogContainerPurchaseProperty.getDialog();

        // Game editor ActionListeners
        ArrayList<GameEditorActionListener> gameEditorActionListeners = new ArrayList<>();
        for (GameEditorActions a : GameEditorActions.values()) {
            gameEditorActionListeners.add(new GameEditorActionListener(a));
        }
        dialogContainerGameEditor.attachActionListeners(gameEditorActionListeners);

        // Start game
        dialogContainerStartGame = new DialogContainerStartGame();
        StartGameButtonActionListener startGameButtonActionListener = new StartGameButtonActionListener();
        dialogContainerStartGame.attachStartGameActionListener(startGameButtonActionListener);
        dialogStartGame = dialogContainerStartGame.getDialog();

        // Forfeit
        dialogContainerForfeit = new DialogContainerForfeit();
        dialogContainerForfeit.attachActionListeners(genericButtonActionListeners);
        dialogForfeit = dialogContainerForfeit.getDialog();

        // Debug log
        dialogContainerDebugLog = new DialogContainerDebugLog(logHelper);
        dialogDebugLog = dialogContainerDebugLog.getDialog();
    }

    private void startGame(int playerCount, ArrayList<String> playerCustomNames, boolean loadFromFile) {

        // If game is not currently running
        if (!controller.getIsGameActive() || loadFromFile) {
            controller.setPlayersCount(playerCount);

            // Set names
            for (int i = 0; i < playerCount; i++) {
                Player player = board.players.get(i + 1);
                player.setCustomName(playerCustomNames.get(i));
                player.setIsPlayerActive(true);
            }
            controller.setIsGameActive(true);

            controller.sendInitGameMessage();
            controller.initialEvaluator();

            macroController = new MacroController(board, controller, logHelper);
        }

        update();
    }

    /**
     * Reset the appearance of all space buttons within the board view.
     */
    private void resetSpaceButtonAppearance() {
        partyModeTimer.stop();
        for (ViewFrameBoard.SpaceButton s : spaceButtons) {
            s.getButton().setBorder(null);
        }
    }

    /**
     * Party!
     */
    private void partyVisuals() {
        class SpaceButtonPartyListener implements ActionListener {
            int startID;
            int endID;

            public SpaceButtonPartyListener() {
                startID = 0;
                endID = 39;
            }

            @Override
            public void actionPerformed(ActionEvent e) {

                if (startID <= 39) {
                    startID++;
                } else {
                    startID = 0;
                }

                if (endID <= 39) {
                    endID++;
                } else {
                    endID = 0;
                }

                SwingHelper.spaceButtonHighlightSpectrum(startID, endID, spaceButtons);
            }
        }

        SpaceButtonPartyListener spaceButtonPartyListener = new SpaceButtonPartyListener();

        partyModeTimer = new Timer(50, spaceButtonPartyListener);
        partyModeTimer.start();
    }

    /**
     * Exit the application.
     */
    private void quitManager() {
        System.exit(0);
    }

    private void saveGame() {
        SerialState gameData = new SerialState();
        gameData.updateData(board, controller, logHelper, currentPlayer, switchboard);

        try {
            FileOutputStream f = new FileOutputStream("test.dat");
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(gameData);
        } catch (IOException e) {
        }
    }

    private void loadGame() {
        SerialState gameData = new SerialState();

        try {
            FileInputStream f = new FileInputStream("test.dat");
            ObjectInputStream o = new ObjectInputStream(f);
            gameData = (SerialState) o.readObject();

            board = gameData.getBoard();
            controller = gameData.getController();
            logHelper = gameData.getLogHelper();
            switchboard = gameData.getSwitchboard();

            logHelper.appendToGameLog("Loaded from file.");

            controller.initialEvaluator();
            update();

        } catch (IOException ignored) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generic ActionListener class for buttons. Effectively forces each button to have an Action that
     * can be referenced later.
     */
    public class GenericButtonActionListener implements ActionListener, Serializable {
        protected ActionsGUI action;
        protected boolean needsPrompt;

        // Default listeners do not require a prompt
        public GenericButtonActionListener(ActionsGUI action) {
            this.action = action;
            needsPrompt = false;
        }

        // For listeners that require a prompt, e.g., Improvements, Mortgage
        public GenericButtonActionListener(ActionsGUI action, boolean needsPrompt) {
            this.action = action;
            this.needsPrompt = needsPrompt;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switchboard.setOrigin(e.getSource());

            // Trigger the corresponding action in the switchboard
            switchboard.actionHandler(action, currentSpaceButtonSelection);
            update();

            if (needsPrompt) {
                updatePrompt(action);
            }
        }

        public ActionsGUI getAction() {
            return action;
        }
    }

    /**
     * ActionListener class for space buttons within the board view.
     */
    public class SpaceButtonActionListener implements ActionListener {

        protected int id;

        public SpaceButtonActionListener(int id) {
            this.id = id;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentSpaceButtonSelection = id;
            update();
        }

    }

    /**
     * Special ActionListener class for starting the game.
     */
    public class StartGameButtonActionListener implements ActionListener, Serializable {

        @Override
        public void actionPerformed(ActionEvent e) {
            startGame(playerCount, playerCustomNames, false);

            dialogStartGame.setVisible(false);
        }

        protected void setPlayerCount(int inputPlayerCount) {
            playerCount = inputPlayerCount;
        }

        protected void setPlayerCustomNames(ArrayList<String> inputPlayerCustomNames) {
            playerCustomNames = inputPlayerCustomNames;
        }

    }

    /**
     * ActionListener class for GameEditor actions. Communicates with a GameEditorControl object directly.
     */
    public class GameEditorActionListener implements ActionListener {

        GameEditorActions action;
        Player player;

        public GameEditorActionListener(GameEditorActions action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            player = board.players.get(dialogContainerGameEditor.getSelectedPlayerID());
            switch (action) {
                case JAIL -> gameEditorController.jailPlayer(player);
                case UNJAIL -> gameEditorController.unjailPlayer(player);
                case GIVE_1000 -> gameEditorController.give1000(player);
                case DEDUCT_1000 -> gameEditorController.deduct1000(player);
                case UNLOCK_ROLL_DICE -> viewFrameControl.setStateOfActionButton(ActionsGUI.CONTROLS_ROLLDICE, true);
                case UNLOCK_END_TURN -> viewFrameControl.setStateOfActionButton(ActionsGUI.CONTROLS_ENDTURN, true);
                case GIVE_ALL_PROPERTIES -> gameEditorController.giveAllProperties(player);
                case RANDOMLY_DISTRIBUTE_ALL_PROPERTIES -> gameEditorController.randomlyDistributeAllProperties();
            }
            if (action != GameEditorActions.UNLOCK_END_TURN && action != GameEditorActions.UNLOCK_ROLL_DICE) {
                update();
            }
        }

        public GameEditorActions getAction() {
            return action;
        }
    }

    /**
     * ActionListener class for items in the menu bar.
     */
    private class MenuActionListener implements ActionListener {

        MenuActions action;

        public MenuActionListener(MenuActions action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (action) {
                case FILE_NEW_GAME -> updateDialogAppearance(dialogStartGame);
                case FILE_QUIT -> quitManager();
                case EDIT_GAME_EDITOR -> {
                    updateDialogAppearance(dialogGameEditor);
                    logHelper.appendToGameLog("Game editor was opened!");
                    update();
                }
                case VIEW_ABOUT -> updateDialogAppearance(dialogAbout);
                case VIEW_DEBUG_LOG -> updateDialogAppearance(dialogDebugLog);
            }
        }
    }

}
