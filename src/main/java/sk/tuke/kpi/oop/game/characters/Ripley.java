package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.openables.Door;

public class Ripley extends AbstractActor implements Actor, Movable, Keeper<Collectible> {
    public static final Topic<Ripley> RIPLEY_DIED = Topic.create("ripley died", Ripley.class);
    private final int speed;
    private int energy;
    private int ammo;
    private final Animation moveAnimation;
    private final Animation dieAnimation;
    private final Backpack backpack;
    public Ripley() {
        super("Ellen");
        moveAnimation = new Animation("sprites/player.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        dieAnimation = new Animation("sprites/player_die.png", 32, 32, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(moveAnimation);
        moveAnimation.pause();
        speed = 2;
        energy = 100;
        ammo = 0;
        this.backpack = new Backpack("Ripley's backpack", 2);
    }
    public Backpack getBackpack() {
        return backpack;
    }
    @Override
    public void startedMoving(Direction direction) {
        moveAnimation.setRotation(direction.getAngle());
        moveAnimation.play();
    }
    @Override
    public void stoppedMoving() {
        moveAnimation.pause();
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        if (energy < 0 || energy > 100 || getScene() == null) {
            return;
        }
        this.energy = energy;
        if (energy == 0) {
            setAnimation(dieAnimation);
            getScene().getMessageBus().publish(RIPLEY_DIED, this);
        }
    }
    public int getAmmo() {
        return ammo;
    }
    public void setAmmo(int ammo) {
        if (ammo >= 0) {
            this.ammo = ammo;
        }
    }
    private final int fontSize = 18;
    Font whiteFont = new Font(fontSize, Color.WHITE, Font.Style.NORMAL);
    Font greenFont = new Font(fontSize, Color.GREEN, Font.Style.NORMAL);
    Font yellowFont = new Font(fontSize, Color.YELLOW, Font.Style.NORMAL);
    Font redFont = new Font(fontSize, Color.RED, Font.Style.NORMAL);
    public void showRipleyState() {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }
        scene.getGame().pushActorContainer(getBackpack());

        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;

        int windowWidth = scene.getGame().getWindowSetup().getWidth();
        int xTextPos = windowWidth - 125;

        int ripleyEnergy = getEnergy();
        Font font;
        if (ripleyEnergy == 100) {
            font = greenFont;
        }
        else if (ripleyEnergy >= 20) {
            font = yellowFont;
        }
        else {
            font = redFont;
        }
        scene.getGame().getOverlay().drawText("Energy:", xTextPos, yTextPos, whiteFont);
        scene.getGame().getOverlay().drawText("        "+getEnergy(), xTextPos, yTextPos, font);

        scene.getGame().getOverlay().drawText("Ammo:", xTextPos, yTextPos - 20, whiteFont);
        if (getAmmo() > 0) {
            scene.getGame().getOverlay().drawText("        "+getAmmo(), xTextPos, yTextPos - 20, whiteFont);

        }
        else {
            scene.getGame().getOverlay().drawText("        x", xTextPos, yTextPos - 20, redFont);
        }
    }
}
