package palecek;

import com.fasterxml.jackson.databind.ObjectMapper;
import palecek.entity.Board;
import palecek.entity.Rule;
import palecek.utils.CSVReader;

import java.util.Collections;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try{
            Rule rule = mapper.readValue(Main.class.getResourceAsStream("/rules.json"), Rule.class);
            System.out.println(rule);
            String[][] boardData = CSVReader.readCSVTo2DArray("src/main/resources/board.csv");
            Board board = new Board(boardData);
            RuleResolver ruleResolver = new RuleResolver();
            ruleResolver.setRules(Map.of("pawn", Collections.singletonList(rule)));
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