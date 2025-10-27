package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.awt.Rectangle;

public class Teleport extends AbstractActor {
    private Teleport connectedTeleport;
    private boolean canTeleport;
    public Teleport() {
        this(null);
    }
    public Teleport(Teleport destinationT) {
        this.connectedTeleport = destinationT;
        Animation defaultAnimation = new Animation("sprites/lift.png", 48, 48);
        setAnimation(defaultAnimation);
        canTeleport = true;
    }
    public Teleport getDestination() {
        if (connectedTeleport != null) {
            return connectedTeleport;
        }
        return null;
    }
    public void setDestination(Teleport destinationTeleport) {
        if  (destinationTeleport == null || destinationTeleport.equals(this)) {
            return;
        }
        this.connectedTeleport = destinationTeleport;
    }
    private boolean isPlayerInside(Player player) {
        int pX = player.getPosX();
        int pY = player.getPosY();

        int pCenterX = pX + player.getWidth() / 2;
        int pCenterY = pY + player.getHeight() / 2;

        Rectangle teleportHitbox = new Rectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight());

        if (teleportHitbox.contains(pCenterX, pCenterY)) {
            return true;
        }
        else {
            canTeleport = true;
            return false;
        }
    }
    public void teleportPlayer(Player player) {
        int dtX = this.getPosX();
        int dtY = this.getPosY();
        // 48 x 48
        int centerX = dtX + 24;
        int centerY = dtY + 24;

        int x = centerX - player.getWidth() / 2;
        int y = centerY - player.getHeight() / 2;

        player.setPosition(x, y);
        canTeleport = false;
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        Player player = scene.getFirstActorByType(Player.class);
        if (player == null) {
            return;
        }
        new ActionSequence<>(
            new Loop<>(
                new Invoke<>(() -> {
                    if (connectedTeleport != null && isPlayerInside(player) && canTeleport) {
                        connectedTeleport.teleportPlayer(player);
                    }
                })
            )
        ).scheduleFor(this);
    }
}

