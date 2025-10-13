package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class Mjolnir extends Hammer {
    private int usages;
    private boolean active;
    private final Animation DefaultAnimation;
    private final Animation ActiveAnimation;
    private final Animation SpawnAnimation;
    public Mjolnir() {
        super(4);
        DefaultAnimation = new Animation("sprites/mjolnir.png", 24, 24);
        ActiveAnimation = new Animation("sprites/mjolnir_active.png", 24, 24, 0.15f, Animation.PlayMode.LOOP_PINGPONG);
        SpawnAnimation = new Animation("sprites/mjolnir_spawn.png", 24, 24, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(SpawnAnimation);
        active = false;
    }
//    public int getUsages() {
//        return usages;
//    }
//    public void use() {
//        if (usages > 0)
//            usages--;
//        if (usages <= 0)
//            getScene().removeActor(this);
//    }
    public void toggle_active() {
        active = !active;
        change_animation();
    }
    private void change_animation() {
        if (active) {
            setAnimation(ActiveAnimation);
        }
        else {
            setAnimation(DefaultAnimation);
        }
    }
}
