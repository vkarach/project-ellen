package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class AlienEgg extends AbstractActor {
    public AlienEgg() {
        Animation defaultAnimation = new Animation("sprites/alien_egg.png", 32, 32, 2, Animation.PlayMode.ONCE);
        setAnimation(defaultAnimation);
    }
}
