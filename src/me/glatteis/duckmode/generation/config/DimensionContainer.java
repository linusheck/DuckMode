package me.glatteis.duckmode.generation.config;

import java.util.Collections;
import java.util.List;

/**
 * Created by Linus on 01.02.2016.
 */
public class DimensionContainer implements Cloneable {

    private String name;

    private Integer startSpawnFrom;
    private Integer endSpawnAt;
    private double chance;
    private String type;
    private int priority;
    private List<SpawnNextTo> spawnNextTo;
    private boolean[] rotate;

    public DimensionContainer(String name) {
        this.name = name;
    }

    public DimensionContainer() {
    }

    public String getName() {
        return name;
    }

    public Integer getStartSpawnFrom() {
        return startSpawnFrom;
    }

    public Integer getEndSpawnAt() {
        return endSpawnAt;
    }

    public double getChance() {
        return chance;
    }

    public int getPriority() {
        return priority;
    }

    public String getType() {
        return type;
    }

    public boolean[] getRotate() {
        return rotate;
    }

    public List<SpawnNextTo> getSpawnNextTo() {
        if (spawnNextTo == null) spawnNextTo = Collections.emptyList();
        return spawnNextTo;
    }

    public void init(int maxHeight) {
        if (startSpawnFrom != null && startSpawnFrom < 0) startSpawnFrom = maxHeight - startSpawnFrom;
        if (endSpawnAt != null && endSpawnAt < 0) endSpawnAt = maxHeight - endSpawnAt;
        if (rotate == null || rotate.length != 3) rotate = new boolean[]{false, true, false};
    }

}
