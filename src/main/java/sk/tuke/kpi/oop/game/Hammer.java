package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Hammer extends AbstractActor {
    private int usages;
    private final Animation DefaultAnimation;
    public Hammer() {
        DefaultAnimation = new Animation("sprites/hammer.png", 16, 16);
        setAnimation(DefaultAnimation);
        usages = 1;
    }
    public int getUsages() {
        return usages;
    }
    public void use() {
        if (usages > 0)
            usages--;
        if (usages == 0)
            getScene().removeActor(this);
    }
}
