package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

import java.util.concurrent.locks.Lock;
public class MissionImpossible implements SceneListener {
    Disposable moveDisposable;
    Disposable keeperDisposable;
    Disposable leakDisposable;
    Ripley ripley;
    public static class Factory implements ActorFactory {
        @Nullable
        public Actor create(@Nullable String type, @Nullable String name) {
            if ("ellen".equals(name)) {
                return new Ripley();
            }
            if ("energy".equals(name)) {
                return new Energy();
            }
            if ("door".equals(name)) {
                return new LockedDoor("Door", Door.Orientation.VERTICAL);
            }
            if ("access card".equals(name)) {
                return new AccessCard();
            }
            if ("ventilator".equals(name)) {
                return new Ventilator();
            }
            if ("locker".equals(name)) {
                return new Locker();
            }
            else {
                return null;
            }
        }
    }
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        this.ripley = scene.getFirstActorByType(Ripley.class);

        MovableController movableController = new MovableController(ripley);
        moveDisposable = scene.getInput().registerListener(movableController);

        KeeperController keeperController = new KeeperController(ripley);
        keeperDisposable = scene.getInput().registerListener(keeperController);

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, r -> disableControls());

        final Action<Ripley> leak = new Loop<>(
            new ActionSequence<>(
                new Wait<>(0.25f),
                new Invoke<>(() -> ripley.getHealth().drain(1))
            )
        );
        scene.getMessageBus().subscribe(Door.DOOR_OPENED, d ->
            leakDisposable = leak.scheduleFor(ripley)
        );

        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, d -> {
            leakDisposable.dispose();
            leakDisposable = null;
        });

    }
    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        ripley.showRipleyState();
    }
    private void disableControls() {
        moveDisposable.dispose();
        moveDisposable = null;

        keeperDisposable.dispose();
        keeperDisposable = null;
    }
}
