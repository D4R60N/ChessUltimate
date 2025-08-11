package palecek.entity;

import palecek.RuleResolver;
import palecek.utils.booleantree.BooleanTree;
import palecek.utils.token.Parser;
import palecek.utils.token.Tokenizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ideology {
    private String name;
    private String description;
    private BooleanTree endingCondition;
    private Map<String, List<Rule>> rules;

    public static Tokenizer tokenizer = new Tokenizer();

    public Ideology() {
        // Default constructor for serialization/deserialization
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BooleanTree getEndingCondition() {
        return endingCondition;
    }

    public void setEndingCondition(String endingCondition) {
        Parser parser = new Parser(tokenizer.tokenize(endingCondition, true), Parser.ParserType.END_CONDITION);
        this.endingCondition = endingCondition.isEmpty() ? null : new BooleanTree(parser.parseExpression());
    }

    public Map<String, List<Rule>> getRules() {
        return rules;
    }

    public void setRules(Rule[] rules) {
        Map<String, List<Rule>> ruleMap = new HashMap<>();
        for (Rule rule : rules) {
            String piece = rule.getPiece();
            if (!ruleMap.containsKey(piece)) {
                ruleMap.put(piece, Collections.singletonList(rule));
            } else {
                ruleMap.get(piece).add(rule);
            }
        }
        this.rules = ruleMap;
    }
}
