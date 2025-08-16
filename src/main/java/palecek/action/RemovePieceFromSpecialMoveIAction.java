package palecek.action;

import palecek.Player;
import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.move.SpecialMoveUtils;
import palecek.utils.Operators;
import palecek.utils.Orientation;
import palecek.utils.Separators;
import palecek.utils.SpecialMovePosition;

import java.util.Set;

public class RemovePieceFromSpecialMoveIAction implements IAction {
    private final boolean isRank;
    private final SpecialMovePosition position;
    private final int offset;
    private final int distance;
    private boolean isRepeting = false;
    private int spacing = 0;


    public RemovePieceFromSpecialMoveIAction(String value) {
        String[] parts = value.split(Separators.TYPE_SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid action format: " + value);
        }

        String[] m = parts[1].split(Separators.DIRECTION_SEPARATOR);
        if (m.length > 3 || m.length < 2) {
            throw new IllegalArgumentException("Invalid position format: " + parts[1]);
        }
        String query = m[0];
        String[] dimensions = m[1].split(Separators.INNER_SEPARATOR);
        int offset = dimensions.length > 0 ? Integer.parseInt(dimensions[0].trim()) : 0;
        int distance = dimensions.length > 1 ?
                (dimensions[1].equals(Operators.INF_OPERATOR) ?
                        Integer.MAX_VALUE - offset : Integer.parseInt(dimensions[1].trim()))
                : offset;
        if (distance < offset) {
            throw new IllegalArgumentException("Invalid distance: " + distance + " is less than offset: " + offset);
        }

        if (query.length() > 2) {
            int spacing = 0;
            if (query.length() > 3) {
                spacing = Integer.parseInt(query.substring(3));
            }
            boolean isRepeting = query.charAt(2) == Operators.REPETITION_OPERATOR.charAt(0);
            this.isRepeting = isRepeting;
            this.spacing = spacing;
        }


        this.isRank = query.startsWith("R");
        this.position = SpecialMovePosition.fromString(query.substring(1, 2));
        this.offset = offset;
        this.distance = distance;
    }

    @Override
    public void performAction(Position from, Position to, Orientation orientation, Board board, Player player, int turn, String payload) {
        Set<Position> expectedPositions = SpecialMoveUtils.calculateExpectedPositions(orientation, board, distance, offset, isRank, isRepeting, spacing, position);

        for (Position p : expectedPositions) {
            Space space = board.getFromPosition(p);
            if (space == null || space.getTail().equals("X")) {
                throw new IllegalArgumentException("Invalid position: " + p + " is not a valid space");
            }
            if (p.equals(from) || p.equals(to)) {
                space.setUncommittedHead(null);
            } else {
                space.setHead(null);
            }
        }
    }
}
