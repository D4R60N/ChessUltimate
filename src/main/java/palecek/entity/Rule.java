package palecek.entity;

import palecek.move.MoveNode;
import palecek.move.MoveComponent;
import palecek.move.SpecialMoveNode;
import palecek.move.SpecialMoveComponent;
import palecek.utils.*;
import palecek.utils.booleantree.BooleanTree;
import palecek.utils.token.Parser;
import palecek.utils.token.Tokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Rule {
    private String piece;
//    private List<Move> move;
//    private List<SpecialMove> specialMove;
    private BooleanTree move;
    private String moveCondition;
    private boolean canCapture;
    private String captureCondition;
    private boolean hasAction;
    private String action;
    private String actionCondition;

    public static Tokenizer tokenizer = new Tokenizer();

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setMove(String move) {
        Parser parser = new Parser(tokenizer.tokenize(move));
        this.move = move.isEmpty() ? null : new BooleanTree(parser.parseExpression());
        System.out.println("a");
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
            return true; // No move defined, so the piece can stay in place
        }
        Map<String, Object> context = Map.of(
                "from", from,
                "to", to,
                "orientation", orientation,
                "board", board
        );
        return move.evaluate(context);
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



    // Calculates the expected positions based on the move components.

//
//    private void trimMoves(List<MoveComponent> moveComponents) {
//        LinkedList<MoveComponent> trimmedComponents = new LinkedList<>();
//        for (int i = 0; i < moveComponents.size(); i++) {
//            MoveComponent line1 = moveComponents.get(i);
//            for (int j = i + 1; j < moveComponents.size(); j++) {
//                MoveComponent line2 = moveComponents.get(j);
//                // Check if the moveComponents are in opposite directions
//                if (line1.getDirection().isOpposite(line2.getDirection())) {
//                    // Check if the moveComponents overlap
//                    if (line1.getFrom() == line2.getFrom()) {
//                        // Check if the second line is remains a line after trimming
//                        if (line2.getFrom() == line2.getTo()) {
//                            trimmedComponents.add(line2);
//                        } else {
//                            line2.setFrom(line2.getFrom() + 1);
//                        }
//                    }
//                }
//            }
//        }
//        // Remove moveComponents that are completely contained in other moveComponents
//        moveComponents.removeAll(trimmedComponents);
//    }
}
