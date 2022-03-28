/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cc.jayv.monopoly3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jay
 */
public class LogHelper {

	ArrayList<String> gameLogContents,
		gameLogContentsFiltered,
		debugLogContents;

	int currentTurn;

	public LogHelper() {
		gameLogContents = new ArrayList<>();
		gameLogContentsFiltered = new ArrayList<>();
		debugLogContents = new ArrayList<>();

		currentTurn = 0;
	}

	/**
	 * Return the elements of the game log.
	 *
	 * @return An ArrayList of String objects, where each element is one line in
	 * the log.
	 */
	public ArrayList<String> getGameLogContents() {
		return gameLogContents;
	}

	/**
	 * Return the elements of the game log that match the given query.
	 *
	 * @param inputQuery The case-insensitive query for all elements in
	 * gameLogContents to be matched against.
	 * @return An ArrayList of String objects, where each element is one line in
	 * the log, matching the given query parameter.
	 */
	public ArrayList<String> getGameLogContentsFiltered(String inputQuery) {
		gameLogContentsFiltered.clear();

		for (String s : gameLogContents) {
			String lowerString = s.toLowerCase();
			String lowerQuery = inputQuery.toLowerCase();

			if (lowerString.contains(lowerQuery) == true) {
				gameLogContentsFiltered.add(s);
			}
		}
		return gameLogContentsFiltered;
	}

	public void clearLogs() {
		gameLogContents.clear();
		gameLogContents.trimToSize();

		gameLogContentsFiltered.clear();
		gameLogContentsFiltered.trimToSize();

		debugLogContents.clear();
		debugLogContents.trimToSize();
	}

	/**
	 * Send a welcome message to the game log when the application is first
	 * launched.
	 */
	public void sendWelcomeMessage() {
		appendToGameLog("Welcome to Java Monopoly Prototype!");
		appendToGameLog("A new game can be started or loaded under the File menu.\n");
	}

	/**
	 * Append the input to the game log as a formatted line.<br>
	 * Entries are prefixed with the current date and time, as well as the
	 * current turn number.<br>
	 *
	 * @param input The contents of the message.
	 * @param turnCounter The current turn.
	 */
	public void appendToGameLog(String input, int turnCounter) {
		Date currentDate = new Date();
		SimpleDateFormat datePrefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

		String currentTurn = Integer.toString(turnCounter);
		String formattedPrefix;
		formattedPrefix = ("[" + currentDate + "] (" + currentTurn + "): ");

		String output = (formattedPrefix + input + "\n");
		gameLogContents.add(output);
	}

	public void appendToGameLog(String input) {
		Date currentDate = new Date();
		SimpleDateFormat datePrefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

		String formattedPrefix;
		formattedPrefix = ("[" + currentDate + "]: ");

		String output = (formattedPrefix + input + "\n");
		gameLogContents.add(output);
	}

	/**
	 * Append the input to the debug log.
	 *
	 * @param input The contents of the message.
	 */
	public void appendToDebugLog(String input) {
		debugLogContents.add(input);
	}
}
