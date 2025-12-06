package sk.tuke.kpi.oop.game.utils;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Explode extends AbstractActor {
    private final SoundUtil explodeSound = new SoundUtil("sounds/explosion.wav");
    private final Animation explodeAnimation;
    public enum Size {SMALL, BIG, RANDOM}
    public Explode(Size size) {
        Animation smallExplosion = new Animation("sprites/small_explosion.png", 16, 16, 0.1f, Animation.PlayMode.ONCE);
        Animation bigExplosion = new Animation("sprites/large_explosion.png", 32, 32, 0.1f, Animation.PlayMode.ONCE);
        if (size == Size.SMALL) {
            explodeAnimation = smallExplosion;
        }
        else if (size == Size.BIG) {
            explodeAnimation = bigExplosion;
            explodeAnimation.setScale(5);
        }
        else {
            if (Math.random() < 0.5) {
                explodeAnimation = smallExplosion;
                explodeAnimation.setScale(5);
            }
            else {
                explodeAnimation = bigExplosion;
                explodeAnimation.setScale(5);
            }
        }
        setAnimation(explodeAnimation);
        explodeAnimation.pause();
    }
    public Explode() {
        this(Size.RANDOM);
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        explodeAnimation.play();
        explodeSound.play(0.1f);
        new ActionSequence<>(
            new Wait<>(explodeAnimation.getFrameCount() * explodeAnimation.getFrameDuration()),
            new Invoke<>(()->scene.removeActor(this))
        ).scheduleFor(this);
    }
}
