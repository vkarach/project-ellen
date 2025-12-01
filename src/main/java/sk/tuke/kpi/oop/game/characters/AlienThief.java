package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.items.Jagermeister;

public class AlienThief extends Alien {
    public AlienThief(Behaviour<? super Alien> behaviour) {
        super(behaviour);
        health.onFatigued(() -> {
            Scene scene = getScene();
            if (scene != null) {
                scene.cancelActions(this);
            }
            Jagermeister jagermeister = new Jagermeister();
            scene.addActor(jagermeister, getPosX(), getPosY());
        });
    }

}
