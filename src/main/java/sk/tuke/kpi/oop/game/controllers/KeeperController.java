package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.items.BreakableTool;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.utils.PauseManager;

public class KeeperController implements KeyboardListener {
    private final Keeper<Collectible> keeper;

    public KeeperController(Keeper<Collectible> keeper) {
        this.keeper = keeper;
    }
    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (PauseManager.isPaused()) {
            return;
        }
        switch (key) {
            case ENTER:
                new Take<>().scheduleFor(keeper);
                break;
            case BACKSPACE:
                new Drop<>().scheduleFor(keeper);
                break;
            case S:
                keeper.getBackpack().shift();
                break;
            case U:
                for (Actor actor : keeper.getScene().getActors()) {
                    if (actor instanceof Usable && keeper.intersects(actor)) {
                        Usable<?> usable = (Usable<?>) actor;
                        new Use<>(usable).scheduleForIntersectingWith(keeper);
                        break;
                    }
                }
                break;
            case B:
                Collectible item = keeper.getBackpack().peek();
                if (item == null) {
                    break;
                }
                if (item instanceof Usable) {
                    Usable<?> usable = (Usable<?>) item;
                    new Use<>(usable).scheduleForIntersectingWith(keeper);
                    if (usable instanceof BreakableTool) {
                        BreakableTool<?> breakable = (BreakableTool<?>) usable;
                        if (breakable.getRemainingUses() == 1) {
                            keeper.getBackpack().remove(item);
                        }
                    }
                }
                break;
        }
    }
}
