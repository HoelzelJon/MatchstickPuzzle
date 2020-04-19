package jonathan.hoelzel.matchsticks.solver;

import jonathan.hoelzel.matchsticks.Util.Pair;
import jonathan.hoelzel.matchsticks.generator.Puzzle;
import jonathan.hoelzel.matchsticks.solver.view.LineView;

public class Solver {
    public Pair<Solution, Integer> solve(Puzzle puzzle) {
        WorkingSolution workingSolution = new WorkingSolution(puzzle);

        int iters = 0;
        boolean foundSomething;
        do {
//            System.out.println(workingSolution + "\n\n");
            foundSomething = false;
            for (LineView view : workingSolution.getRowAndColumnViews()) {
                if (solveLine(view)) {
                    foundSomething = true;
                    iters ++;
                }
            }
            iters ++;
        } while (foundSomething);


        return new Pair<>(new Solution(workingSolution), iters);
    }

    // returns true iff it figured something new out
    private boolean solveLine(LineView view) {
        boolean foundSomething = false;
        int remainingCellsToBurn = view.getTotalToBurn() - view.countCellsWithStatus(BurntStatus.BURNT);
        assert remainingCellsToBurn >= 0;

        int unknownCellCount = view.countCellsWithStatus(BurntStatus.UNKNOWN);
        for (LineView.Match match : view.getAllMatches()) {
            match.fixBurnOrder();

            int unknownInMatch = match.countCellsWithStatus(BurntStatus.UNKNOWN);
            int unknownOutsideMatch = unknownCellCount - unknownInMatch;

            if (unknownOutsideMatch < remainingCellsToBurn) {
                match.markBurnt(remainingCellsToBurn - unknownOutsideMatch);
                foundSomething = true;

            }

            if (unknownInMatch > remainingCellsToBurn) {
                match.markNotBurnt(unknownInMatch - remainingCellsToBurn);
                foundSomething = true;
            }
        }

        return foundSomething;
    }
}
