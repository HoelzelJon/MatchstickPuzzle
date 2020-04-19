package jonathan.hoelzel.matchsticks.solver.view;

import jonathan.hoelzel.matchsticks.Direction;

import static jonathan.hoelzel.matchsticks.Direction.*;

public enum ViewPerspective {
    ROW {
        @Override
        public RelativeHeadDirection transform(Direction dir) {
            if (dir == L) {
                return RelativeHeadDirection.FRONT;
            } else if (dir == R) {
                return RelativeHeadDirection.BACK;
            } else {
                return RelativeHeadDirection.ORTHOGONAL;
            }
        }
    },
    COLUMN {
        @Override
        public RelativeHeadDirection transform(Direction dir) {
            if (dir == U) {
                return RelativeHeadDirection.FRONT;
            } else if (dir == D) {
                return RelativeHeadDirection.BACK;
            } else {
                return RelativeHeadDirection.ORTHOGONAL;
            }
        }
    };

    public abstract RelativeHeadDirection transform(Direction dir);
}
