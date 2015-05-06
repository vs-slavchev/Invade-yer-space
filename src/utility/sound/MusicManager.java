package utility.sound;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.newdawn.easyogg.OggClip;

import utility.TextBox;
import utility.TextBoxManager;

public class MusicManager {
	
	private OggClip backgroundMusic;
	private float gain = 0.78f;
	
	public MusicManager(){
		try{
			backgroundMusic = new OggClip("music/captain_Manol.ogg");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Error: \n" + "music/captain_Manol.ogg" + "\nmissing!",
				    "Error loading music!",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
		}
	}
	
	public void loopBackgroundMusic(){
			backgroundMusic.loop();
			backgroundMusic.setGain(gain);
			backgroundMusic.setBalance(0.0f);
			TextBoxManager.showTextBox(new TextBox("Song: Captain Manol;Artist: Marto D;MnM Studios", 20, 800, 220, 250));
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
