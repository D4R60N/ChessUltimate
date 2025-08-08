package palecek.move.condition;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;
import java.util.Set;

public class OpenNode implements BooleanNode {

    public OpenNode() {
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Board board = (Board) context.get("board");
        @SuppressWarnings("unchecked")
        Set<Position> expectedPositions = (Set<Position>) context.get("positions");

        if (board == null || expectedPositions == null) {
            return false;
        }

        for (Position position : expectedPositions) {
            Space space = board.getFromPosition(position);
            if (space != null && space.getHead() != null) {
                return false;
            }
        }
        return true;
    }
}
