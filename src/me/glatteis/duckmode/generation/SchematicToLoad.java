package me.glatteis.duckmode.generation;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import me.glatteis.duckmode.generation.config.Dimension;
import me.glatteis.duckmode.generation.config.DimensionContainer;

public class SchematicToLoad {

    private BukkitWorld world;
    private Vector vector;
    private DimensionContainer dimensionContainer;
    private Dimension dimensionData;
    private int rotation; //angle
    private int axis; //0 == x, 1 == y, 2 == z

    public SchematicToLoad(BukkitWorld world, Vector vector, DimensionContainer dimensionContainer, Dimension dimensionData, int rotation, int axis) {
        this.world = world;
        this.vector = vector;
        this.dimensionContainer = dimensionContainer;
        this.dimensionData = dimensionData;
        this.rotation = rotation;
        this.axis = axis;
    }

    public Dimension getDimensionData() {
        return dimensionData;
    }

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

    public DimensionContainer getDimensionContainer() {
        return dimensionContainer;
    }

    public int getRotation() {
        return rotation;
    }

    public int getAxis() {
        return axis;
    }
}
