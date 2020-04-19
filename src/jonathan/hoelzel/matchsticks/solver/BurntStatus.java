package jonathan.hoelzel.matchsticks.solver;

public enum BurntStatus {
    BURNT("X"),
    UNBURNT("_"),
    UNKNOWN("?");

    private final String str;

    BurntStatus(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
