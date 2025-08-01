package palecek.utils;

public enum Orientation {
    NORTH(1), SOUTH(0), WEST(2), EAST(3);
    private final int value;
    Orientation(int value) {
        this.value = value;
    }

    public static Orientation fromValue(int value) {
        for (Orientation orientation : Orientation.values()) {
            if (orientation.value == value) {
                return orientation;
            }
        }
        throw new IllegalArgumentException("Invalid orientation value: " + value);
    }
}
