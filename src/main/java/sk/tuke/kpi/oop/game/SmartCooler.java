package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

public class SmartCooler extends Cooler {

    public SmartCooler(Reactor reactor) {
        super(reactor);
    }

    @Override
    public void coolReactor() {
        Reactor reactor = getReactor();
        if (reactor == null) {
            return;
        }
        // 1500 - 2500
        int temp = reactor.getTemperature();
        if (temp > 2500) {
            turnOn();
        }
        if (temp < 1500) {
            turnOff();
        }
        if (isOn()) {
            reactor.decreaseTemperature(2);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::coolReactor)).scheduleFor(this);
    }
}
