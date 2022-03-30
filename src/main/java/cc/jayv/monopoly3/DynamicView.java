/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cc.jayv.monopoly3;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

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
	
	public DynamicView() {
		initComponents();
		
		mainFrame.add(boardFrame);
		mainFrame.setSize(1280, 1280);
		mainFrame.setLayout(null);
		
		mainFrame.setVisible(true);
		boardFrame.setVisible(true);
	}
	
	private void initComponents() {
		internalFrames = new ArrayList<>();
		spaceButtons = new ArrayList<>();
		
		mainFrame = new JFrame();
		boardFrame = boardFrameCreator();
		controlFrame = new JInternalFrame();
		infoFrame = new JInternalFrame();
		
		internalFrames.add(boardFrame);
		internalFrames.add(controlFrame);
		internalFrames.add(infoFrame);
	}
	
	private JInternalFrame boardFrameCreator() {
		JInternalFrame frame = new JInternalFrame();
		
		frame.setSize(1024, 1024);
		frame.setLayout(null);
		frame.setVisible(true);
		
		JButton testButton = new JButton();
		testButton.setBounds((960 - 120), (960 - 120), 120, 120);
		
		for ( int i = 0 ; i < 40 ; i++ ) {
			spaceButtons.add(new JButton());
		}
		
		int posX, posY, sizeX, sizeY;
		
		for (JButton b : spaceButtons) {

			int index = spaceButtons.indexOf(b);
			int cardinalPosition = (index % 10);
			
			if (index == 0) {
				posX = 840;
				posY = 840;
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
			else {
				posX = 0;
				posY = 0;
				sizeX = 0;
				sizeY = 0;
			}
			b.setBounds(posX, posY, sizeX, sizeY);
			
			frame.add(b);
		}
		
		frame.add(testButton);
		
		return frame;
	}
	
	public static void main(String args[]) {
		DynamicView localView = new DynamicView();
	}
	
}
