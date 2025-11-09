package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ChainBomb extends TimeBomb {
    private final float detonationRadius;
    private final float detonationTime;
    public ChainBomb(float detonationTime) {
        super(detonationTime);
        this.detonationTime = detonationTime;
        detonationRadius = 50f;
    }
    private void explodeNearbyBombs() {
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            getPosX() - detonationRadius,
            getPosY() - detonationRadius,
            detonationRadius * 2,
            detonationRadius * 2
        );
        if (getScene() == null) {
            return;
        }
        for (Actor actor : getScene().getActors()) {
            if (actor == this || !(actor instanceof TimeBomb)) {
                continue;
            }
            TimeBomb bomb = (TimeBomb) actor;

            float bW = bomb.getWidth();
            if (bW <= 0) {
                bW = 1;
            }
            float bH = bomb.getHeight();
            if (bH <= 0) {
                bH = 1;
            }

            float bx = bomb.getPosX() - bW / 2;
            float by = bomb.getPosY() - bH / 2;

            Rectangle2D.Float rectangle = new Rectangle2D.Float(bx, by, bW, bH);

            if (explosionArea.intersects(rectangle) && !bomb.isActivated()) {
                bomb.activate();
            }
        }
    }
    @Override
    public void activate() {
        super.activate();
        new ActionSequence<>(
            new Wait<>(detonationTime),
            new Invoke<>(this::explodeNearbyBombs)
        ).scheduleFor(this);
    }
}
