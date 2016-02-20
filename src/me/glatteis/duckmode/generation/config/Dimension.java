package me.glatteis.duckmode.generation.config;

import java.util.List;

/**
 * Created by Linus on 01.02.2016.
 */
public class Dimension {

    private String name;
    private List<DimensionContainer> dimensionContainers;

    private int sizeX;
    private int sizeY;
    private int sizeZ;

    private Integer maxX;
    private Integer maxY;
    private Integer maxZ;

    private Integer minX;
    private Integer minY;
    private Integer minZ;

    public List<DimensionContainer> getDimensionContainers() {
        return dimensionContainers;
    }

    public String getName() {
        return name;
    }

    public Integer getMinZ() {
        return minZ;
    }

    public Integer getMaxY() {
        return maxY;
    }

    public Integer getMaxZ() {
        return maxZ;
    }

    public Integer getMinX() {
        return minX;
    }

    public Integer getMinY() {
        return minY;
    }

    public Integer getMaxX() {
        return maxX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void init(int maxHeight) {
        for (DimensionContainer container : dimensionContainers) {
            container.init(maxHeight);
        }
    }

    public Dimension(String name) {
        this.name = name;
    }

    public Dimension() { }


}
