package jonathan.hoelzel.matchsticks;

import jonathan.hoelzel.matchsticks.Util.Vector;

// (0,0) = top-left
// right = increasing x-values
// down = increasing y-values
public enum Direction {
    U(0, -1, "^"),
    D(0, 1, "v"),
    L(-1, 0, "<"),
    R(1, 0, ">"),
    NOT_SET(Integer.MAX_VALUE, Integer.MAX_VALUE, "X");

    private final Vector movementVector;
    private final String str;

    Direction(int dX, int dY, String str) {
        movementVector = new Vector(dX, dY);
        this.str = str;
    }

    public Vector vector() {
        return movementVector;
    }

    @Override
    public String toString() {
        return str;
    }

    public static Direction getDirectionFor(Vector v) {
        for (Direction dir : Direction.values()) {
            if (dir.vector().equals(v)) {
                return dir;
            }
        }
        throw new RuntimeException("Invalid vector passed to getDirectionFor");
    }
}
