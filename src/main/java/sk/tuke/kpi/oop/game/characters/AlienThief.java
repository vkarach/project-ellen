package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.items.Jagermeister;

public class AlienThief extends Alien {
    public AlienThief(Behaviour<? super Alien> behaviour) {
        super(behaviour);
        health.onFatigued(() -> {
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
