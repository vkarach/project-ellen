package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class Ammo extends AbstractActor implements Usable<Ripley> {
    private boolean isUsed = false;
    public Ammo() {
        Animation defaultAnimation = new Animation("sprites/ammo.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Ripley ripley) {
        if (getScene() == null || isUsed) {
            return;
        }
        ripley.setAmmo(ripley.getAmmo() + 50);
        getScene().removeActor(this);
        isUsed = true;
    }
}
