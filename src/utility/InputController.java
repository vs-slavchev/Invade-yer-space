package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputController {

    public List<Boolean> numberPressed = new ArrayList<>();
    private volatile boolean firePressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean autoFirePressed = false;
    private boolean musicDownPressed = false;
    private boolean musicUpPressed = false;

    public void reset() {
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        firePressed = false;
        autoFirePressed = false;
        musicDownPressed = false;
        musicUpPressed = false;
        Boolean[] values = {false, false, false, false, false};
        numberPressed.addAll(Arrays.asList(values));
    }

    public boolean isMusicDownPressed() {
        return musicDownPressed;
    }

    public void setMusicDownPressed(boolean musicDownPressed) {
        this.musicDownPressed = musicDownPressed;
    }

    public boolean isMusicUpPressed() {
        return musicUpPressed;
    }

    public void setMusicUpPressed(boolean musicUpPressed) {
        this.musicUpPressed = musicUpPressed;
    }

    public boolean isAutoFirePressed() {
        return autoFirePressed;
    }

    public void setAutoFirePressed(boolean autoFirePressed) {
        this.autoFirePressed = autoFirePressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean isFirePressed() {
        return firePressed;
    }

    public void setFirePressed(boolean firePressed) {
        this.firePressed = firePressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

}
