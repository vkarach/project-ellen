package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Fireable;

public class Fire extends AbstractAction<Armed> {
    @Override
    public void execute(float deltaTime) {
        Armed armedActor = getActor();
        if (armedActor == null) {
            return;
        }
        Fireable bullet = armedActor.getFirearm().fire();
        if (bullet == null) {
            return;
        }
        Scene scene = armedActor.getScene();
        if (scene == null) return;


        int offset = 10;
        Direction direction = Direction.fromAngle(armedActor.getAnimation().getRotation());
        int bulletX = armedActor.getPosX() + ((armedActor.getWidth() / 2) - 10) + direction.getDx() * offset;
        int bulletY = armedActor.getPosY() + direction.getDy() * offset;
        scene.addActor((Actor) bullet, bulletX, bulletY);

        float moveTime = 0.5f;

        new ActionSequence<>(
            new Move<>(direction, moveTime),
            new Invoke<>(()->scene.removeActor((Actor) bullet))
        ).scheduleFor((Movable) bullet);

        setDone(true);
    }
}
