package jonathan.hoelzel.matchsticks.Util;

import java.util.Iterator;

public class OrderedRange implements Iterable<Integer> {
    private final int min; // inclusive
    private final int max; // exclusive
    private final Order order;

    public OrderedRange(int min, int max, Order order) {
        assert min < max;

        this.min = min;
        this.max = max;
        this.order = order;
    }

    @Override
    public Iterator<Integer> iterator() {
        if (order == Order.LOW_TO_HIGH) {
            return new LowToHighIterator();
        } else if (order == Order.HIGH_TO_LOW) {
            return new HighToLowIterator();
        } else {
            throw new RuntimeException("Unknown Order");
        }
    }

    public enum Order {
        LOW_TO_HIGH, HIGH_TO_LOW
    }

    private class LowToHighIterator implements Iterator<Integer> {
        private int current;

        private LowToHighIterator() {
            current = min;
        }

        @Override
        public boolean hasNext() {
            return current < max;
        }

        @Override
        public Integer next() {
            return current ++;
        }
    }

    private class HighToLowIterator implements Iterator<Integer> {
        private int current;

        private HighToLowIterator() {
            current = max - 1;
        }

        @Override
        public boolean hasNext() {
            return current >= min;
        }

        @Override
        public Integer next() {
            return current --;
        }
    }
}
