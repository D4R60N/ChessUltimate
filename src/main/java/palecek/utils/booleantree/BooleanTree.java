package palecek.utils.booleantree;

import java.util.Map;

public class BooleanTree {
    private BooleanNode root;

    public BooleanTree(BooleanNode root) {
        this.root = root;
    }

    public boolean evaluate(Map<String, Object> context) {
        if (root == null) {
            throw new IllegalStateException("The tree is empty.");
        }
        return root.evaluate(context);
    }

    public BooleanNode getRoot() {
        return root;
    }

    public void setRoot(BooleanNode root) {
        this.root = root;
    }
}
