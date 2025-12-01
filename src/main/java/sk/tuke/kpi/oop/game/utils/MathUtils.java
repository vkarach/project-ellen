package sk.tuke.kpi.oop.game.utils;

import sk.tuke.kpi.gamelib.Actor;

public class MathUtils {
    public static float distanceBetween(Actor a, Actor b) {
        float ax = a.getPosX() + a.getWidth() / 2f;
        float ay = a.getPosY() + a.getHeight() / 2f;
        float bx = b.getPosX() + b.getWidth() / 2f;
        float by = b.getPosY() + b.getHeight() / 2f;

        float dx = ax - bx;
        float dy = ay - by;
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
}
