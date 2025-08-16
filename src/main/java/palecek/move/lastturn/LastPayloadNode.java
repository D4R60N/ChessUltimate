package palecek.move.lastturn;

import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

import java.util.Set;

public class LastPayloadNode implements BooleanNode {

    private final Set<String> payloads;

    public LastPayloadNode(String value) {
        this.payloads = Set.of(value.substring(2).split(Separators.DIMENSION_SEPARATOR));
        if (this.payloads.isEmpty()) {
            throw new IllegalArgumentException("Payload cannot be empty");
        }
    }

    @Override
    public boolean evaluate(java.util.Map<String, Object> context) {
        String payload = (String) context.get("payload");
        if (payload == null || payload.isEmpty()) {
            throw new IllegalArgumentException("Payload cannot be null or empty");
        }
        String[] parts = payload.split(Separators.DIMENSION_SEPARATOR);
        for (String part : parts) {
            if (this.payloads.contains(part.trim())) {
                return true;
            }
        }
        return false;
    }
}
