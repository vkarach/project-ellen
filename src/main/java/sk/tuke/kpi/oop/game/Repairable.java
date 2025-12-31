package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;

public interface Repairable extends Actor {
    public enum RepairKind {
        HAMMER,
        WRENCH
    }
    boolean repair(RepairKind kind);
}
