package jonathan.hoelzel.matchsticks.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Vector {
    private final int x;
    private final int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector plus(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector minus(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public Vector times(int mult) {
        return new Vector(x * mult, y * mult);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Collection<Vector> getNeighbors() {
        return Arrays.asList(
                new Vector(x-1, y),
                new Vector(x+1, y),
                new Vector(x, y-1),
                new Vector(x, y+1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return x == vector.x &&
                y == vector.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
