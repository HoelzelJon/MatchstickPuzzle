package jonathan.hoelzel.matchsticks;

import jonathan.hoelzel.matchsticks.generator.GeneratedPuzzle;
import jonathan.hoelzel.matchsticks.generator.Generator;
import jonathan.hoelzel.matchsticks.solver.Solver;

public class SeedSolverMain {
    public static void main(String[] args) {
        long seed = 6372321741696187392L;
        int width = 6;
        int height = 6;
        Generator generator = new Generator(new Solver());
        GeneratedPuzzle puzzle = generator.getPuzzle(width, height, seed);

        System.out.println(new Solver().solve(puzzle.getPuzzle()).first());
    }
}
