package utility;

public class InputController {
	
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean firePressed = false;
	private boolean upPressed = false;
	private boolean downPressed = false;
	
	public void reset(){
		this.leftPressed = false;
		this.rightPressed = false;
		this.upPressed = false;
		this.downPressed = false;
		this.firePressed = false;
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
		return this.leftPressed;
	}

	public boolean isRightPressed() {
		return this.rightPressed;
	}

	public boolean isFirePressed() {
		return this.firePressed;
	}

	public boolean isUpPressed() {
		return this.upPressed;
	}

	public boolean isDownPressed() {
		return this.downPressed;
	}
	
	
	
}
