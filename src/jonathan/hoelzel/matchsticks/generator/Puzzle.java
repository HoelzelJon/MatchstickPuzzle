package jonathan.hoelzel.matchsticks.generator;

import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.Util.Util;
import jonathan.hoelzel.matchsticks.Direction;

import java.util.List;
import java.util.Objects;

public class Puzzle {
    private final Grid<Direction> headDirections;
    private final List<Integer> rowCounts;
    private final List<Integer> columnCounts;

    public Puzzle(Grid<Direction> headDirections, List<Integer> rowCounts, List<Integer> columnCounts) {
        assert rowCounts.size() == headDirections.height();
        assert columnCounts.size() == headDirections.width();

        this.headDirections = headDirections;
        this.rowCounts = rowCounts;
        this.columnCounts = columnCounts;
    }

    public Grid<Direction> getHeadDirections() {
        return headDirections;
    }

    public List<Integer> getBurntPerRow() {
        return rowCounts;
    }

    public List<Integer> getBurntPerColumn() {
        return columnCounts;
    }

    @Override
    public String toString() {
        return Util.gridToStringWithBorderNumbers(headDirections, rowCounts, columnCounts, 3);
    }

    public boolean hasGimmeRowOrColumn() {
        return rowCounts.stream().anyMatch(n -> n == 0 || n == headDirections.width())
                || columnCounts.stream().anyMatch(n -> n == 0 || n == headDirections.height());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Puzzle puzzle = (Puzzle) o;
        return Objects.equals(headDirections, puzzle.headDirections) &&
                Objects.equals(rowCounts, puzzle.rowCounts) &&
                Objects.equals(columnCounts, puzzle.columnCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headDirections, rowCounts, columnCounts);
    }
}
