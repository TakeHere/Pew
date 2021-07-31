package fr.takehere.cargame;

import com.sun.istack.internal.Nullable;
import fr.takehere.cargame.display.display.GameFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends Actor{

    public static List<Bullet> bullets = new ArrayList<>();
    public boolean enemyBullet;

    public Bullet(@Nullable Dimension size, Point location, Image image, boolean enemyBullet, boolean playSound) {
        super(size, location, image);
        this.enemyBullet = enemyBullet;

        bullets.add(this);
        if (playSound)
            Utils.playFireSound();
    }
}
