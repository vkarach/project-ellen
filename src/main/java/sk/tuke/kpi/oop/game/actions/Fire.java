package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.utils.SoundUtil;
import sk.tuke.kpi.oop.game.weapons.Fireable;

public class Fire<A extends Armed> extends AbstractAction<A> {
    private final SoundUtil emptyGunShopSound = new SoundUtil("sounds/empty_gun_shop.wav");
    private boolean isDone = false;
    @Override
    public boolean isDone() {
        return isDone;
    }
    public void execute(float deltaTime) {
        if (isDone) {
            return;
        }
        Armed armedActor = getActor();
        if (armedActor == null) {
            isDone = true;
            return;
        }
        Fireable bullet = armedActor.getFirearm().fire();
        if (bullet == null) {
            emptyGunShopSound.play();
            isDone = true;
            return;
        }
        Scene scene = armedActor.getScene();
        if (scene == null) {
            isDone = true;
            return;
        }

        int offset = 10;
        Direction direction = Direction.fromAngle(armedActor.getAnimation().getRotation());
        int bulletX = armedActor.getPosX() + ((armedActor.getWidth() / 2) - 10) + direction.getDx() * offset;
        int bulletY = armedActor.getPosY() + direction.getDy() * offset;
        scene.addActor(bullet, bulletX, bulletY);

        float moveTime = 0.5f;

        new ActionSequence<>(
            new Move<>(direction, moveTime),
            new Invoke<>(()->scene.removeActor(bullet))
        ).scheduleFor(bullet);

        isDone = true;
    }
}
