package me.glatteis.duckmode;

import me.glatteis.duckmode.hats.EntityHat;
import me.glatteis.duckmode.hats.Hat;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StaticMethods {

    private static boolean newGameRunnableAlreadyExecuted = false;

    public static void unZipIt(String file, String outputDir) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(outputDir, entry.getName());
                if (entry.isDirectory())
                    entryDestination.mkdirs();
                else {
                    entryDestination.getParentFile().mkdirs();
                    InputStream in = zipFile.getInputStream(entry);
                    OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    out.close();
                }
            }
        } finally {
            zipFile.close();
        }
    }


    public static void deathscreen(Player p, Player killer) {
        if (killer == null) {
            DuckReflectionMethods.title(p, Messages.getString("you_died"), 5, 20, 5);
        } else {
            DuckReflectionMethods.title(p, Messages.getString("killed"), 5, 20, 5);
            DuckReflectionMethods.subtitle(p, Messages.getString("by") + killer.getName(), 5, 20, 5);
        }
    }

    public static void disableJumping(Player p) {
        p.removePotionEffect(PotionEffectType.JUMP);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
    }

    public static void enableJumping(Player p) {
        p.removePotionEffect(PotionEffectType.JUMP);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5));
    }

    public static void checkForWin() {
        if (DuckMain.state.equals(GameState.PREGAME)) return;
        int aliveDucks = 0;
        for (Duck d : DuckMain.ducks) {
            if (!d.isDead()) {
                aliveDucks++;
            }
        }
        if (aliveDucks <= 1) {
            if (!newGameRunnableAlreadyExecuted) {
                newGameRunnableAlreadyExecuted = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int aliveDucks = 0;
                        for (Duck d : DuckMain.ducks) {
                            if (!d.isDead()) {
                                aliveDucks++;
                            }
                        }
                        if (newGameRunnableAlreadyExecuted && aliveDucks == 1 && !DuckMain.state.equals(GameState.PREGAME)) {
                            for (Duck d : DuckMain.ducks) {
                                if (!d.isDead()) {
                                    d.getPlayer().getInventory().setHelmet(new ItemStack(Material.SMOOTH_BRICK));
                                    for (Duck d2 : DuckMain.ducks) {
                                        DuckReflectionMethods.title(d2.getPlayer(), ChatColor.WHITE + d.getPlayer().getName(), 5, 20, 5);
                                        DuckReflectionMethods.subtitle(d2.getPlayer(), ChatColor.WHITE + Messages.getString("survived"), 5, 20, 5);
                                        d2.getPlayer().playSound(d.getPlayer().getLocation(), Sound.NOTE_PIANO, 20, 5);
                                    }
                                    newGameRunnableAlreadyExecuted = false;
                                    WinTracker.addWin(d);
                                    ContinueGame.setRoundHasEnded(true);
                                }
                            }
                        } else {
                            newGameRunnableAlreadyExecuted = false;
                            ContinueGame.setRoundHasEnded(true);
                        }
                    }
                }.runTaskLater(DuckMain.getPlugin(), 30L);
            }
        }
    }

    public static ItemStack plusOne() {
        ItemStack b = new ItemStack(Material.BANNER);
        BannerMeta meta = (BannerMeta) b.getItemMeta();
        meta.setPatterns(Arrays.asList(
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("bo")),
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("ss")),
                new Pattern(DyeColor.BLACK, PatternType.getByIdentifier("sc")),
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("cs")),
                new Pattern(DyeColor.BLACK, PatternType.getByIdentifier("vhr")),
                new Pattern(DyeColor.BLACK, PatternType.getByIdentifier("rud")),
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("rs")),
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("ts")),
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("bs"))));
        meta.setBaseColor(DyeColor.BLACK);
        b.setItemMeta(meta);
        return b;
    }


    public static void prepareInventory(Duck d) {
        Player p = d.getPlayer();
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.getInventory().clear();
        Hat h = d.getHat();
        if (h instanceof EntityHat) {
            EntityHat eH = (EntityHat) h;
            Entity previousEntity = d.getPlayer();
            for (Entity e : eH.getEntities()) {
                previousEntity.setPassenger(e);
                previousEntity = e;
            }
        }
        p.getInventory().setHelmet(d.getHat().getStack());

        //19, 25
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

}
