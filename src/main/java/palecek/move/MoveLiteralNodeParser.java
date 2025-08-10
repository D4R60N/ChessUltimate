package palecek.move;

import palecek.move.condition.*;
import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

public class MoveLiteralNodeParser {
    public static BooleanNode parse(String value, boolean isCondition) {
        String[] parts = value.split(Separators.TYPE_SEPARATOR);
        if (isCondition) {
            return switch (parts[0]) {
                case "TO" -> new ToNode();
                case "FROM" -> new FromNode();
                case "E" -> new PlayerNode(PlayerNode.PlayerType.ENEMY);
                case "A" -> new PlayerNode(PlayerNode.PlayerType.ALLY);
                case "O" -> new OpenNode();
                case "T" -> new TimerNode(value.substring(2));
                case "B+" -> new BlockedAxisAlignNode();
                case "B*" -> new BlockedDiagonalNode();
                default -> new StringNode(value);
            };
        } else {
            return switch (parts[0]) {
                case "FROM", "TO" -> new MoveNode(value);
                case "T" -> new TimerNode(value.substring(2));
                default -> new SpecialMoveNode(value);
            };
        }
    }
    private static boolean containsSpecialChar(String component) {
        return component.charAt(0) == '$';
    }
}
