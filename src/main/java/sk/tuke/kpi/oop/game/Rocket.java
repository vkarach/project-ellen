package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.utils.Helper;

public class Rocket extends AbstractActor {
    Animation defaultAnimation;
    Animation flyAnimation;
    public Rocket() {
        defaultAnimation = new Animation("sprites/rocket.png");
        defaultAnimation.setScale(2); // 96 128
        flyAnimation = new Animation("sprites/rocket_fly.png", 48, 64, 0.1f, Animation.PlayMode.LOOP);
        flyAnimation.setScale(2);

        setAnimation(defaultAnimation);
    }
    public void fly(Scene scene) {
        setAnimation(flyAnimation);
        int vibration = 2; // 0-5
        Helper helper = new Helper();
        scene.addActor(helper, getPosX(), getPosY());
        scene.follow(helper);
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(()-> {
                    helper.setPosition(getPosX() + vibration, getPosY());
                }),
                new Invoke<>(()-> {
                    helper.setPosition(getPosX() - vibration, getPosY());
                })
            )
        ).scheduleFor(helper);
        float[] posX = { getPosX() };
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(()-> {
                    scene.getCamera().zoom = scene.getCamera().zoom + 0.01f;
                    getAnimation().setScale(getAnimation().getScale() + 0.02f);
                    posX[0] -= 0.5f;
                    setPosition((int) posX[0], getPosY());
                }),
                new Wait<>(0.01f)
            )
        ).scheduleFor(this);
    }
    public void stopFly() {
        setAnimation(defaultAnimation);
    }
}
