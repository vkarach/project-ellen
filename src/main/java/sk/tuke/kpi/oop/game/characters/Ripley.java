package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;

public class Ripley extends AbstractActor implements Actor, Movable {
    private int speed;
    private final Animation moveAnimation;
    public Ripley() {
        super("Ellen");
        moveAnimation = new Animation("sprites/player.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(moveAnimation);
        moveAnimation.pause();
        speed = 150;
    }
    @Override
    public void startedMoving(Direction direction) {
        moveAnimation.setRotation(direction.getAngle());
        moveAnimation.play();

    }
    @Override
    public int getSpeed() {
        return speed;
    }
}
