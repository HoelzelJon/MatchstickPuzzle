package jonathan.hoelzel.matchsticks;

import jonathan.hoelzel.matchsticks.generator.GeneratedPuzzle;
import jonathan.hoelzel.matchsticks.generator.Generator;
import jonathan.hoelzel.matchsticks.solver.Solver;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class GeneratorMain {
    private static final int PUZZLES_TO_GENERATE = 10000;
    private static final int PUZZLES_BETWEEN_LOG = 500;
    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;

    private static final Map<DifficultyRange, Integer> PUZZLES_PER_DIFFICULTY = Map.of(
            new DifficultyRange("Medium", 20, 25), 27,
            new DifficultyRange("Hard", 26, 99), 27,
            new DifficultyRange("Extreme", 100, Integer.MAX_VALUE), 1000);

    public static void main(String[] args) {
        Solver solver = new Solver();
        Generator generator = new Generator(solver);
        List<GeneratedPuzzle> generatedPuzzles = new ArrayList<>(PUZZLES_TO_GENERATE);

        for (int i = 0; i < PUZZLES_TO_GENERATE; i ++) {
            if (i > 0 && i % PUZZLES_BETWEEN_LOG == 0) {
                System.out.println("" + i + " puzzles generated");
            }

            long seed = (long)(Math.random() * Long.MAX_VALUE);
            generatedPuzzles.add(generator.getPuzzle(WIDTH, HEIGHT, seed));
        }

        System.out.println("Average attempts required to generate each puzzle: " +
                generatedPuzzles.stream()
                        .mapToInt(GeneratedPuzzle::getItersToCreate)
                        .average().getAsDouble());

        Map<DifficultyRange, List<GeneratedPuzzle>> puzzlesToKeep = PUZZLES_PER_DIFFICULTY.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey,
                        entry -> generatedPuzzles.stream()
                                .filter(puzzle -> entry.getKey().inRange(puzzle.getDifficulty()))
                                .filter(puzzle -> !puzzle.hasGimmeRowOrColumn())
                                .sorted(Comparator.comparing(GeneratedPuzzle::getDifficulty).reversed())
                                .limit(entry.getValue())
                                .collect(Collectors.toList())));

        for (Entry<DifficultyRange, List<GeneratedPuzzle>> entry : puzzlesToKeep.entrySet()) {
            System.out.println("----------Difficulty: "
                    + entry.getKey().getName() + "----------\n["
                    + entry.getValue().size()
                    + " puzzles]\n");
            entry.getValue().sort(Comparator.comparing(GeneratedPuzzle::getDifficulty).reversed());
            for (GeneratedPuzzle puzzle : entry.getValue()) {
                System.out.println(puzzle + "\n\n");
            }
        }
    }

    private static class DifficultyRange {
        private final String name;
        private final int minDifficulty;
        private final int maxDifficulty;

        private DifficultyRange(String name, int minDifficulty, int maxDifficulty) {
            this.name = name;
            this.minDifficulty = minDifficulty;
            this.maxDifficulty = maxDifficulty;
        }

        private String getName() {
            return name;
        }

        private boolean inRange(int difficulty) {
            return difficulty >= minDifficulty && difficulty <= maxDifficulty;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DifficultyRange that = (DifficultyRange) o;
            return minDifficulty == that.minDifficulty &&
                    maxDifficulty == that.maxDifficulty &&
                    Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, minDifficulty, maxDifficulty);
        }
    }
}
