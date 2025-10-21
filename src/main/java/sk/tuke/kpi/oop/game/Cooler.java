package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;

public class Cooler extends AbstractActor implements Switchable {
    private Reactor reactor;
    private boolean isOn;
    private final Animation coolingAnimation;
    public Cooler(Reactor reactor) {
        this.reactor = reactor;
        isOn = false;
        coolingAnimation = new Animation("sprites/fan.png", 32,32, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(coolingAnimation);
        coolingAnimation.pause();
    }
    protected Reactor getReactor() {
        return reactor;
    }
    @Override
    public void turnOn() {
        isOn = true;
        coolingAnimation.play();
    }
    @Override
    public void turnOff() {
        isOn = false;
        coolingAnimation.pause();
    }
    @Override
    public boolean isOn() {
        return isOn;
    }
    public void coolReactor() {
        if (isOn() && reactor != null) {
            reactor.decreaseTemperature(1);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::coolReactor)).scheduleFor(this);
    }
}
