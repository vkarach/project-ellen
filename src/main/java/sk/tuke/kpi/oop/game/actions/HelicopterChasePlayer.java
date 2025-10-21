package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Helicopter;

public class HelicopterChasePlayer extends AbstractAction<Helicopter> {
    private Helicopter helicopter;

    @Override
    public void execute(float deltaTime) {
        if (getActor() == null || isDone()) {
            return;
        }
        getActor().searchAndDestroy();

        setDone(true);
    }
}
