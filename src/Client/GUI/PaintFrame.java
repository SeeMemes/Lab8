package Client.GUI;

import Client.GUI.DrawObjects.Display;
import Server.Database.Credentials;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PaintFrame extends JFrame {
    Credentials credentials;
    ArrayList<String []> humanList;
    ResourceBundle resourceBundle;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    public final static int INTERVAL = 500;

    public PaintFrame(Credentials credentials, ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new GridLayout());
        Display display = new Display(credentials, resourceBundle);
        add(display);
        Timer timer = new Timer(INTERVAL, evt -> {
            display.repaint();
            display.revalidate();
        });

        timer.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                timer.stop();
            }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
