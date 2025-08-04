package palecek.utils.token;

import palecek.move.MoveLiteralNodeParser;
import palecek.utils.booleantree.*;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public BooleanNode parseExpression() {
        return parseOr();
    }

    private BooleanNode parseOr() {
        BooleanNode node = parseXor();
        while (match(TokenType.OR)) {
            node = new OrNode(node, parseAnd());
        }
        return node;
    }

    private BooleanNode parseXor() {
        BooleanNode node = parseAnd();
        while (match(TokenType.XOR)) {
            node = new XorNode(node, parseOr());
        }
        return node;
    }

    private BooleanNode parseAnd() {
        BooleanNode node = parseNot();
        while (match(TokenType.AND)) {
            node = new AndNode(node, parseNot());
        }
        return node;
    }

    private BooleanNode parseNot() {
        if (match(TokenType.NOT)) {
            return new NotNode(parseNot());
        }
        return parsePrimary();
    }

    private BooleanNode parsePrimary() {
        if (match(TokenType.LPAREN)) {
            BooleanNode node = parseExpression();
            if (!match(TokenType.RPAREN)) throw new RuntimeException("Expected ')'");
            return node;
        }
        if (match(TokenType.LITERAL)) {
            return MoveLiteralNodeParser.parse(prev().value);
        }
        throw new RuntimeException("Unexpected token: " + peek().value);
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            pos++;
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        return pos < tokens.size() && tokens.get(pos).type == type;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token prev() {
        return tokens.get(pos - 1);
    }
}
