package palecek.move.condition;

import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

public class ConditionLiteralNodeParser {
    public static BooleanNode parse(String value) {
        String[] parts = value.split(Separators.DIRECTION_SEPARATOR);
        return switch (parts[0]) {
            case "TO" -> new ToNode();
            case "FROM" -> new FromNode();
            case "E" -> new PlayerNode(PlayerNode.PlayerType.ENEMY);
            case "A" -> new PlayerNode(PlayerNode.PlayerType.ALLY);
            case "O" -> new OpenNode();
            case "T" -> new TimerNode(value.substring(2));
            case "B+" -> new BlockedAxisAlignNode();
            case "B*" -> new BlockedDiagonalNode();
            case "LT" -> new LastTurnPieceNode();
            case "P" -> new PieceNode(parts[1]);
//            case "S" -> new StringNode(parts[1]);
            default -> new StringNode(parts[1]);
        };
    }
}
