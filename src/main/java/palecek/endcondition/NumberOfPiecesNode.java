package palecek.endcondition;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;
import java.util.Set;

public class NumberOfPiecesNode implements BooleanNode {
    private String piece;
    private Integer value;
    private String operator;

    public NumberOfPiecesNode(String piece, String value) {
        this.piece = piece;
        if(value.startsWith(">=") || value.startsWith("<=")) {
            this.operator = value.substring(0, 2);
            this.value = Integer.parseInt(value.substring(2).trim());
        } else if (value.startsWith(">") || value.startsWith("<")) {
            this.operator = value.substring(0, 1);
            this.value = Integer.parseInt(value.substring(1).trim());
        } else {
            this.operator = "=";
            this.value = Integer.parseInt(value.trim());
        }
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Board board = (Board) context.get("board");
        int player = (Integer) context.get("player");
        int count = board.countPiecesByType(piece, player);
        return switch (operator) {
            case ">" -> count > value;
            case "<" -> count < value;
            case "=" -> count == value;
            case ">=" -> count >= value;
            case "<=" -> count <= value;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
