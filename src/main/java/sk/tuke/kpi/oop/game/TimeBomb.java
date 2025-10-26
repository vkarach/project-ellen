package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class TimeBomb extends AbstractActor {
    private boolean active;
    private final float detonationTime;
    Animation defaultAnimation;
    Animation bombActivated;
    Animation bombExplosion;

    public TimeBomb(float detonationTime) {
        this.detonationTime = detonationTime;
        active = false;
        defaultAnimation = new Animation("sprites/bomb.png");
        bombActivated = new Animation("sprites/bomb_activated.png", 16, 16, detonationTime / 6, Animation.PlayMode.ONCE);
        bombExplosion = new Animation("sprites/small_explosion.png", 16, 16, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(defaultAnimation);
    }
    public void activate() {
        if (isActivated()) {
            return;
        }
        active = true;
        setAnimation(bombActivated);
        new ActionSequence<>(
            new Wait<>(detonationTime),
            new Invoke<>(() -> setAnimation(bombExplosion)),
            new Wait<>(0.1f * 8),
            new Invoke<>(() -> {
                if (getScene() != null){
                    getScene().removeActor(this);
                }
            })
        ).scheduleFor(this);
    }
    public boolean isActivated() {
        return active;
    }
}
