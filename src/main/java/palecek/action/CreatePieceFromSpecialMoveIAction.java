package palecek.action;

import palecek.Player;
import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.move.SpecialMoveUtils;
import palecek.utils.*;

import java.util.Set;

public class CreatePieceFromSpecialMoveIAction implements IAction {
    private final boolean isRank;
    private final SpecialMovePosition position;
    private final int offset;
    private final int distance;
    private boolean isRepeting = false;
    private int spacing = 0;
    private final String value;
    private Set<String> payloads;


    public CreatePieceFromSpecialMoveIAction(String value) {
        String[] parts = value.split(Separators.TYPE_SEPARATOR);
        if (parts.length != 3) {
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

        String val = parts[parts.length - 1];
        if (val.contains("P")) {
            String[] payloadParts = val.split(Separators.DIRECTION_SEPARATOR);
            if (payloadParts.length > 3) {
                throw new IllegalArgumentException("Invalid payload format: " + val);
            }
            if (payloadParts.length == 2) {
                String[] payloadsArray = payloadParts[1].split(Separators.DIMENSION_SEPARATOR);
                this.payloads = Set.of(payloadsArray);
            }
            this.value = "P";
        } else {
            this.value = val;
        }
    }

    @Override
    public void performAction(Position from, Position to, Orientation orientation, Board board, Player player, int turn, String payload) {
        if (value.equals("P")) {
            if (payload == null || payload.isEmpty()) {
                throw new IllegalArgumentException("Payload cannot be null for piece creation");
            }
            if (!payloads.contains(payload)) {
                throw new IllegalArgumentException("Invalid payload: " + payload + ". Expected one of: " + payloads);
            }
        }

        Set<Position> expectedPositions = SpecialMoveUtils.calculateExpectedPositions(orientation, board, distance, offset, isRank, isRepeting, spacing, position);

        if (value.equals("P")) {
            for (Position p : expectedPositions) {
                Space space = board.getFromPosition(p);
                if (space == null || space.getTail().equals("X")) {
                    throw new IllegalArgumentException("Invalid position: " + p + " is not a valid space");
                }
                if (p.equals(from) || p.equals(to)) {
                    space.setUncommittedHead("p" + player.getNumber() + "." + payload);
                } else {
                    space.setHead("p" + player.getNumber() + "." + payload);
                }
            }
        } else {
            for (Position p : expectedPositions) {
                Space space = board.getFromPosition(p);
                if (space == null || space.getTail().equals("X")) {
                    throw new IllegalArgumentException("Invalid position: " + p + " is not a valid space");
                }
                if (p.equals(from) || p.equals(to)) {
                    space.setUncommittedHead("p" + player.getNumber() + "." + value);
                } else {
                    space.setHead("p" + player.getNumber() + "." + value);
                }
            }
        }
    }
}
