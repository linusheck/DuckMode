package me.glatteis.duckmode.listener;

import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.game.DuckLobby;
import me.glatteis.duckmode.game.Intermission;
import me.glatteis.duckmode.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class ListenerActivator {

    public static List<Listener> listeners = Arrays.asList(new DuckLobby(), new PlayerGameListener(),
            Intermission.getIntermission(), new PlayerListener(), new ExplosionHandler());
    public static DuckWeapon[] weapons = {new IronSword(), new RocketLauncher(), new DuckArmor(), new OneShotPistol(),
            new Pistol(), new Shotgun(), new TNTBarrel(), new Grenade(), new MachineGun(), new Flamethrower(),
            new TreeCannon(), new SuicideBomb(), new IceRay()};

    public static void activateListeners() {
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, DuckMain.getPlugin());
        }
        for (DuckWeapon o : weapons) {
            Bukkit.getPluginManager().registerEvents(o, DuckMain.getPlugin());
        }
    }

    public static void lobbyCountdown() {
        ((DuckLobby) listeners.get(0)).countdown();
    }

}
