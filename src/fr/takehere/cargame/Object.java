package fr.takehere.cargame;

import com.sun.istack.internal.Nullable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Object {

    public Rectangle body;
    public Shape collision;
    public Image image;

    public double rotation;

    public static List<Object> objects = new ArrayList<>();

    public Object(@Nullable Dimension size, Point location, Image image) {
        if (size != null) {
            this.image = Utils.resizeImage((BufferedImage) image, size.width, size.height);
        }else {
            this.image = image;
        }

        this.body = new Rectangle(location.x, location.y, this.image.getWidth(null), this.image.getHeight(null));

        objects.add(this);
    }
}
