package sk.tuke.kpi.oop.game;

public enum Direction {
    NORTH(0, 1, 0),
    SOUTH(0, -1, 180),
    EAST(1, 0, 270),
    WEST(-1, 0, 90);

    private final int dx;
    private final int dy;
    private final int angle;

    Direction(int dx, int dy, int angle) {
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
    }
    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }
    public int getAngle() {
        return angle;
    }
}
