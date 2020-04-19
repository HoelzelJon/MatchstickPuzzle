package jonathan.hoelzel.matchsticks.generator;

import jonathan.hoelzel.matchsticks.Direction;
import jonathan.hoelzel.matchsticks.Util.DirectionalVectorIterator;
import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.Cell;
import jonathan.hoelzel.matchsticks.Util.OrderedRange;
import jonathan.hoelzel.matchsticks.Util.Vector;
import jonathan.hoelzel.matchsticks.solver.BurntStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static jonathan.hoelzel.matchsticks.Direction.*;

public class GridGenerator {
    private static final int MIN_MATCH_SIZE = 2;
    private static final int RECOVERABLE_ITERATIONS_UNTIL_GIVE_UP = 20;

    private final Random rand;

    public GridGenerator(Random rand) {
        this.rand = rand;
    }

    public Optional<Grid<Cell>> createGrid(int width, int height) {
        Grid<Cell> grid = createInitialGrid(width, height);

        int consecutiveRecoverableFails = 0;
        while (grid.containsAny(cell -> cell.getBurntStatus() == BurntStatus.UNKNOWN)) {
            Status status = addMatch(grid);
            if (status == Status.UNRECOVERABLE_FAIL) {
                return Optional.empty();
            } else if (status == Status.RECOVERABLE_FAIL) {
                consecutiveRecoverableFails ++;
                if (consecutiveRecoverableFails > RECOVERABLE_ITERATIONS_UNTIL_GIVE_UP) {
                    return Optional.empty();
                }
            } else if (status == Status.SUCCESS) {
                consecutiveRecoverableFails = 0;
            }
        }

        return Optional.of(grid);
    }

    private Grid<Cell> createInitialGrid(int width, int height) {
        List<List<Cell>> gridList = new ArrayList<>();
        for (int y = 0; y < height; y ++) {
            List<Cell> list = new ArrayList<>();
            for (int x = 0; x < width; x ++) {
                list.add(new Cell(NOT_SET));
            }
            gridList.add(list);
        }
        return new Grid<>(gridList);
    }

    private Status addMatch(Grid<Cell> grid) {
        Optional<Vector> optionalStartPos = findMostBlockedInPosition(grid);

        if (optionalStartPos.isEmpty()) {
            return Status.UNRECOVERABLE_FAIL;
        }

        Vector startPos = optionalStartPos.get();

        List<Vector> neighbors = getNonSetNeighbors(grid, startPos);
        Vector neighbor = neighbors.get(rand.nextInt(neighbors.size()));

        Direction growthDirection = getDirectionFor(neighbor.minus(startPos));
        Direction headDirection = randomizeDirection(growthDirection);

        int maxDistance = getMaxGrowthDistance(grid, startPos, growthDirection);
        int matchLength = maxDistance == MIN_MATCH_SIZE ?
                MIN_MATCH_SIZE
                : rand.nextInt(maxDistance - MIN_MATCH_SIZE) + MIN_MATCH_SIZE;

        if (hasInvalidNeighbors(grid, startPos, matchLength, growthDirection, headDirection)) {
            return Status.RECOVERABLE_FAIL;
        }

        int burntLength = rand.nextInt(matchLength);

        int count = 0;
        for (Vector v : getMatchHeadToTail(matchLength, growthDirection, headDirection, startPos)) {
            BurntStatus status = count < burntLength ? BurntStatus.BURNT : BurntStatus.UNBURNT;
            grid.put(v, new Cell(headDirection, status));
            count ++;
        }
        return Status.SUCCESS;
    }

    private boolean hasInvalidNeighbors(Grid<Cell> grid, Vector startPos, int matchLength, Direction growthDirection, Direction headDirection) {
        Vector beforeStart = startPos.minus(growthDirection.vector());
        if (grid.inBounds(beforeStart) && grid.get(beforeStart).getHeadDirection() == headDirection) {
            return true;
        }

        Vector afterEnd = startPos.plus(growthDirection.vector().times(matchLength + 1));
        return grid.inBounds(afterEnd) && grid.get(afterEnd).getHeadDirection() == headDirection;
    }

    private Optional<Vector> findMostBlockedInPosition(Grid<Cell> grid) {
        int minVal = 5;
        Vector minPos = null;

        for (Vector v : grid) {
            if (grid.get(v).getHeadDirection() == NOT_SET) {
                int unsetNeighbors = (int) grid.getNeighborValues(v).stream()
                        .filter(cell -> cell.getHeadDirection() == NOT_SET)
                        .count();

                if (unsetNeighbors == 0) {
                    return Optional.empty();
                }

                if (unsetNeighbors < minVal) {
                    minVal = unsetNeighbors;
                    minPos = v;
                }
            }
        }
        assert minPos != null;
        return Optional.of(minPos);
    }

    private List<Vector> getNonSetNeighbors(Grid<Cell> grid, Vector pos) {
        return grid.getNeighborVectors(pos).stream()
                .filter(v -> grid.get(v).getHeadDirection() == NOT_SET)
                .collect(Collectors.toList());
    }

    private Direction randomizeDirection(Direction dir) {
        if (dir == U || dir == D) {
            return rand.nextBoolean() ? U : D;
        } else if (dir == L || dir == R) {
            return rand.nextBoolean() ? L : R;
        } else {
            throw new RuntimeException("Invalid direction passed to randomizeDirection");
        }
    }

    private int getMaxGrowthDistance(Grid<Cell> grid, Vector startPos, Direction growthDir) {
        int size = 0;
        Vector current = startPos;
        while (grid.inBounds(current) && grid.get(current).getHeadDirection() == NOT_SET) {
            size ++;
            current = current.plus(growthDir.vector());
        }

        assert size > 1;
        return size;
    }

    private Iterable<Vector> getMatchHeadToTail(int size, Direction growthDirection, Direction headDirection, Vector start) {
        OrderedRange.Order order = growthDirection == headDirection ? OrderedRange.Order.HIGH_TO_LOW : OrderedRange.Order.LOW_TO_HIGH;
        return () -> new DirectionalVectorIterator(new OrderedRange(0, size, order), start, growthDirection);
    }

    private enum Status {
        SUCCESS, RECOVERABLE_FAIL, UNRECOVERABLE_FAIL
    }
}
