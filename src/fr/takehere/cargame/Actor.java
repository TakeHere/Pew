package fr.takehere.cargame;

import com.sun.istack.internal.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Actor extends Object{

    public float velocityX;
    public float velocityY;

    public static List<Actor> actors = new ArrayList<>();

    public Actor(@Nullable Dimension size, Point location, Image image) {
        super(size, location, image);

        actors.add(this);
    }
}
