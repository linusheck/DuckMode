package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RandomSkin extends Hat {

    List<String> words = new ArrayList<String>();

    public RandomSkin() {
        super(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3), Messages.getString("RandomSkin.head"), Messages.getString("RandomSkin.description"));
        URL url = getClass().getResource("/resources/englishwords.txt");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "eis";
            do {
                if (line.length() < 17 && line.length() > 2) {
                    words.add(line);
                }
                line = br.readLine();
            } while (line != null);
            br.close();
        } catch (IOException e) {
        }

    }

    @Override
    public ItemStack getStack() {
        ItemStack i = item;
        SkullMeta m = (SkullMeta) i.getItemMeta();
        m.setOwner(words.get((int) (Math.random() * (words.size() - 1))));
        i.setItemMeta(m);
        return i;
    }

}
