package me.glatteis.duckmode;

import org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StaticMethods {



    public static void disableJumping(Player p) {
        p.removePotionEffect(PotionEffectType.JUMP);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
    }

    public static void enableJumping(Player p) {
        p.removePotionEffect(PotionEffectType.JUMP);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5));
    }


}
