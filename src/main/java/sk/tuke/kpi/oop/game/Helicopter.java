package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;


public class Helicopter extends AbstractActor {
    private final Animation defaultAnimation;
    private boolean chase;

    public Helicopter() {
        defaultAnimation = new Animation("sprites/heli.png", 64, 64, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(defaultAnimation);
        chase = false;
    }

    public void startChase() {
        chase = true;
//        new Loop<>(new HelicopterChasePlayer()).scheduleFor(this);
    }

    public void stopChase() {
        chase = false;
    }

    public boolean isChase() {
        return chase;
    }

    private boolean isOnPlayer(int pX, int pY, int hX, int hY) {
        return pX == hX && pY == hY;
    }
    private void rotateHeli(String direction) {
        switch (direction) {
            case "left":
                getAnimation().setRotation(90);
                break;
            case "right":
                getAnimation().setRotation(270);
                break;
            case "up":
                getAnimation().setRotation(0);
                break;
            case "down":
                getAnimation().setRotation(180);
                break;
            default:
        }
    }
    private void moveToPlayerAndDamage(Player player) {
        int playerX = player.getPosX();
        int playerY = player.getPosY();

        int heliX = getPosX();
        int heliY = getPosY();

        int jump = 1;

        int dx = playerX - heliX;
        int dy = playerY - heliY;

        if (Math.abs(dx) > Math.abs(dy)) { // x
            if (dx > 0) {
                heliX += jump;
                rotateHeli("right");
            }
            else if (dx < 0) {
                heliX -= jump;
                rotateHeli("left");
            }
        }
        else { // y
            if (dy > 0) {
                heliY += jump;
                rotateHeli("down");
            }
            else if (dy < 0) {
                heliY -= jump;
                rotateHeli("up");
            }
        }


        this.setPosition(heliX, heliY);
        if (isOnPlayer(playerX, playerY, heliX, heliY)) {
            player.setEnergy(player.getEnergy() - 1);
        }
    }

    public void searchAndDestroy() {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }
        Player player = scene.getFirstActorByType(Player.class);
        if (player == null) {
            return;
        }
        new Loop<>(
            new ActionSequence<>(
                new Wait<>(0.0016f),
                new Invoke<>(() -> moveToPlayerAndDamage(player))
            )
        ).scheduleFor(this);
    }
}
