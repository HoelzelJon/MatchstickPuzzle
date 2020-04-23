package jonathan.hoelzel.matchsticks.solver;

import jonathan.hoelzel.matchsticks.Cell;
import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.Util.Util;

import java.util.List;

import static jonathan.hoelzel.matchsticks.Util.Grid.transform;

public class Solution {
    private final Grid<Cell> board;
    private final List<Integer> burntPerRow;
    private final List<Integer> burntPerColumn;
    private final int difficulty;

    public Solution(WorkingSolution workingSolution, int difficulty) {
        this.board = transform(workingSolution.getBoard(), Cell::new);
        this.burntPerRow = workingSolution.getBurntPerRow();
        this.burntPerColumn = workingSolution.getBurntPerColumn();
        this.difficulty = difficulty;
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

    public int getDifficulty() {
        return difficulty;
    }

    public boolean hasNoUnknowns() {
        return !board.containsAny(cell -> cell.getBurntStatus() == BurntStatus.UNKNOWN);
    }

    @Override
    public String toString() {
        return Util.gridToStringWithBorderNumbers(board, burntPerRow, burntPerColumn, 4) + "\nDifficulty: " + difficulty;
    }
}
