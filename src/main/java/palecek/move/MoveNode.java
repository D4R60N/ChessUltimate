package palecek.move;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Rule;
import palecek.utils.*;
import palecek.utils.booleantree.BooleanNode;
import palecek.utils.booleantree.BooleanTree;
import palecek.utils.token.Parser;

import java.util.*;

public class MoveNode implements BooleanNode {

    private List<MoveComponent> moveComponents;
    private final Space space;
    private BooleanTree condition;

    public MoveNode(String move) {
        String[] separatedStrings = move.split(Separators.TYPE_SEPARATOR);
        int lastIndex = separatedStrings.length - 1;
        if (separatedStrings.length > 3 || separatedStrings.length < 2) {
            throw new IllegalArgumentException("Invalid move format: " + move);
        }
        this.space = Space.getFromString(separatedStrings[0]);

        moveComponents = new ArrayList<>();
        if (separatedStrings.length == 3) {
            String[] m = separatedStrings[1].split(Separators.DIMENSION_SEPARATOR);
            if (m.length > 2) {
                throw new IllegalArgumentException("Invalid move format: " + move);
            }
            for (String s : m) {
                String[] parts = s.split(Separators.DIRECTION_SEPARATOR);
                if (parts.length == 2) {
                    String direction = parts[1].trim();
                    String[] distanceParts = parts[0].split(Separators.INNER_SEPARATOR);
                    int from = Integer.parseInt(distanceParts[0].trim());
                    int to = from;
                    if (distanceParts.length == 2) {
                        if (distanceParts[1].equals(Operators.INF_OPERATOR)) {
                            to = Integer.MAX_VALUE;
                        } else {
                            to = Integer.parseInt(distanceParts[1].trim());
                        }

                        if (to < from) {
                            throw new IllegalArgumentException("Invalid from: " + from + " is greater than " + to);
                        }
                    } else if (distanceParts.length > 2) {
                        throw new IllegalArgumentException("Invalid distance format: " + s);
                    }
                    int spacing = 0;
                    if (direction.length() > 2) {
                        spacing = Integer.parseInt(direction.substring(2, 3));
                    }
                    moveComponents.add(new MoveComponent(Direction.fromSymbol(direction.substring(0, 1)), from, to, direction.length() > 1 && direction.charAt(1) == Operators.REPETITION_OPERATOR.charAt(0), spacing));
                } else {
                    throw new IllegalArgumentException("Invalid move format: " + s);
                }
            }
        } else {
            moveComponents.add(new MoveComponent(Direction.FORWARD, 0, 0));
        }
        completeMove();

        String condition = separatedStrings[lastIndex];
        Parser parser = new Parser(Rule.tokenizer.tokenize(condition, false), true);
        this.condition = condition.isEmpty() ? null : new BooleanTree(parser.parseExpression());
    }

    private void completeMove() {
        if (moveComponents.size() == 1) {
            MoveComponent component = moveComponents.getFirst();
            if (component.getDirection() == Direction.FORWARD) {
                moveComponents.add(new MoveComponent(Direction.LEFT, 0, 0));
            } else if (component.getDirection() == Direction.BACKWARD) {
                moveComponents.add(new MoveComponent(Direction.RIGHT, 0, 0));
            } else if (component.getDirection() == Direction.LEFT) {
                moveComponents.add(new MoveComponent(Direction.FORWARD, 0, 0));
            } else if (component.getDirection() == Direction.RIGHT) {
                moveComponents.add(new MoveComponent(Direction.BACKWARD, 0, 0));
            }
        }
        if (moveComponents.getFirst().getDirection().isInLine(moveComponents.get(1).getDirection())) {
            throw new IllegalArgumentException("Invalid move format (Directions are in line): " + this);
        }
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
        Position from = (Position) context.get(space.getValue().toLowerCase());
        Orientation orientation = (Orientation) context.get("orientation");
        Board board = (Board) context.get("board");
        Position to = (Position) context.get("to");
        if (from == null || orientation == null || board == null) {
            return false; // Invalid context
        }
        Set<Position> expectedPositions = calculateExpectedPositions(from, moveComponents, orientation, board);

        Map<String, Object> conditionContext = new HashMap<>();
        conditionContext.put("to", to);
        conditionContext.put("from", from);
        conditionContext.put("board", board);
        conditionContext.put("positions", expectedPositions);
        conditionContext.put("player", context.get("player"));
        conditionContext.put("turn", context.get("turn"));

        return condition.evaluate(conditionContext);
    }

