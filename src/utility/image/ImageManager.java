package utility.image;

import utility.ContentValues;
import utility.InvadeError;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    private final static String path = "images/";
    private final static String ext = ".png";

    private static Map<String, Image> images = new HashMap<String, Image>();

    public static Image loadImage(String fname) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path + fname + ext));
        } catch (IOException e) {
            InvadeError.show(path + fname + ext + " missing!");
        }
        images.put(fname, img);
        return img;
    }

    public static void initImages() {
        ArrayList<String> imageNames = new ArrayList<>();
        imageNames.add("text/mainShip");
        imageNames.add("text/captainManol");
        imageNames.add("text/skyBackground");
        imageNames.add("text/winText");
        imageNames.add("text/startText");
        imageNames.add("text/deathText");
        imageNames.add("text/titleText");
        imageNames.add("text/selector");
        imageNames.add("text/play");
        imageNames.add("text/quit");
        imageNames.add("text/credits");
        imageNames.add("text/comboText");
        imageNames.add("text/maxComboText");
        imageNames.add("text/comboDigits");
        imageNames.add("projectiles/laser");
        imageNames.add("effects/sparks");
        imageNames.add("effects/reflectionSparks");
        imageNames.add("effects/muzzleFlash");
        imageNames.add("effects/speakerUI");

        for (int i = ContentValues.NUMBER_DIFFERENT_PLANETS; i > 0; i--) {
            imageNames.add("planets/planet" + i);
        }
        for (int i = ContentValues.NUMBER_DIFFERENT_GEMS; i > 0; i--) {
            imageNames.add("effects/gems/gem" + i);
        }
        for (int i = 8; i > 0; i--) {
            imageNames.add("projectiles/alienShot" + i);
        }
        for (int i = 8; i > 0; i--) {
            imageNames.add("projectiles/shot" + i);
        }
        for (int i = 8; i > 0; i--) {
            imageNames.add("aliens/alienShip" + i);
        }
        for (int i = 4; i > 0; i--) {
            imageNames.add("effects/explosion" + i);
        }
        loadImages(imageNames);
    }

    public static void loadImages(Iterable<String> fNames) {
        for (String s : fNames) {
            loadImage(s);
        }
    }

    public static Image getImage(String s) {
        if (!images.containsKey(s)) {
            InvadeError.show("Image not loaded properly. key=\"" + s + "\"");
        }
        return images.get(s);
    }
}