package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class ChickenHat extends EntityHat {

    public ChickenHat() {
        super(Messages.getString("ChickenHat.head"), Messages.getString("ChickenHat.description"), new ItemStack(Material.SEEDS));
        entities = new Entity[2];
//		entities[0] = DuckMain.getWorld().spawnEntity(new Location(DuckMain.getWorld(), 0, 400, 0), EntityType.ARMOR_STAND);
//		entities[0].setMetadata("invisible", new FixedMetadataValue(DuckMain.getPlugin(), true));
        entities[0] = DuckMain.getWorld().spawnEntity(new Location(DuckMain.getWorld(), 0, 400, 0), EntityType.CHICKEN);
    }

}
