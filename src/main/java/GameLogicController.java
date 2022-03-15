/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class GameLogicController {
	Board board;
	MainView view;
	
	Player currentPlayer;
	
	boolean isGameActive;
	
	int playersCount,
		turnCounter;
	
	public GameLogicController(Board inputBoard) {
		board = inputBoard;
	}
}
