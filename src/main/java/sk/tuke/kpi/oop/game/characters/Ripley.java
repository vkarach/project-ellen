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
import sk.tuke.kpi.oop.game.utils.SoundUtil;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gun;

public class Ripley extends AbstractActor implements Actor, Movable, Alive, Armed, Keeper {
    public static final Topic<Ripley> RIPLEY_DIED = Topic.create("ripley died", Ripley.class);
    private final SoundUtil footstepsSound = new SoundUtil("sounds/footsteps.wav");
    private int speed = 2;
    private final Animation moveAnimation;
    private final Animation dieAnimation;
    private final Backpack backpack;
    private final Health health;
    private Firearm weapon;
    private final int fontSize = 18;
    private final Font whiteFont = new Font(fontSize, Color.WHITE, Font.Style.NORMAL);
    private final Font greenFont = new Font(fontSize, Color.GREEN, Font.Style.NORMAL);
    private final Font yellowFont = new Font(fontSize, Color.YELLOW, Font.Style.NORMAL);
    private final Font redFont = new Font(fontSize, Color.RED, Font.Style.NORMAL);
    public Ripley() {
        super("Ellen");
        moveAnimation = new Animation("sprites/player.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        dieAnimation = new Animation("sprites/player_die.png", 32, 32, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(moveAnimation);
        moveAnimation.pause();
        health = new Health(100);
        this.backpack = new Backpack("Ripley's backpack", 5);
        this.weapon = new Gun(0, 500);
        health.onFatigued(() -> {
            Scene scene = getScene();
            if (scene != null) {
                scene.cancelActions(this);
                scene.getMessageBus().publish(RIPLEY_DIED, this);
            }
            setAnimation(dieAnimation);
        });
    }
    public Backpack getBackpack() {
        return backpack;
    }
    @Override
    public void startedMoving(Direction direction) {
        moveAnimation.setRotation(direction.getAngle());
        moveAnimation.play();
        footstepsSound.loop(0.6f);
    }
    @Override
    public void stoppedMoving() {
        moveAnimation.pause();
        footstepsSound.stop();
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Health getHealth() {
        return health;
    }
    public void setFirearm(Firearm weapon) {
        this.weapon = weapon;
    }
    public Firearm getFirearm() {
        return weapon;
    }
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

        int ripleyEnergy = health.getValue();
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
        scene.getGame().getOverlay().drawText("        "+ripleyEnergy, xTextPos, yTextPos, font);

        scene.getGame().getOverlay().drawText("Ammo:", xTextPos, yTextPos - 20, whiteFont);
        if (weapon.getAmmo() > 0) {
            scene.getGame().getOverlay().drawText("        "+weapon.getAmmo(), xTextPos, yTextPos - 20, whiteFont);
        }
        else {
            scene.getGame().getOverlay().drawText("        x", xTextPos, yTextPos - 20, redFont);
        }
    }
}
