package utility.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ImageManager {

	private final static String path = "images/";
	private final static String ext = ".png";

	private static Map<String, Image> images = new HashMap<String, Image>();

	public static Image loadImage(String fname){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path + fname + ext));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Error: \n" + path + fname + ext + "\nmissing!",
				    "Error loading image!",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
		}
		images.put(fname, img);
		return img; 
	}

	public static void initImages(){
		ArrayList<String> imageNames = new ArrayList<>();
		
		imageNames.add("mainShip");
		imageNames.add("text/winText");
		imageNames.add("text/startText");
		imageNames.add("text/deathText");
		imageNames.add("text/titleText");
		imageNames.add("text/comboText");
		imageNames.add("text/pausedText");
		imageNames.add("projectiles/laser");
		imageNames.add("effects/sparks");
		imageNames.add("effects/reflectionSparks");
		imageNames.add("effects/shield");
		imageNames.add("effects/muzzleFlash");
		
		for (int i = 14; i > 0; i--) {
			imageNames.add("planets/planet" + i);
		}
		for (int i = 8; i > 0; i--) {
			imageNames.add("projectiles/alienShot" + i);
		}
		for (int i = 7; i > 0; i--) {
			imageNames.add("projectiles/shot" + i);
		}
		for (int i = 8; i > 0; i--) {
			imageNames.add("aliens/alienShip" + i);
		}
		for (int i = 4; i > 0; i--) {
			imageNames.add("effects/explosion" + i);
		}
		for (int i = 3; i > 0; i--) {
			imageNames.add("effects/powerup" + i);
		}

		loadImages(imageNames);
	}
	
	public static void loadImages(String[] fNames){
		for (String s : fNames)
			loadImage(s);
	}

	public static void loadImages(Iterable<String> fNames){
		for (String s : fNames)
			loadImage(s);
	}
	
	public static Image getImage(String s) {
		if(!images.containsKey(s)){
			System.out.println(" Image not found in hashmap. key=\"" + s + "\"");
		}
		return images.get(s);
	}

}
