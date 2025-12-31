package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.utils.PauseManager;

import static sk.tuke.kpi.oop.game.Direction.fromXY;

public class MoveToPlace<A extends Movable> implements Action<A> {
    public enum Type { FirstX, FirstY, Diagonally }
    private Type type = Type.FirstX;
    private final int destX;
    private final int destY;
    private boolean isDone = false;
    private boolean ignoreWalls = false;
    private A actor;
    public MoveToPlace(int destX, int destY, boolean ignoreWalls, Type type) {
        this.destX = destX;
        this.destY = destY;
        this.ignoreWalls = ignoreWalls;
        this.type = type;
    }
    public MoveToPlace(int destX, int destY, boolean ignoreWalls) {
        this.destX = destX;
        this.destY = destY;
        this.ignoreWalls = ignoreWalls;
    }
    public MoveToPlace(int destX, int destY, Type type) {
        this.destX = destX;
        this.destY = destY;
        this.type = type;
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
        float elapsedTime = 0;
    }
    private int step1D(int diff, int speed) {
        if (diff == 0) {
            return 0;
        }
        return Integer.signum(diff) * Math.min(Math.abs(diff), speed);
    }
    public void execute(float deltaTime) {
        if (actor == null || isDone()) {
            return;
        }
        Scene scene = actor.getScene();
        if (scene == null) {
            return;
        }
        if (PauseManager.isPaused()) {
            actor.stoppedMoving();
            return;
        }

        int ax = actor.getPosX();
        int ay = actor.getPosY();

        int speed = actor.getSpeed();

        int dx = destX - ax;
        int dy = destY - ay;

        int stepX = 0;
        int stepY = 0;

        switch (type) {
            case FirstX:
                stepX = step1D(dx, speed);

                if (stepX == 0) {
                    stepY = step1D(dy, speed);
                } else {
                    stepY = 0;
                }
                break;
            case FirstY:
                stepY = step1D(dy, speed);

                if (stepY == 0) {
                    stepX = step1D(dx, speed);
                } else {
                    stepX = 0;
                }
                break;

            case Diagonally:
                if (dx != 0 && dy != 0) {
                    int s = Math.min(Math.min(Math.abs(dx), Math.abs(dy)), speed);
                    stepX = Integer.signum(dx) * s;
                    stepY = Integer.signum(dy) * s;
                }
                else {
                    stepX = step1D(dx, speed);
                    stepY = step1D(dy, speed);
                }
                break;
        }
        actor.setPosition(ax + stepX, ay + stepY);
//        System.out.println("Moving to " + ax + stepX + "," + ay + stepY);

        if (!ignoreWalls && scene.getMap().intersectsWithWall(actor)) {
            actor.setPosition(ax, ay);
            setDone(true);
        }
        else {
            int dirX = Integer.compare(stepX, 0);
            int dirY = Integer.compare(stepY, 0);
            Direction direction = fromXY(dirX, dirY);
            actor.startedMoving(direction);
            boolean isMoving = true;
            if (destX == ax && destY == ay) {
                setDone(true);
            }
        }
    }
}
