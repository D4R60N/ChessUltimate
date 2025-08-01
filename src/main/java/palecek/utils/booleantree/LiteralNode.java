package palecek.utils.booleantree;

import java.util.Map;

public abstract class LiteralNode implements BooleanNode{
    private String value;

    public LiteralNode(String value) {
        this.value = value;
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        return true;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
