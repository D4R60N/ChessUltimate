package palecek;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Rule;
import palecek.utils.Separators;

import java.util.List;
import java.util.Map;

public class RuleResolver {
    private Map<String, List<Rule>> rules;

    public boolean resolveMove(Position from, Position to, GameState gameState) {
        Board board = gameState.getBoard();
        int playerOnTurn = gameState.getPlayerOnTurn();
        String fromSpace = board.getFromPosition(from);
        if (fromSpace == null || fromSpace.equals("X")) {
            throw new IllegalArgumentException("Invalid move: no space at from position");
        }
        String[] splitFromSpace = fromSpace.split(Separators.TYPE_SEPARATOR);
        if (splitFromSpace.length > 2) {
            throw new IllegalArgumentException("Invalid move: from position is not valid");
        }
        if (splitFromSpace.length < 2) {
            throw new IllegalArgumentException("Invalid move: from position does not contain a piece");
        }
        String[] fromSpaceOccupant = splitFromSpace[0].split(Separators.INNER_SEPARATOR);
        String playerStr = fromSpaceOccupant[0];
        if (playerStr.charAt(0) != 'p') {
            throw new IllegalArgumentException("Invalid move: not a piece");
        }
        int player = Integer.parseInt(playerStr.substring(1));
        if (playerOnTurn != player) {
            throw new IllegalArgumentException("Invalid move: not player's turn");
        }

        String toSpace = board.getFromPosition(to);
        if (toSpace == null || toSpace.equals("X")) {
            throw new IllegalArgumentException("Invalid move: no space at to position");
        }
        String[] splitToSpace = toSpace.split(Separators.TYPE_SEPARATOR);
        if (splitToSpace.length > 2) {
            throw new IllegalArgumentException("Invalid move: to position is not valid");
        }
        String toSpaceOccupant = splitToSpace.length > 1 ? splitToSpace[0] : null;
        boolean toHasOccupant = toSpaceOccupant != null;

        // appyng rules
        String pieceType = fromSpaceOccupant[1];

        List<Rule> ruleList = rules.get(pieceType);
        if (ruleList == null) {
            return false;
        }
        Player playerObj = gameState.getPlayer();
        for (Rule rule : ruleList) {
            if (rule.isApplicable(from, to, playerObj.orientation, board)) {
                if(toHasOccupant) {
                    if(rule.canCapture(from, to)) {
                        String fromSpaceTail = splitFromSpace[splitFromSpace.length - 1];
                        String toSpaceTail = splitToSpace[splitToSpace.length - 1];

                        board.setFromPosition(from, fromSpaceTail);

                        String newToSpace = "p" + player + "." + pieceType + "-" + toSpaceTail;
                        board.setFromPosition(to, newToSpace);
                        return true;
                    }
                } else {
                    if (rule.canMove(from, to)) {
                        String fromSpaceTail = splitFromSpace[splitFromSpace.length - 1];
                        String toSpaceTail = splitToSpace[splitToSpace.length - 1];

                        board.setFromPosition(from, fromSpaceTail);

                        String newToSpace = "p" + player + "." + pieceType + "-" + toSpaceTail;
                        board.setFromPosition(to, newToSpace);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setRules(Map<String, List<Rule>> rules) {
        this.rules = rules;
    }
}
