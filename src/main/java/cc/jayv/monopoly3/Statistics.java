package cc.jayv.monopoly3;

import java.util.HashMap;

/**
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
