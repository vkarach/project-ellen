package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.oop.game.story.Dialogue;
import sk.tuke.kpi.oop.game.utils.PauseManager;

public class Speak<A extends Actor> implements Action<A> {
    private final Dialogue dialogue;
    private int index = 0;
    private float interval = 0;
    private int outputCount = 0;
    private float timer = 0;
    private boolean done = false;
    private boolean waitingForKey = false;
    private A actor;
    private final Font whiteFont = new Font(7, Color.WHITE, Font.Style.NORMAL);
    private final Font orangeFont = new Font(6, Color.ORANGE, Font.Style.NORMAL);

    public Speak(Dialogue dialogue) {
        this.dialogue = dialogue;
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
    public void setDone(boolean done) {
        this.done = done;
    }
    @Override
    public void reset() {
        index = 0;
        timer = 0;
        outputCount = 0;
        done = false;
        waitingForKey = false;
    }

    @Override
    public void execute(float deltaTime) {
        if (done) {
            return;
        }
        if (actor == null) {
            return;
        }
        Scene scene = actor.getScene();
        if (scene == null) {
            return;
        }
        if (index >= dialogue.getLines().size()) {
            done = true;
            return;
        }
        Dialogue.Line line = dialogue.getLines().get(index);
        if (timer == 0) {
            interval = line.time;
        }

        Actor speaker = scene.getFirstActorByName(line.speaker);
        if (speaker == null) {
            return;
        }
        if (PauseManager.isPaused()) {
            scene.getOverlay().drawText(line.text.substring(0, outputCount), speaker.getPosX() + speaker.getWidth(), speaker.getPosY() + 30, whiteFont);
            return;
        }
        int len = line.text.length();
        timer += deltaTime;

        int shouldBe;
        if (len > 0 && interval > 0f) {
            int count = (int) Math.floor((timer / interval) * len);
            if (count > len) {
                count = len;
            }
            if (count < 0) {
                count = 0;
            }
            shouldBe = count;
        }
        else {
            shouldBe = len;
        }
        if (shouldBe > outputCount) {
            outputCount = shouldBe;
        }
        if (anyKeyPressed(scene)) {
            outputCount = len;
            timer = interval;
        }

        scene.getOverlay().drawText(line.text.substring(0, outputCount), speaker.getPosX() + speaker.getWidth(), speaker.getPosY() + 30, whiteFont);

        if (waitingForKey) {
            float fraction = timer - (int)timer;
            if (timer >= 1.5 && fraction < 0.5) {
                scene.getOverlay().drawText("Press any key...",
                    speaker.getPosX() + speaker.getWidth(), speaker.getPosY() + 20, orangeFont
                );
            }
            if (anyKeyPressed(scene)) {
                waitingForKey = false;
                outputCount = 0;
                timer = 0f;
                index++;
                if (index >= dialogue.getLines().size()) {
                    done = true;
                }
                return;
            }
            return;
        }
        if (timer >= interval) {
            waitingForKey = true;
            outputCount = len;
            timer = 0f;
        }
    }
    private boolean anyKeyPressed(Scene scene) {
        for (Input.Key key : Input.Key.values()) {
            if (scene.getInput().isKeyPressed(key) && key != Input.Key.P) {
                if (key == Input.Key.ENTER) {
                    done = true;
                }
                return true;
            }
        }
        return false;
    }
}
