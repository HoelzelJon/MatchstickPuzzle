package jonathan.hoelzel.matchsticks.generator;

import jonathan.hoelzel.matchsticks.Cell;
import jonathan.hoelzel.matchsticks.Direction;
import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.solver.BruteForceSolver;
import jonathan.hoelzel.matchsticks.solver.BurntStatus;
import jonathan.hoelzel.matchsticks.solver.Solution;
import jonathan.hoelzel.matchsticks.solver.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Generator {
    private final Random random;
    private final GridGenerator gridGenerator;
    private final Solver solver;
    private final BruteForceSolver bruteForcer;
    private final boolean shouldTryBruteForcing;

    public Generator(Solver solver, boolean shouldTryBruteForcing) {
        random = new Random();
        gridGenerator = new GridGenerator(random);
        this.solver = solver;
        bruteForcer = new BruteForceSolver(solver);
        this.shouldTryBruteForcing = shouldTryBruteForcing;
    }

    public GeneratedPuzzle getPuzzle(int width, int height, long seed) {
        random.setSeed(seed);

        int iters = 0;
        while(true) {
            Optional<Grid<Cell>> optionalGrid = gridGenerator.createGrid(width, height);
            if (optionalGrid.isEmpty()) {
                iters ++;
                continue;
            }
            Grid<Cell> cellGrid = optionalGrid.get();

            List<Integer> burntPerRow = getBurntPerRow(cellGrid);
            List<Integer> burntPerColumn = getBurntPerColumn(cellGrid);

            Grid<Direction> headDirections = Grid.transform(cellGrid, Cell::getHeadDirection);
            Puzzle puzzle = new Puzzle(headDirections, burntPerRow, burntPerColumn);

            Optional<Integer> difficulty = getDifficulty(puzzle);
            if (difficulty.isPresent()) {
//                System.out.println("Created puzzle after " + iters + " iterations");
                return new GeneratedPuzzle(difficulty.get(), seed, iters, puzzle);
            }
            iters ++;
        }
    }

    private List<Integer> getBurntPerRow(Grid<Cell> grid) {
        List<Integer> counts = new ArrayList<>(grid.height());
        for (int y = 0; y < grid.height(); y ++) {
            int count = 0;
            for (int x = 0; x < grid.width(); x ++) {
                if (grid.get(x, y).getBurntStatus() == BurntStatus.BURNT) {
                    count ++;
                }
            }
            counts.add(y, count);
        }
        return counts;
    }

    private List<Integer> getBurntPerColumn(Grid<Cell> grid) {
        List<Integer> counts = new ArrayList<>(grid.width());
        for (int x = 0; x < grid.width(); x ++) {
            int count = 0;
            for (int y = 0; y < grid.height(); y ++) {
                if (grid.get(x, y).getBurntStatus() == BurntStatus.BURNT) {
                    count ++;
                }
            }
            counts.add(x, count);
        }
        return counts;
    }

    private Optional<Integer> getDifficulty(Puzzle puzzle) {
        Solution solution = solver.solve(puzzle).get();

        if (solution.hasNoUnknowns()) {
            return Optional.of(solution.getDifficulty());
        } else {
            return shouldTryBruteForcing
                    ? Optional.empty()
                    : bruteForcer.finishSolving(solution).map(Solution::getDifficulty);
        }
    }
}
