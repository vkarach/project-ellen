package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Usable;

public class PowerSwitch extends AbstractActor implements Usable<Ripley> {
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
    @Override
    public void useWith(Ripley ripley) {
        System.out.println("Device on: " + device.isOn());
        if (device.isOn()) {
            switchOff();
        }
        else  {
            switchOn();
        }
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }

}
