package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor {
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
    public boolean isPowerOn() {
        return powerOn;
    }
    public void setPower(boolean power) {
        powerOn = power;
        updateAnimation();
    }
    public void toggleLight() {
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
