package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.oop.game.tools.Hammer;

public class Gameplay extends Scenario {
    @Override
    public void setupPlay(Scene scene) {
        Reactor reactor = new Reactor();
        Cooler cooler = new Cooler(reactor);
        Hammer hammer = new Hammer();
        Light light = new Light();
        DefectiveLight defectLight = new DefectiveLight();

        PowerSwitch reactor_switch  = new PowerSwitch(reactor);
        PowerSwitch cooler_switch = new PowerSwitch(cooler);
        PowerSwitch light_switch = new PowerSwitch(light);
        PowerSwitch defectLight_switch = new PowerSwitch(defectLight);

        scene.addActor(reactor, 64, 64);

        scene.addActor(light, 64, 150);
        scene.addActor(defectLight, 100, 150);

        scene.addActor(cooler, 64, 34);
        scene.addActor(hammer, 64, 100);

        scene.addActor(reactor_switch, 10, 10);
        scene.addActor(cooler_switch, 20, 10);
        scene.addActor(light_switch, 30, 10);

        reactor_switch.switchOn();
        light_switch.switchOn();
        defectLight_switch.switchOn();

        reactor.addDevice(light);
        reactor.addDevice(defectLight);

        new ActionSequence<>(
            new Wait<>(5),
            new Invoke<>(cooler_switch::switchOn)
        ).scheduleFor(cooler);

        new Invoke<>(new Runnable() {
            public void run() {
                reactor.repairWith(hammer);
            }
        });

        new Invoke<>(() -> {
            reactor.repairWith(hammer);
        });

        new When<>(
            () -> reactor.getTemperature() >= 3000,
            new Invoke<>(() -> reactor.repairWith(hammer))
        ).scheduleFor(reactor);

    }

}
