package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.backends.lwjgl.LwjglBackend;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.scenarios.EscapeRoom;
import sk.tuke.kpi.oop.game.scenarios.FirstSteps;
import sk.tuke.kpi.oop.game.scenarios.MissionImpossible;

public class Main {
    public static void main(String[] args) {
        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);

        Game game = new GameApplication(windowSetup, new LwjglBackend());

        boolean runMissionImpossible = false;
        if (runMissionImpossible) {
            Scene missionImpossible = new World("mission-impossible", "maps/mission-impossible.tmx", new MissionImpossible.Factory());
            game.addScene(missionImpossible);
            MissionImpossible scenario = new MissionImpossible();
            missionImpossible.addListener(scenario);
        }
        else {
            Scene escapeRoom = new World("escape-room", "maps/escape-room.tmx", new EscapeRoom.Factory());
            game.addScene(escapeRoom);
            EscapeRoom scenario = new EscapeRoom();
            escapeRoom.addListener(scenario);
        }
        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);
        game.start();
    }
}
