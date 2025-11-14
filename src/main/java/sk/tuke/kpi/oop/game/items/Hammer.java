package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;

public class Hammer extends BreakableTool<Reactor> implements Collectible{

    public Hammer() {
        this(1);
    }
    public Hammer(int remainingUses) {
        super(remainingUses);
        Animation defaultAnimation = new Animation("sprites/hammer.png", 16, 16);
        setAnimation(defaultAnimation);
    }

    @Override
    public void useWith(Reactor reactor) {
        if (reactor != null && reactor.repair()) {
            super.useWith(reactor);
        }
    }
}
