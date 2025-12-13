package sk.tuke.kpi.oop.game.utils;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Movable;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class SlowdownArea extends AbstractActor {
    private final Map<Movable, Integer> originalSpeeds = new HashMap<>();
    private final int areaWidth;
    private final int areaHeight;
    private Rectangle rectangle;
    public SlowdownArea(int areaWidth, int areaHeight) {
        Animation defaultAnimation = new Animation("sprites/invisible.png");
        setAnimation(defaultAnimation);
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        rectangle = new Rectangle(getPosX(), getPosY(), areaWidth, areaHeight);
        new Loop<>(
            new Invoke<>(()->{
                for (Actor actor : scene.getActors()) {
                    if (actor instanceof Movable) {
                        Movable movable = (Movable) actor;
                        if (rectangle.intersects(movable.getPosX(), movable.getPosY(), movable.getWidth(), movable.getHeight())) {
                            if (!originalSpeeds.containsKey(movable)) {
                                originalSpeeds.put(movable, movable.getSpeed());
                                if (movable.getSpeed() > 1) {
                                    movable.setSpeed(movable.getSpeed() / 2);
                                }
                            }
                        }
                        else {
                            if (originalSpeeds.containsKey(movable)) {
                                int normal = originalSpeeds.remove(movable);
                                movable.setSpeed(normal);
                            }
                        }
                    }
                }
            })
        ).scheduleFor(this);
    }
}
