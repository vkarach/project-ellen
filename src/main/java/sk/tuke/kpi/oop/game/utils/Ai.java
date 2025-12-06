package sk.tuke.kpi.oop.game.utils;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.*;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;
import sk.tuke.kpi.oop.game.actions.MoveToPlace;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Ai {
    static Disposable catchingAction = null;
    public static void chaseActor(Movable catching, Actor target) {
        Scene scene = catching.getScene();
        if (scene == null) {
            return;
        }
        int gridW = scene.getMap().getGridWidth();
        int gridH = scene.getMap().getGridHeight();

        int catchingX = catching.getPosX() / 16;
        int catchingY = catching.getPosY() / 16;
        int goalX =  target.getPosX() / 16;
        int goalY = target.getPosY() / 16;
        System.out.println("tilesW: " + gridW + " tilesH: " + gridH);

        Direction[] directions = {Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST};

        Boolean[][] visited = new Boolean[gridW][gridH];
        Direction[][] parentPath = new Direction[gridW][gridH];

        Boolean[][] walkable = new Boolean[gridW][gridH];
        for (int w = 0; w < gridW; w++) {
            for (int h = 0; h < gridH; h++) {
                walkable[w][h] = scene.getMap().getTile(w, h).getType() == MapTile.Type.CLEAR;
                visited[w][h] = false;
            }
        }
        Queue<Cords> queue = new ArrayDeque<>();

        visited[catchingX][catchingY] = true;
        parentPath[catchingX][catchingY] = Direction.NONE;
        queue.add(new Cords(catchingX, catchingY));

        while (!queue.isEmpty()) {
            Cords cur = queue.remove();
            int x = cur.x;
            int y = cur.y;
            if (x ==  goalX && y == goalY) {
                break;
            }
            for (int i = 0; i < 4; i++) {
                int dx = x + directions[i].getDx();
                int dy = y + directions[i].getDy();

                if (dx < 0 || dy < 0 || dx >= gridW || dy >= gridH) {
                    continue;
                    }

                if (!walkable[dx][dy]) {
                    continue;
                }
                if (visited[dx][dy]) {
                    continue;
                }
                visited[dx][dy] = true;
                parentPath[dx][dy] = Direction.fromXY(-directions[i].getDx(), -directions[i].getDy());
                queue.add(new Cords(dx, dy));
            }
        }
        List<Direction> path = new ArrayList<>();
        if (visited[goalX][goalY]) {
            List<Direction> reversedDir = new ArrayList<>();
            int cx = goalX;
            int cy = goalY;

            while (!(cx == catchingX && cy == catchingY)) {
                Direction back = parentPath[cx][cy];
                reversedDir.add(back);

                cx += back.getDx();
                cy += back.getDy();
            }
            Collections.reverse(reversedDir);
            for (Direction backD : reversedDir) {
                path.add(Direction.fromXY(-backD.getDx(), -backD.getDy()));
            }
        }
        else {
            System.out.println("path not found");
            return;
        }

        int[] idx = {0};
        int[] targetX = {0};
        int[] targetY = {0};
        catchingAction = new Loop<>(
            new ActionSequence<>(
                new Invoke<>(()-> {
                    if (idx[0] >= path.size()) return;
                    targetX[0] = catching.getPosX() + path.get(idx[0]).getDx() * 16;
                    targetY[0] = catching.getPosY() + path.get(idx[0]).getDy() * 16;
                    if (idx[0] < path.size() - 1) {
                        new MoveToPlace<>(targetX[0], targetY[0], true).scheduleFor(catching);
                        idx[0]++;
                    }
                }),
                new When<>(
                    ()-> catching.getPosX() == targetX[0] && catching.getPosY() == targetY[0],
                    new Invoke<>(()->{})
                )
            )
        ).scheduleFor(catching);
    }
}
class Cords {
    int x;
    int y;
    public Cords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
