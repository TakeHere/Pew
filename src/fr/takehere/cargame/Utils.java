package fr.takehere.cargame;


import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Random;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class Utils {

    public static Random random = new Random();

    public static Image resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight){
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        return resultingImage;
    }

    public static Image getImageRessource(String name){
        try {
            return ImageIO.read(CarGame.getInstance().getClass().getResource("ressources/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AudioInputStream getSoundRessource(String name){
        try {
            return getAudioInputStream(CarGame.getInstance().getClass().getResource("ressources/" + name));
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void playSound(AudioInputStream audio){
        try {
            AudioFormat outDataFormat = new AudioFormat(44100.0f, 16, audio.getFormat().getChannels(), true, audio.getFormat().isBigEndian());
            AudioInputStream outputAudio = AudioSystem.getAudioInputStream(outDataFormat, audio);

            Clip clip = AudioSystem.getClip();
            clip.open(outputAudio);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static double mapDecimal(double val, double in_min, double in_max, double out_min, double out_max) {
        return (val - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static int randomNumberBetween(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    public static void restartApplication(){
        StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
        cmd.append(CarGame.class.getName()).append(" ");
        try {
            Runtime.getRuntime().exec(cmd.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void playFireSound(){
        CarGame carGame = CarGame.getInstance();

        AudioInputStream audio = getSoundRessource(carGame.fireSounds.get(random.nextInt(carGame.fireSounds.size())));

        playSound(audio);
    }

    public static void playShotgunSound(){
        CarGame carGame = CarGame.getInstance();

        AudioInputStream audio = getSoundRessource(carGame.shotgunSounds.get(random.nextInt(carGame.shotgunSounds.size())));

        playSound(audio);
    }

    public static void playMegapowerupSound(){
        CarGame carGame = CarGame.getInstance();

        AudioInputStream audio = getSoundRessource(carGame.megapowerupsSounds.get(random.nextInt(carGame.megapowerupsSounds.size())));

        playSound(audio);
    }
}
