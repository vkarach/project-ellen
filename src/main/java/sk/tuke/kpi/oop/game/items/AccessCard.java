package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.openables.Door;

public class AccessCard extends AbstractActor implements Collectible, Usable<Door> {
    public AccessCard() {
        Animation defaultAnimation = new Animation("sprites/key.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Door door) {
        System.out.println("Using access card");
        if (door.isOpen()) {
            door.close();
        }
        else {
            door.open();
        }
    }
    @Override
    public Class<Door> getUsingActorClass() {
        return Door.class;
    }

}
