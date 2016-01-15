package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.ListenerActivator;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WeaponWatch {

    public static ArrayList<List<String>> cooldown = new ArrayList<List<String>>();
    public static HashMap<List<String>, Integer> durability = new HashMap<List<String>, Integer>();

    public static void spawnRandomWeapon(Location l) {
        Random r = new Random();
        int n = r.nextInt(ListenerActivator.weapons.length);
        ((DuckWeapon) ListenerActivator.weapons[n]).spawnWeapon(l);
    }

}
