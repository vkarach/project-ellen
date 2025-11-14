package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.characters.Ripley;


public class Energy extends AbstractActor implements Usable<Ripley> { // extends?
    private boolean isUsed = false;
    public Energy() {
        Animation defaultAnimation = new Animation("sprites/energy.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Ripley ripley) {
        if (getScene() == null || ripley.getEnergy() == 100 || isUsed) {
            return;
        }
        ripley.setEnergy(100);

        getScene().removeActor(this);
        isUsed = true;
    }
}
