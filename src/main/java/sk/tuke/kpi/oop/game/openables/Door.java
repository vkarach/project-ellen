package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class Door extends AbstractActor implements Openable, Usable<Actor> {
    private final SoundUtil openSound = new SoundUtil("sounds/door_open.wav");
    private final SoundUtil closeSound = new SoundUtil("sounds/door_close.wav");
    public static final Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static final Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);
    private boolean isOpen = false;
    private boolean playSound = true;
    private final Animation openDoorAnimation;
    private final Animation closeDoorAnimation;
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
    private final Orientation orientation;

    public Door(String name, Orientation orientation) {
        super(name);
        this.orientation = orientation;
        if (orientation == Orientation.HORIZONTAL) {
            openDoorAnimation = new Animation("sprites/hdoor.png", 32, 16, 0.1f, Animation.PlayMode.ONCE);
            closeDoorAnimation = new Animation("sprites/hdoor.png", 32, 16, 0.1f, Animation.PlayMode.ONCE_REVERSED);
        }
        else {
            openDoorAnimation = new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.ONCE);
            closeDoorAnimation = new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.ONCE_REVERSED);
        }
        setAnimation(openDoorAnimation);
        openDoorAnimation.pause();
    }
    public boolean isOpen() {
        return isOpen;
    }
    public void open() {
        if (isOpen || getScene() == null) {
            return;
        }
        setTile(MapTile.Type.CLEAR);
        isOpen = true;
        setAnimation(openDoorAnimation);
        openDoorAnimation.resetToFirstFrame();
        openDoorAnimation.play();
        if (playSound) {
            openSound.play(0.5f);
        }
        if (getScene() != null) {
            getScene().getMessageBus().publish(DOOR_OPENED, this);
        }
    }
    public void close() {
        if (!isOpen) {
            return;
        }
        setTile(MapTile.Type.WALL);
        isOpen = false;
        setAnimation(closeDoorAnimation);
        closeDoorAnimation.resetToFirstFrame();
        closeDoorAnimation.play();
        if (playSound) {
            closeSound.play(0.5f);
        }

        if (getScene() != null) {
            getScene().getMessageBus().publish(DOOR_CLOSED, this);
        }
    }
    public void close(boolean sound) {
        if (!sound) {
            playSound = false;
            close();
            playSound = true;
        }
        else {
            close();
        }
    }
    public void open(boolean sound) {
        if (!sound) {
            playSound = false;
            open();
            playSound = true;
        }
        else {
            open();
        }
    }
    @Override
    public void useWith(Actor actor) {
        if (actor == null) {
            return;
        }
        if (isOpen) {
            close();
        }
        else {
            open();
        }
    }
    public void setTile(MapTile.Type type) {
        if (getScene() == null) {
            return;
        }
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16).setType(type);
        if (orientation == Orientation.HORIZONTAL) {
            getScene().getMap().getTile(getPosX() / 16 + 1, getPosY() / 16).setType(type);
        }
        else {
            getScene().getMap().getTile(getPosX() / 16, getPosY() / 16 + 1).setType(type);
        }
    }
    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setTile(MapTile.Type.WALL);
    }
}
