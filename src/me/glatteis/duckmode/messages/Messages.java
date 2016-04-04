package me.glatteis.duckmode.messages;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import org.bukkit.Bukkit;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static ResourceBundle RESOURCE_BUNDLE;

    public static void enable() {
        try {
            String bundleName = "me.glatteis.duckmode.messages.messages" +
                    DuckMain.getPlugin().getConfig().getString("message-language");
            RESOURCE_BUNDLE = ResourceBundle.getBundle(bundleName);
        } catch (Exception e) {
            Bukkit.getLogger().info("There seems to be an error in the config concerning the language.");
            e.printStackTrace();
        }
    }

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
