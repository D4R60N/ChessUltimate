package palecek.action;

import palecek.Player;
import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.move.MoveComponent;
import palecek.move.MoveUtils;
import palecek.utils.Orientation;
import palecek.utils.Separators;
import palecek.utils.SpaceType;

import java.util.List;
import java.util.Set;

public class CreatePieceFromMoveIAction implements IAction {
    private final SpaceType origin;
    private final List<MoveComponent> moveComponents;
    private final String value;
    private Set<String> payloads;


    public CreatePieceFromMoveIAction(String value) {
        String[] parts = value.split(Separators.TYPE_SEPARATOR);
        if (parts.length < 2 || parts.length > 4) {
            throw new IllegalArgumentException("Invalid action format: " + value);
        }
        origin = SpaceType.getFromString(parts[1]);
        moveComponents = MoveUtils.parseMove(parts[2], parts.length == 4);
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

        Set<Position> expectedPositions = MoveUtils.calculateExpectedPositions(origin == SpaceType.FROM ? from :to, moveComponents, orientation, board);

        if (value.equals("P")) {
            for (Position p : expectedPositions) {
                Space space = board.getFromPosition(p);
                if (space == null || space.getTail().equals("X")) {
                    continue;
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
                    continue;
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