    private Set<Position> calculateExpectedPositions(Position from, List<MoveComponent> move, Orientation orientation, Board board) {
        Set<Position> expectedPositions = new HashSet<>();

        for (int i = 0; i < move.size(); i++) {
            MoveComponent line1 = move.get(i);
            for (int j = i + 1; j < move.size(); j++) {
                MoveComponent line2 = move.get(j);
                if (line1.getDirection().isOpposite(line2.getDirection())) {
                    continue;
                }
                fillPositions(expectedPositions, line1, line2, from, orientation, board);
            }
        }

        return expectedPositions;
    }

    private void fillPositions(Set<Position> positions, MoveComponent line1, MoveComponent line2, Position from, Orientation orientation, Board board) {
        int maxX = board.getWidth() - 1;
        int maxY = board.getHeight() - 1;
        Direction direction1 = line1.getDirection().getDirectionFromOrientation(orientation);
        Direction direction2 = line2.getDirection().getDirectionFromOrientation(orientation);
        for (int i = line1.getFrom(); i <= Math.min(line1.getTo(), line1.getDirection().isVertical() ? maxY : maxX); i++) {
            for (int j = line2.getFrom(); j <= Math.min(line2.getTo(), line2.getDirection().isVertical() ? maxY : maxX); j++) {
                int x = from.getX(), y = from.getY();
                switch (direction1) {
                    case FORWARD:
                        y += i;
                        break;
                    case BACKWARD:
                        y -= i;
                        break;
                    case LEFT:
                        x += i;
                        break;
                    case RIGHT:
                        x -= i;
                        break;
                }
                switch (direction2) {
                    case FORWARD:
                        y += j;
                        break;
                    case BACKWARD:
                        y -= j;
                        break;
                    case LEFT:
                        x += j;
                        break;
                    case RIGHT:
                        x -= j;
                        break;
                }
                Position position = new Position(x, y);
                if (line1.isRepeting() || line2.isRepeting()) {
                    if (direction1.isVertical()) {
                        int modulo1 = line2.isRepeting() ? line2.getTo() - line2.getFrom() + 1 + line2.getSpacing() : maxX + 1;
                        int modulo2 = line1.isRepeting() ? line1.getTo() - line1.getFrom() + 1 + line1.getSpacing() : maxY + 1;
                        repete(position, modulo1, modulo2, positions, maxX, maxY, direction2, direction1);
                    } else {
                        int modulo1 = line1.isRepeting() ? line1.getTo() - line1.getFrom() + 1 + line1.getSpacing() : maxX + 1;
                        int modulo2 = line2.isRepeting() ? line2.getTo() - line2.getFrom() + 1 + line2.getSpacing() : maxY + 1;
                        repete(position, modulo1, modulo2, positions, maxX, maxY, direction1, direction2);
                    }

                } else {
                    positions.add(position);
                }
            }
        }
    }

    /**
     * Repeats the position in a grid-like manner based on the modulo values.
     *
     * @param pos        The starting position.
     * @param modulo1    The step size for the first direction.
     * @param modulo2    The step size for the second direction.
     * @param positions  The list to add the repeated positions to.
     * @param maxX       The maximum x-coordinate of the grid.
     * @param maxY       The maximum y-coordinate of the grid.
     * @param direction1 The direction for the first axis (horizontal).
     * @param direction2 The direction for the second axis (vertical).
     */
    private void repete(Position pos, int modulo1, int modulo2, Set<Position> positions, int maxX, int maxY, Direction direction1, Direction direction2) {
        int x = pos.getX();
        int y = pos.getY();
        if (direction1.isNegative() && direction2.isNegative()) {
            for (int i = x; i >= 0; i -= modulo1) {
                for (int j = y; j >= 0; j -= modulo2) {
                    positions.add(new Position(i, j));
                }
            }
        } else if (direction1.isNegative()) {
            for (int i = x; i >= 0; i -= modulo1) {
                for (int j = y; j <= maxY; j += modulo2) {
                    positions.add(new Position(i, j));
                }
            }
        } else if (direction2.isNegative()) {
            for (int i = x; i <= maxX; i += modulo1) {
                for (int j = y; j >= 0; j -= modulo2) {
                    positions.add(new Position(i, j));
                }
            }
        } else {
            for (int i = x; i <= maxX; i += modulo1) {
                for (int j = y; j <= maxY; j += modulo2) {
                    positions.add(new Position(i, j));
                }
            }
        }

    }
}
