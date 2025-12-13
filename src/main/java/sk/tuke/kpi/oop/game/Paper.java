package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.utils.MathUtils;

public class Paper extends AbstractActor implements Usable<Ripley>, Collectible {
    private boolean drawPaper = false;
    private boolean inBackpack = false;

    private Disposable drawDisposable;
    public Paper() {
        Animation defaultAnimation = new Animation("sprites/paper.png");
        setAnimation(defaultAnimation);
        defaultAnimation.setScale(0.5f);
    }
    public void taking() {
        inBackpack = true;
        getAnimation().setScale(1f);
        getAnimation().setRotation(0);
    }
    public void dropping() {
        inBackpack = false;
        getAnimation().setScale(0.5f);
    }
    @Override
    public void useWith(Ripley ripley) {
        if (ripley == null || getScene() == null) {
            return;
        }
        drawPaper = !drawPaper;

        if  (!drawPaper) {
            drawDisposable.dispose();
            return;
        }

        final Animation paper = new Animation("sprites/paper_clear.png");
        final Font font = new Font(12, Color.BLACK, Font.Style.NORMAL, 0.25f);
        int pH = 240;
        int pad = 18;

        final String text =
                "Its over..\n" +
                "I was hit\n" +
                "Aliens..\n" +
                "They are everywhere\n" +
                "Anyone who find me\n" +
                "There is one poss-\n" +
                "ible way to stop this\n" +
                "You must destroy\n" +
                "reactor.\n" +
                "Remember to print\n" +
                "command\n" +
                "kill 137!";

        drawDisposable = new Loop<>(
            new Invoke<>(() -> {
                if (!inBackpack && MathUtils.distanceBetween(this, ripley) > 32) {
                    drawPaper = false;
                    drawDisposable.dispose();
                }
                int x = ripley.getPosX() + 20;
                int y = ripley.getPosY() + 20;

                getScene().getOverlay().drawAnimation(paper, x, y, 10);
                String[] lines = text.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    getScene().getOverlay().drawText(
                        lines[i],
                        x + 14,
                        y - 15 + pH - i * pad,
                        font);
                }
            })
        ).scheduleFor(ripley);
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
