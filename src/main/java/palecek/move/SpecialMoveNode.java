package palecek.move;

import palecek.entity.Board;
import palecek.entity.Ideology;
import palecek.entity.Position;
import palecek.entity.Rule;
import palecek.utils.Operators;
import palecek.utils.Orientation;
import palecek.utils.Separators;
import palecek.utils.SpecialMovePosition;
import palecek.utils.booleantree.BooleanNode;
import palecek.utils.booleantree.BooleanTree;
import palecek.utils.token.Parser;

import java.util.*;

public class SpecialMoveNode implements BooleanNode {
    private boolean isRank;
    private SpecialMovePosition position;
    private int offset;
    private int distance;
    private boolean isRepeting = false;
    private int spacing = 0;
    private BooleanTree condition;

    public SpecialMoveNode(String move) {
        String[] separatedStrings = move.split(Separators.TYPE_SEPARATOR);
        if (separatedStrings.length != 2) {
            throw new IllegalArgumentException("Invalid move format: " + move);
        }

        String[] m = separatedStrings[0].split(Separators.DIRECTION_SEPARATOR);
        if (m.length > 3 || m.length < 2) {
            throw new IllegalArgumentException("Invalid move format: " + move);
        }
        String query = m[0];
        String[] dimensions = m[1].split(Separators.INNER_SEPARATOR);
        int offset = dimensions.length > 0 ? Integer.parseInt(dimensions[0].trim()) : 0;
        int distance = dimensions.length > 1 ?
                (dimensions[1].equals(Operators.INF_OPERATOR) ?
                        Integer.MAX_VALUE - offset : Integer.parseInt(dimensions[1].trim()))
                : offset;
        if (distance < offset) {
            throw new IllegalArgumentException("Invalid distance: " + distance + " is less than offset: " + offset);
        }

        if (query.length() > 2) {
            int spacing = 0;
            if (query.length() > 3) {
                spacing = Integer.parseInt(query.substring(3));
            }
            boolean isRepeting = query.charAt(2) == Operators.REPETITION_OPERATOR.charAt(0);
            this.isRepeting = isRepeting;
            this.spacing = spacing;
        }


        this.isRank = query.startsWith("R");
        this.position = SpecialMovePosition.fromString(query.substring(1, 2));
        this.offset = offset;
        this.distance = distance;

        String condition = separatedStrings[1];
        Parser parser = new Parser(Ideology.tokenizer.tokenize(condition, false), Parser.ParserType.CONDITION);
        this.condition = condition.isEmpty() ? null : new BooleanTree(parser.parseExpression());
    }

    public boolean isRank() {
        return isRank;
    }

    public SpecialMovePosition getPosition() {
        return position;
    }

    public int getOffset() {
        return offset;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isRepeting() {
        return isRepeting;
    }

    public int getSpacing() {
        return spacing;
    }


    @Override
    public boolean evaluate(Map<String, Object> context) {
        Position to = (Position) context.get("to");
        Position from = (Position) context.get("from");
        Orientation orientation = (Orientation) context.get("orientation");
        Board board = (Board) context.get("board");
        Set<Position> expectedPositions = SpecialMoveUtils.calculateExpectedPositions(orientation, board, distance, offset, isRank, isRepeting, spacing, position);

        Map<String, Object> conditionContext = new HashMap<>();
        conditionContext.put("to", to);
        conditionContext.put("from", from);
        conditionContext.put("board", board);
        conditionContext.put("positions", expectedPositions);
        conditionContext.put("player", context.get("player"));
        conditionContext.put("turn", context.get("turn"));

        return condition.evaluate(conditionContext);
    }


}
