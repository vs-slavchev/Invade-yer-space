package utility.image;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JOptionPane;

public class AnimationManager {
	
	private static List<Animation> animations = new CopyOnWriteArrayList<>();

	public static void spawnAnimation(final String type, final int x, final int y, final double scale) {

		Animation anim = null;
		String modified_type = type;
		switch (type) {
			case "effects/sparks":
				anim = new Animation(x, y, 0.2, 4, modified_type, false, true, scale);
				break;
			case "effects/reflectionSparks":
				anim = new Animation(x, y, 0.2, 4, modified_type, false, true, scale);
				break;
			case "effects/muzzleFlash":
				anim = new Animation(x, y, 0.5, 4, modified_type, false, true, scale);
				break;
			case "effects/explosion":
				modified_type += Integer.toString((int)Math.round(1 + Math.random()*3));
				anim = new Animation(x, y, 0.12, 7, modified_type, false, true, scale);
				break;
				default:
					JOptionPane.showMessageDialog(null,
							"Error: \n" + modified_type + "\nanimation not supported by AnimationManager.",
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

	public static void drawAnimations(Graphics2D g) {
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