package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Reactor;

public class PerpetualReactorHeating extends AbstractAction<Reactor> {
    private int increasment;

    public PerpetualReactorHeating(int increasment) {
        this.increasment = increasment;
    }

    @Override
    public void execute(float deltaTime) {
        if (getActor() == null || isDone()) {
            return;
        }
        getActor().increaseTemperature(increasment);
    }
}
