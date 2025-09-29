package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Reactor extends AbstractActor {
    private int temperature;
    private int damage;
    private final Animation normalAnimation;
    private final Animation overheatAnimation;
    private final Animation brokenAnimation;
    public Reactor() {
        temperature = 0;
        damage = 0;
        normalAnimation = new Animation("sprites/reactor_on.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        overheatAnimation = new Animation("sprites/reactor_hot.png", 80, 80, 0.05f, Animation.PlayMode.LOOP_PINGPONG);
        brokenAnimation = new Animation("sprites/reactor_broken.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
        increaseTemperature(1);
        System.out.println("Reactor: temperature: " + temperature+ " damage: " + damage);
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
        if (increment < 0) {
            return;
        }
        float multiplier = 1;
        if (damage > 33 && damage < 66) {
            multiplier = 1.5f;
        }
        else if (damage > 66) {
            multiplier = 2;
        }
        temperature += (int)Math.ceil(increment * multiplier);
        if (temperature >= 2000) {
            damage = (int)((temperature - 2000) * 0.025);
            if (damage > 100) {
                damage = 100;
            }
            updateAnimation();
        }
    }
    public void decreaseTemperature(int decrement) {
        if (damage == 100) {
            return;
        }
        int divider = 1;
        if (damage >= 50) {
            divider = 2;
        }
        temperature -= decrement / divider;
        updateAnimation();
    }
    private void updateAnimation() {
        if (temperature >= 6000) {
            setAnimation(brokenAnimation);
        }
        else if (temperature > 4000) {
            setAnimation(overheatAnimation);
        }
        else {
            setAnimation(normalAnimation);
        }
    }
}

