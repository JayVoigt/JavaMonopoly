package cc.jayv.monopoly3;


import java.util.HashMap;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jay
 */
public class Statistics {

	HashMap<String, Integer> statsMap;

	public Statistics() {
		statsMap = new HashMap<>();

		statsMap.put("turnsPlayed", 0);
		statsMap.put("buttonPresses", 0);
		statsMap.put("doublesRolled", 0);
		statsMap.put("totalRentPaid", 0);
	}

	public HashMap<String, Integer> getStatsMap() {
		return statsMap;
	}

	public void putStatsMap(String key, int value) {
		statsMap.put(key, value);
	}
}
