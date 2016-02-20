package me.glatteis.duckmode.messages;

import me.glatteis.duckmode.DuckMain;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static final String BUNDLE_NAME = "me.glatteis.duckmode.messages.messages" + DuckMain.getPlugin().getConfig().getString("message-language");

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    private Messages() {}
}
