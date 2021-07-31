package fr.takehere.cargame.display.display;


import fr.takehere.cargame.*;
import fr.takehere.cargame.Object;
import fr.takehere.cargame.powerups.BulletPowerup;
import fr.takehere.cargame.powerups.HealthPowerup;
import fr.takehere.cargame.powerups.MegaPowerup;
import fr.takehere.cargame.powerups.ShotgunPowerup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GamePane extends JPanel{
    CarGame mainInstance = CarGame.getInstance();
    private static GamePane instance;

    public JLabel megaPowerups = new JLabel();
    public JLabel score = new JLabel();
    public Image healthImage;

    public long lastGathered = System.currentTimeMillis();
    private GamePane() {
        this.setLayout(null);

        this.setFocusable(true);
        this.setBackground(Color.BLACK);

        this.healthImage = Utils.resizeImage((BufferedImage) Utils.getImageRessource("health.png"), 70,70);

        this.add(megaPowerups);
        this.add(score);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                Player player = Player.get();

                if (e.getButton() == 1){
                    //------< Shoot bullets >------

                    if (player.shotgunPowerup){
                        if ((System.currentTimeMillis() - lastGathered) >= mainInstance.cooldown * 1000) {
                            lastGathered = System.currentTimeMillis();

                            Bullet bullet1 = new Bullet(new Dimension(10,20), new Point(player.body.x, player.body.y), Utils.getImageRessource("bullet.png"), false, false);
                            Bullet bullet2 = new Bullet(new Dimension(10,20),
                                    new Point((int) Math.sin(Math.toRadians(player.rotation - 10)) + player.body.x, (int) Math.cos(Math.toRadians(player.rotation - 10)) + player.body.y), Utils.getImageRessource("bullet.png"), false, false);
                            Bullet bullet3 = new Bullet(new Dimension(10,20),
                                    new Point((int) Math.sin(Math.toRadians(player.rotation + 10)) + player.body.x, (int) Math.cos(Math.toRadians(player.rotation + 10)) + player.body.y), Utils.getImageRessource("bullet.png"), false, false);

                            bullet1.rotation = player.rotation;
                            bullet2.rotation = player.rotation - 30;
                            bullet3.rotation = player.rotation + 30;

                            bullet1.velocityX = (float) (Math.sin(Math.toRadians(bullet1.rotation)) * mainInstance.bulletSpeed);
                            bullet1.velocityY = (float) ((Math.cos(Math.toRadians(bullet1.rotation)) * mainInstance.bulletSpeed) * -1);

                            bullet2.velocityX = (float) (Math.sin(Math.toRadians(bullet2.rotation)) * mainInstance.bulletSpeed);
                            bullet2.velocityY = (float) ((Math.cos(Math.toRadians(bullet2.rotation)) * mainInstance.bulletSpeed) * -1);

                            bullet3.velocityX = (float) (Math.sin(Math.toRadians(bullet3.rotation)) * mainInstance.bulletSpeed);
                            bullet3.velocityY = (float) ((Math.cos(Math.toRadians(bullet3.rotation)) * mainInstance.bulletSpeed) * -1);

                            Utils.playShotgunSound();
                        }
                    }else {
                        mainInstance.runNextFrame(() -> {
                            Bullet bullet = new Bullet(new Dimension(10,20), new Point(player.body.x, player.body.y), Utils.getImageRessource("bullet.png"), false, true);
                            bullet.rotation = player.rotation;

                            bullet.velocityX = (float) (Math.sin(Math.toRadians(player.rotation)) * mainInstance.bulletSpeed);
                            bullet.velocityY = (float) ((Math.cos(Math.toRadians(player.rotation)) * mainInstance.bulletSpeed) * -1);
                        });
                    }
                }else if (player.megaPowerup > 0){
                    //----------< Shoot megapowerup >--------

                    for (int i = 0; i < 360; i += 45) {
                        Bullet bullet = new Bullet(new Dimension(10,20), new Point(
                                        (int) Math.sin(Math.toRadians(player.rotation + i)) + player.body.x,
                                        (int) Math.cos(Math.toRadians(player.rotation + i)) + player.body.y),
                                Utils.getImageRessource("bullet.png"), false, false);

                        bullet.rotation = player.rotation + i;

                        bullet.velocityX = (float) (Math.sin(Math.toRadians(bullet.rotation)) * mainInstance.bulletSpeed);
                        bullet.velocityY = (float) ((Math.cos(Math.toRadians(bullet.rotation)) * mainInstance.bulletSpeed) * -1);
                    }

                    player.megaPowerup--;

                    Utils.playMegapowerupSound();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    public static GamePane get(){
        if (instance == null){
            instance = new GamePane();
        }
        return instance;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        Player player = Player.get();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.RED);

        //-------< Objects Rendering >------
        for (Object object : Object.objects) {
            AffineTransform tr = new AffineTransform();

            tr.translate(object.body.x, object.body.y);
            tr.rotate(
                    Math.toRadians(object.rotation),
                    object.body.width / 2,
                    object.body.height - object.body.height
            );
            g2d.drawImage(object.image, tr, null);

            object.collision = tr.createTransformedShape(new Rectangle(object.body.width, object.body.height));

            if (mainInstance.debug){
                g2d.draw(object.collision);
            }
        }

        //--------< Bullets behaviour >-------

        List<Enemy> enemiesToDestroy = new ArrayList<>();
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : Bullet.bullets) {
            if (!bullet.enemyBullet){
                for (Enemy enemy : Enemy.enemies) {
                    if (bullet.collision.intersects(enemy.collision.getBounds())){
                        if (enemy.isAlive){
                            enemy.isAlive = false;
                            enemiesToDestroy.add(enemy);
                            bulletsToRemove.add(bullet);
                        }
                    }
                }
            }else if (bullet.collision.intersects(Player.get().collision.getBounds())){
                bulletsToRemove.add(bullet);
                Player.get().health --;
                if (Player.get().health == 0){
                    Player.get().death();
                }
            }
        }

        for (Enemy enemy : enemiesToDestroy) {
            enemy.destroy();
        }

        for (Bullet bullet : bulletsToRemove) {
            if (bullet.enemyBullet || player.destroyBulletOnImpact){
                Object.objects.remove(bullet);
                Actor.actors.remove(bullet);
                Bullet.bullets.remove(bullet);
            }
        }

        //--------< Collecting powerups >-------
        for (Object powerup : mainInstance.powerups) {
            if (powerup.collision != null){
                if (powerup.collision.intersects(Player.get().collision.getBounds())){
                    if (powerup instanceof HealthPowerup){
                        ((HealthPowerup) powerup).collectPowerup();
                        Utils.playSound(Utils.getSoundRessource("sounds/healthSound.wav"));
                    }else if (powerup instanceof BulletPowerup){
                        ((BulletPowerup) powerup).collectPowerup();
                        Utils.playSound(Utils.getSoundRessource("sounds/upgradeSound.wav"));
                    }else if (powerup instanceof ShotgunPowerup){
                        ((ShotgunPowerup) powerup).collectPowerup();
                        Utils.playSound(Utils.getSoundRessource("sounds/upgradeSound.wav"));
                    }else if (powerup instanceof MegaPowerup){
                        ((MegaPowerup) powerup).collectPowerup();
                        Utils.playSound(Utils.getSoundRessource("sounds/megapowerupPickup.wav"));
                    }
                }
            }
        }

        //------< Draw UI Elements >--------
        megaPowerups.setForeground(new Color(21, 98, 222));
        megaPowerups.setFont(new Font("Bahnschrift", Font.BOLD, 50));
        megaPowerups.setText("MegaPowerups: " + Player.get().megaPowerup);

        score.setForeground(new Color(21, 98, 222));
        score.setFont(new Font("Bahnschrift", Font.BOLD, 50));
        score.setText("Score: " + Player.get().score);

        megaPowerups.setSize(megaPowerups.getPreferredSize());
        megaPowerups.setLocation(GameFrame.width / 2 - megaPowerups.getWidth() / 2,80);

        score.setSize(score.getPreferredSize());
        score.setLocation((GameFrame.width / 4) * 3,80);


        //-------< Hearts Rendering >------
        int heartOffset = (healthImage.getWidth(null) / 2);

        for (int i = 0; i < player.health; i++) {
            g2d.drawImage(healthImage, heartOffset + (heartOffset * i), heartOffset, healthImage.getWidth(null), healthImage.getHeight(null), null);
        }

        //-------< Draw titles >---------
        for (Title title : Title.titles) {
            g2d.setColor(title.color);
            g2d.setFont(new Font("Bahnschrift",Font.BOLD,50));
            g2d.drawString(title.text, title.location.x, title.location.y);
        }

        super.paintComponents(g);
        mainInstance.gameLoop();
    }
}