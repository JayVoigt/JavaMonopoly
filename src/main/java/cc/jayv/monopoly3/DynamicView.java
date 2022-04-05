/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cc.jayv.monopoly3;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
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

	public enum Actions {
		CONTROLS_ROLLDICE,
		CONTROLS_ENDTURN,

		// Property purchase dialog
		PROPERTY_PURCHASE,
		PROPERTY_AUCTION,

		// Mortgaging dialog
		PROPERTY_MORTGAGE,
		PROPERTY_UNMORTGAGE,

		// Show owned properties
		VIEW_SHOWPROPERTIES,

		// Improvements dialog
		IMPROVEMENTS_BUILD_HOUSE,
		IMPROVEMENTS_SELL_HOUSE,
		IMPROVEMENTS_BUILD_HOTEL,
		IMPROVEMENTS_SELL_HOTEL,

		// Jail dialog
		JAIL_POSTBAIL,
		JAIL_ROLLDOUBLES,
		JAIL_USEGOOJFC,
	}

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
		mainFrame.add(boardFrame, "cell 0 0, x 0, y 0, width 1000, height 1000");
		mainFrame.add(infoFrame, "cell 1 0, x 1000, y 0, width 400, height 600");
		mainFrame.add(controlFrame, "cell 1 1, x 1000, y 600, width 400, height 400");
		//mainFrame.setSize(1280, 1280);
		mainFrame.pack();

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
		controlFrame = controlFrameCreator();
		infoFrame = infoFrameCreator();

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

	private JInternalFrame controlFrameCreator() {
		JInternalFrame frame = new JInternalFrame();
		frame.setLayout(new MigLayout());
		frame.setSize(400, 400);
		frame.setVisible(true);

		JLabel staticLabelCurrentPlayer = new JLabel();
		staticLabelCurrentPlayer.setIcon(getIconFromResource("/player-generic.png"));
		staticLabelCurrentPlayer.setText("Current player");
		formatJLabel(staticLabelCurrentPlayer, true);
		frame.add(staticLabelCurrentPlayer, "align left, cell 0 0, width 150");

		JSeparator separator = new JSeparator();
		frame.add(separator, "cell 1 0");

		JLabel labelCurrentPlayer = new JLabel();
		labelCurrentPlayer.setText("n/a");
		formatJLabel(labelCurrentPlayer, false);
		frame.add(labelCurrentPlayer, "align right, cell 2 0, width 150, wrap");

		return frame;
	}

	private void formatJLabel(JLabel label, boolean isBold) {
		if (isBold) {
			label.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
		}
		else {
			label.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
		}
	}

	private JInternalFrame infoFrameCreator() {
		JInternalFrame frame = new JInternalFrame();
		frame.setSize(400, 700);
		frame.setVisible(true);

		return frame;
	}

	public ImageIcon getIconFromResource(String resource) {
		if (getClass().getResource(resource) != null) {
			return new ImageIcon(getClass().getResource(resource));
		}
		else {
			return new ImageIcon(getClass().getResource("/error.png"));
		}
	}

	public class spaceButtonActionHandler implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSpaceButtonSelection = spaceButtons.indexOf(e.getSource());
			System.out.println(currentSpaceButtonSelection);
			update();
		}
		
	}

	public class GenericButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(e.getSource());
		}
	}

	private void initDialogs() {
		DialogCreator dialogCreator;
		ArrayList<ButtonProperties> buttonPropertiesList = new ArrayList<>();

		// Jail
		dialogCreator = new DialogCreator("Jail", "/jail.png");
		buttonPropertiesList.add(new ButtonProperties("Post bail ($50)", "/money.png", new DialogButtonActionListener(Actions.JAIL_POSTBAIL), "cell 0 2, width 150"));
		buttonPropertiesList.add(new ButtonProperties("Roll for doubles", "/dice-icon.png", new DialogButtonActionListener(Actions.JAIL_ROLLDOUBLES), "cell 1 2, width 150"));
		buttonPropertiesList.add(new ButtonProperties("Use Get Out of Jail Free Card", "/goojfc.png", new DialogButtonActionListener(Actions.JAIL_USEGOOJFC), "cell 0 3, span 2, width 308"));
		dialogJail = dialogCreator.createDialogUserPrompt(buttonPropertiesList, "You may post bail for $50, attempt to roll for doubles up to a maximum of 3 times, or use a Get Out of Jail Free Card.");
		dialogCreator.initDialogForView(dialogJail);
		buttonPropertiesList.clear();
		buttonPropertiesList.trimToSize();

		// Purchase property
		dialogCreator = new DialogCreator("Property", "/properties.png");
		buttonPropertiesList.add(new ButtonProperties("Purchase", "/money.png", new DialogButtonActionListener(Actions.PROPERTY_PURCHASE), "width 150"));
		buttonPropertiesList.add(new ButtonProperties("Auction", "/auction.png", new DialogButtonActionListener(Actions.PROPERTY_AUCTION), "width 150"));
		dialogPurchaseProperty = dialogCreator.createDialogUserPrompt(buttonPropertiesList, "Property information here");
		dialogCreator.initDialogForView(dialogPurchaseProperty);
		buttonPropertiesList.clear();
		buttonPropertiesList.trimToSize();

		// Improvements
		dialogCreator = new DialogCreator("Improvements", "/improvements.png");
		buttonPropertiesList.add(new ButtonProperties("Build a house", "/house.png", new DialogButtonActionListener(Actions.IMPROVEMENTS_BUILD_HOUSE), "width 150"));
		buttonPropertiesList.add(new ButtonProperties("Sell a house", "/house.png", new DialogButtonActionListener(Actions.IMPROVEMENTS_SELL_HOUSE), "wrap, width 150"));
		buttonPropertiesList.add(new ButtonProperties("Build a hotel", "/hotel.png", new DialogButtonActionListener(Actions.IMPROVEMENTS_BUILD_HOTEL), "width 150"));
		buttonPropertiesList.add(new ButtonProperties("Sell a hotel", "/hotel.png", new DialogButtonActionListener(Actions.IMPROVEMENTS_SELL_HOTEL), "width 150"));
		dialogImprovements = dialogCreator.createDialogUserPrompt(buttonPropertiesList, "You do not own all properties in this set.");
		dialogCreator.initDialogForView(dialogImprovements);
		buttonPropertiesList.clear();
		buttonPropertiesList.trimToSize();

	}

	public class DialogButtonActionListener implements ActionListener {

		Actions action;
		public DialogButtonActionListener(Actions action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(e.getSource() + " : " + action);
		}
	}

	public static void main(String args[]) {
		DynamicView localView = new DynamicView();
	}

}
