package me.glatteis.duckmode.generation.config;

import me.glatteis.duckmode.generation.SchematicToLoad;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by Linus on 10.02.2016.
 */
public class SpawnNextTo {

    private Integer x;
    private Integer y;
    private Integer z;

    private List<String> containers;

    public boolean hasPlace(SchematicToLoad[][][] map, Vector myPosition, DimensionContainer container, int rotation, int axis) {

        if (containers == null) return true;

        Vector lookPositionRotated = myPosition.add(rotateVector(getXYZ(), rotation, axis));

        return checkPlace(map, lookPositionRotated, container);

    }

    private boolean checkPlace(SchematicToLoad[][][] map, Vector lookPosition, DimensionContainer container) {

        if (isOutOfBounds(map, lookPosition)) return false;

        //Now we have the container we want to take a look at...

        SchematicToLoad schematicToLoad;

        try {
            schematicToLoad = map[lookPosition.getBlockX()][lookPosition.getBlockY()][lookPosition.getBlockZ()];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        if (schematicToLoad == null) return false;

        //Let's see if we want that container:
        if (!containers.contains(schematicToLoad.getDimensionContainer().getName())) return false;

        Vector toLookFor = lookPosition.clone().multiply(-1);

        for (SpawnNextTo spawnNextTo : schematicToLoad.getDimensionContainer().getSpawnNextTo()) {
            if (spawnNextTo.getXYZ().equals(toLookFor)) {
                if (!spawnNextTo.getContainers().contains(container.getName())) return false;
            }
        }

        return true;
    }

    private Vector rotateVector(Vector vector, int degrees, int axis) {

        vector = vector.clone();

        degrees /= 90;

        Vector[] matrix = {
                new Vector(0, 0, -1),
                new Vector(0, -1, 0)
        };

        for (; axis > 0; axis--) {
            for (int i = 0; i < matrix.length; i++) {
                matrix[i] = new Vector(matrix[i].getZ(), matrix[i].getX(), matrix[i].getY());
            }
        }

        for (; degrees > 0; degrees--) {

            Vector rotation = matrix[degrees % matrix.length];

            vector = vector.multiply(rotation);

        }

        return vector;

    }

    private boolean isOutOfBounds(SchematicToLoad[][][] map, Vector position) {
        return (!(map.length >= position.getBlockX() && position.getBlockX() > -1 &&
                map[0].length >= position.getBlockY() && position.getBlockY() > -1 &&
                map[0][0].length >= position.getBlockZ() && position.getBlockZ() > -1));
    }

    private Vector getXYZ() {
        if (x == null) x = 0;
        if (y == null) y = 0;
        if (z == null) z = 0;
        return new Vector(x, y, z);
    }

    private List<String> getContainers() {
        return containers;
    }

}
