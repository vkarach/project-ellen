package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.backends.lwjgl.LwjglBackend;
import sk.tuke.kpi.oop.game.scenarios.EscapeRoom;
import sk.tuke.kpi.oop.game.scenarios.MissionImpossible;
import sk.tuke.kpi.oop.game.scenarios.FinalMission;

public class Main {
    public static void main(String[] args) {
        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);
        //test
        Game game = new GameApplication(windowSetup, new LwjglBackend());

        if (2==1) {
            Scene missionImpossible = new World("mission-impossible", "maps/mission-impossible.tmx", new MissionImpossible.Factory());
            game.addScene(missionImpossible);
            MissionImpossible scenario = new MissionImpossible();
            missionImpossible.addListener(scenario);
        }
        else if (2==1) {
            Scene escapeRoom = new World("escape-room", "maps/escape-room.tmx", new EscapeRoom.Factory());
            game.addScene(escapeRoom);
            EscapeRoom scenario = new EscapeRoom();
            escapeRoom.addListener(scenario);
        }
        else {
            Scene final_mission = new World("not implemented yet", "maps/final-mission.tmx", new FinalMission.Factory());
            game.addScene(final_mission);
            FinalMission scenario = new FinalMission();
            final_mission.addListener(scenario);
        }
        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);
        game.start();
    }
}
