package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
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

        scene.addActor((Actor) bullet, armedActor.getPosX() + armedActor.getWidth() / 2, armedActor.getPosY() + armedActor.getWidth() / 2);

        Direction direction = Direction.fromAngle(armedActor.getAnimation().getRotation());

        new Loop<>(new Move<>(direction, 1)).scheduleFor((Movable) bullet);

        setDone(true);
    }
}
