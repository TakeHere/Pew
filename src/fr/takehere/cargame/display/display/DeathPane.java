package fr.takehere.cargame.display.display;

import fr.takehere.cargame.Player;
import fr.takehere.cargame.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.AttributedString;

public class DeathPane extends JPanel {

    private static DeathPane instance;

    private DeathPane() {
        this.setFocusable(false);
        this.setLayout(null);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JButton playButton = new JButton("Rejouer");
        playButton.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        playButton.setFocusable(false);
        playButton.setSize(new Dimension(200,70));
        playButton.setLocation((GameFrame.width/2) - (playButton.getWidth() / 2), GameFrame.height / 2);

        JLabel score = new JLabel();
        score.setForeground(Color.BLUE);
        score.setFont(new Font("Bahnschrift", Font.BOLD, 50));
        score.setText("Score: " + Player.get().score);
        score.setSize(score.getPreferredSize());
        score.setLocation((GameFrame.width/2) - (score.getWidth() / 2), GameFrame.height / 2 + playButton.getHeight() + 50);

        this.add(score);
        this.add(playButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.restartApplication();
            }
        });
    }

    public static DeathPane get(){
        if (instance == null){
            instance = new DeathPane();
        }
        return instance;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(Utils.getImageRessource("deathImage.png"), 0, 0, GameFrame.width, GameFrame.height, null);

        super.paintComponents(g);
    }
}
