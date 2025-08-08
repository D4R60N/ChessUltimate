package palecek.move.condition;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;

public class BlockedAxisAlignNode implements BooleanNode {

    public BlockedAxisAlignNode() {
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Board board = (Board) context.get("board");
        Position to = (Position) context.get("to");
        Position from = (Position) context.get("from");

        if (board == null || to == null || from == null) {
            return false;
        }

        int minX = Math.min(from.getX(), to.getX());
        int maxX = Math.max(from.getX(), to.getX());
        int minY = Math.min(from.getY(), to.getY());
        int maxY = Math.max(from.getY(), to.getY());

        if (minX != maxX) {
            for (int x = minX + 1; x < maxX; x++) {
                Position pos = new Position(x, from.getY());
                Space space = board.getFromPosition(pos);
                if (space != null && space.getHead() != null) {
                    return true; // Blocked by a player
                }
            }
        }

        if (minY != maxY) {
            for (int y = minY + 1; y < maxY; y++) {
                Position pos = new Position(to.getX(), y);
                Space space = board.getFromPosition(pos);
                if (space != null && space.getHead() != null) {
                    return true; // Blocked by a player
                }
            }
        }

        if (from.getX() != to.getX() || from.getY() != to.getY()) {
            if (minX != maxX) {
                for (int x = minX + 1; x < maxX; x++) {
                    Position pos = new Position(x, to.getY());
                    Space space = board.getFromPosition(pos);
                    if (space != null && space.getHead() != null) {
                        return true; // Blocked by a player
                    }
                }
            }
            if (minY != maxY) {
                for (int y = minY + 1; y < maxY; y++) {
                    Position pos = new Position(from.getX(), y);
                    Space space = board.getFromPosition(pos);
                    if (space != null && space.getHead() != null) {
                        return true; // Blocked by a player
                    }
                }
            }
        }
        return false; // No blocks found
    }
}
