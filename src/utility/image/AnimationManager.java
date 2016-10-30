package utility.image;

import utility.InvadeError;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimationManager {

    private static final List<Animation> animations = new CopyOnWriteArrayList<>();

    public static void spawnAnimation(final String type, final int x, final int y,
                                      final double scale) {
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
                int explosionId = (int) Math.round(1 + Math.random() * 3);
                String modifiedType = type + explosionId;
                anim = new Animation(x, y, 0.12, 7, modifiedType, false, true, scale);
                break;
            default:
                InvadeError.show(type + "animation not supported by AnimationManager.");
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
                .hasNext(); ) {
            Animation anim = iterator.next();
            anim.update();
            if (!anim.getActive()) {
                animations.remove(anim);
            }
        }
    }

    public static void drawAnimations(Graphics2D g) {
        for (final Iterator<Animation> iterator = animations.iterator(); iterator
                .hasNext(); ) {
            Animation anim = iterator.next();
            if (anim != null) {
                anim.drawAnimation(g);
            }
        }
    }

    public static void clearAll() {
        animations.clear();
    }
}