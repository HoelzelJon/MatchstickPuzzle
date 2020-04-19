package jonathan.hoelzel.matchsticks.Util;

import jonathan.hoelzel.matchsticks.Direction;

import java.util.Iterator;

public class DirectionalVectorIterator implements Iterator<Vector> {
    private final Iterator<Integer> countIterator;
    private final Vector base;
    private final Direction offset;

    public DirectionalVectorIterator(Iterable<Integer> counts, Vector base, Direction offset) {
        countIterator = counts.iterator();
        this.base = base;
        this.offset = offset;
    }

    @Override
    public boolean hasNext() {
        return countIterator.hasNext();
    }

    @Override
    public Vector next() {
        int count = countIterator.next();
        Vector ret = base;
        for (int i = 0; i < count; i ++) {
            ret = ret.plus(offset.vector());
        }
        return ret;
    }
}
