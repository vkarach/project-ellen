package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Repairable;

public class Wrench extends BreakableTool<Repairable> implements Collectible {
    public Wrench() {
        super(2);
        Animation defaultAnimation = new Animation("sprites/wrench.png", 16, 16);
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Repairable repairable) {
        if (repairable != null && repairable.repair(Repairable.RepairKind.WRENCH)) {
            super.useWith(repairable);
        }
    }
    @Override
    public Class<Repairable> getUsingActorClass() {
        return Repairable.class;
    }
}
