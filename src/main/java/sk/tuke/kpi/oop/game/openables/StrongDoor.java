package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;

public class StrongDoor extends Door {
    private final Animation openDoorAnimation;
    private final Animation closeDoorAnimation;
    private boolean inMove = false;
    public StrongDoor(String name, Orientation orientation) {
        super(name, orientation);
        if  (orientation == Orientation.HORIZONTAL) {
            openDoorAnimation = new Animation("sprites/hdoor_strong.png", 32, 16, 0.2f, Animation.PlayMode.ONCE);
            closeDoorAnimation = new Animation("sprites/hdoor_strong.png", 32, 16, 0.2f, Animation.PlayMode.ONCE_REVERSED);
        }
        else {
            openDoorAnimation = new Animation("sprites/vdoor_strong.png", 16, 32,0.2f, Animation.PlayMode.ONCE);
            closeDoorAnimation = new Animation("sprites/vdoor_strong.png", 16, 32, 0.2f, Animation.PlayMode.ONCE_REVERSED);
        }
        closeDoorAnimation.setScale(2);
        openDoorAnimation.setScale(2);
        setAnimation(closeDoorAnimation);
        openDoorAnimation.pause();
    }
    @Override
    public void setTile(MapTile.Type type) {
        if (getScene() == null) {
            return;
        }
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16).setType(type);
        if (this.orientation == Orientation.HORIZONTAL) {
            for (int i = 0; i < 4; i++) {
                getScene().getMap().getTile(getPosX() / 16 + i, getPosY() / 16).setType(type);
                getScene().getMap().getTile(getPosX() / 16 + i, getPosY() / 16 + 1).setType(type);
            }
        }
        else {
            for (int i = 0; i < 4; i++) {
                getScene().getMap().getTile(getPosX() / 16, getPosY() / 16 + i).setType(type);
                getScene().getMap().getTile(getPosX() / 16 + 1, getPosY() / 16 + i).setType(type);
            }
        }
    }
    @Override
    public void open() {
        if (inMove) {
            return;
        }
        inMove = true;
        new ActionSequence<>(
            new Wait<>(closeDoorAnimation.getFrameCount() * closeDoorAnimation.getFrameDuration()),
            new Invoke<>(()->inMove = false)
        ).scheduleFor(this);
        super.open();
        setAnimation(openDoorAnimation);
        openDoorAnimation.resetToFirstFrame();
        openDoorAnimation.play();
    }
    @Override
    public void close() {
        if (inMove) {
            return;
        }
        inMove = true;
        new ActionSequence<>(
            new Wait<>(closeDoorAnimation.getFrameCount() * closeDoorAnimation.getFrameDuration()),
            new Invoke<>(()->inMove = false)
        ).scheduleFor(this);
        super.close();
        setAnimation(closeDoorAnimation);
        closeDoorAnimation.resetToFirstFrame();
        closeDoorAnimation.play();
    }
}
