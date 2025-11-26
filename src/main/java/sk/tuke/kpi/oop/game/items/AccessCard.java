package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

public class AccessCard extends AbstractActor implements Collectible, Usable<LockedDoor> {
    public AccessCard() {
        Animation defaultAnimation = new Animation("sprites/key.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(LockedDoor lockedDoor) {
        if (lockedDoor.isLocked()) {
            lockedDoor.unlock();
        }
        else {
            lockedDoor.lock();
        }
    }
    @Override
    public Class<LockedDoor> getUsingActorClass() {
        return LockedDoor.class;
    }

}
