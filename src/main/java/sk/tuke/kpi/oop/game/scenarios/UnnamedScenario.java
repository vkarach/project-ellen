package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.actions.*;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.Rocket;
import sk.tuke.kpi.oop.game.utils.Helper;
import sk.tuke.kpi.oop.game.SpawnPoint;
import sk.tuke.kpi.oop.game.actions.MoveToPlace;
import sk.tuke.kpi.oop.game.actions.Speak;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.*;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.openables.AutoDoor;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;
import sk.tuke.kpi.oop.game.story.DialogueLoader;
import sk.tuke.kpi.oop.game.utils.SlowdownArea;

import java.util.concurrent.atomic.AtomicBoolean;

public class UnnamedScenario implements SceneListener {
    private boolean showState = true;
    Scene scene;
    MovableController movableController;
    KeeperController keeperController;
    ShooterController shooterController;
    Disposable moveDisposable;
    Disposable keeperDisposable;
    Disposable shooterDisposable;
    Ripley ripley;
    Mark mark;
    Rocket rocket;
    Helper helper;
    public static class Factory implements ActorFactory {
        @Nullable
        public Actor create(@Nullable String type, @Nullable String name) {

            if (name.equals("Ellen")) {
                return new Ripley();
            }
            if (name.equals("Mark")) {
                return new Mark();
            }
            else if (name.equals("Body")) {
                return new DeadMan();
            }
            else if (name.contains("Spawner")) {
                int aliens = Integer.parseInt(type);
                return new SpawnPoint(aliens);
            }
            else if (name.contains("door")) {
                Door.Orientation orientation = null;
                if (type.equals("vertical")) {
                    orientation = Door.Orientation.VERTICAL;
                }
                else if (type.equals("horizontal")) {
                    orientation = Door.Orientation.HORIZONTAL;
                }
                if (name.contains("locked")) {
                    return new LockedDoor(name, orientation);
                }
                else if (name.contains("auto")) {
                    return new AutoDoor(name, orientation);
                }
                else {
                    return new Door(name, orientation);
                }
            }
            else if (name.contains("Alien")) {
                Behaviour<? super Alien> behaviour = null;
                if ("walking".equals(type)) {
                    behaviour = new RandomlyMoving<>();
                }
                if (name.contains("Theft")) {
                    return new AlienThief(behaviour);
                }
                else {
                    return new Alien(behaviour);
                }
            }
            else if (name.equals("Rocket")) {
                return new Rocket();
            }
            else if (name.equals("slow area")) {
                String[] parts = type.split(" ");
                int width = Integer.parseInt(parts[0]);
                int heigh = Integer.parseInt(parts[1]);
                return new SlowdownArea(width, heigh);
            }
            return null;
        }
    }
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        DialogueLoader.loadAll();
        this.scene = scene;
        this.ripley = scene.getFirstActorByType(Ripley.class);
        this.mark = scene.getFirstActorByType(Mark.class);
        this.rocket = scene.getFirstActorByType(Rocket.class);
        this.helper = new Helper();
        if (ripley == null || mark == null) {
            return;
        }
        scene.follow(ripley);

        movableController = new MovableController(ripley);
        keeperController = new KeeperController(ripley);
        shooterController = new ShooterController(ripley);
        enableControls();

        Ammo ammo1 = new Ammo();
        scene.addActor(ammo1, ripley.getPosX(), ripley.getPosY());

        rocket.fly(scene);

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, door -> {
            if ("first door".equals(door.getName())) {
                Disposable cutscene = cutsceneApply(1f);
                AtomicBoolean helperDone = new AtomicBoolean(false);
                ripley.setSpeed(1);
                new ActionSequence<>(
                    new MoveToPlace<>(door.getPosX() - 32, door.getPosY()),
                    new MoveToPlace<>(mark.getPosX(), mark.getPosY() - 30),
                    new Speak<>(DialogueLoader.get("first_meeting")),
                    new Invoke<>(()->{
                        scene.addActor(helper, ripley.getPosX(), ripley.getPosY());
                        scene.follow(helper);
                        helper.setSpeed(2);
                        new ActionSequence<>(
                            new MoveToPlace<>(helper.getPosX() + 150, helper.getPosY(), true),
                            new MoveToPlace<>(helper.getPosX(), helper.getPosY(), true),
                            new Invoke<>(()-> helperDone.set(true))
                        ).scheduleFor(helper);
                    }),
                    new When<>(
                        helperDone::get,
                        new Invoke<>(() -> {})
                    ),
                    new Invoke<>(() -> {
                        Ammo ammo = new Ammo();
                        scene.addActor(ammo, mark.getPosX(), mark.getPosY() - 10);
                        cutsceneDisapply(cutscene, 1f);
                        scene.follow(ripley);
                        ripley.setSpeed(2);
                    })
                ).scheduleFor(ripley);
            }
        });
        scene.getMessageBus().subscribe(Mark.MY_JAGERMEISTER, mark -> {
            Disposable cutscene = cutsceneApply(0.5f);
            new ActionSequence<>(
                new Speak<>(DialogueLoader.get("jagermeister_returned")),
                new Invoke<>(() -> {
                    cutsceneDisapply(cutscene, 1f);
                })
            ).scheduleFor(ripley);
        });

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, r -> disableControls());
    }
    public void sceneUpdating(@NotNull Scene scene) {
        if (showState) {
            ripley.showRipleyState();
        }
        for (Actor actor : scene.getActors()) {
            if (actor instanceof Alien) {
                ((Alien) actor).showHealth();
            }
        }
    }
    private void disableControls() {
        moveDisposable.dispose();
        moveDisposable = null;

        keeperDisposable.dispose();
        keeperDisposable = null;

        shooterDisposable.dispose();
        shooterDisposable = null;
    }
    private void enableControls() {
        moveDisposable = scene.getInput().registerListener(movableController);

        keeperDisposable = scene.getInput().registerListener(keeperController);

        shooterDisposable = scene.getInput().registerListener(shooterController);
    }
    private int fovY = 300;
    private Disposable cutsceneApply(float speed) {
        int blockSize = 500;
        showState = false;
        disableControls();
        return new Loop<>(
            new Invoke<>(()-> {
                scene.getOverlay().drawRectangle(0, ripley.getPosY() + fovY, scene.getGame().getWindowSetup().getWidth(), blockSize, Color.BLACK);
                scene.getOverlay().drawRectangle(0, ripley.getPosY() - fovY - blockSize, scene.getGame().getWindowSetup().getWidth(), blockSize, Color.BLACK);
                if (fovY > 150) {
                    fovY -= (int) (2 * speed);
                }
                new ActionSequence<>(
                    new Wait<>(0.5f  / speed),
                    new Invoke<>(()-> {
                        if (scene.getCamera().zoom > 0.8f) {
                            scene.getCamera().zoom -= 0.003f * speed;
                        }
                    })
                ).scheduleFor(ripley);
            }
        )).scheduleFor(ripley);
    }
    private void cutsceneDisapply(Disposable cutscene, float speed) {
        cutscene.dispose();
        enableControls();
        showState = true;
        new ActionSequence<>(
        new While<>(
            () -> scene.getCamera().zoom < 1f,
            new Invoke<>(() -> scene.getCamera().zoom += 0.01f * speed)
        ),
        new Invoke<>(() -> scene.getCamera().zoom = 1)
        ).scheduleFor(ripley);
    }
}
