package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.While;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.utils.PauseManager;

import static sk.tuke.kpi.oop.game.utils.PauseManager.drawPauseMenu;

public class PauseController implements KeyboardListener {
    private Scene scene;
    public PauseController(Scene scene) {
        this.scene = scene;
    }
    @Override
    public void keyPressed(@NotNull Input.Key key) {
        boolean paused = PauseManager.isPaused();
        PauseManager.init(scene);
        if (key == Input.Key.P) {
            PauseManager.toggle();
            new While<>(
                () -> PauseManager.isPaused(),
                new Invoke<>(() -> drawPauseMenu())
            ).scheduleFor(scene.getFirstActorByType(Ripley.class));
        }
        else if (paused && key == Input.Key.UP) {
                PauseManager.selectPrevious();
            }
        else if (paused && key == Input.Key.DOWN) {
            PauseManager.selectNext();
        }
        else if (paused && key == Input.Key.ENTER) {
            PauseManager.select();
        }
        else if (paused && key == Input.Key.LEFT) {
            System.out.println("LEFT pressed at " + System.nanoTime());
            PauseManager.changeSettingValue(-10);
        }
        else if (paused && key == Input.Key.RIGHT) {
            System.out.println("RIGHT pressed at " + System.nanoTime());
            PauseManager.changeSettingValue(+10);
        }
    }
}
