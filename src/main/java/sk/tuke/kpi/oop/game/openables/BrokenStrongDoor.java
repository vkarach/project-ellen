package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.*;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.oop.game.Repairable;

public class BrokenStrongDoor extends StrongDoor implements Repairable {
    private boolean broken = true;
    private int repairs = 0;
    private int hint = 0;
    public BrokenStrongDoor(String name, Orientation orientation) {
        super(name, orientation);
    }
    @Override
    public boolean repair(RepairKind kind) {
        if (!broken) {
            return false;
        }
        if (kind == RepairKind.WRENCH && repairs < 1) {
            Font font = new Font(8, Color.ORANGE, Font.Style.NORMAL);
            String text = hint < 2 ? "need to stop it first" : "try to use hammer";
            getScene().getOverlay().drawText(text, getPosX() + 12, getPosY() + getHeight(), font).showFor(1.5f);
            hint++;
            return false;
        }
        repairs++;
        if (repairs == 2) {
            broken = false;
        }
        return true;
    }
    @Override
    public void turnOn() {
        if (!broken) {
            open();
        }
    }
    @Override
    public void turnOff() {
        if (!broken) {
            close();
        }
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        Animation oldAnimation = getAnimation();

        Animation frame1 = new Animation("sprites/vdoor_strong1.png", 16, 32);
        Animation frame2 = new Animation("sprites/vdoor_strong2.png", 16, 32);
        frame1.setScale(2);
        frame2.setScale(2);

        new While<>(
            () -> repairs == 0,
            new ActionSequence<>(
                new Invoke<>(()->setAnimation(frame2)),
                new Wait<>(1),
                new Invoke<>(()->setAnimation(frame1)),
                new Wait<>(1)
            )
        ).scheduleFor(this);

        new When<>(
            () -> repairs == 2,
            new Invoke<>(()->{
                broken = false;
                setAnimation(oldAnimation);
            })
        ).scheduleFor(this);
    }
}
