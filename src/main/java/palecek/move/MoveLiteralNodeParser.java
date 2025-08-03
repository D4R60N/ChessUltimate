package palecek.move;

import palecek.utils.booleantree.BooleanNode;
import palecek.utils.booleantree.LiteralNode;

public class MoveLiteralNodeParser {
    public static BooleanNode parse(String value) {
        return containsSpecialChar(value)
                ? new SpecialMoveNode(value)
                : new MoveNode(value);
    }
    private static boolean containsSpecialChar(String component) {
        return component.charAt(0) == '$';
    }
}
