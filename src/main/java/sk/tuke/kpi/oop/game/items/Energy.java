package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Ripley;


public class Energy extends AbstractActor implements Usable<Alive> { // extends?
    private boolean isUsed = false;
    public Energy() {
        Animation defaultAnimation = new Animation("sprites/energy.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Alive alive) {
        if (getScene() == null || alive.getHealth().getValue() == 100 || isUsed) {
            return;
        }
        alive.getHealth().restore();

        getScene().removeActor(this);
        isUsed = true;
    }
    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}
