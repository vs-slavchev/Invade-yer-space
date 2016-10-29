package entities.utility;

import utility.ContentValues;
import utility.InvadeError;
import utility.sound.SoundManager;

public class StatusEffect {

    public enum StatusEffectType {LASER, SCATTER, SPEARS, QUAD_ROCKETS, SHIELD, FLAKES};

    private final StatusEffectType type;
    private int duration;
    private boolean active;

    public StatusEffect(StatusEffectType type) {
        this.type = type;
        active = true;
        switch (type) {
            case LASER:
                duration = ContentValues.MAX_PLAYER_LASER_DURATION;
                SoundManager.play("lazor");
                SoundManager.play("lazorSfx");
                break;
            case SCATTER:
                duration = 150;
                break;
            case SPEARS:
                duration = 200;
                break;
            case QUAD_ROCKETS:
                duration = 10;
                SoundManager.play("rocket");
                break;
            case SHIELD:
                duration = ContentValues.MAX_PLAYER_SHIELD_DURATION;
                SoundManager.play("reflectiveShield");
                break;
            case FLAKES:
                duration = 500;
                break;
            default:
                InvadeError.show(type + " not supported by StatusEffect.");
                break;
        }
    }

    public void update() {
        duration--;
        if (duration <= 0) {
            active = false;
        }
    }

    public StatusEffectType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public int getDuration() {
        return duration;
    }

}
