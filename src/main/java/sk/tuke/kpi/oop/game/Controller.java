package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Controller extends AbstractActor {
    private final Animation defaultAnimation;
    public Controller() {
        defaultAnimation = new Animation("sprites/switch.png", 16, 16);
        setAnimation(defaultAnimation);
    }
    public void toggle(Reactor reactor) {
        if (reactor.isRunning()) {
            reactor.turnOff();
        }
        else {
            reactor.turnOn();
        }
    }
}
