package palecek.action;

import palecek.entity.Ideology;
import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanTree;
import palecek.utils.token.Parser;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private List<IAction> action;
    private BooleanTree actionCondition;

    public List<IAction> getAction() {
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
}
