package jonathan.hoelzel.matchsticks.Util;

import java.util.Iterator;

public class VectorRectangleIterator implements Iterator<Vector> {
    private final int minX;
    private final int maxX;
    private final int maxY;
    private int x;
    private int y;

    public VectorRectangleIterator(int maxX, int maxY) {
        this(0, 0, maxX , maxY);
    }

    public VectorRectangleIterator(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.maxY = maxY;
        x = minX;
        y = minY;
    }

    @Override
    public boolean hasNext() {
        return y < maxY;
    }

    @Override
    public Vector next() {
        Vector v = new Vector(x, y);
        x++;
        if (x == maxX) {
            x = minX;
            y++;
        }
        return v;
    }
}
