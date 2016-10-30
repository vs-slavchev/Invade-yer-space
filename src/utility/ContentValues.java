package utility;

import java.awt.*;

public class ContentValues {

    public static final String GAME_VERSION = "v1.22 - 11.10.2015";

    // player related:
    public static final int MAX_PLAYER_SHIELD_DURATION = 800;
    public static final int MAX_PLAYER_LASER_DURATION = 170;
    public static final int KILL_TIME_WINDOW = 7_000;
    public static final double PLAYER_MOVE_SPEED = 300;
    public static final int ACCELERATION = 30;
    public static final int FRICTION = 18;
    public static final int NUM_NORMAL_WEAPONS = 4;
    public static final double INITIAL_WEAPON_COOLING_SPEED = 0.0000001;
    public static final double WEAPON_COOLING_SPEED_MULTIPLIER = 1.05;
    public static final double MAX_WEAPON_COOLING_SPEED = 0.15;

    // enemy related:
    public static final double LEVEL_DIFFICULTY_STEP = 0.3;
    public static final double CHANCE_TO_EXPLODE_ALIEN = 0.1;
    public static final int ENEMY_HP_PER_LVL_MULTIPLIER = 2;
    public static final int ENEMY_HP_BASE = 0;
    public static final int X_SHOOT_RANGE = 250;
    public static final int MIN_ATTACK_INTERVAL = 900;
    public static final int MAX_ATTACK_INTERVAL = 2_000;
    public static final int BASE_BULLET_VELOCITY = 270;
    public static final int ALIENS_MAX_COLUMNS = 20; // MUST be an even number
    public static final double ALIEN_SHOOT_TIME_REDUCTION = 0.988;

    // weapon related:
    public static final int ROCKET_EXPLOSION_RADIUS = 80;
    public static final int MACHINE_GUN_VELOCITY = -700;
    public static final int SINE_WAVE_VELOCITY = -500;
    public static final int GREEN_SHOT_VELOCITY = -600;
    public static final int BOMB_VELOCITY = -250;
    public static final int SPEAR_VELOCITY = -550;
    public static final int SCATTER_SHOT_VELOCITY = -500;
    public static final int ROCKET_VELOCITY = -200;
    public static final int FLAKE_VELOCITY = -400;

    // sound and music
    public static final float GAIN_PER_TICK_MODIFIER = 0.007f;
    public static final float INITIAL_GAIN = 0.72f;
    public static final boolean DECREASE_CLIPS_GAIN = true;

    // used colors
    public static final Color COLOR_TEXT_BOX_FILL_TUTORIAL = new Color(80, 80, 255);
    public static final Color COLOR_TEXT_BOX_FILL_SONG = new Color(66, 105, 140);

    // user interface
    public static final Font INFO_FONT = new Font("Monospaced", Font.BOLD, 18);
    public static final Font TEXT_BOX_FONT = new Font("Dialog", Font.BOLD, 20);
    public static final Font QUESTION_FONT = new Font("Dialog", Font.BOLD, 48);
    public static final int WEAPON_UI_BASE_X = 20;
    public static final int NUM_TUTORIALS = 7;
    public static final int TIME_STEP = 400;

    //visual effects
    public static final double PLANET_SPAWN_CHANCE = 0.0005;
    public static final int PLANET_SPACING = -300;
    public static final int NUMBER_DIFFERENT_PLANETS = 14;
    public static final int PLANET_Y_VEL = 1;
    public static final int NUMBER_DIFFERENT_GEMS = 11;
    public static final int MAX_GEMS = 64;
    public static final int NUM_BACKGROUND_STARS = 400;

}
