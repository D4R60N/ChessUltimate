package palecek.move;

import palecek.move.condition.*;
import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

public class MoveLiteralNodeParser {
    public static BooleanNode parse(String value) {
        String[] parts = value.split(Separators.TYPE_SEPARATOR);
        return switch (parts[0]) {
            case "FROM", "TO" -> new MoveNode(value);
            case "T" -> new TimerNode(value.substring(2));
            default -> new SpecialMoveNode(value);
        };
    }
}
