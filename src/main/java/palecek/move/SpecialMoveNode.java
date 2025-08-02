package palecek.move;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.utils.Operators;
import palecek.utils.Orientation;
import palecek.utils.Separators;
import palecek.utils.SpecialMovePosition;
import palecek.utils.booleantree.BooleanNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecialMoveNode implements BooleanNode {
    private boolean isRank;
    private SpecialMovePosition position;
    private int offset;
    private int distance;
    private boolean isRepeting;

    public SpecialMoveNode(String move) {
        String[] m = move.split(Separators.DIRECTION_SEPARATOR);
        if (m.length > 3 || m.length < 2) {
            throw new IllegalArgumentException("Invalid move format: " + move);
        }
        String query = m[0];
        String[] dimensions = m[1].split(Separators.INNER_SEPARATOR);
        int offset = dimensions.length > 0 ? Integer.parseInt(dimensions[0].trim()) : 0;
        int distance = dimensions.length > 1 ?
                (dimensions[1].equals(Operators.INF_OPERATOR) ?
                        Integer.MAX_VALUE-offset : Integer.parseInt(dimensions[1].trim()))
                : 1;
        if (distance < offset) {
            throw new IllegalArgumentException("Invalid distance: " + distance + " is less than offset: " + offset);
        }
        boolean isRepeting = query.endsWith(Operators.REPETITION_OPERATOR);

        this.isRank = query.startsWith("R");
        this.position = SpecialMovePosition.fromString(query.substring(1, 2));
        this.offset = offset;
        this.distance = distance;
        this.isRepeting = isRepeting;
    }

    public boolean isRank() {
        return isRank;
    }

    public SpecialMovePosition getPosition() {
        return position;
    }

    public int getOffset() {
        return offset;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isRepeting() {
        return isRepeting;
    }


    @Override
    public boolean evaluate(Map<String, Object> context) {
        Position to = (Position) context.get("to");
        Orientation orientation = (Orientation) context.get("orientation");
        Board board = (Board) context.get("board");
        List<Position> expectedPositions = calculateExpectedPositions(orientation, board);

        for (Position pos : expectedPositions) {
            board.setCell(pos.getX(), pos.getY(), "UWU");
        }

        return expectedPositions.contains(to);
    }

    private List<Position> calculateExpectedPositions(Orientation orientation, Board board) {
        List<Position> expectedPositions = new ArrayList<>();

        boolean rank = (orientation == Orientation.NORTH || orientation == Orientation.SOUTH) == isRank;
        SpecialMovePosition pos = position.rotate(orientation);

        int boardOffset = 0;
        int dist = distance;
        int off = offset;
        boolean isBoardEven = (rank ? board.getHeight() : board.getWidth()) % 2 == 0;
        if (rank) {
            distance = Math.min(distance, board.getHeight());
            switch (pos) {
                case CENTER:
                    boardOffset = (int) Math.floor((board.getHeight() - 1) / 2.f);
                    dist = boardOffset - offset;
                    off = Math.max(dist - distance + offset, 0);
                    break;
                case LAST:
                    boardOffset = board.getWidth() - 1;
                    dist = boardOffset - offset + 1;
                    off = Math.max(dist - distance + offset, 0);
                    break;
            }
        } else {
            distance = Math.min(distance, board.getWidth());
            switch (pos) {
                case CENTER:
                    boardOffset = (int) Math.floor((board.getHeight() - 1) / 2.f);
                    dist = boardOffset - offset;
                    off = Math.max(dist - distance + offset, 0);
                    break;
                case LAST:
                    boardOffset = board.getHeight() - 1;
                    dist = boardOffset - offset + 1;
                    off = Math.max(dist - distance + offset, 0);
                    break;
            }
        }

        fillPositions(board, expectedPositions, dist, off, rank);

        if (pos == SpecialMovePosition.CENTER) {
            if (isBoardEven) {
                boardOffset += 1;
            }
            fillPositions(board, expectedPositions, distance + boardOffset, offset + boardOffset, rank);
        }

        return expectedPositions;
    }

    private void fillPositions(Board board, List<Position> expectedPositions, int distance, int offset, boolean rank) {
        if (rank) {
            int limit = Math.min(distance, board.getWidth());
            for (int i = 0; i < board.getWidth(); i++) {
                for (int j = offset; j <= limit; j++) {
                    expectedPositions.add(new Position(i, j));
                }
            }
        } else {
            int limit = Math.min(distance, board.getHeight());
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = offset; j <= limit; j++) {
                    expectedPositions.add(new Position(j, i));
                }
            }
        }
    }
}
