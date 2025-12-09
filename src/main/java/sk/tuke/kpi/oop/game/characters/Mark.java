package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Jagermeister;
import sk.tuke.kpi.oop.game.utils.MathUtils;

public class Mark extends AbstractActor implements Actor, Movable {
    public static final Topic<Mark> MY_JAGERMEISTER = Topic.create("Love my Jagermeister!", Mark.class);
    private final Animation defaultAnimation = new Animation("sprites/mark.png", 32, 32);
    private int speed = 2;
    public Mark() {
        super("Mark");
        setAnimation(defaultAnimation);
        defaultAnimation.setRotation(180);
        defaultAnimation.pause();
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    @Override
    public void startedMoving(Direction direction) {
        defaultAnimation.setRotation(direction.getAngle());
        defaultAnimation.play();
    }
    public void stopMoving() {
        defaultAnimation.pause();
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new When<>(
            (actor) -> {
                Jagermeister j = scene.getFirstActorByType(Jagermeister.class);
                return j != null && MathUtils.distanceBetween(this, j) <= 50f;
            },
            new Invoke<>(() -> {
                Jagermeister j = scene.getFirstActorByType(Jagermeister.class);
                scene.removeActor(j);
                AccessCard accessCard = new AccessCard();
                scene.addActor(accessCard, getPosX(), getPosY() - 10);
                scene.getMessageBus().publish(MY_JAGERMEISTER, this);
            })
        ).scheduleFor(this);
    }
}
