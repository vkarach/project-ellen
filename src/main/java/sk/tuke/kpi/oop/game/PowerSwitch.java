package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.HelicopterChasePlayer;

public class PowerSwitch extends AbstractActor {
    private final Animation defaultAnimation;
    private final Switchable device;
    public PowerSwitch(Switchable device) {
        this.device = device;
        this.defaultAnimation = new Animation("sprites/switch.png", 16, 16);
        setAnimation(defaultAnimation);
    }
    public Switchable getDevice() {
        return device;
    }
    public void switchOn() {
        device.turnOn();
    }
    public void switchOff() {
        device.turnOff();
    }
}
