package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.actions.Move;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.Enemy;
import sk.tuke.kpi.oop.game.characters.MotherAlien;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.openables.Door;



public class EscapeRoom implements SceneListener {
    private Disposable moveDisposable;
    private Disposable keeperDisposable;
    private Disposable shooterDisposable;
    private Ripley ripley;
    public static class Factory implements ActorFactory {
        @Nullable
        public Actor create(@Nullable String type, @Nullable String name) {
            if ("ellen".equals(name)) {
                return new Ripley();
            }
            else if ("energy".equals(name)) {
                return new Energy();
            }
            else if (name.contains("alien")) {
                Behaviour<? super Alien> behaviour;

                if ("running".equals(type)) {
                    behaviour = new RandomlyMoving();
                }
                else if ("waiting1".equals(type)) {
                    behaviour = new Observing<Alien, Door>(
                        Door.DOOR_OPENED,
                        door -> "front door".equals(door.getName()),
                        new RandomlyMoving()
                    );
                }
                else if ("waiting2".equals(type)) {
                    behaviour = new Observing<Alien, Door>(
                        Door.DOOR_OPENED,
                        door -> "back door".equals(door.getName()),
                        new RandomlyMoving()
                    );
                }
                else {
                    behaviour = null;
                }
                if ("alien".equals(name)) {
                    return new Alien(behaviour);
                }
                if ("alien mother".equals(name)) {
                    return new MotherAlien(behaviour);
                }
            }
            else if ("ammo".equals(name)) {
                return new Ammo();
            }
            else if (name.contains("door")) {
                Door.Orientation orientation;
                if ("horizontal".equals(type)) {
                    orientation = Door.Orientation.HORIZONTAL;
                }
                else {
                    orientation = Door.Orientation.VERTICAL;
                }
                return new Door(name, orientation);
            }
            return null;
        }
    }
    private boolean isWin = false;
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        this.ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return;
        }
        scene.follow(ripley);

        MovableController movableController = new MovableController(ripley);
        moveDisposable = scene.getInput().registerListener(movableController);

        KeeperController keeperController = new KeeperController(ripley);
        keeperDisposable = scene.getInput().registerListener(keeperController);

        ShooterController shooterController = new ShooterController(ripley);
        shooterDisposable = scene.getInput().registerListener(shooterController);

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, r -> disableControls());

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, door -> {
            if ("exit door".equals(door.getName())) {
                for (Actor actor : scene.getActors()) {
                    if (actor instanceof Enemy) {
                        scene.removeActor(actor);
                    }
                }
                disableControls();
                Font whiteFont = new Font(25, Color.WHITE, Font.Style.BOLD);
                Animation thumbUpAnimation = new Animation("sprites/thumb_up.png", 498, 368);
                new Move<>(Direction.WEST, Float.MAX_VALUE).scheduleFor(ripley);
                new Loop<>(
                    new ActionSequence<>(
                        new Invoke<>(()->{
                            float cur = ripley.getAnimation().getRotation();
                            ripley.getAnimation().setRotation(cur - 45);
                        }),
                        new Wait<>(0.05f)
                    )
                ).scheduleFor(ripley);
                new Loop<>(
                    new Invoke<>(() -> {
                        scene.getOverlay().drawText("Good job you escaped!", ripley.getPosX(), ripley.getPosY() + 40, whiteFont);
                        scene.getOverlay().drawAnimation(thumbUpAnimation, ripley.getPosX() + 330, ripley.getPosY() + 30, 0.1f);
                        isWin = true;
                    })
                ).scheduleFor(ripley);
            }
        });
    }
    public void sceneUpdating(@NotNull Scene scene) {
        if (!isWin) {
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
}
