package palecek.move.condition;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;
import java.util.Set;

public class PieceNode implements BooleanNode {
    private String value;

    public PieceNode(String value) {
        this.value = value;
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Board board = (Board) context.get("board");
        @SuppressWarnings("unchecked")
        Set<Position> expectedPositions = (Set<Position>) context.get("positions");

        if (board == null || expectedPositions == null || value == null) {
            return false;
        }

        for (Position position : expectedPositions) {
            Space space = board.getFromPosition(position);
            String pieceType = space != null ? space.getPieceType() : null;
            if (pieceType != null && pieceType.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
