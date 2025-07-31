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
}
