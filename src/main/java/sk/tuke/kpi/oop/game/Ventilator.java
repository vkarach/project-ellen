package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;

public class Ventilator extends AbstractActor implements Repairable {
    public static final Topic<Ventilator> VENTILATOR_REPAIRED = Topic.create("ventilator repaired", Ventilator.class);
    private final Animation coolingAnimation;
    private boolean repaired = false;
    public Ventilator() {
        coolingAnimation = new Animation("sprites/ventilator.png", 32, 32, 0.1f, Animation.PlayMode.LOOP);
        setAnimation(coolingAnimation);
        coolingAnimation.stop();
    }
    @Override
    public boolean repair() {
        if (repaired || getScene() == null) {
            return false;
        }
        repaired = true;
        coolingAnimation.play();
        getScene().getMessageBus().publish(VENTILATOR_REPAIRED, this);
        return true;
    }
}

