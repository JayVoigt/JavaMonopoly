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
		JInternalFrame localInternalFrame = new JInternalFrame();
		
		localInternalFrame.setSize(1024, 1024);
		localInternalFrame.setLayout(null);
		localInternalFrame.setVisible(true);
		
		return localInternalFrame;
	}
	
	public static void main(String args[]) {
		DynamicView localView = new DynamicView();
	}
	
}
