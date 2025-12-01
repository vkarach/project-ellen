package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.story.Dialogue;

public class Jagermeister extends AbstractActor implements Collectible {
    public Jagermeister() {
        Animation defaultAnimation = new Animation("sprites/jagermeister.png");
        setAnimation(defaultAnimation);
    }
}
