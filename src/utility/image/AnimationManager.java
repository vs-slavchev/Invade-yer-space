package utility.image;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

public class AnimationManager {
	
	private static List<Animation> animations = new CopyOnWriteArrayList<>();

	public static void spawnAnimation(String type, int x, int y, double scale) {

		Animation anim = null;
		switch (type) {
			case "effects/sparks":
				anim = new Animation(x, y, 0.2, 4, type, false, true, scale);
				break;
			case "effects/reflectionSparks":
				anim = new Animation(x, y, 0.2, 4, type, false, true, scale);
				break;
			case "effects/muzzleFlash":
				anim = new Animation(x, y, 0.5, 4, type, false, true, scale);
				break;
			case "effects/explosion":
				type += Integer.toString((int)Math.round(1 + Math.random()*3));
				anim = new Animation(x, y, 0.12, 7, type, false, true, scale);
				break;
				default:
					JOptionPane.showMessageDialog(null,
							"Error: \n" + type + "\nanimation not supported by AnimationManager.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					 System.exit(0);
					 break;
		}
		synchronized (animations) {
			if (anim != null) {
				animations.add(anim);
			}
		}
	}

	public static synchronized void updateAnimations() {
		for (final Iterator<Animation> iterator = animations.iterator(); iterator
				.hasNext();) {
			Animation anim = iterator.next();
			anim.update();
			if (!anim.getActive()) {
				animations.remove(anim);
			}
		}
	}

	public static synchronized void drawAnimations(Graphics2D g) {
		for (final Iterator<Animation> iterator = animations.iterator(); iterator
				.hasNext();) {
			Animation anim = iterator.next();
			if (anim != null){
				anim.drawAnimation(g);
			}
		}
	}
	
	public static void clearAll(){
		animations.clear();
	}
}