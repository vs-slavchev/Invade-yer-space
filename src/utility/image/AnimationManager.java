package utility.image;

import utility.InvadeError;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimationManager {

    private static final List<Animation> animations = new CopyOnWriteArrayList<>();

    public static void spawnAnimation(final String type, final int x, final int y,
                                      final double scale) {
        Animation anim = null;
        String animName = "effects/" + type;
        switch (type) {
            case "sparks":
                anim = new Animation(x, y, 0.2, 4, animName, false, true, scale);
                break;
            case "reflectionSparks":
                anim = new Animation(x, y, 0.2, 4, animName, false, true, scale);
                break;
            case "muzzleFlash":
                anim = new Animation(x, y, 0.5, 4, animName, false, true, scale);
                break;
            case "explosion":
                int explosionId = (int) Math.round(1 + Math.random() * 3);
                String extendedName = animName + explosionId;
                anim = new Animation(x, y, 0.12, 7, extendedName, false, true, scale);
                break;
            default:
                InvadeError.show(animName + " animation not supported by AnimationManager.");
                break;
        }
        if (anim != null) {
            animations.add(anim);
        }
    }

    public static void updateAnimations() {
        for (Animation anim : animations) {
            anim.update();
            if (!anim.getActive()) {
                animations.remove(anim);
            }
        }
    }

    public static void drawAnimations(Graphics2D g) {
        for (Animation anim : animations) {
            if (anim != null) {
                anim.drawAnimation(g);
            }
        }
    }

    public static void clearAll() {
        animations.clear();
    }
}