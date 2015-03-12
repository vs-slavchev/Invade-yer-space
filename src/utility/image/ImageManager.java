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
	final static String path = "images/";
	final static String ext = ".png";

	static Map<String, Image> images = new HashMap<String, Image>();

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

	public static Image loadImage(String imName, String fname) throws IOException {
		BufferedImage img = null;
		img = ImageIO.read(new File(path + fname + ext));
		images.put(imName, img);
		return img; 
	}

	public static void initImages() throws IOException{
		ArrayList<String> imageNames = new ArrayList<>();
		imageNames.add("mainShip");
		imageNames.add("alienShip1");
		imageNames.add("alienShip2");
		imageNames.add("alienShip3");
		imageNames.add("alienShip4");
		imageNames.add("alienShot");
		imageNames.add("shot");
		imageNames.add("winText");
		imageNames.add("startText");
		imageNames.add("deathText");
		imageNames.add("titleText");
		
		for (int i = 6; i > 0; i--) {
			imageNames.add("planet" + i);
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
