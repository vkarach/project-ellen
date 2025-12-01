package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.Arrays;
import java.util.Random;

public class RandomlyMoving<A extends Movable> implements Behaviour<A> {
    public void setUp(A actor) {
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(() -> {
                    Direction randDir = randomDirection(actor);
                    actor.getAnimation().setRotation(randDir.getAngle());
                    new Move<>(randDir, 0.75f).scheduleFor(actor);
                }),
                new Wait<>(0.75f)
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
        if (actor.getScene() != null) {
            if (actor.getScene().getMap().intersectsWithWall(actor)) {
                actor.setPosition(oldX, oldY);
                return true;
            }
        }
        return false;
    }
}
