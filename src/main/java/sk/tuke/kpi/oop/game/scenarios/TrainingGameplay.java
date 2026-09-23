package sk.tuke.kpi.oop.game.scenarios;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.oop.game.*;
import sk.tuke.kpi.oop.game.items.FireExtinguisher;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.items.Mjolnir;
import sk.tuke.kpi.oop.game.items.Wrench;

public class TrainingGameplay extends Scenario {
    @Override
    public void setupPlay(Scene scene) {
        Reactor reactor = new Reactor();
        Cooler cooler = new Cooler(reactor);
        Hammer hammer = new Hammer();
        Mjolnir mjolnir = new Mjolnir();
        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        Wrench wrench = new Wrench();
        Light light = new Light();
        DefectiveLight defectLight = new DefectiveLight();

        Helicopter heli = new Helicopter();

        ChainBomb gigabomb = new ChainBomb(3);
        ChainBomb gigabomb1 = new ChainBomb(30);
        TimeBomb bomb = new TimeBomb(5);
        TimeBomb bomb1 = new TimeBomb(2);
        TimeBomb bomb2 = new TimeBomb(8);

        PowerSwitch reactorSwitch  = new PowerSwitch(reactor);
        PowerSwitch coolerSwitch = new PowerSwitch(cooler);
        PowerSwitch lightSwitch = new PowerSwitch(light);
        PowerSwitch defectLightSwitch = new PowerSwitch(defectLight);

        scene.addActor(reactor, 64, 64);

        scene.addActor(light, 64, 150);
        scene.addActor(defectLight, 100, 150);

        scene.addActor(cooler, 64, 34);
        scene.addActor(hammer, 64, 100);
        scene.addActor(mjolnir, 164, 100);
        scene.addActor(fireExtinguisher, 128, 100);
        scene.addActor(wrench, 100, 170);

        scene.addActor(reactorSwitch, 10, 10);
        scene.addActor(coolerSwitch, 20, 10);
        scene.addActor(lightSwitch, 30, 10);


        Teleport teleportA = new Teleport(null);
        Teleport teleportB = new Teleport(null);
        Teleport teleportC = new Teleport(null);

        scene.addActor(teleportA, 50, 300);
        scene.addActor(teleportC, 250, 300);
        scene.addActor(teleportB, 250, 50);

        teleportA.setDestination(teleportC);
        teleportC.setDestination(teleportB);
        teleportB.setDestination(teleportA);


        scene.addActor(heli, 200, 200);

        scene.addActor(gigabomb, 150, 150);
        scene.addActor(gigabomb1, 180, 150);
        scene.addActor(bomb, 200, 150);
        scene.addActor(bomb1, 170, 300);
        scene.addActor(bomb2, 50, 150);

        gigabomb.activate();

        reactorSwitch.switchOn();
        lightSwitch.switchOn();
        defectLightSwitch.switchOn();

        reactor.addDevice(light);
        reactor.addDevice(defectLight);

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
