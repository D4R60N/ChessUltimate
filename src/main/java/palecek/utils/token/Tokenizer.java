package palecek.utils.token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Tokenizer {
    private final String andSymbol = "&";
    private final String orSymbol = "|";
    private final String xorSymbol = "^";
    private final String notSymbol = "!";
    private final String regex;

    public Tokenizer() {
        String escapedAnd = Pattern.quote(andSymbol);
        String escapedOr = Pattern.quote(orSymbol);
        String escapedXor = Pattern.quote(xorSymbol);
        String escapedNot = Pattern.quote(notSymbol);
        regex = "(?=" + escapedAnd + "|" + escapedOr + "|" + escapedXor + "|" + escapedNot + "|\\(|\\))"
                + "|(?<=" + escapedAnd + "|" + escapedOr + "|" + escapedXor + "|" + escapedNot + "|\\(|\\))";
    }


    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        String[] parts = input.split(regex);
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;
            switch (part.toUpperCase()) {
                case andSymbol: tokens.add(new Token(TokenType.AND, part)); break;
                case orSymbol: tokens.add(new Token(TokenType.OR, part)); break;
                case xorSymbol: tokens.add(new Token(TokenType.XOR, part)); break;
                case notSymbol: tokens.add(new Token(TokenType.NOT, part)); break;
                case "(": tokens.add(new Token(TokenType.LPAREN, part)); break;
                case ")": tokens.add(new Token(TokenType.RPAREN, part)); break;
                default: tokens.add(new Token(TokenType.LITERAL, part)); break;
            }
        }
        return tokens;
    }
}
