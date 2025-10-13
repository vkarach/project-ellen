package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.tools.FireExtinguisher;
import sk.tuke.kpi.oop.game.tools.Hammer;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

public class Reactor extends AbstractActor {
    private int temperature;
    private int damage;
    private boolean isOn;

    private Light connectedLight;

    private final Animation offAnimation;
    private final Animation workingAnimation;
    private final Animation overheatAnimation;
    private final Animation brokenAnimation;
    private final Animation extinguishedAnimation;
    public Reactor() {
        temperature = 0;
        damage = 0;
        isOn = false;
        offAnimation = new Animation("sprites/reactor.png", 80, 80);
        workingAnimation = new Animation("sprites/reactor_on.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        overheatAnimation = new Animation("sprites/reactor_hot.png", 80, 80, 0.05f, Animation.PlayMode.LOOP_PINGPONG);
        brokenAnimation = new Animation("sprites/reactor_broken.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        extinguishedAnimation = new Animation("sprites/reactor_extinguished.png", 80, 80);
        setAnimation(offAnimation);
    }
    public int getTemperature() {
        return temperature;
    }
    public int getDamage() {
        return damage;
    }
    public void setTemperature(int tmp) {
        if (tmp >= 0) {
            this.temperature = tmp;
        }
    }
    public void setDamage(int dmg) {
        if (dmg >= 0) {
            this.damage = dmg;
        }
    }
    public void increaseTemperature(int increment) {
        if (!isOn) {
            return;
        }
        if (increment < 0) {
            return;
        }
        if (damage == 100) {
            return;
        }
        float multiplier = 1;
        if (damage > 33 && damage < 66) {
            multiplier = 1.5f;
        }
        else if (damage > 66) {
            multiplier = 2;
        }
        temperature += Math.round(increment * multiplier);
        if (temperature >= 2000) {
            int new_damage = (int)((temperature - 2000) * 0.025);
            if (new_damage > 100) {
                new_damage = 100;
                isOn = false;
            }
            if (new_damage > damage) {
                damage = new_damage;
                updateAnimation();
            }
        }
    }
    public void decreaseTemperature(int decrement) {
        if (!isOn) {
            return;
        }
        if (decrement < 0) {
            return;
        }
        if (damage == 100) {
            return;
        }
        int divider = 1;
        if (damage >= 50) {
            divider = 2;
        }
        temperature -= decrement / divider;
        if (temperature < 0) {
            temperature = 0;
        }
        updateAnimation();
    }
    private void updateAnimation() {
        if (temperature >= 6000) {
            setAnimation(brokenAnimation);
        }
        else if (temperature > 4000) {
            float frameDuration = 0.2f - ((temperature - 4000f) / 2000f) * (0.2f - 0.02f);
            overheatAnimation.setFrameDuration(frameDuration);
            setAnimation(overheatAnimation);
        }
        else if (isOn){
            setAnimation(workingAnimation);
        }
        else {
            setAnimation(offAnimation);
        }
    }
    public void repairWith(Hammer hammer) {
        if ((damage > 0 && damage < 100) && hammer != null) {
            hammer.use();
            int new_damage = damage - 50;
            if (new_damage < 0) {
                new_damage = 0;
            }
            damage = new_damage;
            int new_temperature = damage * 40 + 2000;
            if (new_temperature < temperature) {
                temperature = new_temperature;
            }
            updateAnimation();
        }
    }
    public void extinguishWith(FireExtinguisher fireExtinguisher) {
        if (fireExtinguisher == null && damage != 100) {
            return;
        }
        fireExtinguisher.use();
        temperature = 4000;
        setAnimation(extinguishedAnimation);
    }
    public void turnOn() {
        if (connectedLight != null) {
            connectedLight.setPower(true);
        }
        isOn = true;
        updateAnimation();
    }
    public void turnOff() {
        connectedLight.setPower(false);
        isOn = false;
        updateAnimation();
    }
    public boolean isRunning() {
        return isOn;
    }
    public void addLight(Light light) {
        connectedLight = light;
        connectedLight.setPower(isRunning());
    }
    public void removeLight(Light light) {
        light.setPower(false);
        connectedLight = null;
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        turnOn();
        scene.scheduleAction(new PerpetualReactorHeating(1), this);
//        new PerpetualReactorHeating(1).scheduleFor(this);
    }

}

