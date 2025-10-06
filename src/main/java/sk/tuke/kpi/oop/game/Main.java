package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Main extends AbstractActor {
    private Reactor reactor;
    private Hammer hammer;
    private Controller controller;
    private Light light;
    public Main() {
        setAnimation(new Animation("sprites/ammo.png", 16, 16));
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        reactor = new Reactor();
        controller = new Controller();
        hammer = new Hammer();
        light = new Light();

        scene.addActor(reactor, 145, 255);
        scene.addActor(hammer, 100, 170);
        scene.addActor(light, 208, 255); //175, 355

        reactor.addLight(light);
        controller.toggle(reactor);
        light.toggleLight();

        reactor.removeLight(light);

    }
    public void print_info(Reactor reactor, Hammer hammer) {
        System.out.println("----------------------------------------------");
        if (reactor != null) System.out.println("Reactor:\n\tTemperature:" + reactor.getTemperature() + "\n\tReactor damage: " + reactor.getDamage());
        if (hammer != null) System.out.println("Hammer:\n\tUsages: " + hammer.getUsages());
    }
}
