package palecek.move.lastturn;

import palecek.Turn;
import palecek.utils.Regexes;
import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

import java.util.*;

public class LastTurnNode implements BooleanNode {
    private final List<BooleanNode> child;

    public LastTurnNode(String value) {
        this.child = new ArrayList<>();
        String[] subParts = value.split(Separators.SEMICOLON_SEPARATOR);
        boolean hasMove = false, hasSpecialMove = false, hasPayload = false, hasPiece = false;
        for (String subPart : subParts) {
            if (subPart.matches(Regexes.MOVE_REGEX)) {
                if (hasMove) {
                    throw new IllegalArgumentException("Multiple move conditions are not allowed in last turn condition: " + subPart);
                }
                hasMove = true;
                this.child.add(new LastMoveNode(subPart));
            } else if (subPart.matches(Regexes.SPECIAL_MOVE_REGEX)) {
                if (hasSpecialMove) {
                    throw new IllegalArgumentException("Multiple special move conditions are not allowed in last turn condition: " + subPart);
                }
                hasSpecialMove = true;
                this.child.add(new LastSpecialMoveNode(subPart));
            } else if (subPart.matches(Regexes.PAYLOAD_REGEX)) {
                if (hasPayload) {
                    throw new IllegalArgumentException("Multiple payload conditions are not allowed in last turn condition: " + subPart);
                }
                hasPayload = true;
                this.child.add(new LastPayloadNode(subPart));
            } else if (subPart.matches(Regexes.PIECE_REGEX)) {
                if (hasPiece) {
                    throw new IllegalArgumentException("Multiple piece conditions are not allowed in last turn condition: " + subPart);
                }
                hasPiece = true;
                this.child.add(new LastPieceNode(subPart));
            } else {
                throw new IllegalArgumentException("Invalid last turn condition part: " + subPart);
            }
        }
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Turn lastTurn = (Turn) context.get("lastTurn");

        if (lastTurn == null) {
            throw new IllegalArgumentException("Last turn is not available in the context");
        } else if (lastTurn.getTurnNumber() == -1) {
            return false;
        }

        Map<String, Object> newContext = new HashMap<>(context);
        newContext.put("from", lastTurn.getFrom());
        newContext.put("to", lastTurn.getTo());
        newContext.put("piece", lastTurn.getPiece());
        newContext.put("player", lastTurn.getPlayerIndex());
        newContext.put("turn", lastTurn.getTurnNumber());
        newContext.put("payload", lastTurn.getPayload());

        return true;
    }
}
