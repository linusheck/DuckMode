package me.glatteis.duckmode;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;

public class SchematicToLoad {

    private BukkitWorld world;
    private Vector vector;
    private String dimension;
    private String story;
    private String type;

    public BukkitWorld getWorld() {
        return world;
    }

    public void setWorld(BukkitWorld world) {
        this.world = world;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SchematicToLoad(BukkitWorld world, Vector vector, String dimension, String story, String type) {
        this.world = world;
        this.vector = vector;
        this.dimension = dimension;
        this.story = story;
        this.type = type;
    }

}
