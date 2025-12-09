package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Drop<A extends Keeper> extends AbstractAction<A> {
    private boolean isDone = false;
    @Override
    public boolean isDone() {
        return isDone;
    }
    @Override
    public void execute(float deltaTime) {
        A  keeper = getActor();
        if (keeper == null || isDone()) {
            isDone = true;
            return;
        }
        Backpack keepersBackpack = keeper.getBackpack();
        Collectible firstItem = keepersBackpack.peek();
        if  (firstItem == null) {
            isDone = true;
            return;
        }
        keepersBackpack.remove(firstItem);
        Scene scene = keeper.getScene();
        scene.addActor(firstItem, keeper.getPosX(), keeper.getPosY());
        isDone = true;
    }
}
