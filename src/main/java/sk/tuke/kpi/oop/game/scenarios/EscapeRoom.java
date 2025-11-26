package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.Ventilator;
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
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.openables.Door;

import java.util.Arrays;
import java.util.Random;


public class EscapeRoom implements SceneListener {
    Disposable moveDisposable;
    Disposable keeperDisposable;
    Disposable shooterDisposable;
    Ripley ripley;
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
                    behaviour = new RandomlyMoving<>();
                }
                else if ("waiting1".equals(type)) {
                    behaviour = new Observing<Alien, Door>(
                        Door.DOOR_OPENED,
                        door -> "front door".equals(door.getName()),
                        new RandomlyMoving<>()
                    );
                }
                else if ("waiting2".equals(type)) {
                    behaviour = new Observing<Alien, Door>(
                        Door.DOOR_OPENED,
                        door -> "back door".equals(door.getName()),
                        new RandomlyMoving<>()
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
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        this.ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return;
        }

        ripley.setPosition(20, 130);

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
                Font whiteFont = new Font(25, Color.WHITE, Font.Style.BOLD_ITALIC);
                Animation thumbUpAnimation = new Animation("sprites/thumb_up.png", 498, 368, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
                new Loop<>(
                    new Invoke<>(() -> {
                        scene.getOverlay().drawText("Good job you escaped!", ripley.getPosX(), ripley.getPosY() + 30, whiteFont);
                        scene.getOverlay().drawAnimation(thumbUpAnimation, ripley.getPosX() + 330, ripley.getPosY() + 20, 0.1f);
                    })
                ).scheduleFor(ripley);
            }
        });
    }
    public void sceneUpdating(@NotNull Scene scene) {
        ripley.showRipleyState();
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
