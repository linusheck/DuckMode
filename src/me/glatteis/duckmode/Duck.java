package me.glatteis.duckmode;

import me.glatteis.duckmode.hats.Bill;
import me.glatteis.duckmode.hats.EntityHat;
import me.glatteis.duckmode.hats.Hat;
import me.glatteis.duckmode.messages.Messages;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import me.glatteis.duckmode.weapons.DuckArmor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

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

    public void prepareInventory() {
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.getInventory().clear();
        if (h instanceof EntityHat) {
            EntityHat eH = (EntityHat) h;
            Entity previousEntity = p;
            for (Entity e : eH.getEntities()) {
                previousEntity.setPassenger(e);
                previousEntity = e;
            }
        }

        p.getInventory().setHelmet(h.getStack());

        ItemStack tag = new ItemStack(Material.NAME_TAG);
        ItemStack tag2 = tag.clone();
        ItemMeta tagM = tag.getItemMeta();
        ItemMeta tagM2 = tagM.clone();
        tagM.setDisplayName(ChatColor.RESET + Messages.getString("code"));
        tagM.setLore(Arrays.asList("", ChatColor.WHITE + "glatteis"));
        tagM2.setDisplayName(ChatColor.RESET + Messages.getString("textures"));
        tagM2.setLore(Arrays.asList("", ChatColor.WHITE + "IronMansVater"));
        tag.setItemMeta(tagM);
        tag2.setItemMeta(tagM2);
        p.getInventory().setItem(19, tag);
        p.getInventory().setItem(25, tag2);
    }

    public void die(String cause) {
        if (!DuckArmor.willDie(this)) return;
        setDead(true);
        getPlayer().setGameMode(GameMode.SPECTATOR);
        getPlayer().getInventory().clear();
        getPlayer().getInventory().setArmorContents(new ItemStack[4]);
        getPlayer().teleport(new Location(DuckMain.getWorld(), getPlayer().getLocation().getX(), 25., getPlayer().getLocation().getZ()));
        getPlayer().updateInventory();
        for (Duck d2 : DuckMain.ducks) {
            d2.getPlayer().playSound(getPlayer().getLocation(), Sound.ANVIL_LAND, 10, 1);
        }
        DuckReflectionMethods.title(getPlayer(), ChatColor.MAGIC.toString(), 0, 10, 5);
        DuckReflectionMethods.subtitle(getPlayer(), ChatColor.RED + Messages.getString("you_are_dead"), 0, 10, 5);
        DuckReflectionMethods.actionbar(getPlayer(), cause);
        DuckMain.continueGame.checkForWin();
    }


}
