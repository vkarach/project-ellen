package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.gamelib.graphics.Overlay;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Take<A extends Keeper<Collectible>> extends AbstractAction<A>  {
    @Override
    public void execute(float deltaTime) {
        A keeper = getActor();
        if (keeper == null || isDone()) {
            return;
        }
        for (Actor actor : getActor().getScene().getActors()) {
            if (actor instanceof Collectible) {
                Collectible item = (Collectible) actor;
                if (keeper.intersects(item)) {
                    try {
                        keeper.getBackpack().add(item);
                        item.getScene().removeActor(item);
                    }
                    catch  (Exception ex) {
                        Scene scene = keeper.getScene();
                        Overlay overlay = scene.getOverlay();
                        Font font = new Font(8, Color.ORANGE, Font.Style.NORMAL);
                        overlay.drawText(ex.getMessage(), item.getPosX(), item.getPosY() + 30, font).showFor(0.78f);
                    }
                    break;
                }
            }
        }
        setDone(true);
    }
}
