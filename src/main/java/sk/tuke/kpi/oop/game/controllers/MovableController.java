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
    private final Set<Direction> pressedDirections = new HashSet<>();
    private final Movable actor;
    final private Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(
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

            pressedDirections.add(keyDirectionMap.get(key));

            Direction combineDirection = Direction.NONE;
            for (Direction d : pressedDirections) {
                combineDirection = combineDirection.combine(d);
            }
            if (moveAction != null) {
                moveAction.stop();
            }
            moveAction = new Move<>(combineDirection, Float.MAX_VALUE);
            moveAction.scheduleFor(actor);
        }
    }
    @Override
    public void keyReleased(@NotNull Input.Key key) {
        if (keyDirectionMap.containsKey(key)) {
            pressedDirections.remove(keyDirectionMap.get(key));
            if (!pressedDirections.isEmpty()) {
                if (moveAction != null) moveAction.stop();
                Direction combineDirection = Direction.NONE;
                for (Direction d : pressedDirections) {
                    combineDirection = combineDirection.combine(d);
                }
                moveAction = new Move<>(combineDirection, Float.MAX_VALUE);
                moveAction.scheduleFor(actor);
            }
            else if (moveAction != null) {
                moveAction.stop();
            }
        }
    }
}
