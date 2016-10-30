package utility.sound;

import utility.ContentValues;
import utility.InvadeError;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private final static String path = "sounds/";
    private static Map<String, Clip> sounds = new HashMap<>();
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

        for (int i = 1; i <= 5; i++) {
            fileNames.add("explosion" + i);
        }

        for (String name : fileNames) {
            Clip clip = getClip(name);
            sounds.put(name, clip);

            if (ContentValues.DECREASE_CLIPS_GAIN) {
                decreaseGain(clip);
            }
        }
    }

    private static Clip getClip(final String filename) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample = AudioSystem.getAudioInputStream(
                    new File(path + filename + ".wav"));
            clip.open(sample);
        } catch (Exception e) {
            InvadeError.show(filename + ".wav missing!");
        }
        return clip;
    }

    private static void decreaseGain(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-10.0f);
    }

    public static void play(String name) {
        if (!sounds.containsKey(name)) {
            InvadeError.show(name + ".wav not loaded in SoundManager.");
        }
        Clip sound = sounds.get(name);
        if (!sound.isActive()) {
            sound.setFramePosition(0);
            sound.start();
        }
    }

    public static void playOnly(String name) {
        if (!sounds.containsKey(name)) {
            InvadeError.show(name + ".wav not loaded in SoundManager.");
        }

        for (Clip clip : sounds.values()) {
            if (clip.isActive()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
        sounds.get(name).start();
    }

    public static void close() {
        for (Clip clip : sounds.values()) {
            clip.close();
        }
    }

    public static void playExplosionSfx() {
        int type = (int) (1 + Math.round(Math.random() * 4));
        play("explosion" + type);
    }
}