package palecek.entity;

import palecek.Player;
import palecek.action.Action;
import palecek.action.ActionUtils;
import palecek.utils.*;
import palecek.utils.booleantree.BooleanTree;
import palecek.utils.token.Parser;
import palecek.utils.token.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rule {
    private String piece;
    private BooleanTree moveCondition;
    private boolean canCapture;
    private BooleanTree captureCondition;
    private boolean hasAction;
    private List<Action> action;
    private BooleanTree actionCondition;


    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public BooleanTree getMoveCondition() {
        return moveCondition;
    }

    public void setMoveCondition(String moveCondition) {
        Parser parser = new Parser(Ideology.tokenizer.tokenize(moveCondition, true));
        this.moveCondition = moveCondition.isEmpty() ? null : new BooleanTree(parser.parseExpression());
    }

    public boolean isCanCapture() {
        return canCapture;
    }

    public void setCanCapture(boolean canCapture) {
        this.canCapture = canCapture;
    }

    public BooleanTree getCaptureCondition() {
        return captureCondition;
    }

    public void setCaptureCondition(String captureCondition) {
        Parser parser = new Parser(Ideology.tokenizer.tokenize(captureCondition, true));
        this.captureCondition = captureCondition.isEmpty() ? null : new BooleanTree(parser.parseExpression());
    }

    public boolean isHasAction() {
        return hasAction;
    }

    public void setHasAction(boolean hasAction) {
        this.hasAction = hasAction;
    }

    public List<Action> getAction() {
        return action;
    }

    public void setAction(String action) {
        String[] actions = action.split(Separators.SPACE_SEPARATOR);
        this.action = new ArrayList<>(actions.length);
        for (String act : actions) {
            if (!act.isEmpty()) {
                this.action.add(ActionUtils.parseAction(act));
            }
        }
    }

    public BooleanTree getActionCondition() {
        return actionCondition;
    }

    public void setActionCondition(String actionCondition) {
        Parser parser = new Parser(Ideology.tokenizer.tokenize(actionCondition, true));
        this.actionCondition = actionCondition.isEmpty() ? null : new BooleanTree(parser.parseExpression());
    }

    @Override
    public String toString() {
        return "Rule{" +
                "piece='" + piece + '\'' +
                ", moveCondition='" + moveCondition + '\'' +
                ", canCapture=" + canCapture +
                ", captureCondition='" + captureCondition + '\'' +
                ", hasAction=" + hasAction +
                ", action='" + action + '\'' +
                ", actionCondition='" + actionCondition + '\'' +
                '}';
    }

    public boolean isApplicable(Position from, Position to, Orientation orientation, Board board, Player player, int turn) {
        return true; //todo
    }

    public boolean canMove(Position from, Position to, Orientation orientation, Board board, Player player, int turn) {
        if (moveCondition == null) {
            if (!from.equals(to)) {
                return false;
            }
            return true; // No move defined, so the piece can stay in place
        }
        Map<String, Object> context = Map.of(
                "from", from,
                "to", to,
                "orientation", orientation,
                "board", board,
                "player", player,
                "turn", turn
        );
        return moveCondition.evaluate(context);
    }

    public boolean canCapture(Position from, Position to, Orientation orientation, Board board, Player player, int turn) {
        if (!canCapture) {
            return false;
        }
        if (captureCondition == null) {
            if (!from.equals(to)) {
                return false;
            }
            return true; // No move defined, so the piece can stay in place
        }
        Map<String, Object> context = Map.of(
                "from", from,
                "to", to,
                "orientation", orientation,
                "board", board,
                "player", player,
                "turn", turn
        );
        return captureCondition.evaluate(context);
    }

    public boolean canSpecial(Position from, Position to, Orientation orientation, Board board, Player player, int turn) {
        if (!hasAction || action == null || action.isEmpty()) {
            return false;
        }
        if (actionCondition == null) {
            if (!from.equals(to)) {
                return false;
            }
            return true; // No actionCondition defined, so the action can be performed
        }
        Map<String, Object> context = Map.of(
                "from", from,
                "to", to,
                "orientation", orientation,
                "board", board,
                "player", player,
                "turn", turn
        );
        return actionCondition.evaluate(context);
    }

    public void performAction(Position from, Position to, Orientation orientation, Board board, Player player, int turn, String payload) {
        for (Action action : action) {
            action.performAction(from, to, orientation, board, player, turn, payload);
        }
    }
}
