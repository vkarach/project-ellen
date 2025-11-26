package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.weapons.Firearm;

public class Ammo extends AbstractActor implements Usable<Armed> {
    private boolean isUsed = false;
    public Ammo() {
        Animation defaultAnimation = new Animation("sprites/ammo.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Armed armedActor) {
        if (getScene() == null || isUsed) {
            return;
        }
//        armedActor.setAmmo(armedActor.getAmmo() + 50);
        armedActor.getFirearm().reload(50);
        getScene().removeActor(this);
        isUsed = true;
    }
    @Override
    public Class<Armed> getUsingActorClass() {
        return Armed.class;
    }

}
