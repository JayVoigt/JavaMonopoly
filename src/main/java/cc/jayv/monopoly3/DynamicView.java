package cc.jayv.monopoly3;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jay
 */
public class DynamicView implements Serializable  {

	// Core components - GUI
	JFrame mainFrame;

	ViewFrameBoard viewFrameBoard;
	ViewFrameControl viewFrameControl;
	ViewFrameInformation viewFrameInformation;

	ArrayList<ViewFrameBoard.SpaceButton> spaceButtons;

	ArrayList<ButtonActionListener> buttonActionListeners;

	// Core components - logic and control
	GameLogicSwitchboard switchboard;
	GameLogicController controller;
	Board board;
	LogHelper logHelper;

	// Dialog containers
	DialogContainerPurchaseProperty dialogContainerPurchaseProperty;
	DialogContainerGameEditor dialogContainerGameEditor;
	DialogContainerStartGame dialogContainerStartGame;
	DialogContainerImprovements dialogContainerImprovements;
	DialogContainerJail dialogContainerJail;

	GameEditorController gameEditorController;

	// Dialogs
	JDialog dialogJail;
	JDialog dialogPurchaseProperty;
	JDialog dialogImprovements;
	JDialog dialogStartGame;
	JDialog dialogGameEditor;
	JDialog dialogAbout;

	// Root-level menus
	JMenuBar menuBar;
	JMenu menuFile;
	JMenu menuEdit;
	JMenu menuView;

	Border spaceSelectionBorder;

	// Reference variables
	static int currentSpaceButtonSelection;
	Player currentPlayer;

	// Objects required to be class-wide for serialization
	int playerCount;
	ArrayList<String> playerCustomNames;

	JavaMonopoly jm;

