package palecek.move.lastturn;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.move.MoveComponent;
import palecek.move.MoveUtils;
import palecek.utils.Orientation;
import palecek.utils.SpaceType;
import palecek.utils.booleantree.BooleanNode;

import java.util.List;
import java.util.Set;

public class LastMoveNode  implements BooleanNode {
    private final List<MoveComponent> moveComponents;

    public LastMoveNode(String value) {
        this.moveComponents = MoveUtils.parseMove(value, true);
    }

    @Override
    public boolean evaluate(java.util.Map<String, Object> context) {
        Position to = (Position) context.get("to");
        Position from = (Position) context.get("from");
        Orientation orientation = (Orientation) context.get("orientation");
        Board board = (Board) context.get("board");
        Set<Position> expectedPositions = MoveUtils.calculateExpectedPositions(from, moveComponents, orientation, board);
        return expectedPositions.contains(to);
    }
}
