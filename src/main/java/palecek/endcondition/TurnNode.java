package palecek.endcondition;

import palecek.entity.Board;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;

public class TurnNode implements BooleanNode {
    private Integer value;
    private String operator;

    public TurnNode(String value) {
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
        int turn = (Integer) context.get("turn");
        return switch (operator) {
            case ">" -> turn > value;
            case "<" -> turn < value;
            case "=" -> turn == value;
            case ">=" -> turn >= value;
            case "<=" -> turn <= value;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
