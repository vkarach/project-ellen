package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Computer extends AbstractActor implements EnergyConsumer{
    private boolean isPowerOn;
    public Computer() {
        Animation normalAnimation = new Animation("sprites/computer.png", 80, 48, 0.4f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
    }
    @Override
    public void setPowered(boolean power) {
        isPowerOn = power;
    }
    public int add(int a, int b) {
        if (!isPowerOn) {
            return -1;
        }
        return a + b;
    }
    public float add(float a, float b) {
        if (!isPowerOn) {
            return -1;
        }
        return a + b;
    }
    public int sub(int a, int b) {
        if (!isPowerOn) {
            return -1;
        }
        return a - b;
    }
    public float sub(float a, float b) {
        if (!isPowerOn) {
            return -1;
        }
        return a - b;
    }
}
