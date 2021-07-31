package fr.takehere.cargame;

import com.sun.istack.internal.Nullable;
import fr.takehere.cargame.display.display.DeathPane;
import fr.takehere.cargame.display.display.GameFrame;
import fr.takehere.cargame.display.display.GamePane;

import javax.sound.sampled.Clip;
import java.awt.*;

public class Player extends Actor{
    private static Player instance;
    public int health = 5;

    public boolean destroyBulletOnImpact = true;
    public boolean shotgunPowerup = false;
    public int megaPowerup = 0;

    public int score = 0;

    private Player(@Nullable Dimension size, Point location, Image image) {
        super(size, location, image);
    }

    public static Player get(){
        if (instance == null){
            instance = new Player(new Dimension(70, 100), new Point(250,250), Utils.getImageRessource("player.png"));
        }

        return instance;
    }

    public void death() {
        GamePane.get().setVisible(false);
        GameFrame.get().setContentPane(DeathPane.get());
    }
}
