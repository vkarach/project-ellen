package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.backends.lwjgl.LwjglBackend;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.scenarios.FirstSteps;

public class Main {
    public static void main(String[] args) {
        // setting game window: window name and its dimensions
        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);

        // creating instance of game application
        // using class `GameApplication` as implementation of interface `Game`
        Game game = new GameApplication(windowSetup, new LwjglBackend()); // in case of MacOS system use "new Lwjgl2Backend()" as the second parameter

        // creating scene for game
        // using class `World` as implementation of interface `Scene`
        Scene scene = new World("world");

        // adding scene into the game
        game.addScene(scene);
        // exit on pressing escape
        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);
        FirstSteps scenario = new FirstSteps();
        scene.addListener(scenario);
        // running the game
        game.start();

    }
}
