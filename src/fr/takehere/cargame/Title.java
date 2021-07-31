package fr.takehere.cargame;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Title {

    public String text;
    public Point location;
    public Color color;

    public static List<Title> titles = new ArrayList<>();

    public Title(String text, Point location, Color colorArg) {
        this.location = location;
        this.color = colorArg;

        try {
            byte[] ptext;
            ptext = text.getBytes("UTF-8");
            this.text = new String(ptext, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Title title = this;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (color.getAlpha() == 1){
                    timer.cancel();
                    titles.remove(title);
                }
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() - 1);
            }
        },0,15);

        titles.add(this);
    }
}
