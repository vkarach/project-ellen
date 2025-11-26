package sk.tuke.kpi.oop.game;

public enum Direction {
    NONE(0, 0, 0),

    NORTH(0, 1, 0),
    EAST(1, 0, 270),
    SOUTH(0, -1, 180),
    WEST(-1, 0, 90),

    NORTHEAST(1, 1, 315),
    EASTSOUTH(1, -1, 225),
    SOUTHWEST(-1, -1, 135),
    WESTNORTH(-1, 1, 45);

    private final int dx;
    private final int dy;
    private final int angle;

    Direction(int dx, int dy, int angle) {
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
    }
    public Direction combine(Direction other) {
        int newDx = other.dx + this.dx;
        int newDy = other.dy + this.dy;
        for (Direction direction : Direction.values()) {
            if (direction.dx == newDx && direction.dy == newDy) {
                return direction;
            }
        }
        return NONE;
    }
    public static Direction fromAngle(float angle) {

        for (Direction direction : values()) {
            if (direction.angle == angle && !(direction.dx == 0 && direction.dy == 0)) {
                return direction;
            }
        }
        return NONE;
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
