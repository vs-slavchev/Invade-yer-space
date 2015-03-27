package utility.sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;

import java.awt.List;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

	private final static String path = "sounds/";
	private static Map<String, Clip> sounds = new HashMap<String, Clip>();
	private static ArrayList<String> fileNames = new ArrayList<>();
	private static SoundManager soundManager;
	
	public static SoundManager getSoundManager(){
		if ( soundManager == null){
			soundManager = new SoundManager();
		}
		return soundManager;
	}

	public void initSoundManager() {

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

			/*// decrease each clip's gain
			FloatControl gainControl = (FloatControl) clip
					.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-15.0f);*/
		}

	}

	public void play(String name) {
		if (!sounds.containsKey(name)){
			System.out.println(" Sound not found in hashmap. name=\"" + name + "\"");
		}
		if (!sounds.get(name).isActive()) {
			sounds.get(name).setFramePosition(0);
			sounds.get(name).start();
		}
	}

	private Clip getClip(String filename) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path + filename + ".wav"));
			clip.open(sample);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clip;
	}

}
