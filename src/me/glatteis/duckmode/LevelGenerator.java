package me.glatteis.duckmode;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.Random;

public class LevelGenerator {

    public static void buildPlace(final boolean where) {
        Location startLocation;
        int providedSpawns = DuckMain.ducks.size(); //There MUST be at least as many spawn points as ducks!
        if (where) {
            startLocation = new Location(DuckMain.getWorld(), 1000, 20, 0);
            SchematicLoad.clearArea(new Vector(1000, 20, 0), new Vector(1100, 70, 100));
        } else {
            startLocation = new Location(DuckMain.getWorld(), -1000, 20, 0);
            SchematicLoad.clearArea(new Vector(-1000, 20, 0), new Vector(-900, 70, 100));
        }

        String path = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() + "/plugins/DuckMode/Generation/"; //$NON-NLS-1$ //$NON-NLS-2$
        int amount = new File(path).list().length;
        String dimension = String.valueOf(new Random().nextInt(amount - 1) + 1);

        int maxHeight = ((int) (Math.random() * 6)) * 5 + 11;
        int maxX = ((int) (Math.random() * 8)) * 5 + 11;
        int maxZ = ((int) (Math.random() * 8)) * 5 + 11;

        for (int height = 0; height < maxHeight; height = height + 5) {
            for (int x = 0; x < maxX; x = x + 5) {
                for (int z = 0; z < maxZ; z = z + 5) {
                    final Location there = startLocation.clone().add(x, height, z);
                    if (height == 0) {
                        double generate = Math.random();
                        if (generate < 0.5) {
                            continue;
                        }
                        double random = Math.random();
                        String type;
                        if (random < 0.1 && providedSpawns != 0) {
                            providedSpawns--;
                            type = "spawn"; //$NON-NLS-1$
                        } else if (random < 0.2) {
                            type = "weapon"; //$NON-NLS-1$
                        } else {
                            type = "land"; //$NON-NLS-1$
                        }

                        SchematicLoad.addSchematic(new SchematicToLoad(new BukkitWorld(there.getWorld()), new Vector(there.getX(), there.getY(), there.getZ()),
                                dimension, "ground", type)); //$NON-NLS-1$
                    } else {
                        if (height + 5 > maxHeight && providedSpawns != 0) {
                            SchematicLoad.addSchematic(new SchematicToLoad(new BukkitWorld(there.getWorld()), new Vector(there.getX(), there.getY(), there.getZ()), dimension, "air", "spawn")); //$NON-NLS-1$ //$NON-NLS-2$
                            continue;
                        }
                        double random = Math.random();
                        String type = "land"; //$NON-NLS-1$
                        if (random < 0.1 && providedSpawns > 0) {
                            providedSpawns--;
                            type = "spawn"; //$NON-NLS-1$
                        } else if (random < 0.35) {
                            type = "weapon"; //$NON-NLS-1$
                        }
                        if (height + 5 > maxHeight && providedSpawns > 0 && (!type.equals("spawn"))) { //$NON-NLS-1$
                            Bukkit.getLogger().info("Spawn escape code triggered."); //$NON-NLS-1$
                            type = "spawn"; //$NON-NLS-1$
                            providedSpawns--;
                        }
                        SchematicLoad.addSchematic(new SchematicToLoad(new BukkitWorld(there.getWorld()), new Vector(there.getX(), there.getY(), there.getZ()),
                                dimension, null, type));
                    }
                }
            }
        }

    }
}
