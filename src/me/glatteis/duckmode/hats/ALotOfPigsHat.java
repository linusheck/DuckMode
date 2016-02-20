package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class ALotOfPigsHat extends EntityHat {

    public ALotOfPigsHat() {
        super(Messages.getString("ALotOfPigsHat.head"), Messages.getString("ALotOfPigsHat.description"), new ItemStack(Material.PORK));
        entities = new Entity[8];
        Arrow a = (Arrow) DuckMain.getWorld().spawnEntity(new Location(DuckMain.getWorld(), 2, 21, 5), EntityType.ARROW);

        entities[0] = a;
        for (int i = 1; i < 8; i++) {
            entities[i] = DuckMain.getWorld().spawnEntity(new Location(DuckMain.getWorld(), 2, 21, 5), EntityType.PIG);
        }
    }


}
