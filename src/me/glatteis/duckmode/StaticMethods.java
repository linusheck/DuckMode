package me.glatteis.duckmode;

import me.glatteis.duckmode.hats.EntityHat;
import me.glatteis.duckmode.hats.Hat;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import org.apache.commons.io.IOUtils;
import org.bukkit.*;
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
            DuckReflectionMethods.title(p, Messages.getString("you_died"), 5, 20, 5); //$NON-NLS-1$
        } else {
            DuckReflectionMethods.title(p, Messages.getString("killed"), 5, 20, 5); //$NON-NLS-1$
            DuckReflectionMethods.subtitle(p, Messages.getString("by") + killer.getName(), 5, 20, 5); //$NON-NLS-1$
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
                                        DuckReflectionMethods.subtitle(d2.getPlayer(), ChatColor.WHITE + Messages.getString("survived"), 5, 20, 5); //$NON-NLS-1$
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
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("bo")), //$NON-NLS-1$
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("ss")), //$NON-NLS-1$
                new Pattern(DyeColor.BLACK, PatternType.getByIdentifier("sc")), //$NON-NLS-1$
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("cs")), //$NON-NLS-1$
                new Pattern(DyeColor.BLACK, PatternType.getByIdentifier("vhr")), //$NON-NLS-1$
                new Pattern(DyeColor.BLACK, PatternType.getByIdentifier("rud")), //$NON-NLS-1$
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("rs")), //$NON-NLS-1$
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("ts")), //$NON-NLS-1$
                new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("bs")))); //$NON-NLS-1$
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
        ItemStack s = new ItemStack(Material.SKULL_ITEM);
        ItemMeta m = s.getItemMeta();
        m.setDisplayName(Messages.getString("QUACK")); //$NON-NLS-1$
        s.setItemMeta(m);
        p.getInventory().setItem(0, s);
        p.getInventory().setHelmet(s);
        Hat h = d.getHat();
        if (h instanceof EntityHat) {
            EntityHat eH = (EntityHat) h;
            Entity previousEntity = d.getPlayer();
            for (Entity e : eH.getEntities()) {
                Bukkit.getLogger().info("Entity: " + e.getType() + ", PreviousEntity: " + previousEntity.getType());
                previousEntity.setPassenger(e);
                previousEntity = e;
            }
        }
        p.getInventory().setHelmet(d.getHat().getStack());

//		Skull s = (Skull)  p.getInventory().getItem(0);
//		s.setSkullType(SkullType.CREEPER);
        for (int i = 1; i < 9; i++) {
            if (i == 4)
                continue;
            ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta paneMeta = pane.getItemMeta();
            paneMeta.setDisplayName(""); //$NON-NLS-1$
            pane.setItemMeta(paneMeta);
            p.getInventory().setItem(i, pane);
            p.getInventory().getItem(i).setDurability((short) 15);
        }
        //19, 25
        ItemStack tag = new ItemStack(Material.NAME_TAG);
        ItemStack tag2 = tag.clone();
        ItemMeta tagM = tag.getItemMeta();
        ItemMeta tagM2 = tagM.clone();
        tagM.setDisplayName(ChatColor.RESET + Messages.getString("code")); //$NON-NLS-1$
        tagM.setLore(Arrays.asList("", ChatColor.WHITE + "glatteis")); //$NON-NLS-1$ //$NON-NLS-2$
        tagM2.setDisplayName(ChatColor.RESET + Messages.getString("textures")); //$NON-NLS-1$
        tagM2.setLore(Arrays.asList("", ChatColor.WHITE + "IronMansVater")); //$NON-NLS-1$ //$NON-NLS-2$
        tag.setItemMeta(tagM);
        tag2.setItemMeta(tagM2);
        p.getInventory().setItem(19, tag);
        p.getInventory().setItem(25, tag2);
    }

}
