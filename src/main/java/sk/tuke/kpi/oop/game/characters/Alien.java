package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

public class Alien extends AbstractActor implements Movable, Alive, Enemy {
    private final Behaviour<? super Alien> behaviour;
    Animation defaultAnimation;
    private int speed = 1;
    public Health health;
    public int maxHealth = 60;
    public Alien(Behaviour<? super Alien> behaviour) {
        this.behaviour = behaviour;
        defaultAnimation = new Animation("sprites/alien.png", 32, 32, 0.1f, Animation.PlayMode.LOOP);
        setAnimation(defaultAnimation);
        health = new Health(maxHealth);
        health.onFatigued(() -> {
            Scene scene = getScene();
            if (scene != null) {
                scene.cancelActions(this);
                scene.removeActor(this);
            }
        });
        defaultAnimation.pause();
    }
    @Override
    public Health getHealth() {
        return health;
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    @Override
    public void startedMoving(Direction direction) {
        defaultAnimation.play();
    }
    @Override
    public void stoppedMoving() {
        defaultAnimation.stop();
    }
    public void showHealth() {
        Scene scene = getScene();
        if (scene == null || health.getValue() == maxHealth) {
            return;
        }

        scene.getOverlay().drawRectangle(getPosX() + getWidth()/2, getPosY() + getHeight(), maxHealth / 2, 5, Color.BLACK);
        scene.getOverlay().drawRectangle(getPosX() + getWidth()/2, getPosY() + getHeight(), health.getValue() / 2, 5, Color.LIME);

//        Font whiteFont = new Font(11, Color.WHITE, Font.Style.NORMAL);
//        scene.getOverlay().drawText(""+health.getValue(), getPosX() + getWidth()/2, getPosY() + getHeight() + 1, whiteFont);
    }
    private boolean canHit = true;
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(
            new Invoke<>(()-> {
                for (Actor actor : scene.getActors()) {
                    if (!(actor instanceof Enemy) && actor instanceof Alive) {
                        Alive aliveActor = (Alive) actor;
                        if (this.intersects(actor)) {
                            if (canHit) {
                                aliveActor.getHealth().drain(25);
                                canHit = false;
                                new ActionSequence<>(
                                    new Wait<>(1),
                                    new Invoke<>(()-> canHit = true)
                                ).scheduleFor(this);
                            }
                        }
                    }
                }
            })
        ).scheduleFor(this);
        if (behaviour != null) {
            behaviour.setUp(this);
        }
    }
}
