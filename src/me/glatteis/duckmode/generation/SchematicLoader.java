package me.glatteis.duckmode.generation;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.SchematicFormat;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.game.PlayerSpawnPoints;
import me.glatteis.duckmode.reflection.DuckReflectionHelper;
import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class SchematicLoader {

    private static WorldEditPlugin e = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

    private static ArrayList<SchematicToLoad> schematicsToLoad = new ArrayList<SchematicToLoad>();

    @SuppressWarnings("deprecation")
    private static EditSession session = e.getWorldEdit().getEditSessionFactory()
            .getEditSession(new BukkitWorld(DuckMain.getWorld()), Integer.MAX_VALUE);

    public static void addSchematic(SchematicToLoad s) {
        schematicsToLoad.add(s);
    }

    public static void clear() {
        schematicsToLoad.clear();
    }

    public static void loadAllSchematics() {
        loadAllSchematics(null);
    }

    @SuppressWarnings("deprecation")
    public static void loadAllSchematics(final ThenTask finished) {
        new BukkitRunnable() {
            int position = -1;

            public void run() {
                long millis = System.currentTimeMillis();
                while (millis - System.currentTimeMillis() < 25) {
                    position++;
                    if (position >= schematicsToLoad.size()) {
                        schematicsToLoad.clear();
                        this.cancel();
                        if (finished != null) finished.finished();
                        return;
                    }
                    SchematicToLoad s = schematicsToLoad.get(position);
                    Vector v = s.getVector();
                    DuckMain.getWorld().getChunkAt(v.getBlockX(), v.getBlockY()).load();
                    loadSchematic(v, s.getDimensionData().getName(), s.getDimensionContainer().getName(), s.getRotation());
                    specialStuff(s, new Location(DuckMain.getWorld(), v.getX(), v.getY(), v.getZ()));
                }
            }
        }.runTaskTimer(DuckMain.getPlugin(), 0L, 1L);
    }

    private static Location findBlock(Material m, Location l, SchematicToLoad schematicToLoad) {
        for (int x = 0; x < schematicToLoad.getDimensionData().getSizeX(); x++) {
            for (int y = 0; y < schematicToLoad.getDimensionData().getSizeY(); y++) {
                for (int z = 0; z < schematicToLoad.getDimensionData().getSizeZ(); z++) {
                    if (DuckMain.getWorld().getBlockAt(l.clone().add(x, y, z)).getType().equals(m)) {
                        return (l.clone().add(x, y + 1, z));
                    }
                }
            }
        }
        return null;
    }

    private static void specialStuff(SchematicToLoad s, Location there) {
        if (s.getDimensionContainer().getType() == null || s.getDimensionData() == null) return;
        if (s.getDimensionContainer().getType().equals("spawn")) {
            Location l = findBlock(Material.GOLD_BLOCK, there, s);
            if (l == null) {
                l = there.clone().add(
                        s.getDimensionData().getSizeX() / 2,
                        s.getDimensionData().getSizeY() / 2,
                        s.getDimensionData().getSizeZ() / 2
                );
            }
            PlayerSpawnPoints.spawnPoints.add(l.add(0, 0.2, 0));
        } else if (s.getDimensionContainer().getType().equals("weapon")) {
            Location spawn = findBlock(Material.IRON_BLOCK, there, s);
            if (spawn == null) return;
            WeaponWatch.spawnRandomWeapon(spawn);
            SpawnWeapons.locations.add(spawn);
        }
    }


    @SuppressWarnings("deprecation")
    private static boolean loadSchematic(Vector v, String dimension, String container, int rotation) {

        String path = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +
                "/plugins/DuckMode/Generation/" + dimension + "/" + container + "/";

        File typeFile = new File(path);
        if (!typeFile.exists()) {
            Bukkit.getLogger().info("File does not exist: " + typeFile.getPath());
            return false;
        }
        int amount = typeFile.list().length;

        if (amount == 0) {
            return false;
        }

        String schematicPath = typeFile.toString() + "/" + typeFile.list()[new Random().nextInt(amount)];

        File f = new File(schematicPath);

        if (!f.exists()) {
            System.out.println("Schematic does not exist.");
            return false;
        }

        try {
            CuboidClipboard clipboard = SchematicFormat.getFormat(f).load(f);
            clipboard.rotate2D(rotation);
            clipboard.paste(session, v, false);
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return false;
    }

    static void clearArea(org.bukkit.util.Vector vector1, org.bukkit.util.Vector vector2) {
        final org.bukkit.util.Vector v1 = new org.bukkit.util.Vector(
                Math.min(vector1.getX(), vector2.getX()),
                Math.min(vector1.getY(), vector2.getY()),
                Math.min(vector1.getZ(), vector2.getZ()));

        final org.bukkit.util.Vector v2 = new org.bukkit.util.Vector(
                Math.max(vector1.getX(), vector2.getX()),
                Math.max(vector1.getY(), vector2.getY()),
                Math.max(vector1.getZ(), vector2.getZ()));

        for (int x = v1.getBlockX(); x < v2.getBlockX(); x++) {
            for (int y = v1.getBlockY(); y < v2.getBlockY(); y++) {
                for (int z = v1.getBlockZ(); z < v2.getBlockZ(); z++) {
                    DuckMain.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

    public static void initLighting(org.bukkit.util.Vector vector1, org.bukkit.util.Vector vector2) {
        final org.bukkit.util.Vector v1 = new org.bukkit.util.Vector(
                Math.min(vector1.getX(), vector2.getX()),
                Math.min(vector1.getY(), vector2.getY()),
                Math.min(vector1.getZ(), vector2.getZ()));

        final org.bukkit.util.Vector v2 = new org.bukkit.util.Vector(
                Math.max(vector1.getX(), vector2.getX()),
                Math.max(vector1.getY(), vector2.getY()),
                Math.max(vector1.getZ(), vector2.getZ()));

        for (int cx = v1.getBlockX(); cx <= v2.getBlockX(); cx+= 16) {
            for (int cz = v1.getBlockZ(); cz <= v2.getBlockZ(); cz += 16) {
                final org.bukkit.Chunk chunk;
                Object craftChunk = DuckReflectionHelper.getCraftBukkitClass("CraftChunk").cast(chunk = new Location(DuckMain.getWorld(), cx, 0, cz).getChunk());
                try {
                    Object handle = craftChunk.getClass().getMethod("getHandle").invoke(craftChunk);
                    handle.getClass().getMethod("initLighting").invoke(handle);
                    //This code is useful, but using it would be laggy on slow internet connections, servers and computers...
                    /*for (int x = 0; x < 16; x += 6) for (int y = 0; y < v2.getBlockY(); y += 6) for (int z = 0; z < 16; z += 6) {
                        final Material material = chunk.getBlock(x, y, z).getType();
                        chunk.getBlock(x, y, z).setType(Material.SEA_LANTERN);
                        final int finalX = x, finalY = y, finalZ = z;
                        new BukkitRunnable() {
                            public void run() {
                                chunk.getBlock(finalX, finalY, finalZ).setType(material);
                            }
                        }.runTaskLater(DuckMain.getPlugin(), 10);
                    }*/
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public abstract static class ThenTask {
        public abstract void finished();
    }

}
