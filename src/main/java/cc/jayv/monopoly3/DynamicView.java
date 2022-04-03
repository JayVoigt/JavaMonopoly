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
	JInternalFrame controlFrame;
	JInternalFrame infoFrame;

	ArrayList<JInternalFrame> internalFrames;
	ArrayList<JButton> spaceButtons;

	GUISwitchBoard guiSwitchBoard;
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
		guiSwitchBoard = new GUISwitchBoard(controller);

		initGUIComponents();

		mainFrame.setLayout(new MigLayout());
		mainFrame.add(boardFrame, "cell 0 0");
		mainFrame.add(controlFrame, "cell 1 1");
		mainFrame.setSize(1280, 1280);

		mainFrame.setVisible(true);
		boardFrame.setVisible(true);

		initDialogs();
	}

	public void update() {
	}

	private void initGUIComponents() {
		internalFrames = new ArrayList<>();
		spaceButtons = new ArrayList<>();

		mainFrame = new JFrame();
		boardFrame = boardFrameCreator();
		controlFrame = controlsCreator();
		infoFrame = new JInternalFrame();

		internalFrames.add(boardFrame);
		internalFrames.add(controlFrame);
		internalFrames.add(infoFrame);
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
				posY = 840 - (80 * cardinalPosition);
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

	private JInternalFrame controlsCreator() {
		JInternalFrame frame = new JInternalFrame();
		frame.setSize(400, 400);

		return frame;
	}


	public class spaceButtonActionHandler implements ActionListener {
		
		public spaceButtonActionHandler() {
			
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSpaceButtonSelection = spaceButtons.indexOf(e.getSource());
			System.out.println(currentSpaceButtonSelection);
			update();
		}
		
	}

	private void initDialogs() {
		DialogCreator dialogCreator;
		ArrayList<DialogCreator.ButtonContents> buttonContentsList = new ArrayList<>();

		// Jail
		dialogCreator = new DialogCreator("Jail", "/jail.png", guiSwitchBoard);
		buttonContentsList.add(new DialogCreator.ButtonContents("Post bail ($50)", "/money.png", GUISwitchBoard.Actions.JAIL_POSTBAIL, "cell 0 2, width 150"));
		buttonContentsList.add(new DialogCreator.ButtonContents("Roll for doubles", "/dice-icon.png", GUISwitchBoard.Actions.JAIL_ROLLDOUBLES, "cell 1 2, width 150"));
		buttonContentsList.add(new DialogCreator.ButtonContents("Use Get Out of Jail Free Card", "/goojfc.png", GUISwitchBoard.Actions.JAIL_USEGOOJFC, "cell 0 3, span 2, width 308"));
		dialogJail = dialogCreator.createDialogUserPrompt(buttonContentsList, "You may post bail for $50, attempt to roll for doubles up to a maximum of 3 times, or use a Get Out of Jail Free Card.");
		dialogCreator.initDialogForView(dialogJail);
		buttonContentsList.clear();
		buttonContentsList.trimToSize();

		// Purchase property
		dialogCreator = new DialogCreator("Property", "/properties.png", guiSwitchBoard);
		buttonContentsList.add(new DialogCreator.ButtonContents("Purchase", "/money.png", GUISwitchBoard.Actions.PROPERTY_PURCHASE, "width 150"));
		buttonContentsList.add(new DialogCreator.ButtonContents("Auction", "/auction.png", GUISwitchBoard.Actions.PROPERTY_AUCTION, "width 150"));
		dialogPurchaseProperty = dialogCreator.createDialogUserPrompt(buttonContentsList, "Property information here");
		dialogCreator.initDialogForView(dialogPurchaseProperty);
		buttonContentsList.clear();
		buttonContentsList.trimToSize();

		// Improvements
		dialogCreator = new DialogCreator("Improvements", "/improvements.png", guiSwitchBoard);
		buttonContentsList.add(new DialogCreator.ButtonContents("Build a house", "/house.png", GUISwitchBoard.Actions.IMPROVEMENTS_BUILD_HOUSE, "width 150"));
		buttonContentsList.add(new DialogCreator.ButtonContents("Sell a house", "/house.png", GUISwitchBoard.Actions.IMPROVEMENTS_SELL_HOUSE, "wrap, width 150"));
		buttonContentsList.add(new DialogCreator.ButtonContents("Build a hotel", "/hotel.png", GUISwitchBoard.Actions.IMPROVEMENTS_BUILD_HOTEL, "width 150"));
		buttonContentsList.add(new DialogCreator.ButtonContents("Sell a hotel", "/hotel.png", GUISwitchBoard.Actions.IMPROVEMENTS_SELL_HOTEL, "width 150"));
		dialogImprovements = dialogCreator.createDialogUserPrompt(buttonContentsList, "You do not own all properties in this set.");
		dialogCreator.initDialogForView(dialogImprovements);
		buttonContentsList.clear();
		buttonContentsList.trimToSize();

	}

	public static void main(String args[]) {
		DynamicView localView = new DynamicView();
	}

}
