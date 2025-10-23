package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;

public class FireExtinguisher extends BreakableTool<Reactor> {
    public FireExtinguisher() {
        super(1);
        Animation fireextinguisherAnimation = new Animation("sprites/extinguisher.png", 16,16);
        setAnimation(fireextinguisherAnimation);
    }
    @Override
    public void useWith(Reactor reactor) {
        if (reactor != null && reactor.extinguish()) {
            super.useWith(reactor);
        }
    }
}
