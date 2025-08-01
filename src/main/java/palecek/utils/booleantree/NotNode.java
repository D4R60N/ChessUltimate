package palecek.utils.booleantree;

import java.util.Map;

public class NotNode implements BooleanNode {
    private BooleanNode child;

    public NotNode(BooleanNode child) {
        this.child = child;
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        return !child.evaluate(context);
    }

    public BooleanNode getChild() {
        return child;
    }

    public void setChild(BooleanNode child) {
        this.child = child;
    }
}
