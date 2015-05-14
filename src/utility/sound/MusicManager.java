package utility.sound;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.newdawn.easyogg.OggClip;

import utility.TextBox;
import utility.TextBoxManager;

public class MusicManager {

	private final String path = "music/";
	private final String extension = ".ogg";
	private final int numberOfSongs = 2;
	
	private String[] songNames = new String[numberOfSongs];
	private OggClip[] songs = new OggClip[numberOfSongs];
	private OggClip backgroundMusic;
	private float gain = 0.72f;
	private int delay = 210;
	private boolean delayActive = false;
	
	public MusicManager() {
		songNames[0] = "Wrath of Manol";
		songNames[1] = "Captain Manol";

		for (int i = 0; i < numberOfSongs; i++) {
			loadSong(i, songNames[i]);
		}
		backgroundMusic = songs[0];
	}
	
	private void loadSong(int index, String name){
		validateIndex(index);
		String fileName = path + name + extension;
		try{
			songs[index] = new OggClip(fileName);
		} catch (IOException e){
			JOptionPane.showMessageDialog(null,
					"Error: \n" + fileName + "\nmissing!",
				    "Error loading music!",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
		}
		
	}
	
	public void loopBackgroundMusic(int index) {
		validateIndex(index);
		backgroundMusic.stop();
		backgroundMusic = songs[index];
		backgroundMusic.loop();
		backgroundMusic.setGain(gain);
		backgroundMusic.setBalance(0.0f);
		TextBoxManager.showTextBox(new TextBox(
				"Song: " + songNames[index] + ";Artist: Marto D;MnM Studios",
				20, 800, 240, 400));
	}
	
	private void validateIndex(int index) {
		if (index >= numberOfSongs){
			JOptionPane.showMessageDialog(null,
					"Error: song index not supported.\nindex = "
				+ index + "\nmax index = " + (numberOfSongs-1),
					"Error indexing music!",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
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
	
	public void temporaryDecreaseGain(){
		backgroundMusic.setGain(0.5f);
		delayActive = true;
		update();
	}
	
	public void update(){
		if (delayActive) {
			delay--;
		}
		
		if (delay < 0) {
			delayActive = false;
			backgroundMusic.setGain(gain);
		}
	}
	
	public void stopBackgroundMusic(){
		backgroundMusic.stop();
	}
	
}
