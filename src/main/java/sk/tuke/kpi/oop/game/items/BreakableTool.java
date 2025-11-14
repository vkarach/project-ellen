package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.oop.game.Usable;

public abstract class BreakableTool<A extends Actor> extends AbstractActor implements Usable<A> {
    private int remainingUses;
    public BreakableTool(int remainingUses) {
        this.remainingUses = remainingUses;
    }
    public int getRemainingUses() {
        return remainingUses;
    }
    @Override
    public void useWith(A actor) {
        if (remainingUses > 0) {
            remainingUses--;
        }
        if (remainingUses <= 0 && getScene() != null) {
            getScene().removeActor(this);
        }
    }
}
