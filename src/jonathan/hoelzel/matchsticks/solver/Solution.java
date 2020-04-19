package jonathan.hoelzel.matchsticks.solver;

import jonathan.hoelzel.matchsticks.Cell;
import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.Util.Util;

import java.util.List;

public class Solution {
    private final Grid<Cell> board;
    private final List<Integer> burntPerRow;
    private final List<Integer> burntPerColumn;

    public Solution(WorkingSolution workingSolution) {
        this.board = workingSolution.getBoard();
        this.burntPerRow = workingSolution.getBurntPerRow();
        this.burntPerColumn = workingSolution.getBurntPerColumn();
    }

    public boolean hasNoUnknowns() {
        return !board.containsAny(cell -> cell.getBurntStatus() == BurntStatus.UNKNOWN);
    }

    @Override
    public String toString() {
        return Util.gridToStringWithBorderNumbers(board, burntPerRow, burntPerColumn, 4);
    }
}
