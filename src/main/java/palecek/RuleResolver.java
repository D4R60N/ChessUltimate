package palecek;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Rule;
import palecek.entity.Space;
import palecek.utils.Separators;

import java.util.List;
import java.util.Map;

public class RuleResolver {
    private Map<String, List<Rule>> rules;

    public boolean resolveMove(Position from, Position to, GameState gameState) {
        Board board = gameState.getBoard();
        int playerOnTurn = gameState.getPlayerOnTurn();
        Space fromSpace = board.getFromPosition(from);
        if (fromSpace == null || fromSpace.getTail().equals("X")) {
            throw new IllegalArgumentException("Invalid move: no space at from position");
        }
        String[] fromSpaceOccupant = fromSpace.getHead().split(Separators.INNER_SEPARATOR);
        String playerStr = fromSpaceOccupant[0];
        if (playerStr.charAt(0) != 'p') {
            throw new IllegalArgumentException("Invalid move: not a piece");
        }
        int player = Integer.parseInt(playerStr.substring(1));
        if (playerOnTurn != player) {
            throw new IllegalArgumentException("Invalid move: not player's turn");
        }

        Space toSpace = board.getFromPosition(to);
        if (toSpace == null || toSpace.getTail().equals("X")) {
            throw new IllegalArgumentException("Invalid move: no space at to position");
        }
        String toSpaceOccupant = toSpace.getHead();
        boolean toHasOccupant = toSpaceOccupant != null;

        // appyng rules
        String pieceType = fromSpaceOccupant[1];

        List<Rule> ruleList = rules.get(pieceType);
        if (ruleList == null) {
            return false;
        }
        Player playerObj = gameState.getPlayer();
        for (Rule rule : ruleList) {
            if (rule.isApplicable(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn())) {
                if(toHasOccupant) {
                    if(rule.canCapture(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn())) {
                        fromSpace.setHead(null);
                        toSpace.setHead("p" + player + "." + pieceType);
                        return true;
                    }
                } else {
                    if (rule.canMove(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn())) {
                        fromSpace.setHead(null);
                        toSpace.setHead("p" + player + "." + pieceType);
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
