package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements Switchable, EnergyConsumer {
    private final Animation offAnimation;
    private final Animation onAnimation;
    private boolean isOn;
    private boolean powerOn;
    public Light() {
        isOn = false;
        powerOn = false;
        offAnimation = new Animation("sprites/light_off.png", 16, 16);
        onAnimation = new Animation("sprites/light_on.png", 16, 16);
        setAnimation(offAnimation);
    }
    @Override
    public boolean isOn() {
        return isOn;
    }
    @Override
    public void turnOn() {
        isOn = true;
        updateAnimation();
    }
    @Override
    public void turnOff() {
        isOn = false;
        updateAnimation();
    }
    public boolean isPowerOn() {
        return powerOn;
    }
    @Override
    public void setPowered(boolean power) {
        powerOn = power;
        updateAnimation();
    }
    public void toggle() {
        isOn = !isOn;
        updateAnimation();
    }
    private void updateAnimation() {
        if (isOn && powerOn) {
            setAnimation(onAnimation);
        }
        else {
            setAnimation(offAnimation);
        }
    }
}
