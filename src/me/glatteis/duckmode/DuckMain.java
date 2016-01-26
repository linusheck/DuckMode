package me.glatteis.duckmode;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import me.glatteis.duckmode.hats.Hats;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuckMain extends JavaPlugin {


    public static int maxPlayerCount = 4;
    public static boolean indevResourcePack = false;
    public static List<Duck> ducks = new ArrayList<Duck>();
    public static GameState state;
    public static Plugin plugin;
    public static HashMap<Integer, Duck> duckCount = new HashMap<Integer, Duck>();
    public static Location spawnLocation;
    public static int autoStart;
    private static String resourcesVersion = "RESOURCES - 0.4.5";
    private static World duckWorld;
    private String duckModeConsoleWelcome = "\n\n" +
            "######                       #     #                      \n" +
            "#     # #    #  ####  #    # ##   ##  ####  #####  ###### \n" +
            "#     # #    # #    # #   #  # # # # #    # #    # #      \n" +
            "#     # #    # #      ####   #  #  # #    # #    # #####  \n" +
            "#     # #    # #      #  #   #     # #    # #    # #      \n" +
            "#     # #    # #    # #   #  #     # #    # #    # #      \n" +
            "######   ####   ####  #    # #     #  ####  #####  ###### \n\n" +
            "Version " + this.getDescription().getVersion() + "\n\n" +
            "Look out for updates: www.spigotmc.org/resources/9108/\n\n";

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
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    private void writeVersion(String versionFile) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(versionFile, "UTF-8");
        writer.print(resourcesVersion);
        writer.flush();
        writer.close();
    }

    @Override
    public void onLoad() {
        try {
            String duckModeFolder = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +
                    "/plugins/DuckMode";
            File versionFile = new File(duckModeFolder + "/version.txt");
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
            String duckModeFile = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +
                    "/plugins/DuckMode.zip";
            getLogger().info("Copying DuckMode folder...");

            URL resourceURL = getClass().getResource("/resources/DuckMode.zip");
            File dest = new File(duckModeFile);
            FileUtils.copyURLToFile(resourceURL, dest);
            String folder = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +
                    "/plugins/";
            StaticMethods.unZipIt(duckModeFile, folder);
            writeVersion(versionFile.toString());
            getLogger().info("Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        getLogger().info("Setting up DuckMode...");

        setPlugin(this);
        configSetup();

        getLogger().info("Getting world folder...");

        File f = new File(new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() +
                "/DuckMode/");
        try {
            if (f.exists()) {
                FileUtils.deleteDirectory(f);
            }
        } catch (IOException e) {
            getLogger().info("Could not delete DuckMode World.");
        }

        getLogger().info("Creating world...");

        WorldCreator c = new WorldCreator("DuckMode");
        c.generateStructures(false);
        c.type(WorldType.FLAT);
        c.generatorSettings("3;air");
        World w = getServer().createWorld(c);
        setWorld(w);
        w.setDifficulty(Difficulty.PEACEFUL);
        w.setAutoSave(false);
        w.setGameRuleValue("doDaylightCycle", "false");
        getServer().setSpawnRadius(0);

        getLogger().info("Generating lobby...");

        SchematicLoad.addSchematic(new SchematicToLoad(new BukkitWorld(duckWorld), new Vector(0, 20, 0), "Static", "lobby", "lobby"));
        SchematicLoad.loadAllSchematics(new SchematicLoad.ThenTask() {
            @Override
            public void finished() {
                DuckLobby.configureLobby();
            }
        });

        getLogger().info("Activating listeners...");

        state = GameState.LOBBY;
        ListenerActivator.activateListeners();

        spawnLocation = new Location(duckWorld, 4, 21, 5);
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            getLogger().info("Hey! Install WorldEdit!");
            getServer().shutdown();
        }

        Bukkit.getLogger().info(duckModeConsoleWelcome);
    }

    @Override
    public void onDisable() {
        getServer().unloadWorld("DuckMode", false);
        plugin = null; //Have you ever thought about the fact that this line is basically the instance commiting suicide?
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("test")) {
            StaticMethods.checkForWin();
        } else if (cmd.getName().equalsIgnoreCase("addsecrethats")) {
            Hats.addSecretHats();
        } else if (cmd.getName().equalsIgnoreCase("start")) {
            ListenerActivator.lobbyCountdown();
        }
        return true;
    }

    private void configSetup() {
        getConfig().addDefault("max-player-count", 4);
        getConfig().addDefault("indev-resource-pack", false);
        getConfig().addDefault("message-language", "EN");
        getConfig().addDefault("auto-start-player-count", 0);
        getConfig().options().copyDefaults(true);
        saveConfig();
        indevResourcePack = getConfig().getBoolean("indev-resource-pack");
        maxPlayerCount = getConfig().getInt("max-player-count");
        autoStart = getConfig().getInt("auto-start-player-count");
    }
}
