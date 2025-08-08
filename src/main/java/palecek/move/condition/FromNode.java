package palecek.move.condition;

import palecek.entity.Position;
import palecek.utils.booleantree.BooleanNode;

import java.util.Set;

public class FromNode implements BooleanNode {

    public FromNode() {
    }

    @Override
    public boolean evaluate(java.util.Map<String, Object> context) {
        Position from = (Position) context.get("from");
        @SuppressWarnings("unchecked")
        Set<Position> expectedPositions = (Set<Position>) context.get("positions");

        if (from == null || expectedPositions == null) {
            return false;
        }

        return expectedPositions.contains(from);
    }

}
