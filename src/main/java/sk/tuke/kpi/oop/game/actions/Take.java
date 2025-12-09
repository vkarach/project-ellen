package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.gamelib.graphics.Overlay;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class Take<A extends Keeper> extends AbstractAction<A>  {
    private final SoundUtil itemPickup = new SoundUtil("sounds/item_pickup.wav");
    private boolean isDone = false;
    @Override
    public boolean isDone() {
        return isDone;
    }
    @Override
    public void execute(float deltaTime) {
        A keeper = getActor();
        if (keeper == null || isDone) {
            isDone = true;
            return;
        }
        for (Actor actor : getActor().getScene().getActors()) {
            if (actor instanceof Collectible) {
                Collectible item = (Collectible) actor;
                if (keeper.intersects(item)) {
                    try {
                        itemPickup.play();
                        keeper.getBackpack().add(item);
                        item.getScene().removeActor(item);
                    }
                    catch  (Exception e) {
                        Scene scene = keeper.getScene();
                        Overlay overlay = scene.getOverlay();
                        Font font = new Font(8, Color.ORANGE, Font.Style.NORMAL);
                        overlay.drawText(e.getMessage(), item.getPosX(), item.getPosY() + 30, font).showFor(1);
                        isDone = true;
                    }
                    break;
                }
            }
        }
        isDone = true;
    }
}
