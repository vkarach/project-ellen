package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.oop.game.Usable;

public abstract class BreakableTool extends AbstractActor { //implements Usable<Actor> {
    private int remainingUses;
    public BreakableTool(int remainingUses) {
        this.remainingUses = remainingUses;
    }
    public int getRemainingUsages() {
        return remainingUses;
    }
//    @Override
    public void use() {
        if (remainingUses > 0)
            remainingUses--;
        if (remainingUses <= 0)
            getScene().removeActor(this);
    }
}
