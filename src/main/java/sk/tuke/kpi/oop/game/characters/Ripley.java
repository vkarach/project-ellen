package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Ripley extends AbstractActor implements Actor, Movable, Keeper<Collectible> {
    private final int speed;
    private int energy;
    private int ammo;
    private final Animation moveAnimation;
    private final Backpack backpack;
    public Ripley() {
        super("Ellen");
        moveAnimation = new Animation("sprites/player.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(moveAnimation);
        moveAnimation.pause();
        speed = 2;
        energy = 100;
        ammo = 14;
        this.backpack = new Backpack("Ripley's backpack", 2);
    }
    public Backpack getBackpack() {
        return backpack;
    }
    @Override
    public void startedMoving(Direction direction) {
        moveAnimation.setRotation(direction.getAngle());
        moveAnimation.play();
    }
    @Override
    public void stoppedMoving() {
        moveAnimation.pause();
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        if (energy >= 0 && energy <= 100) {
            this.energy = energy;
        }
    }
    public int getAmmo() {
        return ammo;
    }
    public void setAmmo(int ammo) {
        if (ammo >= 0) {
            this.ammo = ammo;
        }
    }
}
