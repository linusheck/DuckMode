package me.glatteis.duckmode.levelcreator;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Linus on 30.04.2016.
 */
public class LoadOrCreateWindow extends JFrame {

    private LevelCreator creator;

    // duck source == http://www.stanford.edu/dept/CTL/cgi-bin/academicskillscoaching/wp-content/uploads/2012/07/baby-duck.jpg


    public LoadOrCreateWindow(LevelCreator creator) {
        super("DuckMode Level Editor");
        this.creator = creator;
        setResizable(false);
        setSize(600, 450);


        JPanel panel = new JPanelWithBackground("baby-duck.jpg");
        add(panel);
        panel.setOpaque(false);
        panel.setLayout(null);
        JLabel title = new JLabel("DuckMode Level Editor");
        title.setBounds(350, 350, 200, 40);
        title.setFont(new Font("Helvitica", Font.PLAIN, 14));
        title.setForeground(Color.WHITE);
        panel.add(title);
        JButton buttonNew = new JButton("Create new level");
        buttonNew.setBounds(50, 200, 200, 100);
        buttonNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DimensionEditor editor = new DimensionEditor(null);
                editor.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                editor.setVisible(true);
                setVisible(false);
                dispose();
            }
        });
        panel.add(buttonNew);
        JButton buttonLoad = new JButton("Load existing level");
        buttonLoad.setBounds(350, 200, 200, 100);
        panel.add(buttonLoad);
        JButton whatIsThis = new JButton("Help, what is this?");
        whatIsThis.setBounds(50, 350, 200, 40);
        panel.add(whatIsThis);
    }

    public static class JPanelWithBackground extends JPanel {

        //Credit: StackOverflow user Guillaume Polet

        private Image backgroundImage;

        // Some code to initialize the background image.
        // Here, we use the constructor to load the image. This
        // can vary depending on the use case of the panel.
        public JPanelWithBackground(String fileName) {
            try {
                backgroundImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the background image.
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }


}
