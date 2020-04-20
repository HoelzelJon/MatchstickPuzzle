package jonathan.hoelzel.matchsticks.generator;

import java.util.Objects;

public class GeneratedPuzzle {
    private final int difficulty;
    private final long randSeed;
    private final int itersToCreate;
    private final Puzzle puzzle;

    public GeneratedPuzzle(int difficulty, long randSeed, int itersToCreate, Puzzle puzzle) {
        this.difficulty = difficulty;
        this.randSeed = randSeed;
        this.itersToCreate = itersToCreate;
        this.puzzle = puzzle;
    }

    public int getDifficulty() {
       return difficulty;
    }

    public int getItersToCreate() {
        return itersToCreate;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public boolean hasGimmeRowOrColumn() {
        return puzzle.hasGimmeRowOrColumn();
    }

    @Override
    public String toString() {
        return "Seed: " + randSeed + "\nDifficulty: " + difficulty + "\n" + puzzle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedPuzzle that = (GeneratedPuzzle) o;
        return Objects.equals(puzzle, that.puzzle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(puzzle);
    }
}
