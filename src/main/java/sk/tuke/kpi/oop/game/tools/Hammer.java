package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;

public class Hammer extends BreakableTool<Reactor> {

    public Hammer() {
        this(1);
    }

    public Hammer(int remainingUses) {
        super(1);
        Animation DefaultAnimation = new Animation("sprites/hammer.png", 16, 16);
        setAnimation(DefaultAnimation);
    }

    @Override
    public void useWith(Reactor reactor) {
        if (reactor != null && reactor.repair()) {
            super.useWith(reactor);
        }
    }
}
