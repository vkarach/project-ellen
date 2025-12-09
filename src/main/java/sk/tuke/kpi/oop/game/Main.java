package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.backends.lwjgl.LwjglBackend;
import sk.tuke.kpi.oop.game.scenarios.FinalMission;

public class Main {
    public static void main(String[] args) {
        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);
        //test
        Game game = new GameApplication(windowSetup, new LwjglBackend());

        Scene finalMission = new World("Final Mission", "maps/final-mission.tmx", new FinalMission.Factory());
        game.addScene(finalMission);
        FinalMission scenario = new FinalMission();
        finalMission.addListener(scenario);

        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);
        game.start();
    }
}
