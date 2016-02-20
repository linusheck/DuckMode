package me.glatteis.duckmode;

import me.glatteis.duckmode.game.GameState;
import me.glatteis.duckmode.game.WinTracker;
import me.glatteis.duckmode.messages.Messages;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StaticMethods {

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

    public static void disableJumping(Player p) {
        p.removePotionEffect(PotionEffectType.JUMP);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
    }

    public static void enableJumping(Player p) {
        p.removePotionEffect(PotionEffectType.JUMP);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5));
    }

    private static boolean newGameRunnableAlreadyExecuted = false;

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
                                    DuckMain.continueGame.setRoundHasEnded(true);
                                }
                            }
                        } else {
                            newGameRunnableAlreadyExecuted = false;
                            DuckMain.continueGame.setRoundHasEnded(true);
                        }
                    }
                }.runTaskLater(DuckMain.getPlugin(), 30L);
            }
        }
    }





}
