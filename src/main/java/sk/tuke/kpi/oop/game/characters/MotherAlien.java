package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

public class MotherAlien extends Alien {
    private final Animation defaultAnimation;
    private final Health health;

    public MotherAlien(Behaviour<? super Alien> behaviour) {
        super(behaviour);
        int maxHealth = 250;
        health = new Health(maxHealth);
        defaultAnimation = new Animation("sprites/mother.png", 112, 162, 0.1f, Animation.PlayMode.LOOP);
        setAnimation(defaultAnimation);
        this.health.onFatigued(() -> {
            Scene scene = getScene();
            if (scene != null) {
                scene.cancelActions(this);
                scene.removeActor(this);
            }
        });
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
