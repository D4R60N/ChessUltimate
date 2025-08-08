package palecek;

import com.fasterxml.jackson.databind.ObjectMapper;
import palecek.entity.Board;
import palecek.entity.Rule;
import palecek.entity.Space;
import palecek.utils.CSVReader;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try{
            Rule[] rules = mapper.readValue(Main.class.getResourceAsStream("/rules.json"), Rule[].class);
            Space[][] boardData = CSVReader.readCSVTo2DArray("src/main/resources/board2.csv");
            Board board = new Board(boardData);
            RuleResolver ruleResolver = new RuleResolver();
            Map<String, List<Rule>> ruleMap = new HashMap<>();
            for (Rule rule : rules) {
                String piece = rule.getPiece();
                if (!ruleMap.containsKey(piece)) {
                    ruleMap.put(piece, Collections.singletonList(rule));
                } else {
                    ruleMap.get(piece).add(rule);
                }
            }
            ruleResolver.setRules(ruleMap);
            GameState gameState = new GameState(0, 2, board, ruleResolver);
            System.out.println(board);
            System.out.println(gameState.getPlayerOnTurn());
            String input = "";
            while (!input.equals("exit")) {
                System.out.println("Enter your move (or 'exit' to quit): ");
                input = System.console().readLine();
                if (input.equals("exit")) {
                    break;
                }
                try {
                    gameState.makeMove(input);
                    System.out.println(gameState.getBoard());
                    System.out.println(gameState.getPlayerOnTurn());
                } catch (Exception e) {
                    System.err.println("Invalid move: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}