package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

public class MotherAlien extends Alien {
    private final Health health;
    private final Animation defaultAnimation;
    public MotherAlien(Behaviour<? super Alien> behaviour) {
        super(behaviour);
        health = new Health(200);
        defaultAnimation = new Animation("sprites/mother.png", 112, 162, 0.1f, Animation.PlayMode.LOOP);
        setAnimation(defaultAnimation);
    }
    @Override
    public Health getHealth() {
        return health;
    }
    public void startedMoving(Direction direction) {
        defaultAnimation.play();
    }
    @Override
    public void stoppedMoving() {
        defaultAnimation.stop();
    }

}
