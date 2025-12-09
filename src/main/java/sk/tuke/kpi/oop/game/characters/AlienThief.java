package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.items.Jagermeister;

public class AlienThief extends Alien {
    private final Animation defaultAnimation;
    private final Animation alienDie;
    public AlienThief(Behaviour<? super Alien> behaviour) {
        super(behaviour);
        defaultAnimation = new Animation("sprites/alien.png", 32, 32, 0.1f, Animation.PlayMode.LOOP);
        alienDie = new Animation("sprites/alien_die.png", 32, 32, 0.1f, Animation.PlayMode.ONCE);
        getHealth().onFatigued(() -> {
            if (getScene() != null) {
                getScene().cancelActions(this);
            }
            alienDie.setRotation(defaultAnimation.getRotation());
            setAnimation(alienDie);
            new When<>(
                () -> getAnimation().getCurrentFrameIndex() == getAnimation().getFrameCount() - 1,
                new Invoke<>(() -> {
                    Scene scene = getScene();
                    if (scene != null) {
                        System.out.println("removing scene");
                        Jagermeister jagermeister = new Jagermeister();
                        scene.addActor(jagermeister, getPosX(), getPosY());
                        scene.removeActor(this);
                    }
                })
            ).scheduleFor(this);
        });
    }
}
