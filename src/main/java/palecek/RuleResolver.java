package palecek;

import palecek.entity.*;
import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuleResolver {
    private List<Ideology> ideologies = new ArrayList<>();

    public boolean resolveEndCondition(int player, GameState gameState) {
        Ideology ideology = ideologies.get(player);
        if (ideology == null || ideology.getEndingCondition() == null) {
            return false;
        }
        BooleanTree endingCondition = ideology.getEndingCondition();
        Map<String, Object> context = Map.of(
                "board", gameState.getBoard(),
                "player", player,
                "turn", gameState.getTurn()
        );
        return endingCondition.evaluate(context);
    }

    public boolean resolveMove(Position from, Position to, String payload, GameState gameState) {
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

        List<Rule> ruleList = ideologies.get(playerOnTurn).getRules().get(pieceType);
        if (ruleList == null) {
            return false;
        }
        Player playerObj = gameState.getPlayer();
        for (Rule rule : ruleList) {
            if (rule.isApplicable(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn())) {
                if (toHasOccupant) {
                    if (rule.canCapture(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn(), gameState.getLastTurn())) {
                        fromSpace.setUncommittedHead(null);
                        toSpace.setUncommittedHead("p" + player + "." + pieceType);
                        try {
                            rule.canSpecial(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn(), payload, gameState.getLastTurn());
                        } catch (IllegalArgumentException e) {
                            fromSpace.rollback();
                            toSpace.rollback();
                            System.err.println(e.getMessage());
                            return false;
                        }
                        fromSpace.commit();
                        toSpace.commit();

                        Turn lastTurn = new Turn(
                                from, to, gameState.getPlayerOnTurn(), gameState.getTurn(), payload, pieceType
                        );
                        gameState.setLastTurn(lastTurn);
                        return true;
                    }
                } else {
                    if (rule.canMove(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn(), gameState.getLastTurn())) {
                        fromSpace.setUncommittedHead(null);
                        toSpace.setUncommittedHead("p" + player + "." + pieceType);
                        try {
                            rule.canSpecial(from, to, playerObj.getOrientation(), board, gameState.getPlayer(), gameState.getTurn(), payload, gameState.getLastTurn());
                        } catch (IllegalArgumentException e) {
                            fromSpace.rollback();
                            toSpace.rollback();
                            System.err.println(e.getMessage());
                            return false;
                        }
                        fromSpace.commit();
                        toSpace.commit();

                        Turn lastTurn = new Turn(
                                from, to, gameState.getPlayerOnTurn(), gameState.getTurn(), payload, pieceType
                        );
                        gameState.setLastTurn(lastTurn);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addRules(Ideology ideology) {
        this.ideologies.add(ideology);
    }
}
