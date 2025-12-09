package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.openables.LockedDoor;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class AccessCard extends AbstractActor implements Collectible, Usable<LockedDoor> {
    private final SoundUtil useSound = new SoundUtil("sounds/Interact_KeyCard.wav");
    public AccessCard() {
        Animation defaultAnimation = new Animation("sprites/key.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(LockedDoor lockedDoor) {
        useSound.play(); // bad with open door sound
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
