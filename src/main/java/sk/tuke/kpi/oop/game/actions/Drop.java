package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Drop<A extends Keeper<Collectible>> extends AbstractAction<A> {
    @Override
    public void execute(float deltaTime) {
        A  keeper = getActor();
        if (keeper == null || isDone()) {
            return;
        }
        Backpack keepersBackpack = keeper.getBackpack();
        Collectible firstItem = keepersBackpack.peek();
        if  (firstItem == null) {
            return;
        }
        keepersBackpack.remove(firstItem);
        Scene scene = keeper.getScene();
        scene.addActor(firstItem, keeper.getPosX(), keeper.getPosY());
        setDone(true);
    }
}
