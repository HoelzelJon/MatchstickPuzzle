package jonathan.hoelzel.matchsticks.Util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid<T> implements Iterable<Vector> {
    private final List<List<T>> grid;

    public Grid(T[][] arr) {
        this(Arrays.stream(arr).map(Arrays::asList).collect(Collectors.toList()));
    }

    public Grid(List<List<T>> grid) {
        List<Integer> sizes = grid.stream().map(List::size).collect(Collectors.toList());
        int mySize = sizes.get(0);
        for (int i : sizes) {
            assert i == mySize;
        }

        this.grid = grid;
    }

    public static <T, U> Grid<T> transform(Grid<U> other, Function<U, T> aTransformation) {
        return new Grid<>(Util.transform(other.grid, aTransformation));
    }

    public T get(Vector pos) {
        return get(pos.getX(), pos.getY());
    }

    public T get(int x, int y) {
        assert inBounds(x, y);
        return grid.get(y).get(x);
    }

    public void put(Vector v, T t) {
        put(v.getX(), v.getY(), t);
    }

    public void put(int x, int y, T t) {
        assert inBounds(x, y);
        grid.get(y).set(x, t);
    }

    public int width() {
        return grid.get(0).size();
    }

    public int height() {
        return grid.size();
    }

    public boolean containsAny(Predicate<T> filter) {
        return grid.stream().anyMatch(aList -> aList.stream().anyMatch(filter));
    }

    public boolean inBounds(Vector pos) {
        return inBounds(pos.getX(), pos.getY());
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }

    public Collection<Vector> getNeighborVectors(Vector pos) {
        return streamNeighbors(pos).collect(Collectors.toList());
    }

    // includes orthogonal but not diagonal neighbors
    public Collection<T> getNeighborValues(Vector pos) {
        return streamNeighbors(pos)
                .map(this::get)
                .collect(Collectors.toList());
    }

    private Stream<Vector> streamNeighbors(Vector pos) {
        return pos.getNeighbors().stream().filter(this::inBounds);
    }

    @Override
    public Iterator<Vector> iterator() {
        return new VectorRectangleIterator(width(), height());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < height(); y ++) {
            for (int x = 0; x < width(); x ++) {
                builder.append(get(x, y).toString() + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid<?> grid1 = (Grid<?>) o;
        return Objects.equals(grid, grid1.grid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grid);
    }
}
