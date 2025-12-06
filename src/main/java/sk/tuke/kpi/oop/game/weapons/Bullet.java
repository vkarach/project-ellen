package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class Bullet extends AbstractActor implements Movable, Fireable {
    SoundUtil ricochetSound = new SoundUtil("sounds/ricochet.wav");
    SoundUtil metalImpactSound = new SoundUtil("sounds/metal_Impact.wav");
    private final SoundUtil AliveImpact = new SoundUtil("sounds/alien_impact.wav");
    private final SoundUtil shoot = new SoundUtil("sounds/pistol_fire.wav");
    private final SoundUtil cartridgeDrop = new SoundUtil("sounds/cartridge_drop.wav");
    private final int speed = 5;
    Animation defaultAnimation;
    public Bullet() {
        defaultAnimation = new Animation("sprites/bullet.png");
        setAnimation(defaultAnimation);
    }
    public int getSpeed() {
        return speed;
    }
    public void startedMoving(Direction direction) {
        defaultAnimation.setRotation(direction.getAngle());
    }
    @Override
    public void collidedWithWall() {
        Scene scene = getScene();
        if (scene != null) {
            if (Math.random() < 0.3) {
                ricochetSound.play(0.5f);
            }
            else {
                metalImpactSound.play(0.5f);
            }
            scene.removeActor(this);
        }
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        shoot.play(0.5f);
        cartridgeDrop.play();
        super.addedToScene(scene);
        new Loop<>(
            new Invoke<>(() -> {
                for (Actor actor : scene.getActors()) {
                    if (actor instanceof Alive && this.intersects(actor) && !(actor instanceof Armed)) {
                        Alive aliveActor = (Alive) actor;
                        aliveActor.getHealth().drain(15);
                        AliveImpact.play();
                        scene.removeActor(this);
                        break;
                    }
                }
            })
        ).scheduleFor(this);
    }
}
