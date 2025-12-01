package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Jagermeister;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.utils.MathUtils;

public class Mark extends AbstractActor implements Actor, Movable {
    public static final Topic<Mark> MY_JAGERMEISTER = Topic.create("Love my Jagermeister!", Mark.class);
    Animation defaultAnimation = new Animation("sprites/mark.png", 32, 32);
    private final int speed = 2;
    public Mark() {
        super("Mark");
        setAnimation(defaultAnimation);
        defaultAnimation.setRotation(180);
        defaultAnimation.pause();
    }
    public int getSpeed() {
        return speed;
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
