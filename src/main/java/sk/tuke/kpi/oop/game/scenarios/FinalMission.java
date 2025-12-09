package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.actions.*;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.*;
import sk.tuke.kpi.oop.game.controllers.PauseController;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.openables.StrongDoor;
import sk.tuke.kpi.oop.game.utils.*;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static sk.tuke.kpi.oop.game.utils.MathUtils.randomNumber;

public class FinalMission implements SceneListener {
    private final SoundUtil ambientSound = new SoundUtil("sounds/ambient.wav");
    private final SoundUtil alarmSound = new SoundUtil("sounds/emergency_alarm.wav");
    private boolean showState = true;
    private boolean firstMeeting = false;
    private Scene scene;

    private MovableController movableController;
    private KeeperController keeperController;
    private ShooterController shooterController;

    private Disposable moveDisposable;
    private Disposable keeperDisposable;
    private Disposable shooterDisposable;

    private Ripley ripley;
    private Mark mark;
    private Rocket rocket;
    private Helper helper;
    public static class Factory implements ActorFactory {
        private final List<Reactor> reactors = new ArrayList<>();
        @Nullable
        public Actor create(@Nullable String type, @Nullable String name) {
            assert name != null;
            assert type != null;
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
                else if (name.contains("strong")) {
                    return new StrongDoor(name, orientation);
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
            else if (name.equals("ammo")) {
                return new Ammo();
            }
            else if (name.equals("med kit")) {
                return new Energy();
            }
            else if (name.contains("reactor")) {
                Reactor reactor = new Reactor(name);
                reactor.turnOn();
                reactors.add(reactor);
                return reactor;
            }
            else if (name.contains("cooler")) {
                for (Reactor r : reactors) {
                    if (r.getName().equals(type)) {
                        if (name.contains("smart")) {
                            return new SmartCooler(r);
                        }
                        return new Cooler(r);
                    }
                }
            }
            return null;
        }
    }
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        ambientSound.loop(0.1f);
//        DialogueLoader.loadAll();
        try {
            DialogueLoader.load("dialogues/dialogues.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        PauseController pauseController = new PauseController(scene);
        scene.getInput().registerListener(pauseController);

        enableControls();

        Ammo ammo1 = new Ammo();
        scene.addActor(ammo1, ripley.getPosX(), ripley.getPosY());

        ripley.getHealth().drain(50);
        Energy energy = new Energy();
        scene.addActor(energy, ripley.getPosX() + 20, ripley.getPosY() + 20);

//        Explode explosion = new Explode(Explode.Size.BIG);
//        scene.addActor(explosion, ripley.getPosX(), ripley.getPosY());
//
//        Disposable alarm = startAlarm();
//        new ActionSequence<>(
//            new Wait<>(1.5f),
//            new Invoke<>(()->stopAlarm(alarm))
//        ).scheduleFor(ripley);

//        Reactor reactor = scene.getFirstActorByType(Reactor.class);
//        ripley.setPosition(reactor.getPosX(), reactor.getPosY());

//        ripley.setSpeed(10);

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, door -> {
            if ("first door".equals(door.getName()) && !firstMeeting) {
                firstMeeting = true;
                scene.cancelActions(ripley);
                Disposable cutscene = cutsceneApply(0.7f);
                ripley.setSpeed(1);
                boolean[] helperDone = {false};
                new ActionSequence<>(
                    new MoveToPlace<>(door.getPosX() - 32, door.getPosY()),
                    new MoveToPlace<>(mark.getPosX(), mark.getPosY() - 30),
//                    new Speak<>(DialogueLoader.get("first_meeting")),
                    new Speak<>(DialogueLoader.get("first_meeting")),
                    new Invoke<>(()->{
                        scene.addActor(helper, ripley.getPosX(), ripley.getPosY());
                        scene.follow(helper);
                        helper.setSpeed(2);
                        new ActionSequence<>(
                            new MoveToPlace<>(helper.getPosX() + 150, helper.getPosY(), true),
                            new MoveToPlace<>(helper.getPosX(), helper.getPosY(), true),
                            new Invoke<>(()-> helperDone[0] = true)
                        ).scheduleFor(helper);
                    }),
                    new When<>(
                        ()->helperDone[0],
                        new Invoke<>(() -> {})
                    ),
                    new Invoke<>(() -> {
                        Ammo ammo = new Ammo();
                        scene.addActor(ammo, mark.getPosX(), mark.getPosY() - 10);
                        cutsceneDisapply(cutscene, 0.8f);
                        scene.follow(ripley);
                        ripley.setSpeed(2);
                    })
                ).scheduleFor(ripley);
            }
        });
        scene.getMessageBus().subscribe(Mark.MY_JAGERMEISTER, mark -> {
            Disposable cutscene = cutsceneApply(5f);
            new ActionSequence<>(
                new Speak<>(DialogueLoader.get("jagermeister_returned")),
                new Invoke<>(() -> {
                    cutsceneDisapply(cutscene, 1f);
                })
            ).scheduleFor(ripley);
        });
        scene.getMessageBus().subscribe(Door.DOOR_OPENED, door -> {
            if (ripley != null) {
                return;
            }
            Disposable cutscene = cutsceneApply(0.7f);
            ripley.setSpeed(2);
            mark.setSpeed(2);
            ripley.setPosition(mark.getPosX(), mark.getPosY() - 30);
            new ActionSequence<>(
                new Speak<>(DialogueLoader.get("no_time_to_explain")),
                new Invoke<>(() -> {
                    new ActionSequence<>(
                        new MoveToPlace<>(door.getPosX() + 32, door.getPosY()),
                        new MoveToPlace<>(rocket.getPosX() + 46, rocket.getPosY() - 16)
                    ).scheduleFor(mark);
                }),
                new MoveToPlace<>(door.getPosX() + 32, door.getPosY()),
                new MoveToPlace<>(rocket.getPosX() + 16, rocket.getPosY() - 16),
                new Wait<>(0.4f),
                new Invoke<>(() -> {
                    ripley.setPosition(0,0);
//                    scene.removeActor(ripley);
                    scene.removeActor(mark);
                    rocket.fly(scene);
                }),
                new Invoke<>(() -> {
                    new Loop<>(
                        new ActionSequence<>(
                            new Invoke<>(()->{
                                Explode explosion = new Explode();
                                int rx = randomNumber(rocket.getPosX(), rocket.getPosX() + (int) (800 * scene.getCamera().zoom / 2));
                                int ry = randomNumber(rocket.getPosY(), rocket.getPosY() - (int) (600 * scene.getCamera().zoom / 2));
                                scene.addActor(explosion, rx, ry);
                            }),
                            new Wait<>(1)
                        )
                    ).scheduleFor(rocket);
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
    final private int fovY = 300;
    private Disposable cutsceneApply(float speed) {
//        int blockSize = 500;
        showState = false;
        disableControls();
        return new Loop<>(
            new Invoke<>(()-> {
                new ActionSequence<>(
                    new Wait<>(0.5f  / speed),
                    new Invoke<>(()-> {
                        if (PauseManager.isPaused()) {
                            return;
                        }
                        if (scene.getCamera().zoom > 0.7f) {
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
    private Disposable startAlarm() {
        boolean[] draw = {true};
        return new Loop<>(
            new ActionSequence<>(
                new Invoke<>(() -> {
                    draw[0] = !draw[0];

                    if (draw[0]) {
                        new While<>(
                            ()->draw[0],
                            new Invoke<>(() -> {
                                scene.getGame().getOverlay().drawRectangle(
                                    0,
                                    0,
                                    scene.getGame().getWindowSetup().getWidth(),
                                    scene.getGame().getWindowSetup().getHeight(),
                                    new Color(1, 0, 0, 0.4f)
                                );
                            })
                        ).scheduleFor(ripley);
                        alarmSound.play(1f);
                    }
                    else {
                        alarmSound.stop();
                    }
                }),
                new Wait<>(0.5f)
            )
        ).scheduleFor(ripley);
    }
    private void stopAlarm(Disposable alarm) {
        alarm.dispose();
        alarmSound.stop();
    }
}
//                scene.getOverlay().drawRectangle(0, ripley.getPosY() + fovY, scene.getGame().getWindowSetup().getWidth(), blockSize, Color.BLACK);
//                scene.getOverlay().drawRectangle(0, ripley.getPosY() - fovY - blockSize, scene.getGame().getWindowSetup().getWidth(), blockSize, Color.BLACK);
//                if (PauseManager.isPaused()) {
//                    return;
//                }
//                if (fovY > 150) {
//                    fovY -= (int) (2 * speed);
//                }
