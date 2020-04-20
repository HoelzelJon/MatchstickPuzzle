package jonathan.hoelzel.matchsticks;

import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.generator.Puzzle;
import jonathan.hoelzel.matchsticks.solver.Solver;

import java.util.Arrays;
import java.util.List;

import static jonathan.hoelzel.matchsticks.Direction.*;

public class ManualSolverMain {
    public static void main(String[] args) {
        Grid<Direction> directions = new Grid<>(new Direction[][]{
                {L, L, R, R, R, R},
                {U, D, L, L, L, L},
                {U, D, L, L, L, U},
                {U, D, D, U, D, U},
                {U, D, D, U, D, U},
                {U, L, L, L, D, U}
        });
        List<Integer> burntPerRow = Arrays.asList(4, 3, 4, 3, 3, 2);
        List<Integer> burntPerColumn = Arrays.asList(4, 3, 4, 2, 3, 3);

        System.out.println(new Solver().solve(new Puzzle(directions, burntPerRow, burntPerColumn)).first());
    }

}