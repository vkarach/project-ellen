package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class PowerSwitch extends AbstractActor {
    private final Switchable device;
    public PowerSwitch(Switchable device) {
        this.device = device;
        Animation defaultAnimation = new Animation("sprites/switch.png", 16, 16);
        setAnimation(defaultAnimation);
    }
    public Switchable getDevice() {
        return device;
    }
    public void switchOn() {
        if (device == null) {
            return;
        }
        device.turnOn();
    }
    public void switchOff() {
        if (device == null) {
            return;
        }
        device.turnOff();
    }
}
