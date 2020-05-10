package jonathan.hoelzel.matchsticks;

import jonathan.hoelzel.matchsticks.generator.GeneratedPuzzle;
import jonathan.hoelzel.matchsticks.generator.Generator;
import jonathan.hoelzel.matchsticks.solver.BruteForceSolver;
import jonathan.hoelzel.matchsticks.solver.Solution;
import jonathan.hoelzel.matchsticks.solver.Solver;


public class SeedSolverMain {
    public static void main(String[] args) {
        long seed = 4523392140995079168L;
        int width = 6;
        int height = 6;
        Generator generator = new Generator(new Solver(), false);
        GeneratedPuzzle puzzle = generator.getPuzzle(width, height, seed);

        Solver solver = new Solver();
        Solution solution = solver.solve(puzzle.getPuzzle()).get();
        System.out.println(solution + "\n\n");
        System.out.println(new BruteForceSolver(solver).finishSolving(solution).get());
    }
}
