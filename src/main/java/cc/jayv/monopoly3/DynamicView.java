/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cc.jayv.monopoly3;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
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

	JInternalFrame boardFrame;
	ControlFrame controlFrame;
	JInternalFrame infoFrame;

	ArrayList<JButton> spaceButtons;

	LogicSwitchboard switchboard;
	GameLogicController controller;
	Board board;
	LogHelper logHelper;

	JDialog dialogJail;
	JDialog dialogPurchaseProperty;
	JDialog dialogImprovements;
	
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

		logHelper = new LogHelper();
		controller = new GameLogicController(board, logHelper);
		switchboard = new LogicSwitchboard(controller);

		initGUIComponents();

		mainFrame.setLayout(new MigLayout());
		initControlFrame();
		mainFrame.add(boardFrame, "cell 0 0, x 0, y 0, width 1000, height 1000");
		mainFrame.add(infoFrame, "cell 1 0, x 1000, y 0, width 308, height 600");
		mainFrame.add(controlFrame.getFrame(), "cell 1 1, x 1000, y 600, width 308, height 400");
		mainFrame.pack();

		mainFrame.setVisible(true);
		boardFrame.setVisible(true);

		initDialogs();
	}

	public void update() {
	}

	private void initGUIComponents() {
		spaceButtons = new ArrayList<>();

		mainFrame = new JFrame();
		boardFrame = boardFrameCreator();
		controlFrame = new ControlFrame();
		infoFrame = infoFrameCreator();
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

		JButton testButton = new JButton();
		testButton.setBounds((960 - 120), (960 - 120), 120, 120);

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
			b.setText(Integer.toString(index));
			b.setFocusable(false);
			b.setVisible(true);
			frame.add(b);
		}	// end button creation

		JLabel boardImage = new JLabel();
		boardImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/board-px-template.png")));
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
			System.out.println(currentSpaceButtonSelection);
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
