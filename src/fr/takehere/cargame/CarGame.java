package fr.takehere.cargame;

import fr.takehere.cargame.display.display.GameFrame;
import fr.takehere.cargame.display.display.GamePane;

import javax.sound.sampled.AudioInputStream;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.sql.Time;
import java.util.*;
import java.util.List;

public class CarGame {

    private static CarGame instance;
    public final int bulletSpeed = 15;
    public final float cooldown = 0.5f;
    public final boolean debug = false;

    public List<Object> powerups = new ArrayList<>();

    public List<String> fireSounds = new ArrayList<>();
    public List<String> shotgunSounds = new ArrayList<>();
    public List<String> megapowerupsSounds = new ArrayList<>();

    public static void main(String[] args) {
        instance = new CarGame();

        instance.fireSounds.add("sounds/firesound1.wav");
        instance.fireSounds.add("sounds/firesound2.wav");
        instance.fireSounds.add("sounds/firesound3.wav");
        instance.fireSounds.add("sounds/firesound4.wav");
        instance.fireSounds.add("sounds/firesound5.wav");

        instance.shotgunSounds.add("sounds/shotgunSound1.wav");
        instance.shotgunSounds.add("sounds/shotgunSound2.wav");
        instance.shotgunSounds.add("sounds/shotgunSound3.wav");
        instance.shotgunSounds.add("sounds/shotgunSound4.wav");

        instance.megapowerupsSounds.add("sounds/megapowerupSound1.wav");
        instance.megapowerupsSounds.add("sounds/megapowerupSound2.wav");
        instance.megapowerupsSounds.add("sounds/megapowerupSound3.wav");
        instance.megapowerupsSounds.add("sounds/megapowerupSound4.wav");

        Player.get();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                instance.runNextFrame(() -> Player.get().score += 100);
            }
        },0,1000);

        Enemy.generateEnemies();

        GameFrame gameFrame = GameFrame.get();
    }


    int targetFps = 80;
    int targetTime = 1000000000 / targetFps;
    int fps = 0;
    long launchTime = System.currentTimeMillis();

    Point lastMousePos;

    private List<Runnable> runNextFrame = new ArrayList<>();

    public void gameLoop() {
        Player player = Player.get();

        //--------< Frame calculations >-------
        long startTime = System.nanoTime();

        for (java.lang.Object o : runNextFrame.toArray()) {
            Runnable runnable = (Runnable) o;
            if (runnable != null)
                runnable.run();
        }
        runNextFrame.clear();

        //-------< Actors and Player movements >------

        Enemy.moveToPlayer();

        Point mousePos = GamePane.get().getMousePosition();
        if (mousePos != null)
            lastMousePos = mousePos;

        if (lastMousePos != null){
            //Calculate distance between mouse and player for calculating speed
            int distance = (int) Math.sqrt(Math.pow(lastMousePos.x - player.body.x,2) + Math.pow(lastMousePos.y - player.body.y, 2));
            double speed = Utils.mapDecimal(distance, 1,300,0,30);

            //Rotate to follow the mouse
            player.rotation = Math.toDegrees(Math.atan2(lastMousePos.y - player.body.y, lastMousePos.x - player.body.x)) + 90;

            //Move player
            player.velocityX = (float) (Math.sin(Math.toRadians(player.rotation)) * speed);
            player.velocityY = (float) ((Math.cos(Math.toRadians(player.rotation)) * speed) * -1);
        }

        for (Actor actor : Actor.actors) {
            actor.body.x += actor.velocityX;
            actor.body.y += actor.velocityY;
        }

        //--------< Fps calculations >--------
        long totalTime = System.nanoTime() - startTime;

        if (totalTime < targetTime){
            try {
                Thread.sleep((targetTime - totalTime) / 1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        fps++;
        if (System.currentTimeMillis() - launchTime > 1000){
            GameFrame.get().setTitle(GameFrame.title + " | fps: " + fps);
            launchTime += 1000;
            fps = 0;
        }
        GamePane.get().repaint();
    }

    public void runNextFrame(Runnable runnable){
        runNextFrame.add(runnable);
    }

    public static CarGame getInstance() {
        return instance;
    }
}
