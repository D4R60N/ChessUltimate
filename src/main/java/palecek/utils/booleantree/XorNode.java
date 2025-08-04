package palecek.utils.booleantree;

import java.util.Map;

public class XorNode implements BooleanNode {
    private BooleanNode left;
    private BooleanNode right;

    public XorNode(BooleanNode left, BooleanNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        return left.evaluate(context) ^ right.evaluate(context);
    }

    public BooleanNode getLeft() {
        return left;
    }

    public void setLeft(BooleanNode left) {
        this.left = left;
    }

    public BooleanNode getRight() {
        return right;
    }

    public void setRight(BooleanNode right) {
        this.right = right;
    }
}
