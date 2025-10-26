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
        if (isPowerOn() && !repaired) {
            int randomNum = random.nextInt(201);
            if (randomNum == 1) {
                toggleLight();
            }
        }
    }
    @Override
    public boolean repair() {
        repaired = true;
        if (!isOn()) {
            toggleLight();
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
