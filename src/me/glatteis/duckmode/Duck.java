package me.glatteis.duckmode;

import me.glatteis.duckmode.hats.Bill;
import me.glatteis.duckmode.hats.Hat;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Duck {


    private Hat h = new Bill();
    private Location spawnLocation;
    private Player p;
    private boolean dead = false;

    public Duck(Player player, Location l) {
        this.p = player;
        this.spawnLocation = l;
    }

    public Hat getHat() {
        return h;
    }

    public void setHat(Hat hat) {
        h = hat;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean is) {
        dead = is;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Player getPlayer() {
        return p;
    }

}
