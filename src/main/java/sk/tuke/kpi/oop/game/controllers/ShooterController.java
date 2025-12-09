package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.actions.Fire;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.utils.PauseManager;

public class ShooterController implements KeyboardListener {
    private final Armed armedActor;
    public ShooterController(Armed armedActor) {
        this.armedActor = armedActor;
    }
    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (PauseManager.isPaused()) {
            return;
        }
        if(key.equals(Input.Key.SPACE)) {
            new Fire<>().scheduleFor(armedActor);
        }
    }
}
