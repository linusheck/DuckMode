package me.glatteis.duckmode;

import java.util.HashMap;

public class WinTracker {

    public static HashMap<Duck, Integer> wins = new HashMap<Duck, Integer>();


    public static void addWin(Duck duck) {
        if (wins.get(duck) == null) {
            wins.put(duck, 1);
        } else {
            wins.put(duck, wins.get(duck) + 1);
        }
    }

}