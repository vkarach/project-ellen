package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.utils.SoundUtil;


public class Energy extends AbstractActor implements Usable<Alive> {
    private final SoundUtil useSound = new SoundUtil("sounds/Interact_UseHealth.wav");
    private boolean isUsed = false;
    public Energy() {
        Animation defaultAnimation = new Animation("sprites/energy.png");
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(Alive alive) {
        if (alive == null || getScene() == null || alive.getHealth().getValue() == 100 || isUsed) {
            return;
        }
        useSound.play(0.4f);
        alive.getHealth().restore();

        getScene().removeActor(this);
        isUsed = true;
    }
    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}
