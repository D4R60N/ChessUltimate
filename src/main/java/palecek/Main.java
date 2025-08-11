package palecek;

import com.fasterxml.jackson.databind.ObjectMapper;
import palecek.entity.Board;
import palecek.entity.Ideology;
import palecek.entity.Rule;
import palecek.entity.Space;
import palecek.utils.CSVReader;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try{
            Ideology ideology = mapper.readValue(Main.class.getResourceAsStream("/rules.json"), Ideology.class);
            Space[][] boardData = CSVReader.readCSVTo2DArray("src/main/resources/board2.csv");
            Board board = new Board(boardData);
            RuleResolver ruleResolver = new RuleResolver();
            ruleResolver.addRules(ideology);
            ruleResolver.addRules(ideology);
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
                if (gameState.evaluateTurn()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}