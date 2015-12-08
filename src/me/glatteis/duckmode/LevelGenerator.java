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

        //Bukkit.getLogger().info("DATEN: " + maxHeight + "; " + maxX + "; " + maxZ);

        for (int height = 0; height < maxHeight; height = height + 5) {
            for (int x = 0; x < maxX; x = x + 5) {
                for (int z = 0; z < maxZ; z = z + 5) {
                    //boolean isForcedSpawn = false;
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
                        if (height + 5 > maxHeight && providedSpawns > 0 && type != "spawn") { //$NON-NLS-1$
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


//		for (int x = 0; x < 25; x = x + 5) {
//			for (int z = 0; z < 25; z = z + 5) {
//				boolean build = new Random().nextBoolean();
//				if (build) {
//					Location there = startLocation.clone().add(x, 0, z);
//					SchematicLoad.loadSchematic(new BukkitWorld(there.getWorld()), new Vector(there.getX(), there.getY(), there.getZ()),
//							"else", String.valueOf(new Random().nextInt(10) + 1));
//					for (int x2 = 0; x2 < 6; x2++) {
//						for (int y2 = 0; y2 < 20; y2++) {
//							for (int z2 = 0; z2 < 6; z2++) {
//								if (DuckMain.getWorld().getBlockAt(there.clone().add(x2, y2, z2)).getType().equals(Material.IRON_BLOCK)) {
//									StaticMethods.spawnRandomWeapon(there.clone().add(x2, y2 + 1, z2));
//								}
//							}
//						}
//					}
//				}	
//			}
//		}
//		for (int i = 0; i < (DuckMain.ducks.size() > 2 ? 2 : 1); i++) {
//			Location l = startLocation.clone().add(new Random().nextInt(4) * 5, 0, new Random().nextInt(4) * 5);
//			SchematicLoad.clearArea(new BukkitWorld(DuckMain.getWorld()), new Vector(l.getX(), l.getY(), l.getZ()), new Vector(l.getX() + 4, l.getY() + 20, l.getZ() + 4));
//			SchematicLoad.loadSchematic(new BukkitWorld(l.getWorld()), new Vector(l.getX(), l.getY(), l.getZ()),
//					"spawn", String.valueOf(new Random().nextInt(6) + 1));
//			for (int x2 = 0; x2 < 6; x2++) {
//				for (int y2 = 0; y2 < 20; y2++) {
//					for (int z2 = 0; z2 < 6; z2++) {
//						if (DuckMain.getWorld().getBlockAt(l.clone().add(x2, y2, z2)).getType().equals(Material.IRON_BLOCK)) {
//							StaticMethods.spawnRandomWeapon(l.clone().add(x2, y2 + 1, z2));
//						}
//						if (DuckMain.getWorld().getBlockAt(l.clone().add(x2, y2, z2)).getType().equals(Material.GOLD_BLOCK)) {
//							spawnPoints.add(l.clone().add(x2, y2 + 1, z2));
//						}
//					}
//				}
//			}
//		

}
