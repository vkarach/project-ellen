package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;

public interface Collectible extends Actor {
    default void taking(){}
    default void dropping(){}

}
