package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;
import sk.tuke.kpi.oop.game.utils.PauseManager;

import java.util.Arrays;
import java.util.Random;

public class RandomlyMoving<A extends Movable> implements Behaviour<A> {
    private boolean move = false;
    private final float moveTime = 0.75f;
    public void setUp(A actor) {
        new Loop<>(
            new ActionSequence<>(
                new When<>(
                    () -> !PauseManager.isPaused(),
                    new Invoke<>(() -> {
                        if (!move) {
                            Direction randDir = randomDirection(actor);
                            actor.getAnimation().setRotation(randDir.getAngle());
                            new Move<>(randDir, moveTime).scheduleFor(actor);
                            move = true;
                            new ActionSequence<>(
                                new Wait<>(moveTime),
                                new Invoke<>(()->move = false)
                            ).scheduleFor(actor);
                        }
                    })
                )
            )
        ).scheduleFor(actor);
    }
    private Direction randomDirection(Actor actor) {
        Direction randDir;
        while (true) {
            Direction[] dirs = Arrays.stream(Direction.values())
                .filter(d -> d != Direction.NONE)
                .toArray(Direction[]::new);
            randDir = dirs[new Random().nextInt(dirs.length)];

            if (isWallInFront(actor, randDir)) {
                continue;
            }
            return randDir;
        }
    }
    private boolean isWallInFront(Actor actor, Direction direction) {
        int oldX = actor.getPosX();
        int oldY = actor.getPosY();
        actor.setPosition(actor.getPosX() + direction.getDx(), actor.getPosY() + direction.getDy());
        if (actor.getScene() != null && actor.getScene().getMap().intersectsWithWall(actor)) {
            actor.setPosition(oldX, oldY);
            return true;
        }
        return false;
    }
}
