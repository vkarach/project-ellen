package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.utils.Helper;
import sk.tuke.kpi.oop.game.utils.PauseManager;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class Rocket extends AbstractActor implements Movable {
    private final SoundUtil flySound = new SoundUtil("sounds/rocket_fly.wav");
    private final Animation defaultAnimation;
    private final Animation flyAnimation;
    private boolean isFlying = false;
    private Disposable flyingDisposable = null;
    public Rocket() {
        defaultAnimation = new Animation("sprites/rocket.png");
        defaultAnimation.setScale(2); // 96 128
        flyAnimation = new Animation("sprites/rocket_fly.png", 48, 64, 0.1f, Animation.PlayMode.LOOP);
        flyAnimation.setScale(2);

        setAnimation(defaultAnimation);
    }
    public boolean isFlying() {
        return isFlying;
    }
    public void fly(Scene scene) {
        isFlying = true;
        flySound.play(0.1f);
        setAnimation(flyAnimation);
        int vibration = 2; // 0-5
        Helper helper = new Helper();
        scene.addActor(helper, getPosX(), getPosY());
        scene.follow(helper);
//        new Loop<>(
//            new ActionSequence<>(
//                new Invoke<>(()-> {
//                    helper.setPosition(getPosX(), getPosY() + vibration);
//                }),
//                new Invoke<>(()-> {
//                    helper.setPosition(getPosX(), getPosY() - vibration);
//                })
//            )
//        ).scheduleFor(helper);
        float[] posX = { getPosX() };
        flyingDisposable =  new Loop<>(
            new ActionSequence<>(
//                new Invoke<>(()->{
//                    new ActionSequence<>(
//                        new Invoke<>(()-> {
//                            helper.setPosition(getPosX(), getPosY() + vibration);
//                        }),
//                        new Invoke<>(()-> {
//                            helper.setPosition(getPosX(), getPosY() - vibration);
//                        })
//                    ).scheduleFor(helper);
//                }),
                new Invoke<>(()-> {
                    if (PauseManager.isPaused()) {
                        return;
                    }
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
        isFlying = false;
        flySound.stop();
        flyingDisposable.dispose();
    }

    @Override
    public int getSpeed() {
        return 2;
    }
}
