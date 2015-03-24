package utility.sound;

import java.io.IOException;
import java.util.Random;

import org.newdawn.easyogg.OggClip;

public class MusicManager {
	
	private OggClip backgroundMusic;
	
	public MusicManager(){
		try{
			backgroundMusic = new OggClip("music/captain_Manol.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loopBackgroundMusic(){
			backgroundMusic.loop();
			backgroundMusic.setGain(0.50f);
			backgroundMusic.setBalance(-1.0f);
	}
	
	public void stopBackgroundMusic(){
		backgroundMusic.stop();
	}
	
}
