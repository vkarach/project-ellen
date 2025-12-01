package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Enemy;
import sk.tuke.kpi.oop.game.utils.MathUtils;

public class AutoDoor extends Door {
    private boolean isOpen = false;
    public  AutoDoor(String name, Door.Orientation orientation) {
        super(name, orientation);
    }
    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(
            new Invoke<>(() -> {
                boolean shouldOpen = false;   // сбрасываем каждый цикл

                for (Actor actor : scene.getActors()) {
                    if (actor instanceof Alive
                        && !(actor instanceof Enemy)
                        && MathUtils.distanceBetween(this, actor) < 40f)
                    {
                        shouldOpen = true;
                        break;
                    }
                }
                if (shouldOpen) {
                    if (!isOpen()) {
                        super.open();
                    }
                }
                else {
                    if (isOpen()) {
                        super.close();
                    }
                }
            })
        ).scheduleFor(this);
    }
}
