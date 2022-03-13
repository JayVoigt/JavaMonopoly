
import java.io.IOException;
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
public class JavaMonopoly {
	Board board;
	GameLogicController controller;
	MainView view;

	public JavaMonopoly() {
		try {
			board = new Board();
		} catch (IOException ex) {
			Logger.getLogger(JavaMonopoly.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		controller = new GameLogicController();
		
		view = new MainView();
		
		view.appendToGameLog("Successfully initialized");
	}
	
	public static void main(String args[]) {
		new JavaMonopoly();
	}
}
