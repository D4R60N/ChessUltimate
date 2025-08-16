package palecek.move.condition;

import palecek.Turn;
import palecek.entity.Position;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;
import java.util.Set;

public class LastTurnPieceNode implements BooleanNode {

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Turn lastTurn = (Turn) context.get("lastTurn");
        if (lastTurn == null) {
            throw new IllegalArgumentException("Last turn is not available in the context");
        } else if (lastTurn.getTurnNumber() == -1) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Set<Position> expectedPositions = (Set<Position>) context.get("positions");
        Position to = lastTurn.getTo();

        if (to == null || expectedPositions == null) {
            return false;
        }

        return expectedPositions.contains(to);
    }
}
