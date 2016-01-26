package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.Messages;
import me.glatteis.duckmode.StaticMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Hats {

    private static boolean secretHatsAlreadyAdded = false;
    private static Hat[] sampleHats = {new Bill(), new Burger(), new Fedora(), new StrangeCake(), new NotchHead(), new RandomSkin(), new Pumpkin()};

    public static void setHat(Duck d, ItemStack i) {
        if (d.getHat() != null && d.getHat() instanceof EntityHat) {
            EntityHat eH = (EntityHat) d.getHat();
            for (Entity e : eH.getEntities()) {
                if (e == null || e.isDead() || !e.isValid()) continue;
                Bukkit.getLogger().info("Removing " + e.getType());
                e.remove();
            }
        }
        for (Hat h : sampleHats) {
            if (ChatColor.stripColor(h.getName())
                    .equals(ChatColor.stripColor(i.getItemMeta().getDisplayName()))) {
                Bukkit.getLogger().info(h.getName() + " added");
                try {
                    d.setHat(h.getClass().newInstance());
                    StaticMethods.prepareInventory(d);
                } catch (InstantiationException e) {
                    d.getPlayer().sendMessage(Messages.getString("Hats.error_notice"));
                } catch (IllegalAccessException e) {
                    d.getPlayer().sendMessage(Messages.getString("Hats.error_notice"));
                }
            }
        }
    }

    public static void setHat(Duck d, String i) {
        if (d.getHat() != null && d.getHat() instanceof EntityHat) {
            EntityHat eH = (EntityHat) d.getHat();
            for (Entity e : eH.getEntities()) {
                if (e == null || e.isDead() || !e.isValid()) continue;
                Bukkit.getLogger().info("Removing " + e.getType());
                e.remove();
            }
        }
        for (Hat h : sampleHats) {
            if (ChatColor.stripColor(h.getName())
                    .equals(ChatColor.stripColor(i))) {
                try {
                    d.setHat(h.getClass().newInstance());
                    StaticMethods.prepareInventory(d);
                } catch (InstantiationException e) {
                    d.getPlayer().sendMessage(Messages.getString("Hats.error_notice"));
                } catch (IllegalAccessException e) {
                    d.getPlayer().sendMessage(Messages.getString("Hats.error_notice"));
                }
            }
        }
    }


    public static void openHatInventory(Player p) {
        Inventory hatInv = Bukkit.getServer().createInventory(null, sampleHats.length + (9 - sampleHats.length % 9), ChatColor.BLUE + Messages.getString("Hats.hats_inventory_title"));
        for (Hat h : sampleHats) {
            ItemStack i = h instanceof EntityHat ? new ItemStack(((EntityHat) h).getShowItemStack()) : new ItemStack(h.getStack());
            ItemMeta m = i.getItemMeta();
            m.setDisplayName(ChatColor.BLUE + ChatColor.BOLD.toString() + h.getName());
            m.setLore(Arrays.asList(ChatColor.RESET + h.getLore()));
            i.setItemMeta(m);
            hatInv.addItem(i);
        }
        p.openInventory(hatInv);
    }

    public static void addSecretHats() {
        if (secretHatsAlreadyAdded) {
            return;
        }
        secretHatsAlreadyAdded = true;
        Hat[] previousSampleHats = sampleHats.clone();
        Hat[] secretHats = {new IronMansVaterHead(), new EisHead(), new LUKFUHead()};
        sampleHats = new Hat[previousSampleHats.length + secretHats.length];
        for (int i = 0; i < previousSampleHats.length; i++) {
            sampleHats[i] = previousSampleHats[i];
        }
        for (int i = previousSampleHats.length; i < sampleHats.length; i++) {
            sampleHats[i] = secretHats[i - previousSampleHats.length];
        }
    }

}
