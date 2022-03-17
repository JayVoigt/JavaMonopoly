
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
	MainWindow view;

	public JavaMonopoly() throws IOException {
		try {
			board = new Board();
		} catch (IOException ex) {
			Logger.getLogger(JavaMonopoly.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		view = new MainWindow(board);
		view.setVisible(true);
		
		for ( int i = 0 ; i < board.spaces.size() ; i++ ) {
			System.out.println(board.spaces.get(i).getFriendlyName());
		}
	}
	
	public static void main(String args[]) throws IOException {
		new JavaMonopoly();
	}
}
