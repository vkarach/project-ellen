package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.actions.Shift;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.items.BreakableTool;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.utils.PauseManager;

public class KeeperController implements KeyboardListener {
    private final Keeper keeper;

    public KeeperController(Keeper keeper) {
        this.keeper = keeper;
    }
    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (PauseManager.isPaused()) {
            return;
        }
        if (key == Input.Key.ENTER) {
            new Take<>().scheduleFor(keeper);
        }
        else if (key == Input.Key.BACKSPACE) {
            new Drop<>().scheduleFor(keeper);
        }
        else if (key == Input.Key.S) {
            new Shift<>().scheduleFor(keeper);
        }
        else if (key == Input.Key.U) {
            for (Actor actor : keeper.getScene().getActors()) {
                if (actor instanceof Usable && keeper.intersects(actor)) {
                    Usable<?> usable = (Usable<?>) actor;
                    new Use<>(usable).scheduleForIntersectingWith(keeper);
                    break;
                }
            }
        }
        else if (key == Input.Key.B) {
            Collectible item = keeper.getBackpack().peek();
            if (item == null) {
                return;
            }
            if (item instanceof Usable) {
                Usable<?> usable = (Usable<?>) item;
                new Use<>(usable).scheduleForIntersectingWith(keeper);
                if (usable instanceof BreakableTool && ((BreakableTool<?>) usable).getRemainingUses() == 1) {
                    keeper.getBackpack().remove(item);
                }
            }
        }
    }
}
