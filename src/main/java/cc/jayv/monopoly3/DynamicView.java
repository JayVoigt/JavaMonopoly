package cc.jayv.monopoly3;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jay
 */
public class DynamicView {

	JFrame mainFrame;

	BoardFrame boardFrame;
	ControlFrame controlFrame;
	InfoFrame infoFrame;

	ArrayList<BoardFrame.SpaceButton> spaceButtons;

	final LogicSwitchboard switchboard;
	final GameLogicController controller;
	Board board;
	LogHelper logHelper;

	JDialog dialogJail;
	JDialog dialogPurchaseProperty;
	JDialog dialogImprovements;

	JDialog dialogStartGame;
	JDialog dialogGameEditor;
	JDialog dialogAbout;

	JMenuBar menuBar;
	JMenu menuFile;
	JMenu menuEdit;
	JMenu menuView;

	Border spaceSelectionBorder;
	Player currentPlayer;

	static int currentSpaceButtonSelection;

	public DynamicView() {

		// Set to FlatLaf appearance
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());

		} catch (UnsupportedLookAndFeelException e) {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
				Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
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
		switchboard = new LogicSwitchboard(controller);

		initGUIComponents();
		initMenuBar();

		// Initialize main frame
		mainFrame.setLayout(new MigLayout());
		initControlFrame();
		mainFrame.add(boardFrame.getInternalFrame(), "cell 0 0, x 0, y 0, width 1000, height 1000");
		mainFrame.add(infoFrame.getFrame(), "cell 1 0, x 1000, y 0, width 308, height 600");
		mainFrame.add(controlFrame.getFrame(), "cell 1 1, x 1000, y 600, width 308, height 400");
		mainFrame.pack();
		mainFrame.setVisible(true);

		spaceSelectionBorder = SwingHelper.createBorderStyleHighlight(java.awt.Color.MAGENTA, true);

		initDialogs();
	}

	public void update() {
		currentPlayer = board.players.get(board.getCurrentPlayerID());
		boardFrame.update(board, currentSpaceButtonSelection);

		controlFrame.update(board);
		infoFrame.update(board);
		updateGuiMandatoryDialogs();
	}

	private void updateGuiMandatoryDialogs() {
		dialogPurchaseProperty.setVisible(currentPlayer.getRequiredDecisionPropertyAction());
		dialogJail.setVisible(currentPlayer.getRequiredDecisionPostedBail());

		if (dialogPurchaseProperty.isVisible()) {
			controlFrame.setStateOfActionButton(Actions.CONTROLS_ENDTURN, false);
			controlFrame.setStateOfActionButton(Actions.CONTROLS_ROLLDICE, false);
		}
	}

	private void initGUIComponents() {

		ArrayList<spaceButtonActionHandler> spaceButtonActionHandlers = new ArrayList<>();

		for ( int i = 0 ; i < 40 ; i++ ) {
			spaceButtonActionHandlers.add(new spaceButtonActionHandler(i));
		}

		// Core components
		mainFrame = new JFrame();
		boardFrame = new BoardFrame(logHelper, spaceButtonActionHandlers);
		spaceButtons = boardFrame.getSpaceButtonArrayList();
		controlFrame = new ControlFrame();
		infoFrame = new InfoFrame();
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
		menuFile.add(menuFileLoadGame);

		JMenuItem menuFileSaveGame = new JMenuItem("Load Game...", SwingHelper.getImageIconFromResource(("/floppygame.png")));
		menuFile.add(menuFileSaveGame);

		JMenuItem menuFileQuit = new JMenuItem("Quit", SwingHelper.getImageIconFromResource(("/red-x.png")));
		menuFileQuit.addActionListener(new MenuActionListener(MenuActions.FILE_QUIT));
		menuFile.add(menuFileQuit);

		JMenuItem menuEditGameEditor = new JMenuItem("Game Editor...", SwingHelper.getImageIconFromResource("/matrix-anim.gif"));
		menuEditGameEditor.addActionListener(new MenuActionListener(MenuActions.EDIT_GAME_EDITOR));
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
				case FILE_NEW_GAME -> dialogStartGame.setVisible(true);
				case FILE_QUIT -> quitManager();
				case EDIT_GAME_EDITOR -> {
					dialogGameEditor.setVisible(true);
					logHelper.appendToGameLog("Game log was opened!");
				}
				case ABOUT -> dialogAbout.setVisible(true);
			}
		}
	}

	private void initControlFrame() {
		ArrayList<ButtonActionListener> buttonActionListeners = new ArrayList<>();

		buttonActionListeners.add(new ButtonActionListener(Actions.CONTROLS_ROLLDICE));
		buttonActionListeners.add(new ButtonActionListener(Actions.CONTROLS_ENDTURN));
		buttonActionListeners.add(new ButtonActionListener(Actions.CONTROLS_SHOW_IMPROVEMENTS, true));

		controlFrame.initButtons(buttonActionListeners);
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
		ViewDialog viewDialog;
		ArrayList<ButtonProperties> buttonPropertiesList = new ArrayList<>();

		// Jail
		viewDialog = new ViewDialog("Jail", "/jail.png");
		buttonPropertiesList.add(new ButtonProperties("Post bail ($50)", "/money.png", new ButtonActionListener(Actions.JAIL_POSTBAIL), "cell 0 2, width 150"));
		buttonPropertiesList.add(new ButtonProperties("Roll for doubles", "/dice-icon.png", new ButtonActionListener(Actions.JAIL_ROLLDOUBLES), "cell 1 2, width 150"));
		buttonPropertiesList.add(new ButtonProperties("Use Get Out of Jail Free Card", "/goojfc.png", new ButtonActionListener(Actions.JAIL_USEGOOJFC), "cell 0 3, span 2, width 308"));
		dialogJail = viewDialog.createDialogUserPrompt(buttonPropertiesList, "You may post bail for $50, attempt to roll for doubles up to a maximum of 3 times, or use a Get Out of Jail Free Card.");
		dialogJail.pack();
		buttonPropertiesList.clear();
		buttonPropertiesList.trimToSize();

		// Purchase property
		viewDialog = new ViewDialog("Property", "/properties.png");
		buttonPropertiesList.add(new ButtonProperties("Purchase", "/money.png", new ButtonActionListener(Actions.PROPERTY_PURCHASE), "width 150"));
		buttonPropertiesList.add(new ButtonProperties("Auction", "/auction.png", new ButtonActionListener(Actions.PROPERTY_AUCTION), "width 150"));
		dialogPurchaseProperty = viewDialog.createDialogUserPrompt(buttonPropertiesList, "Property information here");
		dialogPurchaseProperty.pack();
		buttonPropertiesList.clear();
		buttonPropertiesList.trimToSize();

		// Improvements
		viewDialog = new ViewDialog("Improvements", "/improvements.png");
		buttonPropertiesList.add(new ButtonProperties("Build a house", "/house.png", new ButtonActionListener(Actions.IMPROVEMENTS_BUILD_HOUSE), "width 150"));
		buttonPropertiesList.add(new ButtonProperties("Sell a house", "/house.png", new ButtonActionListener(Actions.IMPROVEMENTS_SELL_HOUSE), "wrap, width 150"));
		buttonPropertiesList.add(new ButtonProperties("Build a hotel", "/hotel.png", new ButtonActionListener(Actions.IMPROVEMENTS_BUILD_HOTEL), "width 150"));
		buttonPropertiesList.add(new ButtonProperties("Sell a hotel", "/hotel.png", new ButtonActionListener(Actions.IMPROVEMENTS_SELL_HOTEL), "width 150"));
		dialogImprovements = viewDialog.createDialogUserPrompt(buttonPropertiesList, "You do not own all properties in this set.");
		buttonPropertiesList.clear();
		buttonPropertiesList.trimToSize();

		// About
		dialogAbout = new AboutDialog().getDialog();

		// Game editor
		dialogGameEditor = new GameEditorDialog().getDialog();

		StartGameDialog startGameDialog = new StartGameDialog();
		StartGameButtonActionListener startGameButtonActionListener = new StartGameButtonActionListener();
		startGameDialog.attachStartGameActionListener(startGameButtonActionListener);
		dialogStartGame = startGameDialog.getDialog();	// need better variable names

	}

	public class StartGameButtonActionListener implements ActionListener {

		int playerCount;
		ArrayList<String> playerCustomNames;

		@Override
		public void actionPerformed(ActionEvent e) {
			startGame(playerCount, playerCustomNames);

			dialogStartGame.setVisible(false);
		}

		protected void setPlayerCount(int playerCount) {
			this.playerCount = playerCount;
		}

		protected void setPlayerCustomNames(ArrayList<String> playerCustomNames) {
			this.playerCustomNames = playerCustomNames;
		}

	}

	private void startGame(int playerCount, ArrayList<String> playerCustomNames) {

		// If game is not currently running
		if (!controller.getIsGameActive()) {
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

	public class ButtonActionListener implements ActionListener {

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

	private void quitManager() {
		System.exit(0);
	}

	private void updatePrompt(Actions action) {
		switch(action) {
			case CONTROLS_SHOW_IMPROVEMENTS -> ViewDialog.initDialogForView(dialogImprovements);
			case GAME_SHOW_JAIL -> ViewDialog.initDialogForView(dialogJail);
			case GAME_SHOW_PURCHASE -> ViewDialog.initDialogForView(dialogPurchaseProperty);
		}
	}

	public static void main(String args[]) {
		DynamicView localView = new DynamicView();
	}

}
