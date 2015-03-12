package utility.sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// note: having too many clips open may cause
// "LineUnavailableException: No Free Voices"

public class SoundManager {

	final static String path = "sounds/";
	static Map<String, Clip> sounds = new HashMap<String, Clip>();
	private static ArrayList<String> fileNames = new ArrayList<>();

	public static void initSoundManager() {

		fileNames.add("beep");
		fileNames.add("bigpowerup");
		fileNames.add("explosion");
		fileNames.add("warp");
		fileNames.add("powerup3");
		fileNames.add("powerup5");
		fileNames.add("powerup6");

		for (String name : fileNames) {
			Clip clip = getClip(name);
			sounds.put(name, clip);

			// decrease each clip's gain
			FloatControl gainControl = (FloatControl) clip
					.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-15.0f);
		}

	}

	public static void play(String name) {
		if (!sounds.containsKey(name)){
			System.out.println(" Sound not found in hashmap. name=\"" + name + "\"");
		}
		if (!sounds.get(name).isActive()) {
			sounds.get(name).setFramePosition(0);
			sounds.get(name).start();
		}
	}

	private static Clip getClip(String filename) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream sample = AudioSystem.getAudioInputStream(new File(
					path + filename + ".wav"));
			clip.open(sample);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clip;
	}

}
