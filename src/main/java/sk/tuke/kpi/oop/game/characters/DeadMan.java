package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class DeadMan extends AbstractActor {
    public DeadMan() {
        Animation defaultAnimation = new Animation("sprites/body.png");
        setAnimation(defaultAnimation);
    }
}
