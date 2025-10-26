package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

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
