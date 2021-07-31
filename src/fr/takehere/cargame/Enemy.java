package fr.takehere.cargame;

import com.sun.istack.internal.Nullable;
import fr.takehere.cargame.display.display.GameFrame;
import fr.takehere.cargame.powerups.BulletPowerup;
import fr.takehere.cargame.powerups.HealthPowerup;
import fr.takehere.cargame.powerups.MegaPowerup;
import fr.takehere.cargame.powerups.ShotgunPowerup;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Enemy extends Actor{

    public boolean isAlive = true;
    public Timer bulletTimer = new Timer();
    public static List<Enemy> enemies = new ArrayList<>();

    public Enemy(@Nullable Dimension size, Point location, Image image) {
        super(size, location, image);

        enemies.add(this);
        Enemy enemy = this;

        bulletTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CarGame.getInstance().runNextFrame(() -> {
                    Bullet bullet = new Bullet(new Dimension(10,20), new Point(enemy.body.x, enemy.body.y), Utils.getImageRessource("enemyBullet.png"), true, true);
                    bullet.rotation = enemy.rotation;

                    bullet.velocityX = (float) (Math.sin(Math.toRadians(enemy.rotation)) * CarGame.getInstance().bulletSpeed);
                    bullet.velocityY = (float) ((Math.cos(Math.toRadians(enemy.rotation)) * CarGame.getInstance().bulletSpeed) * -1);
                });
            }
        }, Utils.randomNumberBetween(500,1000), Utils.randomNumberBetween(1000,1500));
    }

    Random random = new Random();

    public void destroy(){

        CarGame.getInstance().runNextFrame(() -> Player.get().score += 50);

        if (random.nextInt(3) == 0){
            dropPowerup(body.getLocation());
        }

        Object.objects.remove(this);
        Actor.actors.remove(this);
        enemies.remove(this);

        bulletTimer.cancel();
    }

    static boolean bulletPowerupDroppped = false;
    static boolean shotgunPowerupDroppped = false;

    public void dropPowerup(Point location){
        switch (random.nextInt(4)){
            case 0:
                new HealthPowerup(new Dimension(50,50), location, Utils.getImageRessource("healthPowerup.png"));
                break;
            case 1:
                new MegaPowerup(new Dimension(50,50), location, Utils.getImageRessource("megaPowerup.png"));
                break;
            case 2:
                if (bulletPowerupDroppped){
                    dropPowerup(location);
                    return;
                }

                bulletPowerupDroppped = true;
                new BulletPowerup(new Dimension(50,50), location, Utils.getImageRessource("bulletPowerup.png"));
                break;
            case 3:
                if (shotgunPowerupDroppped){
                    dropPowerup(location);
                    return;
                }

                shotgunPowerupDroppped = true;
                new ShotgunPowerup(new Dimension(50,50), location, Utils.getImageRessource("shotgunPowerup.png"));
                break;
        }
    }

    public static void moveToPlayer(){
        Player player = Player.get();

        for (Enemy enemy : enemies) {
            int distance = (int) Math.sqrt(Math.pow(player.body.x - enemy.body.x,2) + Math.pow(player.body.y - enemy.body.y, 2));
            double speed = Utils.mapDecimal(distance, 1,300,0,5);

            enemy.rotation = Math.toDegrees(Math.atan2(player.body.y - enemy.body.y, player.body.x - enemy.body.x)) + 90;

            enemy.velocityX = (float) (Math.sin(Math.toRadians(enemy.rotation)) * speed);
            enemy.velocityY = (float) ((Math.cos(Math.toRadians(enemy.rotation)) * speed) * -1);
        }
    }

    public static int time = 3000;
    public static Timer enemiesTimer = new Timer();
    public static boolean titleSent = false;

    public static void generateEnemies(){
        enemiesTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random rdm = new Random();
                if (time >= 500){
                    time -= 30;
                }else if (!titleSent){
                    new Title("Manche finale !", new Point(GameFrame.width/2 - 200,250), new Color(226, 26, 26));
                    titleSent = true;
                }

                CarGame.getInstance().runNextFrame(() -> new Enemy(new Dimension(50,80),
                        new Point(rdm.nextInt(GameFrame.width + 30),rdm.nextInt(GameFrame.height) + 30),
                        Utils.getImageRessource("police.png")));
                generateEnemies();
            }
        }, time);
    }
}
