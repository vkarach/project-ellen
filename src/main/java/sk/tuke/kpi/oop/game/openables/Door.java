package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Usable;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class Door extends AbstractActor implements Openable, Usable<Actor> {
    private final SoundUtil openSound = new SoundUtil("sounds/door_open.wav");
    private final SoundUtil closeSound = new SoundUtil("sounds/door_close.wav");
    public static final Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static final Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);
    private boolean isOpen = false;
    private boolean inMove = false;
    private final Animation openDoorAnimation;
    private final Animation closeDoorAnimation;
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
    public Orientation orientation;
    private final String name;
    public Door(String name, Orientation orientation) {
        super(name);
        this.orientation = orientation;
        this.name = name;
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
        if (isOpen || getScene() == null || inMove) {
            return;
        }
        setTile(MapTile.Type.CLEAR);
        isOpen = true;
        setAnimation(openDoorAnimation);
        openDoorAnimation.resetToFirstFrame();
        inMove = true;
        new ActionSequence<>(
            new Wait<>(openDoorAnimation.getFrameCount() * openDoorAnimation.getFrameDuration()),
            new Invoke<>(()->inMove = false)
        ).scheduleFor(this);
        openDoorAnimation.play();
        openSound.play(0.5f);
        getScene().getMessageBus().publish(DOOR_OPENED, this);
    }
    public boolean canClose() {
        for (Actor actor : getScene().getActors()) {
            if (actor == this) {
                continue;
            }
//            if (MathUtils.rectangleActorHitbox(this).contains(actor.getPosX() +actor.getWidth() / 2, actor.getPosY() + actor.getHeight() / 2) ) {
//                return false;
//            }
        }
        return true;
    }
    public void close() {
        if (!isOpen || getScene() == null || inMove || !canClose()) {
            return;
        }
        isOpen = false;
        inMove = true;
        new ActionSequence<>(
            new Wait<>(closeDoorAnimation.getFrameCount() * closeDoorAnimation.getFrameDuration()),
            new Invoke<>(()->inMove = false)
        ).scheduleFor(this);
        setTile(MapTile.Type.WALL);
        setAnimation(closeDoorAnimation);
        closeDoorAnimation.resetToFirstFrame();
        closeDoorAnimation.play();
        closeSound.play(0.5f);
        getScene().getMessageBus().publish(DOOR_CLOSED, this);
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
