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

	public void spawnAnimation(String type, int x, int y, double scale) {

		Animation anim = null;
		switch (type) {
			case "effects/sparks":
				anim = new Animation(x, y, 0.5, 4, type, false, true, scale);
				break;
			case "effects/reflectionSparks":
				anim = new Animation(x, y, 0.5, 4, type, false, true, scale);
				break;
			case "effects/explosion":
				type += Integer.toString((int)Math.round(1 + Math.random()*3));
				double randomScale = scale + Math.random();
				anim = new Animation(x, y, 0.15, 7, type, false, true, scale);
				break;
		}
		synchronized (animations) {
			if (anim != null) {
				animations.add(anim);
			}
		}
	}

	public synchronized void updateAnimations() {
		for (final Iterator<Animation> iterator = animations.iterator(); iterator
				.hasNext();) {
			Animation anim = iterator.next();
			anim.update();
			if (!anim.getActive()) {
				animations.remove(anim);
			}
		}
		/*for (Animation anim : animations ){
			anim.update();
			if (!anim.getActive()) {
				animations.remove(anim);
			}
		}*/
	}

	public synchronized void drawAnimations(Graphics2D g) {
		for (final Iterator<Animation> iterator = animations.iterator(); iterator
				.hasNext();) {
			Animation anim = iterator.next();
			if (anim != null){
				anim.drawAnimation(g);
			}
		}
	}
}