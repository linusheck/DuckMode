package me.glatteis.duckmode;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import me.glatteis.duckmode.hats.Hats;
import me.glatteis.duckmode.weapons.ListenerActivator;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuckMain extends JavaPlugin {

    public static int maxPlayerCount = 4;
    public static boolean indevResourcePack = false;
    private static String resourcesVersion = "RESOURCES - 0.4.5"; //$NON-NLS-1$
    public static List<Duck> ducks = new ArrayList<Duck>();
    public static GameState state;
    private static World duckWorld;
    public static Plugin plugin;
    public static HashMap<Integer, Duck> duckCount = new HashMap<Integer, Duck>();
    public static Location spawnLocation;


    public static Plugin getPlugin() {
        return plugin;
    }

    public static void setPlugin(Plugin p) {
        plugin = p;
    }

    public static World getWorld() {
        return duckWorld;
    }

    public static void setWorld(World w) {
        duckWorld = w;
    }

    private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    private void writeVersion(String versionFile) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(versionFile, "UTF-8"); //$NON-NLS-1$
        writer.print(resourcesVersion);
        writer.flush();
        writer.close();
    }

    @Override
    public void onLoad() {
        try {
            String duckModeFolder = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +  //$NON-NLS-1$
                    "/plugins/DuckMode"; //$NON-NLS-1$
            File versionFile = new File(duckModeFolder + "/version.txt"); //$NON-NLS-1$
            if (new File(duckModeFolder).exists()) {
                if (!versionFile.exists()) {
                    writeVersion(versionFile.toString());
                } else {
                    String version = readFileAsString(versionFile.toString());
                    if (version.equals(resourcesVersion)) {
                        return;
                    } else {
                        FileUtils.deleteDirectory(new File(duckModeFolder)); //Integrating is bad.
                    }
                }

            }
            String duckModeFile = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +  //$NON-NLS-1$
                    "/plugins/DuckMode.zip"; //$NON-NLS-1$
            getLogger().info("Copying DuckMode folder..."); //$NON-NLS-1$
//		if (versionFile.exists()) {
//			Bukkit.getLogger().info("Checking for resources update...");
//			URL versionURL = new URL("https://drive.google.com/uc?export=download&id=0B3GfPwC2qZsfX1JOWFg5Rk5GZ1U");
//			BufferedReader versionIn = new BufferedReader(new InputStreamReader(versionURL.openStream()));
//			String versionOnline = versionIn.readLine();
//			String versionOffline = Files.readAllLines(versionFile.toPath(), Charset.defaultCharset()).get(0);
//			
//			Bukkit.getLogger().info("Online version of resources is " + versionOnline);
//			Bukkit.getLogger().info("Local version is " + versionOffline);
//			
//			if ((versionOnline).equals(versionOffline)) {
//				return;
//				}
//			}
//			Bukkit.getLogger().info("Downloading resources for DuckMode!");
//			URL url = new URL("https://drive.google.com/uc?export=download&id=0B3GfPwC2qZsfR0FZQ1BOM1I4eTQ");
//			File path = new File(new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() + 
//					"/plugins/DuckMode.zip");
//			FileUtils.copyURLToFile(url, path);
            URL resourceURL = getClass().getResource("/resources/DuckMode.zip"); //$NON-NLS-1$
            File dest = new File(duckModeFile);
            FileUtils.copyURLToFile(resourceURL, dest);
            String folder = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +  //$NON-NLS-1$
                    "/plugins/"; //$NON-NLS-1$
            StaticMethods.unZipIt(duckModeFile, folder);
            writeVersion(versionFile.toString());
            getLogger().info("Success!"); //$NON-NLS-1$
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {

        Bukkit.getLogger().info("*****************************************"); //$NON-NLS-1$
        Bukkit.getLogger().info("Hey! This is a beta version of Duck Mode."); //$NON-NLS-1$
        Bukkit.getLogger().info("This plugin still spams the console!"); //$NON-NLS-1$
        Bukkit.getLogger().info("You will have to update JAR file and resources by yourself.."); //$NON-NLS-1$
        Bukkit.getLogger().info("Stop by to the spigot page to see if a new update has been released."); //$NON-NLS-1$
        Bukkit.getLogger().info("Please report all bugs to the spigot page!"); //$NON-NLS-1$
        Bukkit.getLogger().info("*****************************************"); //$NON-NLS-1$

        getConfig().addDefault("max-player-count", 4); //$NON-NLS-1$
        getConfig().addDefault("indev-resource-pack", false); //$NON-NLS-1$
        getConfig().addDefault("message-language", "EN");
        getConfig().options().copyDefaults(true);
        saveConfig();
        indevResourcePack = getConfig().getBoolean("indev-resource-pack"); //$NON-NLS-1$
        maxPlayerCount = getConfig().getInt("max-player-count"); //$NON-NLS-1$

        setPlugin(this);
        try {
            File f = getServer().getWorld("DuckMode").getWorldFolder(); //$NON-NLS-1$
            try {
                if (f.exists()) {
                    FileUtils.deleteDirectory(f);
                }
            } catch (IOException e) {
                getLogger().info("Could not delete DuckMode World."); //$NON-NLS-1$
            }
        } catch (NullPointerException e) {
        }
        WorldCreator c = new WorldCreator("DuckMode"); //$NON-NLS-1$
        c.generateStructures(false);
        c.type(WorldType.FLAT);
        c.generatorSettings("3;air"); //$NON-NLS-1$
        World w = getServer().createWorld(c);
        setWorld(w);
        w.setDifficulty(Difficulty.PEACEFUL);
        w.setAutoSave(false);
        w.setGameRuleValue("doDaylightCycle", "false"); //$NON-NLS-1$ //$NON-NLS-2$
        getServer().setSpawnRadius(0);
        SchematicLoad.addSchematic(new SchematicToLoad(new BukkitWorld(duckWorld), new Vector(0, 20, 0), "Static", "lobby", "lobby")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SchematicLoad.loadAllSchematics();
        state = GameState.LOBBY;
        ListenerActivator.activateListeners();
        new BukkitRunnable() { //This is in a delayed task because lobby might not be loaded yet
            @Override
            public void run() {
                DuckLobby.configureLobby();
            }

        }.runTaskLater(DuckMain.getPlugin(), 4L);

        spawnLocation = new Location(duckWorld, 4, 21, 5);
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) { //$NON-NLS-1$
            getLogger().info("Hey! Install WorldEdit!"); //$NON-NLS-1$
            getServer().shutdown();
        }

//		File source = new File(path + "/plugins/DuckMode/Resources/");
//		File goal = getWorld().getWorldFolder();
//		try {
//		    FileUtils.copyDirectory(source, goal);
        //		} catch (IOException e) {
//		    e.printStackTrace();
//		}
    }

    @Override
    public void onDisable() {
        getServer().unloadWorld("DuckMode", false); //$NON-NLS-1$
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("test")) { //$NON-NLS-1$
            StaticMethods.checkForWin();

        } else if (cmd.getName().equalsIgnoreCase("addsecrethats")) { //$NON-NLS-1$
            Hats.addSecretHats();
        }
        return true;
    }


}
