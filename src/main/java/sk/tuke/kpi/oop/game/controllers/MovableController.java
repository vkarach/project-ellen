package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MovableController implements KeyboardListener {
    private Move<Movable> moveAction;
    private final Set<Input.Key> pressedKeys = new HashSet<>();
    private final Movable actor;
    private Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(
        Map.entry(Input.Key.UP, Direction.NORTH),
        Map.entry(Input.Key.DOWN, Direction.SOUTH),
        Map.entry(Input.Key.RIGHT, Direction.EAST),
        Map.entry(Input.Key.LEFT, Direction.WEST)
    );
    public MovableController(Movable actor) {
        this.actor = actor;
    }
    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (keyDirectionMap.containsKey(key)) {
            pressedKeys.add(key);
            Direction direction = keyDirectionMap.get(key);
            if (moveAction != null) {
                moveAction.stop();
            }
            moveAction = new Move<>(direction, Float.MAX_VALUE);
            moveAction.scheduleFor(actor);
        }
    }
    @Override
    public void keyReleased(@NotNull Input.Key key) {
        if (keyDirectionMap.containsKey(key)) {
            pressedKeys.remove(key);
            if (!pressedKeys.isEmpty()) {
                Input.Key last = pressedKeys.iterator().next();

                if (moveAction != null) moveAction.stop();

                moveAction = new Move<>(keyDirectionMap.get(last), Float.MAX_VALUE);
                moveAction.scheduleFor(actor);
            } else {
                if (moveAction != null) moveAction.stop();
            }
        }
    }
}
