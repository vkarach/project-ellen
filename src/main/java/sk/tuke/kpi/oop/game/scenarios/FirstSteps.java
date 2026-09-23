package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.*;


public class FirstSteps implements SceneListener {
    private Ripley ripley;
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        this.ripley = new Ripley();
        scene.addActor(ripley, 0, 0);

        Energy energy = new Energy();
        scene.addActor(energy, 100, 0);

        Ammo ammo = new Ammo();
        scene.addActor(ammo, 100, 100);

        Hammer hammer = new Hammer();
        scene.addActor(hammer, -100, 0);

        Wrench wrench = new Wrench();
        scene.addActor(wrench, -100, -100);

        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        scene.addActor(fireExtinguisher, -100, 100);

        MovableController movableController = new MovableController(ripley);
        scene.getInput().registerListener(movableController);

        KeeperController keeperController = new KeeperController(ripley);
        scene.getInput().registerListener(keeperController);

        // watches when ripley on energy and ammo use it
        new Loop<>(
            new Invoke<>(() -> {
                if (ripley.intersects(energy)) {
                    new Use<>(energy).scheduleFor(ripley);
                }
                if (ripley.intersects(ammo)) {
                    new Use<>(ammo).scheduleFor(ripley);
                }
            })
        ).scheduleFor(ripley);
    }
    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        ripley.showRipleyState();
    }
}
