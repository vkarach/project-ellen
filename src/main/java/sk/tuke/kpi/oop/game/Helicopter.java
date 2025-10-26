package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.graphics.Animation;


public class Helicopter extends AbstractActor {
    private final Animation defaultAnimation;
    private boolean chase;
    public Helicopter() {
        defaultAnimation = new Animation("sprites/heli.png", 64,64, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
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
    public void searchAndDestroy() {
        Scene scene = getScene();
        Player player = scene.getFirstActorByType(Player.class);
        int playerPosX = player.getPosX();
        int playerPosY = player.getPosY();

        int heliPosX = getPosX();
        int heliPosY = getPosY();

        int jump = 1;

        if (playerPosX > heliPosX) {
            heliPosX += jump;
        }
        else {
            heliPosX -= jump;
        }
        if (playerPosY > heliPosY) {
            heliPosY += jump;
        }
        else {
            heliPosY -= jump;
        }

        this.setPosition(heliPosX, heliPosY);
    }
}
