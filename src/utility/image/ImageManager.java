package utility.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageManager {

	// this may need modifying
	private final static String path = "images/";
	private final static String ext = ".png";

	private static Map<String, Image> images = new HashMap<String, Image>();

	public static Image getImage(String s) {
		if(!images.containsKey(s)){
			System.out.println(" Image not found in hashmap. key=\"" + s + "\"");
		}
		return images.get(s);
	}

	public static Image loadImage(String fname) throws IOException {
		BufferedImage img = null;
		img = ImageIO.read(new File(path + fname + ext));
		images.put(fname, img);
		return img; 
	}

	public Image loadImage(String imName, String fname) throws IOException {
		BufferedImage img = null;
		//img = ImageIO.read(new File(path + fname + ext));
		img = ImageIO.read(getClass().getResource(path + fname + ext));
		images.put(imName, img);
		return img; 
	}

	public static void initImages() throws IOException{
		ArrayList<String> imageNames = new ArrayList<>();
		imageNames.add("mainShip");
		imageNames.add("projectiles/alienShot1");
		imageNames.add("projectiles/shot1");
		imageNames.add("text/winText");
		imageNames.add("text/startText");
		imageNames.add("text/deathText");
		imageNames.add("text/titleText");
		imageNames.add("effects/sparks");
		
		for (int i = 14; i > 0; i--) {
			imageNames.add("planets/planet" + i);
		}
		for (int i = 8; i > 0; i--) {
			imageNames.add("aliens/alienShip" + i);
		}
		for (int i = 4; i > 0; i--) {
			imageNames.add("effects/explosion" + i);
		}

		loadImages(imageNames);
	}
	
	public static void loadImages(String[] fNames) throws IOException {
		for (String s : fNames)
			loadImage(s);
	}

	public static void loadImages(Iterable<String> fNames) throws IOException {
		for (String s : fNames)
			loadImage(s);
	}

}
