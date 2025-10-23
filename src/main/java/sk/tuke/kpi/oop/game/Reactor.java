package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

import java.util.HashSet;
import java.util.Set;

public class Reactor extends AbstractActor implements Switchable, Repairable {
    private int temperature;
    private int damage;
    private boolean isOn;

    private final Set<EnergyConsumer> devices;

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

        devices = new HashSet<>();
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
            if (new_damage >= 100) {
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
    @Override
    public boolean repair() {
        if ((damage > 0 && damage < 100)) {
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
            return true;
        }
        return false;
    }
    public boolean extinguish() {
        if (damage != 100) {
            return false;
        }
        temperature = 4000;
        setAnimation(extinguishedAnimation);
        return true;
    }
    @Override
    public void turnOn() {
        isOn = true;
        for (EnergyConsumer device : devices) {
            device.setPowered(true);
        }
        updateAnimation();
    }
    @Override
    public void turnOff() {
        isOn = false;
        for (EnergyConsumer device : devices) {
            device.setPowered(false);
        }
        updateAnimation();
    }
    @Override
    public boolean isOn() {
        return isOn;
    }
    public void addDevice(EnergyConsumer device) {
        devices.add(device);
        device.setPowered(isOn());
    }
    public void removeDevice(EnergyConsumer device) {
        device.setPowered(false);
        devices.remove(device);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        scene.scheduleAction(new PerpetualReactorHeating(1), this);
    }

}

