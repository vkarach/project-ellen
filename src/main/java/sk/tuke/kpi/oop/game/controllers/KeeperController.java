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
        switch (key) {
            case ENTER:
                new Take<>().scheduleFor(keeper);
                break;
            case BACKSPACE:
                new Drop<>().scheduleFor(keeper);
                break;
            case S:
                new Shift<>().scheduleFor(keeper);
                break;
            case U:
                handleUse();
                break;
            case B:
                handleUseFromBackpack();
                break;
            default:
                break;
        }
    }
    private void handleUse() {
        if (keeper.getScene() == null) {
            return;
        }
        for (Actor actor : keeper.getScene().getActors()) {
            if (actor instanceof Usable && keeper.intersects(actor)) {
                Usable<?> usable = (Usable<?>) actor;
                new Use<>(usable).scheduleForIntersectingWith(keeper);
                break;
            }
        }
    }
    private void handleUseFromBackpack() {
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
