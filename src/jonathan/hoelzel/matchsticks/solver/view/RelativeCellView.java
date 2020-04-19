package jonathan.hoelzel.matchsticks.solver.view;

import jonathan.hoelzel.matchsticks.solver.BurntStatus;

public class RelativeCellView {
    private final RelativeHeadDirection dir;
    private final BurntStatus status;

    public RelativeCellView(RelativeHeadDirection dir, BurntStatus status) {
        this.dir = dir;
        this.status = status;
    }

    public RelativeHeadDirection getDir() {
        return dir;
    }

    public BurntStatus getStatus() {
        return status;
    }
}
