package me.glatteis.duckmode;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import me.glatteis.duckmode.game.ContinueGame;
import me.glatteis.duckmode.game.DuckLobby;
import me.glatteis.duckmode.game.GameState;
import me.glatteis.duckmode.generation.SchematicLoader;
import me.glatteis.duckmode.generation.SchematicToLoad;
import me.glatteis.duckmode.generation.config.Dimension;
import me.glatteis.duckmode.generation.config.DimensionContainer;
import me.glatteis.duckmode.hats.Hats;
import me.glatteis.duckmode.listener.ListenerActivator;
import me.glatteis.duckmode.messages.Messages;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DuckMain extends JavaPlugin {


    private static DuckMain plugin;
    private static GameState state;
    public static final Dimension STATIC_DIMENSION = new Dimension("Static");
    private static World duckWorld;

    private final ContinueGame continueGame = new ContinueGame();
    private final List<Duck> ducks = new ArrayList<Duck>();
    private final List<Player> spectators = new ArrayList<Player>();
    private final HashMap<Integer, Duck> duckCount = new HashMap<Integer, Duck>();
    private Hats hats;
    private final String resourcesVersion = "RESOURCES - SNAPSHOT_19_03_2016";
    private String joinTitle = null;
    private String joinSubtitle = null;
    private int maxPlayerCount = 4;

    public static GameState getState() {
        return state;
    }

    public static Dimension getStaticDimension() {
        return STATIC_DIMENSION;
    }

    public static World getDuckWorld() {
        return duckWorld;
    }

    public ContinueGame getContinueGame() {
        return continueGame;
    }

    public List<Duck> getDucks() {
        return ducks;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public HashMap<Integer, Duck> getDuckCount() {
        return duckCount;
    }

    public Hats getHats() {
        return hats;
    }

    public String getResourcesVersion() {
        return resourcesVersion;
    }

    public String getJoinTitle() {
        return joinTitle;
    }

    public String getJoinSubtitle() {
        return joinSubtitle;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public boolean isIndevResourcePack() {
        return indevResourcePack;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getAutoStart() {
        return autoStart;
    }

    public String getDuckModeConsoleWelcome() {
        return duckModeConsoleWelcome;
    }

    private boolean indevResourcePack = false;
    private Location spawnLocation;
    private int autoStart;

    private final String duckModeConsoleWelcome = "\n\n" +
            "######                       #     #                      \n" +
            "#     # #    #  ####  #    # ##   ##  ####  #####  ###### \n" +
            "#     # #    # #    # #   #  # # # # #    # #    # #      \n" +
            "#     # #    # #      ####   #  #  # #    # #    # #####  \n" +
            "#     # #    # #      #  #   #     # #    # #    # #      \n" +
            "#     # #    # #    # #   #  #     # #    # #    # #      \n" +
            "######   ####   ####  #    # #     #  ####  #####  ###### \n\n" +
            "Version " + this.getDescription().getVersion() + "\n\n" +
            "Look out for updates: www.spigotmc.org/resources/9108/\n\n";

    public static DuckMain getPlugin() {
        return plugin;
    }

    public static void setPlugin(DuckMain p) {
        plugin = p;
    }

    public static World getWorld() {
        return duckWorld;
    }

    public static void setWorld(World w) {
        duckWorld = w;
    }

    public static void setState(GameState state) {
        DuckMain.state = state;
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
            unzip(duckModeFile, folder);
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
        Messages.enable();

        getLogger().info("Initializing all of the hats...");

        hats = new Hats();

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

        SchematicLoader.addSchematic(new SchematicToLoad(new BukkitWorld(duckWorld), new Vector(0, 20, 0),
                new DimensionContainer("lobby/lobby"), STATIC_DIMENSION, 0, 0));
        SchematicLoader.loadAllSchematics(new SchematicLoader.ThenTask() {
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
        plugin = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("addsecrethats")) {
            hats.addSecretHats();
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
        getConfig().addDefault("join-title", "%default%");
        getConfig().addDefault("join-subtitle", "%default%");
        getConfig().options().copyDefaults(true);
        saveConfig();
        indevResourcePack = getConfig().getBoolean("indev-resource-pack");
        maxPlayerCount = getConfig().getInt("max-player-count");
        autoStart = getConfig().getInt("auto-start-player-count");
        joinTitle = getConfig().getString("join-title").equals("%default%") ? null : getConfig().getString("join-title");
        joinSubtitle = getConfig().getString("join-subtitle").equals("%default%") ? null : getConfig().getString("join-subtitle");
    }

    private void unzip(String file, String outputDir) throws IOException {
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

}
