package me.glatteis.duckmode.generation;

import com.google.gson.Gson;
import me.glatteis.duckmode.generation.config.Dimension;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Linus on 01.02.2016.
 */
public class JSONToDimensionParser {

    public static Dimension parse(File file) throws IOException {
        Gson gson = new Gson();

        InputStream stream = new FileInputStream(file);
        String jsonTxt = IOUtils.toString(stream);

        return gson.fromJson(jsonTxt, Dimension.class);
    }

}
