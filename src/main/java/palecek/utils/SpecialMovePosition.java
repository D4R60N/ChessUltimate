package palecek.utils;

public enum SpecialMovePosition {
    FIRST, LAST, CENTER;

    public static SpecialMovePosition fromString(String position) {
        switch (position) {
            case "f":
                return FIRST;
            case "l":
                return LAST;
            case "c":
                return CENTER;
            default:
                throw new IllegalArgumentException("Unknown special move position: " + position);
        }
    }

    public SpecialMovePosition rotate(Orientation orientation) {
        switch (orientation) {
            case NORTH, EAST:
                return this;
            case SOUTH, WEST:
                return switch (this) {
                    case FIRST -> LAST;
                    case LAST -> FIRST;
                    case CENTER -> CENTER;
                };
            default:
                throw new IllegalArgumentException("Unknown orientation: " + orientation);
        }
    }
}
