package me.glatteis.duckmode.levelcreator;

import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import me.glatteis.duckmode.generation.config.Dimension;

import javax.swing.*;

/**
 * Created by Linus on 30.04.2016.
 */
public class LevelCreator {

    private Dimension dimension;
    private JFrame currentFrame;

    public static void main(String[] args) {
        new LevelCreator();
    }

    public LevelCreator() {
        try {
            UIManager.setLookAndFeel(new WindowsClassicLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        currentFrame = new LoadOrCreateWindow(this);
        currentFrame.setVisible(true);
        currentFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


}
