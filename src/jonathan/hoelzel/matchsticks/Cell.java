package jonathan.hoelzel.matchsticks;

import jonathan.hoelzel.matchsticks.solver.BurntStatus;

public class Cell {
    private final Direction dir;
    private final BurntStatus status;

    public Cell(Direction dir) {
        this(dir, BurntStatus.UNKNOWN);
    }

    public Cell withStatus(BurntStatus newStatus) {
        return new Cell(dir, newStatus);
    }

    public Cell(Direction dir, BurntStatus status) {
        this.dir = dir;
        this.status = status;
    }

    public Direction getHeadDirection() {
        return dir;
    }

    public BurntStatus getBurntStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "" + dir + status;
    }
}
