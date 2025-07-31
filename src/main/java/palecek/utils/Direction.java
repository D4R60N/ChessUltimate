package palecek.utils;

public enum Direction {
    FORWARD("f"), BACKWARD("b"), LEFT("l"), RIGHT("r");
    private final String symbol;

    Direction(String symbol) {
        this.symbol = symbol;
    }

    public Direction getDirectionFromOrientation(Orientation orientation) {
        return switch (orientation) {
            case NORTH -> this;
            case EAST -> switch (this) {
                case FORWARD -> RIGHT;
                case BACKWARD -> LEFT;
                case LEFT -> FORWARD;
                case RIGHT -> BACKWARD;
            };
            case SOUTH -> switch (this) {
                case FORWARD -> BACKWARD;
                case BACKWARD -> FORWARD;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
            case WEST -> switch (this) {
                case FORWARD -> LEFT;
                case BACKWARD -> RIGHT;
                case LEFT -> BACKWARD;
                case RIGHT -> FORWARD;
            };
            default -> throw new IllegalArgumentException("Unknown orientation: " + orientation);
        };
    }

    public static Direction fromSymbol(String symbol) {
        for (Direction direction : values()) {
            if (direction.symbol.equals(symbol)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("No Direction with symbol " + symbol);
    }

    public boolean isOpposite(Direction other) {
        return (this == FORWARD && other == BACKWARD) ||
               (this == BACKWARD && other == FORWARD) ||
               (this == LEFT && other == RIGHT) ||
               (this == RIGHT && other == LEFT);
    }

    public boolean isVertical() {
        return this == FORWARD || this == BACKWARD;
    }

    public boolean isNegative() {
        return this == BACKWARD || this == RIGHT;
    }

    public boolean isInLine(Direction other) {
        return (this == FORWARD && other == BACKWARD) ||
               (this == BACKWARD && other == FORWARD) ||
               (this == LEFT && other == RIGHT) ||
               (this == RIGHT && other == LEFT);
    }
}

