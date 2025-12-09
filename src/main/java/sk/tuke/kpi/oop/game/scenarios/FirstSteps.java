package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.*;


public class FirstSteps implements SceneListener { // extends Scenario
    private Ripley ripley;
    private final int fontSize = 18;

//    Color customColor = new Color(0, 0, 0, 0);

//    private Font whiteFont = new Font(fontSize, Color.WHITE, Font.Style.NORMAL);
//    private Font greenFont = new Font(fontSize, Color.GREEN, Font.Style.NORMAL);
//    private Font yellowFont = new Font(fontSize, Color.YELLOW, Font.Style.NORMAL);
//    private Font redFont = new Font(fontSize, Color.RED, Font.Style.NORMAL);

//    private void drawInterface(Scene scene) {
//        scene.getGame().pushActorContainer(ripley.getBackpack());
//
//        int windowHeight = scene.getGame().getWindowSetup().getHeight();
//        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
//
//        int windowWidth = scene.getGame().getWindowSetup().getWidth();
//        int xTextPos = windowWidth - 125;
//
//        int ripleyEnergy = ripley.getEnergy();
//        Font font;
//        if (ripleyEnergy == 100) {
//            font = greenFont;
//        }
//        else if (ripleyEnergy >= 20) {
//            font = yellowFont;
//        }
//        else {
//            font = redFont;
//        }
//        scene.getGame().getOverlay().drawText("Energy:", xTextPos, yTextPos, whiteFont);
//        scene.getGame().getOverlay().drawText("        "+ripley.getEnergy(), xTextPos, yTextPos, font);
//
//        scene.getGame().getOverlay().drawText("Ammo:", xTextPos, yTextPos - 20, whiteFont);
//        if (ripley.getAmmo() > 0) {
//            scene.getGame().getOverlay().drawText("        "+ripley.getAmmo(), xTextPos, yTextPos - 20, whiteFont);
//
//        }
//        else {
//            scene.getGame().getOverlay().drawText("        x", xTextPos, yTextPos - 20, redFont);
//        }
//    }
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        this.ripley = new Ripley();
        scene.addActor(ripley, 0, 0);

        Energy energy = new Energy();
        scene.addActor(energy, 100, 0);

        Ammo ammo = new Ammo();
        scene.addActor(ammo, 100, 100);

        Hammer hammer = new Hammer();
        scene.addActor(hammer, -100, 0);

        Wrench wrench = new Wrench();
        scene.addActor(wrench, -100, -100);

        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        scene.addActor(fireExtinguisher, -100, 100);

        MovableController movableController = new MovableController(ripley);
        scene.getInput().registerListener(movableController);

        KeeperController keeperController = new KeeperController(ripley);
        scene.getInput().registerListener(keeperController);

        // watches when ripley on energy and ammo use it
        new Loop<>(
            new Invoke<>(() -> {
                if (ripley.intersects(energy)) {
                    new Use<>(energy).scheduleFor(ripley);
                }
                if (ripley.intersects(ammo)) {
                    new Use<>(ammo).scheduleFor(ripley);
                }
            })
        ).scheduleFor(ripley);
    }
    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        ripley.showRipleyState();
    }
}
