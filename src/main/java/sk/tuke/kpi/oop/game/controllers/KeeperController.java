package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.items.Collectible;

public class KeeperController implements KeyboardListener {
    private final Keeper<Collectible> keeper;

    public KeeperController(Keeper<Collectible> keeper) {
        this.keeper = keeper;
    }
    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (key == Input.Key.ENTER) {
            new Take<>().scheduleFor(keeper);
        }
        if (key == Input.Key.BACKSPACE) {
            new Drop<>().scheduleFor(keeper);
        }
        if (key == Input.Key.S) {
            keeper.getBackpack().shift();
        }
        if (key == Input.Key.U) {
            for (Actor actor : keeper.getScene().getActors()) {
                if (actor instanceof Usable && keeper.intersects(actor)) {
                    Usable<?> usable = (Usable<?>) actor;
                    new Use(usable).scheduleForIntersectingWith(keeper);
                    break;
                }
            }
        }
    }
}
