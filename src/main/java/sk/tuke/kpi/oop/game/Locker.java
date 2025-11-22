package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Hammer;

public class Locker extends AbstractActor implements Usable<Ripley> {
    private boolean used;
    public Locker() {
        Animation defaultAnimation = new Animation("sprites/locker.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Ripley actor) {
        if (used || getScene() == null) {
            return;
        }
        System.out.println("Using locker");
        Hammer hammer = new Hammer();
        hammer.setPosition(getPosX(), getPosY());
        getScene().addActor(hammer);
        used = true;
    }
    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
