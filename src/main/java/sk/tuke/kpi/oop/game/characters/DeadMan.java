package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class DeadMan extends AbstractActor {
    public DeadMan() {
        Animation defaultAnimation = new Animation("sprites/body.png");
        setAnimation(defaultAnimation);
    }
    public DeadMan(boolean storyDeadMan) {
        Animation defaultAnimation = new Animation("sprites/dead_man.png");
        setAnimation(defaultAnimation);
    }
}
