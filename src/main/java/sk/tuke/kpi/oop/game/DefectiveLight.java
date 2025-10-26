package sk.tuke.kpi.oop.game;
import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

import java.util.Random;

public class DefectiveLight extends Light implements Repairable {
    private final Random random = new Random();
    private boolean repaired;
    public void defectLight() {
        if (isPowerOn() && !repaired && random.nextInt(25) == 0) {
            toggle();
            new ActionSequence<>(
                new Wait<>(0.5f),
                new Invoke<>(this::toggle)
            ).scheduleFor(this);
        }
    }
    @Override
    public boolean repair() {
        if (repaired) {
            return false;
        }
        repaired = true;
        if (!isOn()) {
            toggle();
        }
        new ActionSequence<>(
            new Wait<>(10),
            new Invoke<>(() -> {repaired = false; })
        ).scheduleFor(this);
        return true;
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::defectLight)).scheduleFor(this);
    }

}
