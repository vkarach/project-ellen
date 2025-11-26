package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Armed;

public class Bullet extends AbstractActor implements Movable, Fireable {
    private final int speed = 4;
    Animation defaultAnimation;
    public Bullet() {
        defaultAnimation = new Animation("sprites/bullet.png");
        setAnimation(defaultAnimation);
    }
    public int getSpeed() {
        return speed;
    }
    public void startedMoving(Direction direction) {
        defaultAnimation.setRotation(direction.getAngle());
    }
    @Override
    public void collidedWithWall() {
        Scene scene = getScene();
        if (scene != null) {
            scene.removeActor(this);
        }
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(
            new Invoke<>(() -> {
                for (Actor actor : scene.getActors()) {
                    if (actor instanceof Alive && this.intersects(actor) && !(actor instanceof Armed)) {
                        Alive aliveActor = (Alive) actor;
                        aliveActor.getHealth().drain(15);
                        scene.removeActor(this);
                        break;
                    }
                }
            })
        ).scheduleFor(this);
    }
}
