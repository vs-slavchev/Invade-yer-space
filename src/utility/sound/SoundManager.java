package utility.sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;

import utility.ContentValues;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

	private final static String path = "sounds/";
	private static Map<String, Clip> sounds = new HashMap<String, Clip>();
	private static ArrayList<String> fileNames = new ArrayList<>();

	public static void initSoundManager() {

		fileNames.add("shieldUp");
		fileNames.add("menuSound");
		fileNames.add("yarr");
		fileNames.add("lazor");
		fileNames.add("meCannon");
		fileNames.add("rocket");
		fileNames.add("nooo");
		fileNames.add("reflectiveShield");
		fileNames.add("lazorSfx");
		fileNames.add("weaponSwitch");
		fileNames.add("tutorialSound");
		fileNames.add("manolWin");
		fileNames.add("frequentShoot");
		fileNames.add("cannonShoot");
		fileNames.add("sineShoot");
		fileNames.add("tripleShoot");
		
		for ( int i = 1; i <= 5; i++){
			fileNames.add("explosion" + i);
		}

		for (String name : fileNames) {
			@SuppressWarnings("resource")
			Clip clip = getClip(name);
			sounds.put(name, clip);

			// decrease each clip's gain
			if (ContentValues.DECREASE_CLIPS_GAIN){
			decreaseGain(clip);
			}
		}
	}

	private static void decreaseGain(Clip clip) {
		FloatControl gainControl = (FloatControl) clip
				.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-10.0f);
	}

	public static void play(String name) {
		if (!sounds.containsKey(name)){
			JOptionPane.showMessageDialog(null,
					"Error: \n" + name + ".wav" + "\nnot found in SoundManager HashMap!",
				    "Error playing sound!",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
		}
		if (!sounds.get(name).isActive()) {
			sounds.get(name).setFramePosition(0);
			sounds.get(name).start();
		}
	}
	
	public static void playOnly(String name) {
		if (!sounds.containsKey(name)){
			JOptionPane.showMessageDialog(null,
					"Error: \n" + name + ".wav" + "\nnot found in SoundManager HashMap!",
				    "Error playing sound!",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
		}
		
		// stop all sounds
		for (Clip clip : sounds.values()) {
			if (clip.isActive()) {
				clip.stop();
				clip.setFramePosition(0);
			}
		}
		// play the sound
		sounds.get(name).start();
	}

	private static Clip getClip(final String filename) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			@SuppressWarnings("resource")
			AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path + filename + ".wav"));
			clip.open(sample);
		} catch (Exception e) {
			 JOptionPane.showMessageDialog(null,
					"Error: \n" + path + filename + ".wav" + "\nmissing!",
				    "Error loading sound!",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
		}
		return clip;
	}
	
	public static void close(){
		for (Clip clip : sounds.values()) {
			clip.close();
		}
	}
	
	public static void playExplosionSfx(){
		int type = (int) (1 + Math.round(Math.random()*4));
		play("explosion" + type);
	}

}
