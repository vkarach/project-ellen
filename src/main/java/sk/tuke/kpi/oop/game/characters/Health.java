package sk.tuke.kpi.oop.game.characters;
import java.util.ArrayList;
import java.util.List;

public class Health {
    private boolean fatigueTriggered = false;
    private final List<FatigueEffect> fatigueEffects = new ArrayList<>();
    private int value;
    private final int maxHealth;
    public Health(int startHealth, int maxHealth) {
        this.value = startHealth;
        this.maxHealth = maxHealth;
    }
    @FunctionalInterface
    public interface FatigueEffect {
        void apply();
    }
    public void onFatigued(FatigueEffect effect) {
        if (effect == null) {
            return;
        }
        fatigueEffects.add(effect);
    }
    private void triggerFatigueOnce() {
        if (fatigueTriggered) {
            return;
        }
        fatigueTriggered = true;
        for (FatigueEffect effect : fatigueEffects) {
            effect.apply();
        }
    }
    public Health(int health) {
        this.value = health;
        this.maxHealth = health;
    }
    public int getValue() {
        return this.value;
    }
    public void refill(int amount) {
        if (amount < 0) {
            return;
        }
        value += amount;
        if (value > maxHealth) {
            value = maxHealth;
        }
    }
    public void restore() {
        value = maxHealth;
    }
    public void drain(int amount) {
        if (value == 0 ||  amount <= 0) {
            return;
        }
        value -= amount;
        if (value <= 0) {
            value = 0;
            triggerFatigueOnce();
        }
    }
    public void exhaust() {
        value = 0;
        triggerFatigueOnce();
    }
}
