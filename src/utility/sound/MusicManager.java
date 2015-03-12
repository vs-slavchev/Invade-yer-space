package utility.sound;

import java.io.IOException;
import java.util.Random;

import org.newdawn.easyogg.OggClip;

public class MusicManager {
	
	private OggClip backgroundMusic;
	
	public MusicManager(){
		try{
			this.backgroundMusic = new OggClip("music/captain_Manol.ogg");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loopBackgroundMusic(){
			this.backgroundMusic.loop();
			this.backgroundMusic.setGain(0.75f);
			this.backgroundMusic.setBalance(-1.0f);
	}
	
	public void stopBackgroundMusic(){
		this.backgroundMusic.stop();
	}
	
}
