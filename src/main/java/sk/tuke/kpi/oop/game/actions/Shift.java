package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;

public class Shift<A extends Keeper> extends AbstractAction<A> {
    private boolean isDone = false;
    @Override
    public boolean isDone() {
        return isDone;
    }
    @Override
    public void execute(float deltaTime) {
        A keeper = getActor();
        if (keeper == null || keeper.getBackpack() == null || isDone) {
            isDone = true;
            return;

        }
        Backpack backpack = keeper.getBackpack();
        backpack.shift();
        isDone = true;
    }
}
