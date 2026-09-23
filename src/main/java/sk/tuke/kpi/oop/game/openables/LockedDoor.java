package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.gamelib.graphics.Overlay;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class LockedDoor extends Door {
    private final SoundUtil unlockFail = new SoundUtil("sounds/door_fail.wav");
    private boolean isLocked = true;
    public  LockedDoor(String name, Orientation orientation) {
        super(name, orientation);
    }
    public boolean isLocked() {
        return isLocked;
    }
    public void lock() {
        isLocked = true;
        super.close();
    }
    public void unlock() {
        isLocked = false;
        super.open();
    }
    @Override
    public void useWith(Actor actor) {
        Scene scene = actor.getScene();
        if (scene != null) {
            Overlay overlay = scene.getOverlay();
            Font font = new Font(8, Color.ORANGE, Font.Style.NORMAL);
            overlay.drawText("Door locked.\nUse access card.", actor.getPosX(), actor.getPosY() + 40, font).showFor(1);
            unlockFail.play();
        }
    }
}
