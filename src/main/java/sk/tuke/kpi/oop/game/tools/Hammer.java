package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class  Hammer extends BreakableTool {
    private final Animation DefaultAnimation;
    public Hammer() {
        this(1);
    }
    public Hammer(int remainingUses) {
        super(1);
        DefaultAnimation = new Animation("sprites/hammer.png", 16, 16);
        setAnimation(DefaultAnimation);
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

}
