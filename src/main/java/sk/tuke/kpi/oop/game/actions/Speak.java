package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.oop.game.story.Dialogue;

public class Speak<A extends Actor> implements Action<A> {
    private float interval = 0;
    private final Dialogue dialogue;
    private int index = 0;
    private float timer = 0;
    private boolean done = false;


//    private final float interval;

    private A actor;
    private final Font font = new Font(8, Color.WHITE, Font.Style.NORMAL);

    public Speak(Dialogue dialogue) {//, float intervalSeconds) {
        this.dialogue = dialogue;
//        this.interval = intervalSeconds;
    }

    @Override
    public void setActor(A actor) {
        this.actor = actor;
    }

    @Override
    public A getActor() {
        return actor;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void reset() {
        index = 0;
        timer = 0;
        done = false;
    }

    @Override
    public void execute(float deltaTime) {
        if (done) return;
        if (actor == null) return;

        Scene scene = actor.getScene();
        if (scene == null) return;

        if (index >= dialogue.lines.size()) {
            done = true;
            return;
        }
        Dialogue.Line line = dialogue.lines.get(index);

        if (timer == 0) {
            interval = line.time;
//            System.out.println(interval);
        }

        Actor speaker = scene.getFirstActorByName(line.speaker);
        if (speaker == null) {
            return;
        }

        scene.getOverlay().drawText(line.text, speaker.getPosX() + speaker.getWidth(), speaker.getPosY() + 30, font);

//        System.out.println("speaker: " + line.speaker + "text: " + line.text + "time: " + line.time);

        timer += deltaTime;
        if (timer >= interval) {
            timer = 0;
            index++;
            if (index >= dialogue.lines.size()) {
                done = true;
            }
        }
    }
}
