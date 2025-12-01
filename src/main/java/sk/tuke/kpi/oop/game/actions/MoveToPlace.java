package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;

import static sk.tuke.kpi.oop.game.Direction.fromXY;

public class MoveToPlace<A extends Movable> implements Action<A> {
    private float elapsedTime = 0;
    private boolean isMoving = false;
    private final int destX;
    private final int destY;
    private boolean isDone = false;
    private boolean ignoreWalls = false;
    private A actor;
    public MoveToPlace(int destX, int destY, boolean ignoreWalls) {
        this.destX = destX;
        this.destY = destY;
        this.ignoreWalls = ignoreWalls;
    }
    public MoveToPlace(int destX, int destY) {
        this.destX = destX;
        this.destY = destY;
    }
    public void setActor(A actor) {
        this.actor = actor;
    }
    public A getActor() {
        return actor;
    }
    public boolean isDone() {
        return isDone;
    }
    public void setDone(boolean done) {
        this.isDone = done;
        actor.stoppedMoving();
    }
    public void reset() {
        elapsedTime = 0;
    }
    public void execute(float deltaTime) {
//        System.out.println("Move" + actor.getPosX() + " " + actor.getPosY());
        if (actor == null || isDone()) {
            return;
        }
        Scene scene = actor.getScene();
        if (scene == null) {
            return;
        }
//        int ax = actor.getPosX();
//        int ay = actor.getPosY();
//
//        int dx = 0;
//        if (ax < destX) {
//            dx = 1;
//        }
//        else if (ax > destX) {
//            dx = -1;
//        }
//        int dy = 0;
//        if (dx == 0) {
//            if (ay < destY) {
//                dy = 1;
//            } else if (ay > destY) {
//                dy = -1;
//            }
//        }
//        actor.setPosition(ax + dx * actor.getSpeed(), ay + dy * actor.getSpeed());

        int ax = actor.getPosX();
        int ay = actor.getPosY();

        int speed = actor.getSpeed();

        int diffX = destX - ax;
        int stepX = 0;
        if (diffX != 0) {
            int abs = Math.abs(diffX);
            stepX = (diffX > 0 ? 1 : -1) * Math.min(abs, speed);
        }

        int diffY = destY - ay;
        int stepY = 0;
        if (stepX == 0 && diffY != 0) {
            int abs = Math.abs(diffY);
            stepY = (diffY > 0 ? 1 : -1) * Math.min(abs, speed);
        }
        actor.setPosition(ax + stepX, ay + stepY);

        if (!ignoreWalls && scene.getMap().intersectsWithWall(actor)) {
            actor.setPosition(ax, ay);
            setDone(true);
        }
        else {
            int dirX = Integer.compare(stepX, 0);
            int dirY = Integer.compare(stepY, 0);
            Direction direction = fromXY(dirX, dirY);
            actor.startedMoving(direction);
            isMoving = true;
            if (destX == ax && destY == ay) {
                setDone(true);
            }
        }
    }
}
