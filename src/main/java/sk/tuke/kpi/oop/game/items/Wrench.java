package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.DefectiveLight;

public class Wrench extends BreakableTool<DefectiveLight> implements Collectible {
    public Wrench() {
        super(2);
        Animation defaultAnimation = new Animation("sprites/wrench.png", 16, 16);
        setAnimation(defaultAnimation);
    }
    @Override
    public void useWith(DefectiveLight defectLight) {
        if (defectLight != null && defectLight.repair()) {
            super.useWith(defectLight);
        }
    }
}
