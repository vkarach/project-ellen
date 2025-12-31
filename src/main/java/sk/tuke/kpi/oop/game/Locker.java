package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.*;

import java.util.Map;
import java.util.function.Supplier;

public class Locker extends AbstractActor implements Usable<Ripley> {
    private boolean used;
    private String itemName = "hammer";
    private final Map<String, Supplier<Usable<?>>> ITEMS = Map.of(
        "hammer", Hammer::new,
        "wrench", Wrench::new,
        "ammo", Ammo::new,
        "med", Energy::new
    );
    public enum Type {TYPE1, TYPE2}
    private Direction direction = Direction.NORTH;

    public Locker() {
        Animation defaultAnimation = new Animation("sprites/locker.png");
        setAnimation(defaultAnimation);
    }
    public Locker(Type type) {
        this();
        if (type == Type.TYPE2) {
            Animation defaultAnimation = new Animation("sprites/locker_type2.png");
            setAnimation(defaultAnimation);
        }
    }
    public Locker(Type type, String itemName) {
        this(type);
        this.itemName = itemName;
    }
    public Locker(Type type, String itemName, Direction direction) {
        this(type, itemName);
        if (direction != Direction.NONE) {
            this.direction = direction;
            getAnimation().setRotation(direction.getAngle());
        }
    }
    @Override
    public void useWith(Ripley actor) {
        if (used || getScene() == null) {
            return;
        }

        Supplier<Usable<?>> sup = ITEMS.get(itemName);
        Usable<?> item = sup.get();

        ((Actor) item).setPosition(getPosX() + direction.getDx() * 16, getPosY() + direction.getDy() * 16);
        getScene().addActor((Actor) item);
        used = true;
    }
    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
