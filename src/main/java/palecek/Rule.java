package palecek;

import palecek.utils.Direction;
import palecek.utils.Operators;
import palecek.utils.Orientation;
import palecek.utils.Separators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Rule {
    private String piece;
    private List<Move> move;
    private String moveCondition;
    private boolean canCapture;
    private String captureCondition;
    private boolean hasAction;
    private String action;
    private String actionCondition;

    // Getters and Setters


    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setMove(String move) {
        this.move = parseMove(move);
    }

    private List<Move> parseMove(String move) {
        List<Move> moves = new ArrayList<>();
        String[] moveComponents = move.split(Separators.OR_SEPARATOR);
        for (String component : moveComponents) {
            String[] m = component.split(Separators.DIMENSION_SEPARATOR);
            if (m.length > 4) {
                throw new IllegalArgumentException("Invalid move format: " + component);
            }
            Move newMove = new Move();
            for (String s : m) {
                String[] parts = s.split(Separators.DIRECTION_SEPARATOR);
                if (parts.length == 2) {
                    String direction = parts[1].trim();
                    String[] distanceParts = parts[0].split(Separators.INNER_SEPARATOR);
                    int distance = Integer.parseInt(distanceParts[0].trim());
                    int to = distance;
                    if (distanceParts.length == 2) {
                        if (distanceParts[1].equals(Operators.INF_OPERATOR)) {
                            to = Integer.MAX_VALUE;
                        } else {
                            to = Integer.parseInt(distanceParts[1].trim());
                        }

                        if (to < distance) {
                            throw new IllegalArgumentException("Invalid distance: " + distance + " is greater than " + to);
                        }
                    } else if (distanceParts.length > 2) {
                        throw new IllegalArgumentException("Invalid distance format: " + s);
                    }
                    newMove.addMoveComponent(new MoveComponent(Direction.fromSymbol(direction.substring(0, 1)), distance, to, direction.length() > 1 && direction.charAt(1) == Operators.REPETITION_OPERATOR.charAt(0)));
                } else {
                    throw new IllegalArgumentException("Invalid move format: " + s);
                }
            }
            // Trim moves to remove unnecessary components
            trimMoves(newMove.getMoveComponents());
            completeMove(newMove);
            moves.add(newMove);
        }
        return moves;
    }

    private void completeMove(Move move) {
        List<MoveComponent> components = move.getMoveComponents();
        if (components.size() == 1) {
            MoveComponent component = components.getFirst();
            if (component.getDirection() == Direction.FORWARD) {
                components.add(new MoveComponent(Direction.LEFT, 0, 0));
            } else if (component.getDirection() == Direction.BACKWARD) {
                components.add(new MoveComponent(Direction.RIGHT, 0, 0));
            } else if (component.getDirection() == Direction.LEFT) {
                components.add(new MoveComponent(Direction.FORWARD, 0, 0));
            } else if (component.getDirection() == Direction.RIGHT) {
                components.add(new MoveComponent(Direction.BACKWARD, 0, 0));
            }

        } else if (components.size() == 3) {
            boolean hasForward = false, hasBackward = false, hasLeft = false, hasRight = false;
            for (MoveComponent component : components) {
                if (component.getDirection() == Direction.FORWARD) {
                    hasForward = true;
                } else if (component.getDirection() == Direction.BACKWARD) {
                    hasBackward = true;
                } else if (component.getDirection() == Direction.LEFT) {
                    hasLeft = true;
                } else if (component.getDirection() == Direction.RIGHT) {
                    hasRight = true;
                }
            }
            if (!hasForward) {
                components.add(new MoveComponent(Direction.FORWARD, 0, 0));
            }
            if (!hasBackward) {
                components.add(new MoveComponent(Direction.BACKWARD, 0, 0));
            }
            if (!hasLeft) {
                components.add(new MoveComponent(Direction.LEFT, 0, 0));
            }
            if (!hasRight) {
                components.add(new MoveComponent(Direction.RIGHT, 0, 0));
            }
        }
    }

    public String getMoveCondition() {
        return moveCondition;
    }

    public void setMoveCondition(String moveCondition) {
        this.moveCondition = moveCondition;
    }

    public boolean isCanCapture() {
        return canCapture;
    }

    public void setCanCapture(boolean canCapture) {
        this.canCapture = canCapture;
    }

    public String getCaptureCondition() {
        return captureCondition;
    }

    public void setCaptureCondition(String captureCondition) {
        this.captureCondition = captureCondition;
    }

    public boolean isHasAction() {
        return hasAction;
    }

    public void setHasAction(boolean hasAction) {
        this.hasAction = hasAction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionCondition() {
        return actionCondition;
    }

    public void setActionCondition(String actionCondition) {
        this.actionCondition = actionCondition;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "piece='" + piece + '\'' +
                ", move='" + move + '\'' +
                ", moveCondition='" + moveCondition + '\'' +
                ", canCapture=" + canCapture +
                ", captureCondition='" + captureCondition + '\'' +
                ", hasAction=" + hasAction +
                ", action='" + action + '\'' +
                ", actionCondition='" + actionCondition + '\'' +
                '}';
    }

    public boolean isApplicable(Position from, Position to, Orientation orientation, Board board) {
        if (move == null || move == Collections.EMPTY_LIST) {
            if (!from.equals(to)) {
                return false;
            }
        }
        for (Move move : this.move) {
            if (move == null || move.getMoveComponents() == null || move.getMoveComponents().isEmpty()) {
                continue;
            }
            List<Position> expectedPositions = calculateExpectedPositions(from, move.getMoveComponents(), orientation, board);

            for (Position expectedPosition : expectedPositions) {
                board.setFromPosition(expectedPosition, "lul");
            }

            if (expectedPositions.contains(to)) {
                return true;
            }
        }

        return false;
    }

    public boolean canMove(Position from, Position to) {
        return true;
    }

    public boolean canCapture(Position from, Position to) {
        if (!canCapture) {
            return false;
        }
        return true;
    }


    private List<Position> calculateExpectedPositions(Position from, List<MoveComponent> move, Orientation orientation, Board board) {
        List<Position> expectedPositions = new ArrayList<>();

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

    private void fillPositions(List<Position> positions, MoveComponent line1, MoveComponent line2, Position from, Orientation orientation, Board board) {
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
                        int modulo1 = line2.isRepeting() ? line2.getTo() : maxX+1;
                        int modulo2 = line1.isRepeting() ? line1.getTo() : maxY+1;
                        repete(position, modulo1, modulo2, positions, maxX, maxY, direction2, direction1);
                    } else {
                        int modulo1 = line1.isRepeting() ? line1.getTo() : maxX+1;
                        int modulo2 = line2.isRepeting() ? line2.getTo() : maxY+1;
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
    private void repete(Position pos, int modulo1, int modulo2, List<Position> positions, int maxX, int maxY, Direction direction1, Direction direction2) {
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

    private void trimMoves(List<MoveComponent> moveComponents) {
        LinkedList<MoveComponent> trimmedComponents = new LinkedList<>();
        for (int i = 0; i < moveComponents.size(); i++) {
            MoveComponent line1 = moveComponents.get(i);
            for (int j = i + 1; j < moveComponents.size(); j++) {
                MoveComponent line2 = moveComponents.get(j);
                // Check if the moveComponents are in opposite directions
                if (line1.getDirection().isOpposite(line2.getDirection())) {
                    // Check if the moveComponents overlap
                    if (line1.getFrom() == line2.getFrom()) {
                        // Check if the second line is remains a line after trimming
                        if (line2.getFrom() == line2.getTo()) {
                            trimmedComponents.add(line2);
                        } else {
                            line2.setFrom(line2.getFrom() + 1);
                        }
                    }
                }
            }
        }
        // Remove moveComponents that are completely contained in other moveComponents
        moveComponents.removeAll(trimmedComponents);
    }
}
