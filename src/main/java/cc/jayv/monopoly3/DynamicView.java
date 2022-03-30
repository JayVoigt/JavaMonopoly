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

		mainFrame.setSize(1024, 1024);
		boardFrame.setSize(512, 512);
		
		mainFrame.setLayout(new GridBagLayout());
		boardFrame.setLayout(null);
		
		for ( int i = 0 ; i < 40 ; i++ ) {
			spaceButtons.add(new JButton());
		}
		
		for (JButton b : spaceButtons) {
			boardFrame.add(b);
			b.setSize(80, 120);
			b.setVisible(true);
		}
		
		mainFrame.add(boardFrame);
		mainFrame.add(controlFrame);
		mainFrame.add(infoFrame);
		
		for (JInternalFrame f : internalFrames) {
			f.setVisible(true);
		}
		
		mainFrame.setVisible(true);
	}
	
	private void initComponents() {
		internalFrames = new ArrayList<>();
		spaceButtons = new ArrayList<>();
		
		mainFrame = new JFrame();
		boardFrame = new JInternalFrame();
		controlFrame = new JInternalFrame();
		infoFrame = new JInternalFrame();
		
		internalFrames.add(boardFrame);
		internalFrames.add(controlFrame);
		internalFrames.add(infoFrame);
	}
	
	public static void main(String args[]) {
		DynamicView localView = new DynamicView();
	}
}
