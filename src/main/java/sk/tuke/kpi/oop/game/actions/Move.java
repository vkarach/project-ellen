package sk.tuke.kpi.oop.game.actions;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;


public class Move<A extends Movable> implements Action<A> {
    private A actor;
    Direction direction;
    private boolean isMoving;
    private final float duration;
    private float elapsedTime;
    private float posX;
    private float posY;
    public Move(Direction direction, float duration) {
        this.direction = direction;
        this.duration = duration;
        this.elapsedTime = 0;
    }
    public Move(Direction direction) {
        this(direction, 0);
    }
    public void setActor(A actor) {
        this.actor = actor;
    }
    public A getActor() {
        return actor;
    }
    public boolean isDone() {
        if (duration == 0f) {
            return elapsedTime > 0f;
        }
        return elapsedTime >= duration - 1e-5f;
    }
    public void reset() {
        elapsedTime = 0;
        isMoving = false;
    }
    public void stop() {
        if (actor != null) {
            actor.stoppedMoving();
        }
        elapsedTime = duration;
        isMoving = false;
    }
    @Override
    public void execute(float deltaTime) {
        if (actor == null || isDone()) {
            return;
        }
        Scene scene = actor.getScene();
        if (scene == null) {
            stop();
            return;
        }
        if (!isMoving) {
            actor.startedMoving(direction);
            isMoving = true;
            posX = actor.getPosX();
            posY = actor.getPosY();
        }
        int dX = direction.getDx();
        int dY = direction.getDy();
        float divider = 1;
        if (dX != 0 && dY != 0) {
            divider = 1.5f;
        }
        int speed = actor.getSpeed();

//        Scene scene = actor.getScene();
        int oldX = actor.getPosX();
        int oldY = actor.getPosY();
        posX += dX * speed / divider;
        posY += dY * speed / divider;

        actor.setPosition((int) posX,(int) posY);
        if (scene.getMap().intersectsWithWall(actor)) {
            actor.collidedWithWall();
            if (actor == null) {
                stop();
                return;
            }
            posX = oldX;
            posY = oldY;
            actor.setPosition(oldX, oldY);
        }

        elapsedTime += deltaTime;
        if (isDone()) {
            actor.stoppedMoving();
            isMoving = false;
        }
    }
}
