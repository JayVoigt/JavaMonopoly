package cc.jayv.monopoly3;


import com.jcraft.jsch.IO;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jay
 */
public class JavaMonopoly implements Serializable{

	DynamicView dynamicView;

	public JavaMonopoly() {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				dynamicView = new DynamicView();
			}
		};
		EventQueue.invokeLater(runner);
	}

	public static void main(String args[]) throws IOException {
		new JavaMonopoly();
	}

	public void loadFromFile() {
		 try {
			 FileInputStream fileInputStream = new FileInputStream("testFile.dat");
			 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			 dynamicView = (DynamicView) objectInputStream.readObject();
		 } catch (IOException | ClassNotFoundException e) {
			 e.printStackTrace();
		 }
	}

	public void saveToFile() {
		 try {
			 FileOutputStream fileOutputStream = new FileOutputStream("testFile.dat");
			 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			 objectOutputStream.writeObject(dynamicView);
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	}

}