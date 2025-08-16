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

public class RemovePieceFromMoveIAction implements IAction {
    private final SpaceType origin;
    private final List<MoveComponent> moveComponents;


    public RemovePieceFromMoveIAction(String value) {
        String[] parts = value.split(Separators.TYPE_SEPARATOR);
        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid action format: " + value);
        }
        origin = SpaceType.getFromString(parts[1]);
        moveComponents = MoveUtils.parseMove(parts[2], true);
    }

    @Override
    public void performAction(Position from, Position to, Orientation orientation, Board board, Player player, int turn, String payload) {
        Set<Position> expectedPositions = MoveUtils.calculateExpectedPositions(origin == SpaceType.FROM ? from : to, moveComponents, orientation, board);

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
