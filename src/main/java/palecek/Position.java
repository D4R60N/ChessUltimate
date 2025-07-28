package palecek;

import palecek.utils.Orientation;
import palecek.utils.Separators;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(String positionString) {
        String[] parts = positionString.split(Separators.DIMENSION_SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid position format: " + positionString);
        }
        try {
            this.x = Integer.parseInt(parts[0].trim());
            this.y = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid position format: " + positionString, e);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    private int translateLettersToInt(char letter) {
        if (letter >= 'a' && letter <= 'z') {
            return letter - 'a';
        } else if (letter >= 'A' && letter <= 'Z') {
            return letter - 'A';
        } else {
            throw new IllegalArgumentException("Invalid letter: " + letter);
        }
    }

    private char translateIntToLetters(int number) {
        if (number >= 0 && number < 26) {
            return (char) (number + 'a');
        } else {
            throw new IllegalArgumentException("Invalid number: " + number);
        }
    }

    public int translateStringToInt(String string) {
        int result = 0;
        for (int i = 0; i < string.length(); i++) {
            result += (int) (translateLettersToInt(string.charAt(i)) * Math.pow(26, i));
        }
        return result;
    }

    public String translateIntToString(int number) {
        StringBuilder result = new StringBuilder();
        while (number > 0) {
            int remainder = number % 26;
            result.insert(0, translateIntToLetters(remainder));
            number /= 26;
        }
        return result.toString();
    }

    public Position rotate(Orientation orientation) {
        switch (orientation) {
            case NORTH:
                return new Position(x, y);
            case EAST:
                return new Position(y, -x);
            case SOUTH:
                return new Position(-x, -y);
            case WEST:
                return new Position(-y, x);
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }

    public Position rotateAround(Position center, Orientation orientation) {
        int relativeX = this.x - center.getX();
        int relativeY = this.y - center.getY();

        Position relativePosition = new Position(relativeX, relativeY);

        Position rotatedRelativePosition = relativePosition.rotate(orientation);

        int finalX = rotatedRelativePosition.getX() + center.getX();
        int finalY = rotatedRelativePosition.getY() + center.getY();

        return new Position(finalX, finalY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }
}
