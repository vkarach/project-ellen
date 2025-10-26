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

            float bw = bomb.getWidth();
            if (bw <= 0) {
                bw = 1;
            }
            float bh = bomb.getHeight();
            if (bh <= 0) {
                bh = 1;
            }

            float bx = bomb.getPosX() - bw / 2f;
            float by = bomb.getPosY() - bh / 2f;

            Rectangle2D.Float rectangle = new Rectangle2D.Float(bx, by, bw, bh);

            if (explosionArea.intersects(rectangle)) {
                if (!bomb.isActivated()) {
                    bomb.activate();
                }
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
