package jonathan.hoelzel.matchsticks;

import jonathan.hoelzel.matchsticks.Util.Vector;

// (0,0) = top-left
// right = increasing x-values
// down = increasing y-values
public enum Direction {
    R(1, 0, 0, ">"),
    U(0, -1, 90, "^"),
    L(-1, 0, 180, "<"),
    D(0, 1, 270, "v"),
    NOT_SET(Integer.MAX_VALUE, Integer.MAX_VALUE, Double.NaN, "X");

    private final Vector movementVector;
    private final double angleInRadians;
    private final String str;

    Direction(int dX, int dY, double angleInDegrees, String str) {
        movementVector = new Vector(dX, dY);
        this.angleInRadians = Math.toRadians(angleInDegrees);
        this.str = str;
    }

    public Vector vector() {
        return movementVector;
    }

    public double angle() {
        return angleInRadians;
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
