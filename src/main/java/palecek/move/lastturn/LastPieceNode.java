package palecek.move.lastturn;

import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

import java.util.HashSet;
import java.util.Set;

public class LastPieceNode implements BooleanNode {

    private final Set<String> pieces;

    public LastPieceNode(String value) {
        this.pieces = new HashSet<>();
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Piece name cannot be null or empty");
        }
        String[] parts = value.split(Separators.DIMENSION_SEPARATOR);
        for (String part : parts) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty()) {
                this.pieces.add(trimmedPart);
            }
        }
    }

    @Override
    public boolean evaluate(java.util.Map<String, Object> context) {
        String piece = (String) context.get("piece");
        return piece != null && pieces.contains(piece);
    }
}
