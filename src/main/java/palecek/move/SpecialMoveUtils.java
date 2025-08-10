package palecek.move;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.utils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpecialMoveUtils {

    public static Set<Position> calculateExpectedPositions(Orientation orientation, Board board, int distance, int offset, boolean isRank, boolean isRepeting, int spacing, SpecialMovePosition position) {
        Set<Position> expectedPositions = new HashSet<>();

        boolean rank = (orientation == Orientation.NORTH || orientation == Orientation.SOUTH) == isRank;
        SpecialMovePosition pos = position.rotate(orientation);

        int boardOffset = 0;
        int dist = distance;
        int off = offset;
        boolean isBoardEven = (rank ? board.getHeight() : board.getWidth()) % 2 == 0;
        boolean isReversed = false;
        if (rank) {
            distance = Math.min(distance, board.getHeight());
            switch (pos) {
                case CENTER:
                    boardOffset = (int) Math.floor((board.getHeight() - 1) / 2.f);
                    dist = boardOffset - offset;
                    off = Math.max(dist - distance + offset, 0);
                    isReversed = true;
                    break;
                case LAST:
                    boardOffset = board.getWidth() - 1;
                    dist = boardOffset - offset;
                    off = Math.max(dist - distance + offset, 0);
                    isReversed = true;
                    break;
            }
        } else {
            distance = Math.min(distance, board.getWidth());
            switch (pos) {
                case CENTER:
                    boardOffset = (int) Math.floor((board.getHeight() - 1) / 2.f);
                    dist = boardOffset - offset;
                    off = Math.max(dist - distance + offset, 0);
                    isReversed = true;
                    break;
                case LAST:
                    boardOffset = board.getHeight() - 1;
                    dist = boardOffset - offset;
                    off = Math.max(dist - distance + offset, 0);
                    isReversed = true;
                    break;
            }
        }

        fillPositions(board, expectedPositions, dist, off, rank, isReversed, isRepeting, spacing, distance, offset);

        if (pos == SpecialMovePosition.CENTER) {
            if (isBoardEven) {
                boardOffset += 1;
            }
            fillPositions(board, expectedPositions, distance + boardOffset, offset + boardOffset, rank, false, isRepeting, spacing, distance, offset);
        }

        return expectedPositions;
    }

    private static void fillPositions(Board board, Set<Position> expectedPositions, int dist, int off, boolean rank, boolean isReversed, boolean isRepeting, int spacing, int distance, int offset) {
        int length = distance - offset + 1;
        if (isReversed) {
            if (rank) {
                int modulo = isRepeting ? length + spacing : board.getHeight();
                for (int k = off; k+length >= 0; k -= modulo) {
                    int limit = Math.min(dist + k - off, board.getHeight() - 1);
                    for (int i = 0; i < board.getWidth(); i++) {
                        for (int j = k; j <= limit; j++) {
                            expectedPositions.add(new Position(i, j));
                        }
                    }
                }
            } else {
                int modulo = isRepeting ? length + spacing : board.getWidth();
                for (int k = off; k+length >= 0; k -= modulo) {
                    int limit = Math.min(dist + k - off, board.getWidth() - 1);
                    for (int i = 0; i < board.getHeight(); i++) {
                        for (int j = k; j <= limit; j++) {
                            expectedPositions.add(new Position(j, i));
                        }
                    }
                }
            }
        } else {
            if (rank) {
                int modulo = isRepeting ? length + spacing : board.getHeight();
                for (int k = off; k < board.getHeight(); k += modulo) {
                    int limit = Math.min(dist + k - off, board.getHeight() - 1);
                    for (int i = 0; i < board.getWidth(); i++) {
                        for (int j = k; j <= limit; j++) {
                            expectedPositions.add(new Position(i, j));
                        }
                    }
                }
            } else {
                int modulo = isRepeting ? length + spacing : board.getWidth();
                for (int k = off; k < board.getWidth(); k += modulo) {
                    int limit = Math.min(dist + k - off, board.getWidth() - 1);
                    for (int i = 0; i < board.getHeight(); i++) {
                        for (int j = k; j <= limit; j++) {
                            expectedPositions.add(new Position(j, i));
                        }
                    }
                }
            }
        }
    }
}
