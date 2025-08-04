package palecek.move;

import palecek.move.condition.PlayerNode;
import palecek.move.condition.StringNode;
import palecek.move.condition.ToNode;
import palecek.utils.booleantree.BooleanNode;
import palecek.utils.booleantree.LiteralNode;

public class MoveLiteralNodeParser {
    public static BooleanNode parse(String value, boolean isCondition) {
        if (isCondition) {
            return switch (value) {
                case "TO" -> new ToNode();
                case "E" -> new PlayerNode(PlayerNode.PlayerType.ENEMY);
                case "A" -> new PlayerNode(PlayerNode.PlayerType.ALLY);
                default -> new StringNode(value);
            };
        } else {
            return containsSpecialChar(value)
                    ? new SpecialMoveNode(value)
                    : new MoveNode(value);
        }
    }
    private static boolean containsSpecialChar(String component) {
        return component.charAt(0) == '$';
    }
}
