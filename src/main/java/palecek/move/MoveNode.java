package palecek.move;

import palecek.entity.Board;
import palecek.entity.Ideology;
import palecek.entity.Position;
import palecek.entity.Rule;
import palecek.utils.*;
import palecek.utils.booleantree.BooleanNode;
import palecek.utils.booleantree.BooleanTree;
import palecek.utils.token.Parser;

import java.util.*;

public class MoveNode implements BooleanNode {

    private List<MoveComponent> moveComponents;
    private final SpaceType spaceType;
    private BooleanTree condition;

    public MoveNode(String move) {
        String[] separatedStrings = move.split(Separators.TYPE_SEPARATOR);
        int lastIndex = separatedStrings.length - 1;
        if (separatedStrings.length > 3 || separatedStrings.length < 2) {
            throw new IllegalArgumentException("Invalid move format: " + move);
        }
        this.spaceType = SpaceType.getFromString(separatedStrings[0]);

        moveComponents = MoveUtils.parseMove(separatedStrings[1], separatedStrings.length == 3);

        String condition = separatedStrings[lastIndex];
        Parser parser = new Parser(Ideology.tokenizer.tokenize(condition, false), Parser.ParserType.CONDITION);
        this.condition = condition.isEmpty() ? null : new BooleanTree(parser.parseExpression());
    }



    public List<MoveComponent> getMoveComponents() {
        return moveComponents;
    }

    public void setMoveComponents(List<MoveComponent> moveComponents) {
        this.moveComponents = moveComponents;
    }

    public void addMoveComponent(MoveComponent moveComponent) {
        this.moveComponents.add(moveComponent);
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Position from = (Position) context.get(spaceType.getValue().toLowerCase());
        Orientation orientation = (Orientation) context.get("orientation");
        Board board = (Board) context.get("board");
        Position to = (Position) context.get("to");
        if (from == null || orientation == null || board == null) {
            return false; // Invalid context
        }
        Set<Position> expectedPositions = MoveUtils.calculateExpectedPositions(from, moveComponents, orientation, board);

        Map<String, Object> conditionContext = new HashMap<>();
        conditionContext.put("to", to);
        conditionContext.put("from", from);
        conditionContext.put("board", board);
        conditionContext.put("positions", expectedPositions);
        conditionContext.put("player", context.get("player"));
        conditionContext.put("turn", context.get("turn"));
        conditionContext.put("lastTurn", context.get("lastTurn"));

        return condition.evaluate(conditionContext);
    }


}
