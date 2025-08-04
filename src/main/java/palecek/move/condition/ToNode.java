package palecek.move.condition;

import palecek.entity.Position;
import palecek.utils.booleantree.BooleanNode;

import java.util.Set;

public class ToNode implements BooleanNode {

    public ToNode() {
    }

    @Override
    public boolean evaluate(java.util.Map<String, Object> context) {
        Position to = (Position) context.get("to");
        @SuppressWarnings("unchecked")
        Set<Position> expectedPositions = (Set<Position>) context.get("positions");

        if (to == null || expectedPositions == null) {
            return false;
        }

        return expectedPositions.contains(to);
    }

}
