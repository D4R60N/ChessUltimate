package palecek.entity;

public class Board {
    private final int width;
    private final int height;
    private Space[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Space[height][width];
    }

    public Board(Space[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            throw new IllegalArgumentException("Grid cannot be null or empty");
        }
        this.height = grid.length;
        this.width = grid[0].length;
        this.grid = grid.clone();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Space getFromPosition(Position position) {
        return getCell(position.getX(), position.getY());
    }

    public void setFromPosition(Position position, Space value) {
        setCell(position.getX(), position.getY(), value);
    }

    public void setHead(Position position, String head) {
        Space space = getCell(position.getX(), position.getY());
        if (space != null) {
            space.setHead(head);
        } else {
            throw new IllegalArgumentException("No space found at position: " + position);
        }
    }

    public String getHead(Position position) {
        Space space = getCell(position.getX(), position.getY());
        if (space != null) {
            return space.getHead();
        }
        return null;
    }

    public void setTail(Position position, String tail) {
        Space space = getCell(position.getX(), position.getY());
        if (space != null) {
            space.setTail(tail);
        } else {
            throw new IllegalArgumentException("No space found at position: " + position);
        }
    }

    public String getTail(Position position) {
        Space space = getCell(position.getX(), position.getY());
        if (space != null) {
            return space.getTail();
        }
        return null;
    }

    public Space[][] getGrid() {
        return grid;
    }

    public void setCell(int x, int y, Space value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = value;
        }
    }

    public Space getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return null;
    }

    @Override
    public String toString() {
        int maxCellLength = 1;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                String cell = grid[row][col].toString();
                if (cell != null && cell.length() > maxCellLength)
                    maxCellLength = cell.length();
            }
        }

        int colLabelLength = 1;
        int cellWidth = Math.max(maxCellLength, colLabelLength);

        StringBuilder sb = new StringBuilder();

        // Print column headers
        sb.append("   "); // Padding for row labels
        for (int col = 0; col < width; col++) {
            String label = String.valueOf(col);
            sb.append("|").append(center(label, cellWidth));
        }
        sb.append("|\n");

        // Print separator line
        sb.append("   ");
        for (int col = 0; col < width; col++) {
            sb.append("+").append("-".repeat(cellWidth));
        }
        sb.append("+\n");

        // Print rows
        for (int row = 0; row < height; row++) {
            sb.append(String.format("%2d ", row)); // Pad row labels
            for (int col = 0; col < width; col++) {
                String cell = grid[row][col].toString();
                if (cell == null) cell = "";
                sb.append("|").append(center(cell, cellWidth));
            }
            sb.append("|\n");

            // Print separator after each row
            sb.append("   ");
            for (int col = 0; col < width; col++) {
                sb.append("+").append("-".repeat(cellWidth));
            }
            sb.append("+\n");
        }

        return sb.toString();
    }

    // Helper method to center a string within a width w
    private String center(String s, int w) {
        if (s.length() >= w) {
            return s;
        }
        int paddingTotal = w - s.length();
        int paddingStart = paddingTotal / 2;
        int paddingEnd = paddingTotal - paddingStart;
        return " ".repeat(paddingStart) + s + " ".repeat(paddingEnd);
    }



}
