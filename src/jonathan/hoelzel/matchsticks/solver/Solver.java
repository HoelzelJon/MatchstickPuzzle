package jonathan.hoelzel.matchsticks.solver;

import jonathan.hoelzel.matchsticks.Util.Pair;
import jonathan.hoelzel.matchsticks.generator.Puzzle;
import jonathan.hoelzel.matchsticks.solver.view.LineView;

import java.util.Optional;

public class Solver {
    public Optional<Solution> solve(Puzzle puzzle) {
        WorkingSolution workingSolution = new WorkingSolution(puzzle);
        Pair<SolutionStatus, Integer> status = trySolving(workingSolution);
        return status.first() == SolutionStatus.IMPOSSIBLE_TO_SOLVE
                ? Optional.empty()
                : Optional.of(new Solution(workingSolution, status.second()));
    }

    // returns (status, difficulty)
    public Pair<SolutionStatus, Integer> trySolving(WorkingSolution workingSolution) {
        int iters = 0;
        boolean foundSomething;
        do {
//            System.out.println(workingSolution + "\n\n");
            foundSomething = false;
            for (LineView view : workingSolution.getRowAndColumnViews()) {
                SolutionStatus solutionStatus = solveLine(view);

                if (solutionStatus == SolutionStatus.PROGRESS) {
                    foundSomething = true;
                    iters ++;
                } else if (solutionStatus == SolutionStatus.IMPOSSIBLE_TO_SOLVE) {
                    return new Pair<>(SolutionStatus.IMPOSSIBLE_TO_SOLVE, -1);
                }
            }
            iters ++;
        } while (foundSomething);
        SolutionStatus status = foundSomething ? SolutionStatus.PROGRESS : SolutionStatus.NO_PROGRESS;
        return new Pair<>(status, iters);
    }

    // returns true iff it figured something new out
    private SolutionStatus solveLine(LineView view) {
        boolean foundSomething = false;
        int remainingCellsToBurn = view.getTotalToBurn() - view.countCellsWithStatus(BurntStatus.BURNT);
        if (remainingCellsToBurn < 0) {
            return SolutionStatus.IMPOSSIBLE_TO_SOLVE;
        }

        int unknownCellCount = view.countCellsWithStatus(BurntStatus.UNKNOWN);
        for (LineView.Match match : view.getAllMatches()) {
            match.fixBurnOrder();

            int unknownInMatch = match.countCellsWithStatus(BurntStatus.UNKNOWN);
            int unknownOutsideMatch = unknownCellCount - unknownInMatch;

            if (unknownOutsideMatch < remainingCellsToBurn) {
                if (!match.markBurnt(remainingCellsToBurn - unknownOutsideMatch)) {
                    return SolutionStatus.IMPOSSIBLE_TO_SOLVE;
                }
                foundSomething = true;

            }

            if (unknownInMatch > remainingCellsToBurn) {
                if (!match.markNotBurnt(unknownInMatch - remainingCellsToBurn)) {
                    return SolutionStatus.IMPOSSIBLE_TO_SOLVE;
                }
                foundSomething = true;
            }
        }

        return foundSomething ? SolutionStatus.PROGRESS : SolutionStatus.NO_PROGRESS;
    }

    public enum SolutionStatus {
        PROGRESS, NO_PROGRESS, IMPOSSIBLE_TO_SOLVE
    }
}
