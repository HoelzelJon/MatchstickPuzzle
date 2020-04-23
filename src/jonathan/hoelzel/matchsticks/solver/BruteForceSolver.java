package jonathan.hoelzel.matchsticks.solver;

import jonathan.hoelzel.matchsticks.Util.Pair;
import jonathan.hoelzel.matchsticks.Util.Vector;

import java.util.*;

public class BruteForceSolver {
    private static final int BRUTE_FORCE_DIFFICULTY = 1000;

    private final Solver baseSolver;

    public BruteForceSolver(Solver baseSolver) {
        this.baseSolver = baseSolver;
    }

    public Optional<Solution> finishSolving(Solution inputSolution) {
        if (inputSolution.hasNoUnknowns()) {
            return Optional.of(inputSolution);
        }

        List<Solution> allSolutions = bruteForce(new WorkingSolution(inputSolution));

        return allSolutions.size() == 1
                ? Optional.of(allSolutions.get(0))
                : Optional.empty();
    }

    private List<Solution> bruteForce(WorkingSolution workingSolution) {
        Vector unknownPos = workingSolution.getBoard()
                .findAny(cell -> cell.getBurntStatus() == BurntStatus.UNKNOWN)
                .get();

        workingSolution.setBurntStatus(unknownPos, BurntStatus.BURNT);
        List<Solution> allSolutions = new ArrayList<>(getAllSolutionsFor(workingSolution));

        workingSolution.setBurntStatus(unknownPos, BurntStatus.UNBURNT);
        allSolutions.addAll(getAllSolutionsFor(workingSolution));

        workingSolution.setBurntStatus(unknownPos, BurntStatus.UNKNOWN);
        return allSolutions;
    }

    private List<Solution> getAllSolutionsFor(WorkingSolution workingSolution) {
        WorkingSolution clone = new WorkingSolution(workingSolution);
        Pair<Solver.SolutionStatus, Integer> status = baseSolver.trySolving(clone);

        if (status.first() == Solver.SolutionStatus.IMPOSSIBLE_TO_SOLVE) {
            return Collections.emptyList();
        } else if (clone.hasNoUnknowns()) {
            return List.of(new Solution(clone, status.second() + BRUTE_FORCE_DIFFICULTY));
        } else {
            return bruteForce(workingSolution);
        }
    }
}
