package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.DefectiveLight;

public class Wrench extends BreakableTool<DefectiveLight>{
    public Wrench() {
        super(1);
        Animation DefaultAnimation = new Animation("sprites/wrench.png", 16, 16);
        setAnimation(DefaultAnimation);
    }
    @Override
    public void useWith(DefectiveLight defectLight) {
        if (defectLight != null && defectLight.repair()) {
            super.useWith(defectLight);
        }
    }
}
