package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;

public class Door extends AbstractActor implements Openable {
    public static final Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static final Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);
    private boolean isOpen = false;
    Animation openDoorAnimation;
    Animation closeDoorAnimation;
    public Door() {
        openDoorAnimation = new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.ONCE);
        closeDoorAnimation = new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.ONCE_REVERSED);
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
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16).setType(MapTile.Type.CLEAR);
        isOpen = true;
        setAnimation(openDoorAnimation);
        openDoorAnimation.resetToFirstFrame();
        openDoorAnimation.play();
        getScene().getMessageBus().publish(DOOR_OPENED, this);
    }
    public void close() {
        if (!isOpen || getScene() == null) {
            return;
        }
        isOpen = false;
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16).setType(MapTile.Type.WALL);
        setAnimation(closeDoorAnimation);
        closeDoorAnimation.resetToFirstFrame();
        closeDoorAnimation.play();
        getScene().getMessageBus().publish(DOOR_CLOSED, this);
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        scene.getMap().getTile(getPosX() / 16, getPosY() / 16).setType(MapTile.Type.WALL);
    }
}
