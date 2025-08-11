package palecek.endcondition;

import palecek.move.condition.*;
import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

public class EndConditionLiteralNodeParser {
    public static BooleanNode parse(String value) {
        String[] parts = value.split(Separators.TYPE_SEPARATOR);
        return switch (parts[0]) {
            case "NUM" -> new NumberOfPiecesNode(parts[1], parts[2]);
            case "T" -> new TurnNode(parts[1]);
            default -> throw new IllegalArgumentException("Unknown end condition type: " + parts[0]);
        };
    }
}
