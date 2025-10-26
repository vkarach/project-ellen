package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class Mjolnir extends Hammer {
    private boolean active;
    private final Animation defaultAnimation;
    private final Animation activeAnimation;
    public Mjolnir() {
        super(4);
        defaultAnimation = new Animation("sprites/mjolnir.png", 24, 24);
        activeAnimation = new Animation("sprites/mjolnir_active.png", 24, 24, 0.15f, Animation.PlayMode.LOOP_PINGPONG);
        Animation spawnAnimation = new Animation("sprites/mjolnir_spawn.png", 24, 24, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(spawnAnimation);
        active = false;
    }
    public void toggleActive() {
        active = !active;
        updateAnimation();
    }
    private void updateAnimation() {
        if (active) {
            setAnimation(activeAnimation);
        }
        else {
            setAnimation(defaultAnimation);
        }
    }
}
