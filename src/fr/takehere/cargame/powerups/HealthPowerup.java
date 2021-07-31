package fr.takehere.cargame.powerups;

import com.sun.istack.internal.Nullable;
import fr.takehere.cargame.CarGame;
import fr.takehere.cargame.Object;
import fr.takehere.cargame.Player;

import java.awt.*;

public class HealthPowerup extends Object {

    public HealthPowerup(@Nullable Dimension size, Point location, Image image) {
        super(size, location, image);

        CarGame.getInstance().powerups.add(this);
    }

    public void collectPowerup(){
        HealthPowerup powerup = this;

        CarGame.getInstance().runNextFrame(new Runnable() {
            @Override
            public void run() {
                Player.get().health++;

                CarGame.getInstance().powerups.remove(powerup);
                Object.objects.remove(powerup);
            }
        });
    }
}
