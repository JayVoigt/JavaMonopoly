package cc.jayv.monopoly3;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jay
 */
public class DynamicView {

	JFrame mainFrame;

	JInternalFrame boardFrame;
	ControlFrame controlFrame;
	InfoFrame infoFrame;
	SpaceSelectionArea spaceSelectionArea;

	ArrayList<JButton> spaceButtons;

	LogicSwitchboard switchboard;
	GameLogicController controller;
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

	private enum LocalActions {
		START_GAME
	}

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
		mainFrame.add(boardFrame, "cell 0 0, x 0, y 0, width 1000, height 1000");
		mainFrame.add(infoFrame.getFrame(), "cell 1 0, x 1000, y 0, width 308, height 600");
		mainFrame.add(controlFrame.getFrame(), "cell 1 1, x 1000, y 600, width 308, height 400");
		mainFrame.pack();
		mainFrame.setVisible(true);

		boardFrame.setFrameIcon(SwingHelper.getImageIconFromResource("/board.png"));
		boardFrame.setVisible(true);

		spaceSelectionBorder = SwingHelper.createBorderStyleHighlight(java.awt.Color.MAGENTA, true);

		initDialogs();
	}

	public void update() {
		spaceSelectionArea.update(board, currentSpaceButtonSelection);

		for (JButton b : spaceButtons) {
			if (spaceButtons.indexOf(b) == currentSpaceButtonSelection) {
				b.setBorder(spaceSelectionBorder);
			}
			else {
				b.setBorder(null);
			}
		}

	}

	private void initGUIComponents() {
		spaceButtons = new ArrayList<>();

		// Core components
		mainFrame = new JFrame();
		boardFrame = boardFrameCreator();
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
		menuFileNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		menuFile.add(menuFileNewGame);

		JMenuItem menuFileLoadGame = new JMenuItem("Load Game...", SwingHelper.getImageIconFromResource(("/floppygame.png")));
		menuFile.add(menuFileLoadGame);

		JMenuItem menuFileSaveGame = new JMenuItem("Load Game...", SwingHelper.getImageIconFromResource(("/floppygame.png")));
		menuFile.add(menuFileSaveGame);

		JMenuItem menuFileQuit = new JMenuItem("Quit", SwingHelper.getImageIconFromResource(("/red-x.png")));
		menuFile.add(menuFileQuit);

		JMenuItem menuViewAbout = new JMenuItem("About", SwingHelper.getImageIconFromResource("/robot.png"));
		menuViewAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogAbout.setVisible(true);
			}
		});
		menuView.add(menuViewAbout);

		// Ready
		menuBar.setVisible(true);
		mainFrame.setJMenuBar(menuBar);
	}

	private void initControlFrame() {
		ArrayList<ButtonActionListener> buttonActionListeners = new ArrayList<>();

		buttonActionListeners.add(new ButtonActionListener(Actions.CONTROLS_ROLLDICE));
		buttonActionListeners.add(new ButtonActionListener(Actions.CONTROLS_ENDTURN));
		buttonActionListeners.add(new ButtonActionListener(Actions.CONTROLS_SHOW_IMPROVEMENTS, true));

		controlFrame.initButtons(buttonActionListeners);
	}

	private JInternalFrame boardFrameCreator() {
		JInternalFrame frame = new JInternalFrame();

		frame.setSize(1000, 1000);
		frame.setLayout(null);
		frame.setVisible(true);

		JScrollPane gameLogScrollPane = new JScrollPane();
		JTextArea gameLogTextArea = new JTextArea();

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
			spaceButtons.add(new JButton());
		}

		int posX, posY, sizeX, sizeY;

		for (JButton b : spaceButtons) {

			b.addActionListener(new spaceButtonActionHandler());

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

			b.setBounds(posX, posY, sizeX, sizeY);

			// Set button to transparent - is still clickable but not visible
			java.awt.Color transparentColor = new java.awt.Color(0, 0, 0, 0);
			b.setBackground(transparentColor);

			// General button appearance
			b.setBorder(null);
			b.setBorderPainted(true);
			b.setFocusable(false);
			b.setVisible(true);
			b.setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));

			frame.add(b);
		}	// end button creation

		JLabel boardImage = new JLabel();
		boardImage.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/board-px-template.png"))));
		boardImage.setBounds(0, 0, 960, 960);

		frame.add(boardImage);

		return frame;
	}

	private JInternalFrame infoFrameCreator() {
		JInternalFrame frame = new JInternalFrame();
		frame.setSize(400, 700);
		frame.setVisible(true);

		return frame;
	}

	public class spaceButtonActionHandler implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSpaceButtonSelection = spaceButtons.indexOf(e.getSource());
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
		buttonPropertiesList.clear();
		buttonPropertiesList.trimToSize();

		// Purchase property
		viewDialog = new ViewDialog("Property", "/properties.png");
		buttonPropertiesList.add(new ButtonProperties("Purchase", "/money.png", new ButtonActionListener(Actions.PROPERTY_PURCHASE), "width 150"));
		buttonPropertiesList.add(new ButtonProperties("Auction", "/auction.png", new ButtonActionListener(Actions.PROPERTY_AUCTION), "width 150"));
		dialogPurchaseProperty = viewDialog.createDialogUserPrompt(buttonPropertiesList, "Property information here");
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
		dialogGameEditor.setVisible(true);

		StartGameDialog startGameDialog = new StartGameDialog();
		StartGameButtonActionListener startGameButtonActionListener = new StartGameButtonActionListener();
		startGameDialog.attachStartGameActionListener(startGameButtonActionListener);
		dialogStartGame = startGameDialog.getDialog();	// need better variable names
		dialogStartGame.setVisible(true);

	}

	public class StartGameButtonActionListener implements ActionListener {

		int playerCount;
		ArrayList<String> playerCustomNames;

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("test");
			System.out.println("players: " + playerCount);
			for (String s : playerCustomNames) {
				System.out.println(s);
			}
			startGame(playerCount, playerCustomNames);
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
			}

			controller.setIsGameActive(true);
		}

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

	private class MenuActionListener implements ActionListener {

		MenuActions action;
		public MenuActionListener(MenuActions action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

		}
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
