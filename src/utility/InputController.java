package utility;

public class InputController {
	
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean firePressed = false;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean onePressed = false;
	private boolean twoPressed = false;
	private boolean threePressed = false;
	private boolean fourPressed = false;
	private boolean autoFirePressed = false;
	private boolean musicDownPressed = false;
	private boolean musicUpPressed = false;
	private boolean pausePressed = false;
	
	public void reset(){
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		firePressed = false;
		onePressed = false;
		twoPressed = false;
		threePressed = false;
		fourPressed = false;
		autoFirePressed = false;
		musicDownPressed = false;
		musicUpPressed = false;
		pausePressed = false;
	}

	public boolean isPausePressed() {
		return pausePressed;
	}

	public void setPausePressed(boolean pausePressed) {
		this.pausePressed = pausePressed;
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

	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}

	public void setFirePressed(boolean firePressed) {
		this.firePressed = firePressed;
	}

	public void setUpPressed(boolean upPressed) {
		this.upPressed = upPressed;
	}

	public void setDownPressed(boolean downPressed) {
		this.downPressed = downPressed;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public boolean isFirePressed() {
		return firePressed;
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}
	
	public boolean isOnePressed() {
		return onePressed;
	}

	public void setOnePressed(boolean onePressed) {
		this.onePressed = onePressed;
	}

	public boolean isTwoPressed() {
		return twoPressed;
	}

	public void setTwoPressed(boolean twoPressed) {
		this.twoPressed = twoPressed;
	}

	public boolean isThreePressed() {
		return threePressed;
	}

	public void setThreePressed(boolean threePressed) {
		this.threePressed = threePressed;
	}

	public boolean isFourPressed() {
		return fourPressed;
	}

	public void setFourPressed(boolean fourPressed) {
		this.fourPressed = fourPressed;
	}
	
	
	
}
