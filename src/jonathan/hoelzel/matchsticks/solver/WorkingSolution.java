package jonathan.hoelzel.matchsticks.solver;

import jonathan.hoelzel.matchsticks.Cell;
import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.Util.Util;
import jonathan.hoelzel.matchsticks.Util.Vector;
import jonathan.hoelzel.matchsticks.generator.Puzzle;
import jonathan.hoelzel.matchsticks.solver.view.LineView;
import jonathan.hoelzel.matchsticks.solver.view.ViewPerspective;

import java.util.ArrayList;
import java.util.List;

public class WorkingSolution {
    private final Grid<Cell> board;
    private final List<Integer> burntPerRow;
    private final List<Integer> burntPerColumn;

    public WorkingSolution(Puzzle puzzle) {
        this.board = Grid.transform(puzzle.getHeadDirections(), Cell::new);
        this.burntPerRow = puzzle.getBurntPerRow();
        this.burntPerColumn = puzzle.getBurntPerColumn();
    }

    public WorkingSolution(Solution solution) {
        this.board = Grid.transform(solution.getBoard(), Cell::new);
        this.burntPerRow = solution.getBurntPerRow();
        this.burntPerColumn = solution.getBurntPerColumn();
    }

    public WorkingSolution(WorkingSolution other) {
        this.board = Grid.transform(other.board, Cell::new);
        this.burntPerRow = other.burntPerRow;
        this.burntPerColumn = other.burntPerColumn;
    }

    public List<LineView> getRowAndColumnViews() {
        List<LineView> views = getRowViews();
        views.addAll(getColumnViews());
        return views;
    }

    private List<LineView> getRowViews() {
        List<LineView> views = new ArrayList<>();
        for (int y = 0; y < board.height(); y ++) {
            views.add(new LineView(board, y, ViewPerspective.ROW, burntPerRow.get(y)));
        }
        return views;
    }

    private List<LineView> getColumnViews() {
        List<LineView> views = new ArrayList<>();
        for (int x = 0; x < board.width(); x ++) {
            views.add(new LineView(board, x, ViewPerspective.COLUMN, burntPerColumn.get(x)));
        }
        return views;
    }

    public boolean hasNoUnknowns() {
        return !board.containsAny(cell -> cell.getBurntStatus() == BurntStatus.UNKNOWN);
    }

    public Grid<Cell> getBoard() {
        return board;
    }

    public List<Integer> getBurntPerRow() {
        return burntPerRow;
    }

    public List<Integer> getBurntPerColumn() {
        return burntPerColumn;
    }

    public void setBurntStatus(Vector pos, BurntStatus status) {
        board.put(pos, board.get(pos).withStatus(status));
    }

    @Override
    public String toString() {
        return Util.gridToStringWithBorderNumbers(board, burntPerRow, burntPerColumn, 4);
    }
}
