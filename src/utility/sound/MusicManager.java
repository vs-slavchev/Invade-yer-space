package utility.sound;

import java.io.IOException;
import java.util.Random;

import org.newdawn.easyogg.OggClip;

import utility.ContentValues;

public class MusicManager {
	
	private OggClip backgroundMusic;
	private float gain = 0.87f;
	
	public MusicManager(){
		try{
			backgroundMusic = new OggClip("music/captain_Manol.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loopBackgroundMusic(){
			backgroundMusic.loop();
			backgroundMusic.setGain(gain);
			backgroundMusic.setBalance(0.0f); // -1.0f
	}
	
	public void modifyGain(float value){
		gain += value;
		if(gain < 0.3f){
			gain = 0.3f; 
		}else if(gain > 1.0f){
			gain = 1.0f;
		}
		backgroundMusic.setGain(gain);
	}
	
	public void stopBackgroundMusic(){
		backgroundMusic.stop();
	}
	
}
