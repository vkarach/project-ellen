package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class FireExtinguisher extends BreakableTool {
    private int usages;
    private final Animation fireextinguisherAnimation;
    public FireExtinguisher() {
        super(1);
        fireextinguisherAnimation = new Animation("sprites/extinguisher.png", 16,16);
        setAnimation(fireextinguisherAnimation);
    }
//    public int getUsages() {
//        return usages;
//    }
//    public void use() {
//        if (usages > 0) {
//            usages--;
//        }
//        if (usages <= 0) {
//            getScene().removeActor(this);
//        }
//    }
}
