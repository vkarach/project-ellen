package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class SpawnPoint extends AbstractActor {
    private int aliens;
    public SpawnPoint(int aliens) {
        this.aliens = aliens;
        Animation defaultDefault = new Animation("sprites/spawn.png");
        setAnimation(defaultDefault);

    }
    private boolean canSpawnALien() {
        Scene scene = getScene();
        if (scene == null || aliens < 0) {
            return false;
        }
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return false;
        }
        float nestCenterX = getPosX() + getWidth() / 2f;
        float nestCenterY = getPosY() + getHeight() / 2f;
        float dx = ripley.getPosX() - nestCenterX;
        float dy = ripley.getPosY() - nestCenterY;
        float distance = (float)Math.sqrt(dx*dx + dy*dy);
        System.out.println("Distance: " + distance);
        return distance <= 50f;
    }
    private void spawnAlien() {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }
        aliens--;
        Alien alien = new Alien(new RandomlyMoving<>());
        scene.addActor(alien, getPosX(), getPosY());
    }
    private boolean spawnCooldown = false;
    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(
            new Invoke<>(() -> {
                if (canSpawnALien() && !spawnCooldown) {
                    spawnAlien();
                    spawnCooldown = true;
                    new ActionSequence<>(
                        new Wait<>(3),
                        new Invoke<>(()-> spawnCooldown = false)
                    ).scheduleFor(this);
                }
            })
        ).scheduleFor(this);
    }
}
