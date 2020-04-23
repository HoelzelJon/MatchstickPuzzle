package jonathan.hoelzel.matchsticks.solver.view;

import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.Util.OrderedRange;
import jonathan.hoelzel.matchsticks.solver.BurntStatus;
import jonathan.hoelzel.matchsticks.Cell;

import java.util.ArrayList;
import java.util.List;

import static jonathan.hoelzel.matchsticks.solver.view.ViewPerspective.COLUMN;
import static jonathan.hoelzel.matchsticks.solver.view.ViewPerspective.ROW;

@SuppressWarnings("ALL")
public class LineView {
    private final Grid<Cell> underlying;
    private final int viewIndex;
    private final ViewPerspective perspective;
    private final int totalToBurn;

    public LineView(Grid<Cell> underlying, int viewIndex, ViewPerspective perspective, int totalToBurn) {
        this.underlying = underlying;
        this.viewIndex = viewIndex;
        this.perspective = perspective;
        this.totalToBurn = totalToBurn;
    }

    public int size() {
        if (perspective == ROW) {
            return underlying.width();
        } else if (perspective == COLUMN) {
            return underlying.height();
        } else {
            throw new RuntimeException("Invalid ViewPerspective");
        }
    }

    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();

        Match latest = getMatchAt(0);
        matches.add(latest);
        while (latest.getEndIndex() < size()) {
            latest = getMatchAt(latest.getEndIndex());
            matches.add(latest);
        }

        return matches;
    }

    private Match getMatchAt(int startIdx) {
        RelativeHeadDirection dir = get(startIdx).getDir();

        if (dir == RelativeHeadDirection.ORTHOGONAL) {
            return new Match(startIdx, startIdx+1);
        }

        int currIdx = startIdx + 1;
        while (currIdx < size() && get(currIdx).getDir() == dir) {
            currIdx ++;
        }
        return new Match(startIdx, currIdx);
    }

    public RelativeCellView get(int index) {
        Cell c = getCellAt(index);
        return new RelativeCellView(perspective.transform(c.getHeadDirection()), c.getBurntStatus());
    }

    public int getTotalToBurn() {
        return totalToBurn;
    }

    public int countCellsWithStatus(BurntStatus status) {
        return countCellsWithStatus(0, size(), status);
    }

    private int countCellsWithStatus(int start, int end, BurntStatus status) {
        return (int) allCells(start, end).stream()
                .filter(cell -> cell.getStatus() == status)
                .count();
    }

    private List<RelativeCellView> allCells(int start, int end) {
        List<RelativeCellView> cells = new ArrayList<>();
        for (int i = start; i <end; i ++) {
            cells.add(get(i));
        }
        return cells;
    }

    private boolean markFirstUnknown(OrderedRange range, BurntStatus status, int count) {
        int remaining = count;
        for (int i : range) {
            if (remaining > 0 && get(i).getStatus() == BurntStatus.UNKNOWN) {
                remaining --;
                setStatus(i, status);
            }
        }
        return remaining == 0;
    }

    private void setStatus(int index, BurntStatus status) {
        putCellAt(index, getCellAt(index).withStatus(status));
    }

    private Cell getCellAt(int index) {
        if (perspective == ROW) {
            return underlying.get(index, viewIndex);
        } else if (perspective == COLUMN) {
            return underlying.get(viewIndex, index);
        } else {
            throw new RuntimeException("Invalid ViewPerspective");
        }
    }

    private void putCellAt(int index, Cell cell) {
        if (perspective == ROW) {
            underlying.put(index, viewIndex, cell);
        } else if (perspective == COLUMN) {
            underlying.put(viewIndex, index, cell);
        } else {
            throw new RuntimeException("Invalid ViewPerspective");
        }
    }

    public class Match {
        private final int startIndex; // inclusive
        private final int endIndex; // exclusive

        private Match(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        private int getEndIndex() {
            return endIndex;
        }
        
        public int countCellsWithStatus(BurntStatus status) {
            return LineView.this.countCellsWithStatus(startIndex, endIndex, status);
        }

        public void fixBurnOrder() {
            boolean foundUnburnt = false;
            for (int i : getHeadToTailIndices()) {
                if (foundUnburnt && get(i).getStatus() == BurntStatus.UNKNOWN) {
                    setStatus(i, BurntStatus.UNBURNT);
                } else if (get(i).getStatus() == BurntStatus.UNBURNT) {
                    foundUnburnt = true;
                }
            }

            boolean foundBurnt = false;
            for (int i : getTailToHeadIndices()) {
                if (foundBurnt && get(i).getStatus() == BurntStatus.UNKNOWN) {
                    setStatus(i, BurntStatus.BURNT);
                } else if (get(i).getStatus() == BurntStatus.BURNT) {
                    foundBurnt = true;
                }
            }
        }

        // return false iff failed
        public boolean markBurnt(int count) {
            return markFirstUnknown(getHeadToTailIndices(), BurntStatus.BURNT, count);
        }

        // return false iff failed
        public boolean markNotBurnt(int count) {
            return markFirstUnknown(getTailToHeadIndices(), BurntStatus.UNBURNT, count);
        }

        private OrderedRange getHeadToTailIndices() {
            if (get(startIndex).getDir() == RelativeHeadDirection.FRONT) {
                return new OrderedRange(startIndex, endIndex, OrderedRange.Order.LOW_TO_HIGH);
            } else {
                return new OrderedRange(startIndex, endIndex, OrderedRange.Order.HIGH_TO_LOW);
            }
        }

        private OrderedRange getTailToHeadIndices() {
            if (get(startIndex).getDir() == RelativeHeadDirection.FRONT) {
                return new OrderedRange(startIndex, endIndex, OrderedRange.Order.HIGH_TO_LOW);
            } else {
                return new OrderedRange(startIndex, endIndex, OrderedRange.Order.LOW_TO_HIGH);
            }
        }
    }
}
