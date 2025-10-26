package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.oop.game.tools.FireExtinguisher;
import sk.tuke.kpi.oop.game.tools.Hammer;
import sk.tuke.kpi.oop.game.tools.Wrench;

public class Gameplay extends Scenario {
    @Override
    public void setupPlay(Scene scene) {
        Reactor reactor = new Reactor();
        Cooler cooler = new Cooler(reactor);
        Hammer hammer = new Hammer();
        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        Wrench wrench = new Wrench();
        Light light = new Light();
        DefectiveLight defectLight = new DefectiveLight();

        PowerSwitch reactorSwitch  = new PowerSwitch(reactor);
        PowerSwitch coolerSwitch = new PowerSwitch(cooler);
        PowerSwitch lightSwitch = new PowerSwitch(light);
        PowerSwitch defectLightSwitch = new PowerSwitch(defectLight);

        scene.addActor(reactor, 64, 64);

        scene.addActor(light, 64, 150);
        scene.addActor(defectLight, 100, 150);

        scene.addActor(cooler, 64, 34);
        scene.addActor(hammer, 64, 100);
        scene.addActor(fireExtinguisher, 128, 100);
        scene.addActor(wrench, 100, 170);

        scene.addActor(reactorSwitch, 10, 10);
        scene.addActor(coolerSwitch, 20, 10);
        scene.addActor(lightSwitch, 30, 10);

        reactorSwitch.switchOn();
        lightSwitch.switchOn();
        defectLightSwitch.switchOn();

        reactor.addDevice(light);
        reactor.addDevice(defectLight);

        new ActionSequence<>(
            new Wait<>(5),
            new Invoke<>(coolerSwitch::switchOn)).scheduleFor(cooler
        );

        new ActionSequence<>(
            new Wait<>(5),
            new Invoke<>(wrench::useWith)).scheduleFor(defectLight
        );


        new When<>(
            () -> reactor.getTemperature() >= 3000,
            new Invoke<>(hammer::useWith)
        ).scheduleFor(reactor);

        new When<>(
            () -> reactor.getDamage() == 100,
            new Invoke<>(fireExtinguisher::useWith)
        ).scheduleFor(reactor);

    }

}
