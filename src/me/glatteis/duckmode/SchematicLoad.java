package me.glatteis.duckmode;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.World;
import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SchematicLoad {

    private static WorldEditPlugin e = null;
    private static ArrayList<SchematicToLoad> schematicsToLoad = new ArrayList<SchematicToLoad>();

    public static void addSchematic(SchematicToLoad s) {
        schematicsToLoad.add(s);
    }

    public static void clear() {
        schematicsToLoad.clear();
    }

    public static void loadAllSchematics() {

        new BukkitRunnable() {
            int position = -1;

            public void run() {
                for (int i = 0; i < 4; i++) {
                    position++;
                    if (position >= schematicsToLoad.size()) {
                        schematicsToLoad.clear();
                        this.cancel();
                        return;
                    }
                    SchematicToLoad s = schematicsToLoad.get(position);
                    Chunk c = DuckMain.getWorld().getChunkAt(s.getVector().getBlockX(), s.getVector().getBlockX());
                    if (!c.isLoaded()) {
                        c.load();
                    }
                    if (s.getStory() == null) {
                        buildingGeneration(s, s.getVector());
                        continue;
                    }
                    loadSchematic(s.getWorld(), s.getVector(), s.getDimension(), s.getStory(), s.getType());
                    Vector v = s.getVector();
                    specialStuff(s, new Location(DuckMain.getWorld(), v.getX(), v.getY(), v.getZ()));
                }
            }
        }.runTaskTimer(DuckMain.getPlugin(), 0L, 1L);
    }

    private static void buildingGeneration(SchematicToLoad s, Vector v) {
        Location there = new Location(DuckMain.getWorld(), v.getX(), v.getY(), v.getZ());
        String story = "air"; //$NON-NLS-1$
        if (!there.clone().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
            story = "building"; //$NON-NLS-1$
        } else if (!s.getType().equals("spawn")) { //$NON-NLS-1$
            double generate = Math.random();
            if (generate < 0.9) {
                return;
            }
        }
        loadSchematic(new BukkitWorld(DuckMain.getWorld()), v, s.getDimension(), story, s.getType());
        specialStuff(s, there);
    }

    private static Location findBlock(Material m, Location l) {
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 20; y++) {
                for (int z = 0; z < 6; z++) {
                    if (DuckMain.getWorld().getBlockAt(l.clone().add(x, y, z)).getType().equals(m)) {
                        return (l.clone().add(x, y + 1, z));
                    }
                }
            }
        }
        return null;
    }

    private static void specialStuff(SchematicToLoad s, Location there) {
        if (s.getType().equals("spawn")) { //$NON-NLS-1$
            Location l = findBlock(Material.GOLD_BLOCK, there);
            if (l == null) l = there.clone().add(2, 2, 2);
            PlayerSpawnPoints.spawnPoints.add(l);
        } else if (s.getType().equals("weapon")) { //$NON-NLS-1$
            Location spawn = findBlock(Material.IRON_BLOCK, there);
            if (spawn == null) return;
            WeaponWatch.spawnRandomWeapon(spawn);
            SpawnWeapons.locations.add(spawn);
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean loadSchematic(World w, Vector v, String dimension, String story, String type) {
        if (e == null) {
            e = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"); //$NON-NLS-1$
        }
        String path = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +  //$NON-NLS-1$
                "/plugins/DuckMode/Generation/" + dimension + "/" + story + "/" + type + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        File typeFile = new File(path);
        if (!typeFile.exists()) {
            return false;
        }
        int amount = typeFile.list().length;

        if (amount < 1) {
            return false;
        }

        String schematicPath = path + String.valueOf(new Random().nextInt(amount) + 1) + ".schematic"; //$NON-NLS-1$
        File f = new File(schematicPath);

        if (!f.exists()) {
            return false;
        }

        EditSession session = e.getWorldEdit().getEditSessionFactory().getEditSession(w, 346585452);
        try {
            SchematicFormat.getFormat(f).load(f).paste(session, v, false);
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return false;

    }

    public static void clearArea(final Vector v1, final Vector v2) {
        new BukkitRunnable() {
            public void run() {
                if (e == null) {
                    e = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"); //$NON-NLS-1$
                }
                @SuppressWarnings("deprecation")
                EditSession session = e.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(DuckMain.getWorld()), 346585452);
                try {
                    session.setBlocks(new CuboidRegion(v1, v2), new BaseBlock(0));
                } catch (MaxChangedBlocksException e1) {
                    Bukkit.getLogger().info("Something really bad happened: WordEdit MaxChangedBlocksException"); //$NON-NLS-1$
                }
            }
        }.runTask(DuckMain.getPlugin());

    }

}
