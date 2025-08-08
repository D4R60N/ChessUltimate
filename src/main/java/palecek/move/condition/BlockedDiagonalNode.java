package palecek.move.condition;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;

public class BlockedDiagonalNode implements BooleanNode {

    public BlockedDiagonalNode() {
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Board board = (Board) context.get("board");
        Position to = (Position) context.get("to");
        Position from = (Position) context.get("from");

        if (board == null || to == null || from == null) {
            return false;
        }

        return bresenhamBetween(from, to, board);
    }

    private boolean bresenhamBetween(Position a, Position b, Board board) {

        int y0 = a.getY();
        int x0 = a.getX();
        int y1 = b.getY();
        int x1 = b.getX();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        // walk the line from (x0,y0) to (x1,y1)
        while (true) {
            // skip endpoints: only add if not equal to start or end
            if (!(x0 == a.getX() && y0 == a.getY()) && !(x0 == b.getX() && y0 == b.getY())) {
                Space space = board.getFromPosition(new Position(x0, y0));
                if (space != null && space.getHead() != null) {
                    return true; // blocked
                }
            }

            // reached end
            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        return false;
    }
}
