package sk.tuke.kpi.oop.game;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

import java.util.Random;

public class DefectiveLight extends Light {
    private final Random random = new Random();
    public DefectiveLight() {
        super();
    }
    public void defectLight() {
        if (isPowerOn()) {
            int randomNum = random.nextInt(201);
            if (randomNum == 1)
                toggleLight();
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::defectLight)).scheduleFor(this);
    }

}
