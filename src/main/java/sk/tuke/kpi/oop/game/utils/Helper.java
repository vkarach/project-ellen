package sk.tuke.kpi.oop.game.utils;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Movable;

public class Helper extends AbstractActor implements Movable {
    private int speed = 3;
    public Helper() {
        Animation defaultAnimation = new Animation("sprites/invisible.png");
        setAnimation(defaultAnimation);
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
