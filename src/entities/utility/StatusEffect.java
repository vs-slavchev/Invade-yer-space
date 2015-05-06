package entities.utility;

import javax.swing.JOptionPane;

import utility.ContentValues;
import utility.sound.SoundManager;

public class StatusEffect {
	
	private int duration;
	private String name;
	private boolean active;
	
	public StatusEffect(String name){
		active = true;
		this.name  = name;
		switch(name){
		case "laser":
			duration = 150;
			SoundManager.play("lazor");
			SoundManager.play("lazorSfx");
			break;
		case "scatter":
			duration = 150;
			break;
		case "spears":
			duration = 200;
			break;
		case "quadRockets":
			duration = 10;
			SoundManager.play("rocket");
			break;
		case "shield":
			duration = ContentValues.MAX_PLAYER_SHIELD_DURATION;
			SoundManager.play("reflectiveShield");
			break;
		default:
			JOptionPane.showMessageDialog(null,
					"Error: \n" + name + "\nnot supported by StatusEffect.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
			 break;
		}
	}
	
	public void update(){
		duration--;
		if (duration <= 0){
			active = false;
		}
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public int getDuration() {
		return duration;
	}
	
}
