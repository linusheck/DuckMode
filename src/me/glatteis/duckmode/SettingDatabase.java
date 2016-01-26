package me.glatteis.duckmode;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SettingDatabase {

    public static HashMap<Sign, String> settingsSigns = new HashMap<Sign, String>(); //Contains Sign objects for every setting
    public static List<String> settings = Arrays.asList(SettingTypes.ROUNDS.toString(), SettingTypes.POINTS_TO_WIN.toString(), ChatColor.YELLOW + SettingTypes.START_GAME.toString(), SettingTypes.HATS.toString(), SettingTypes.START_GAME.toString());
    public static HashMap<String, Integer> intSetting = new HashMap<String, Integer>(); //Contains index of current setting for each setting
    public static HashMap<String, List<Integer>> settingSwitches = null; //Contains list of what is possible

    public static Integer switchSettingsFor(String setting) {
        if (settingSwitches == null) {
            configureList();
        }
        Integer indexOfCurrent = intSetting.get(setting);
        if (indexOfCurrent == null || settingSwitches.get(setting) == null) {
            return null;
        }
        if (indexOfCurrent == settingSwitches.get(setting).size() - 1) {
            indexOfCurrent = 0;
        } else {
            indexOfCurrent++;
        }
        intSetting.put(setting, indexOfCurrent);
        return settingSwitches.get(setting).get(indexOfCurrent);
    }

    private static void configureList() {
        settingSwitches = new HashMap<String, List<Integer>>();
        settingSwitches.put(SettingTypes.POINTS_TO_WIN.toString(), Arrays.asList(5, 10, 15, 20, 25, 30, 40, 50));
        settingSwitches.put(SettingTypes.ROUNDS.toString(), Arrays.asList(2, 5, 10, 15, 20));
    }

}