	public DynamicView(JavaMonopoly jm) {

		this.jm = jm;
		// Set to FlatLaf appearance
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());

		} catch (UnsupportedLookAndFeelException e) {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
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

		spaceSelectionBorder = SwingHelper.createBorderStyleHighlight(java.awt.Color.MAGENTA, true);

		initDialogs();

	}

	public void update() {
		currentPlayer = board.players.get(board.getCurrentPlayerID());
		viewFrameBoard.update(board, currentSpaceButtonSelection);

		viewFrameControl.update(board);
		viewFrameInformation.update(board);
		updateGuiMandatoryDialogs();
		updateGuiOptionalDialogs();
	}

	private void updateGuiMandatoryDialogs() {

		// Jail
		if (!currentPlayer.getInitialJailTurn() && currentPlayer.getIsJailed()) {
			 dialogContainerJail.update(board, currentPlayer);
			 showDialog(dialogJail, true);
		}
		else {
			showDialog(dialogJail, false);
		}

		// Purchase decision
		if (currentPlayer.getRequiredDecisionPropertyAction()) {
			dialogContainerPurchaseProperty.update(board, currentPlayer.getCurrentPosition());
		}
		showDialog(dialogPurchaseProperty, currentPlayer.getRequiredDecisionPropertyAction());

		// Disable control buttons when mandatory action dialogs visible
		// IDE-suggested code
		for (JDialog jDialog : Arrays.asList(dialogPurchaseProperty, dialogJail, dialogPurchaseProperty)) {
			if (jDialog.isVisible()) {
				disableControlButtons();
			}
		}
	}

	private void updateGuiOptionalDialogs() {
		if (dialogImprovements.isVisible()) {
			dialogContainerImprovements.update(board, currentPlayer, currentSpaceButtonSelection, controller);
		}
	}

	private void disableControlButtons() {
		viewFrameControl.setStateOfActionButton(Actions.CONTROLS_ENDTURN, false);
		viewFrameControl.setStateOfActionButton(Actions.CONTROLS_ROLLDICE, false);
	}

	private void initGUIComponents() {

		ArrayList<spaceButtonActionHandler> spaceButtonActionHandlers = new ArrayList<>();

		for ( int i = 0 ; i < 40 ; i++ ) {
			spaceButtonActionHandlers.add(new spaceButtonActionHandler(i));
		}

		// Core components
		mainFrame = new JFrame();
		viewFrameBoard = new ViewFrameBoard(logHelper, spaceButtonActionHandlers);
		spaceButtons = viewFrameBoard.getSpaceButtonArrayList();
		viewFrameControl = new ViewFrameControl();
		viewFrameInformation = new ViewFrameInformation();
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();

		// Root level menus
		menuFile = new JMenu();
		menuFile.setText("File");
		menuBar.add(menuFile);

		menuEdit = new JMenu();
		menuEdit.setText("Edit");
		menuBar.add(menuEdit);

		menuView = new JMenu();
		menuView.setText("View");
		menuBar.add(menuView);

		// File
		JMenuItem menuFileNewGame = new JMenuItem("New Game...", SwingHelper.getImageIconFromResource("/newgame.png"));
		menuFileNewGame.addActionListener(new MenuActionListener(MenuActions.FILE_NEW_GAME));
		menuFile.add(menuFileNewGame);

		JMenuItem menuFileLoadGame = new JMenuItem("Load Game...", SwingHelper.getImageIconFromResource(("/floppygame.png")));
		menuFileLoadGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadGame();
			}
		});
		menuFile.add(menuFileLoadGame);

		JMenuItem menuFileSaveGame = new JMenuItem("Save Game...", SwingHelper.getImageIconFromResource(("/floppygame.png")));
		menuFileSaveGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveGame();
			}
		});
		menuFile.add(menuFileSaveGame);

		JMenuItem menuFileQuit = new JMenuItem("Quit", SwingHelper.getImageIconFromResource(("/red-x.png")));
		menuFileQuit.addActionListener(new MenuActionListener(MenuActions.FILE_QUIT));
		menuFile.add(menuFileQuit);

		JMenuItem menuEditGameEditor = new JMenuItem("Game Editor...", SwingHelper.getImageIconFromResource("/matrix-anim.gif"));
		menuEditGameEditor.addActionListener(new MenuActionListener(MenuActions.EDIT_GAME_EDITOR));
		menuEditGameEditor.setForeground(java.awt.Color.red);
		menuEdit.add(menuEditGameEditor);

		JMenuItem menuViewAbout = new JMenuItem("About", SwingHelper.getImageIconFromResource("/robot.png"));
		menuViewAbout.addActionListener(new MenuActionListener(MenuActions.ABOUT));
		menuView.add(menuViewAbout);

		// Ready
		menuBar.setVisible(true);
		mainFrame.setJMenuBar(menuBar);
	}

	private class MenuActionListener implements ActionListener {

		MenuActions action;
		public MenuActionListener(MenuActions action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (action) {
				case FILE_NEW_GAME -> showDialog(dialogStartGame);
				case FILE_QUIT -> quitManager();
				case EDIT_GAME_EDITOR -> {
					showDialog(dialogGameEditor);
					logHelper.appendToGameLog("Game editor was opened!");
					update();
				}
				case ABOUT -> showDialog(dialogAbout);
			}
		}
	}

	private void showDialog(JDialog dialog) {
		showDialog(dialog, true);
	}

	private void showDialog(JDialog dialog, boolean visible) {
		if (visible) {
			dialog.setLocationRelativeTo(viewFrameBoard.getInternalFrame());
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
		else {
			dialog.setVisible(false);
		}
	}

	private void initButtonActionListeners() {
		// Create all possible ActionListeners for dialogs
		buttonActionListeners = new ArrayList<>();

		for (Actions a : Actions.values()) {

			switch (a) {
				case CONTROLS_SHOW_MORTGAGE, CONTROLS_SHOW_FORFEIT, CONTROLS_SHOW_IMPROVEMENTS -> {
					buttonActionListeners.add(new ButtonActionListener(a, true));
				}
				default -> buttonActionListeners.add(new ButtonActionListener(a, false));
			}

		}
	}

	private void initControlFrame() {
		viewFrameControl.initButtons(buttonActionListeners);
	}

	public class spaceButtonActionHandler implements ActionListener {

		int id;

		public spaceButtonActionHandler(int id) {
			this.id = id;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			currentSpaceButtonSelection = id;
			update();
		}

	}

	private void initDialogs() {
		TemplateDialogGenerator templateDialog;
		ArrayList<TemplateDialogButtonProperties> templateDialogButtonPropertiesList = new ArrayList<>();

		// Jail
		dialogContainerJail = new DialogContainerJail();
		dialogContainerJail.attachActionListeners(buttonActionListeners);
		dialogJail = dialogContainerJail.getDialog();

		// Purchase property
		dialogContainerPurchaseProperty = new DialogContainerPurchaseProperty();
		dialogContainerPurchaseProperty.attachActionListeners(buttonActionListeners);
		dialogPurchaseProperty = dialogContainerPurchaseProperty.getDialog();

		// Improvements
		dialogContainerImprovements = new DialogContainerImprovements();
		dialogContainerImprovements.attachActionListeners(buttonActionListeners);
		dialogImprovements = dialogContainerImprovements.getDialog();

		// About
		dialogAbout = new DialogContainerAbout().getDialog();

		// Game editor
		dialogContainerGameEditor = new DialogContainerGameEditor();
		dialogGameEditor = dialogContainerGameEditor.getDialog();

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

	}

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

	private void startGame(int playerCount, ArrayList<String> playerCustomNames, boolean loadFromFile) {

		// If game is not currently running
		if (!controller.getIsGameActive() || loadFromFile) {
			controller.setPlayersCount(playerCount);

			// Set names
			for ( int i = 0 ; i < playerCount ; i++ ) {
				board.players.get(i + 1).setCustomName(playerCustomNames.get(i));
				board.players.get(i + 1).setIsPlayerActive(true);
			}
			controller.setIsGameActive(true);

			controller.sendInitGameMessage();
			controller.initialEvaluator();
		}

		update();

	}

	public class ButtonActionListener implements ActionListener, Serializable {

		Actions action;
		boolean needsPrompt;

		public ButtonActionListener(Actions action) {
			this.action = action;
			needsPrompt = false;
		}

		public ButtonActionListener(Actions action, boolean needsPrompt) {
			this.action = action;
			this.needsPrompt = needsPrompt;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switchboard.setOrigin(e.getSource());
			switchboard.actionHandler(action, currentSpaceButtonSelection);
			update();

			if (needsPrompt) {
				updatePrompt(action);
			}
		}

		public Actions getAction() {
			return action;
		}
	}

	public class GameEditorActionListener implements ActionListener {

		GameEditorActions action;
		Player player;

		public GameEditorActionListener(GameEditorActions action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			player = board.players.get(dialogContainerGameEditor.getSelectedPlayer());
			switch (action) {
				case JAIL -> gameEditorController.jailPlayer(player);
				case UNJAIL -> gameEditorController.unjailPlayer(player);
				case GIVE_1000 -> gameEditorController.give1000(player);
				case DEDUCT_1000 -> gameEditorController.deduct1000(player);
				case GIVE_ALL_PROPERTIES -> gameEditorController.giveAllProperties(player);
			}
			update();
		}

		public GameEditorActions getAction() {
			return action;
		}
	}

	private void quitManager() {
		System.exit(0);
	}

	private void updatePrompt(Actions action) {
		switch(action) {
			case CONTROLS_SHOW_IMPROVEMENTS -> TemplateDialogGenerator.initDialogForView(dialogImprovements);
			case GAME_SHOW_JAIL -> TemplateDialogGenerator.initDialogForView(dialogJail);
			case GAME_SHOW_PURCHASE -> TemplateDialogGenerator.initDialogForView(dialogPurchaseProperty);
		}
	}

//	public static void main(String args[]) {
//		DynamicView localView = new DynamicView();
//	}

	private void saveGame() {
		ArrayList<Object> gameData = new ArrayList<>();
		gameData.add(board);
		gameData.add(controller);
		gameData.add(logHelper);
		gameData.add(currentPlayer);
		gameData.add(playerCount);
		gameData.add(playerCustomNames);

		try {
			FileOutputStream f = new FileOutputStream("test.dat");
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(gameData);
		} catch (IOException e) {
			System.out.println("a");
		}
	}

	private void loadGame() {
		ArrayList<Object> gameData = new ArrayList<>();

		try {
			FileInputStream f = new FileInputStream("test.dat");
			ObjectInputStream o = new ObjectInputStream(f);
			gameData = (ArrayList<Object>) o.readObject();

			board = (Board) gameData.get(0);
			controller = (GameLogicController) gameData.get(1);
			logHelper = (LogHelper) gameData.get(2);
			currentPlayer = (Player) gameData.get(3);
			playerCount = (int) gameData.get(4);
			playerCustomNames = (ArrayList<String>) gameData.get(5);

			logHelper.appendToGameLog("Loaded from file.");

			startGame(playerCount, playerCustomNames, true);
			update();

		} catch (IOException e) {
			System.out.println("b");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
