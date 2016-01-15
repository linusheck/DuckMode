package me.glatteis.duckmode;

import me.glatteis.duckmode.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class ListenerActivator {

    public static List<Listener> listeners = Arrays.asList(new DuckLobby(), new PlayerGameListener(),
            new Intermission(), new PlayerListener(), new ExplosionHandler());
    public static Object[] weapons = {new IronSword(), new RocketLauncher(), new DuckArmor(), new OneShotPistol(), new Pistol(), new Shotgun(), new TNTBarrel(), new Grenade()};

    public static void activateListeners() {
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, DuckMain.getPlugin());
        }
        for (Object o : weapons) {
            Bukkit.getPluginManager().registerEvents((Listener) o, DuckMain.getPlugin());
        }
    }

    public static void lobbyCountdown() {
        ((DuckLobby) listeners.get(0)).countdown();
    }

}
