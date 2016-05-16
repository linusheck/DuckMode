package me.glatteis.duckmode.levelcreator;

import me.glatteis.duckmode.generation.config.Dimension;

import javax.swing.*;

/**
 * Created by Linus on 30.04.2016.
 */
public class DimensionEditor extends JFrame {

    private JPanel panel;
    private Dimension dimension;

    public DimensionEditor(Dimension dimension) {
        super("DuckMode Dimension Editor");
        this.dimension = dimension == null ? new Dimension() : dimension;
        panel = new JPanel();
        add(panel);
    }

}
