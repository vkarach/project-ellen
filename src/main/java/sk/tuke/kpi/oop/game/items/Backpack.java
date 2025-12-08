package sk.tuke.kpi.oop.game.items;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.ActorContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Backpack implements ActorContainer<Collectible> {
    private final String name;
    private final int capacity;
    private int size;
    private final ArrayList<Collectible> items;
    public Backpack(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }
    @NotNull
    public Iterator<Collectible> iterator() {
        return items.iterator();
    }
    public int getCapacity() {
        return capacity;
    }
    @NotNull
    public List<Collectible> getContent() {
        return new ArrayList<>(items);
    }
    @NotNull
    public String getName() {
        return name;
    }
    public int getSize() {
        return size;
    }
    public void add(@NotNull Collectible item) {
        if (capacity - size == 0) {
            throw new IllegalStateException(name + " is full");
        }
        items.add(item);
        size++;
    }
    public void remove(@NotNull Collectible item) {
        if (!items.remove(item)) {
            throw new IllegalStateException(item + " not found in " + name);
        }
        items.remove(item);
        size--;
    }
    public Collectible peek() {
        if (size > 0) {
            return items.get(size - 1);
        }
        return null;
    }
    public void shift() {
        if (size < 2) {
            return;
        }
        Collectible topItem = items.get(size -1);
        items.remove(topItem);
        items.add(0, topItem);
    }
}
