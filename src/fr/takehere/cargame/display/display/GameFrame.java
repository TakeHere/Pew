package fr.takehere.cargame.display.display;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private static GameFrame instance;
    public static String title = "Pew";

    public static int width = 1500;
    public static int height = 1000;

    private GameFrame() throws HeadlessException {
        super(title);
        this.setSize(width,height);
        this.setContentPane(GamePane.get());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
    }

    public static GameFrame get(){
        if (instance == null){
            System.out.println("Gameframe initialisation");
            instance = new GameFrame();
        }
        return instance;
    }
}
