package utility.image;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimationManager {
	
	private static AnimationManager animationManager;
	private static List<Animation> animations = new CopyOnWriteArrayList<>();
	
	public static AnimationManager getAnimationManager(){
		if (animationManager == null) {
			animationManager = new AnimationManager();
			}
			return animationManager;
	}

	/*public static void spawnAnimation(String type, int x, int y, double scale) {

		Animation anim = null;
		switch (type) {
		case "alienShip":
				anim = new Animation(x, y, 0.3, 3, type, false, 1);
			break;
		}
		synchronized (animations) {
			if (anim != null) {
				animations.add(anim);
			}
		}
	}*/

	public static synchronized void updateAnimations() {
		for (final Iterator iterator = animations.iterator(); iterator
				.hasNext();) {
			Animation anim = (Animation) iterator.next();
			anim.update();
			if (!anim.getActive()) {
				animations.remove(anim);
			}
		}
	}

	public static synchronized void drawAnimations(Graphics2D g) {
		for (final Iterator iterator = animations.iterator(); iterator
				.hasNext();) {
			Animation anim = (Animation) iterator.next();
			if (anim != null){
				anim.drawAnimation(g);
			}
		}
	}
}
