package palecek;

import palecek.entity.Board;
import palecek.entity.Position;
import palecek.utils.Orientation;
import palecek.utils.Separators;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private int playerOnTurn;
    private int numberOfPlayers; // Assuming a two-player game
    private Board board;
    private RuleResolver ruleResolver;
    private List<Player> players;
    private int turnCount;
    private Turn lastTurn;

    public GameState(int playerOnTurn, int numberOfPlayers, Board board, RuleResolver ruleResolver) {
        this.playerOnTurn = playerOnTurn;
        this.numberOfPlayers = numberOfPlayers;
        this.board = board;
        this.ruleResolver = ruleResolver;
        this.turnCount = 0;
        this.lastTurn = new Turn();
        if (numberOfPlayers <= 0) {
            throw new IllegalArgumentException("Number of players must be greater than 0");
        }
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        if (ruleResolver == null) {
            throw new IllegalArgumentException("RuleResolver cannot be null");
        }
        if (playerOnTurn < 0 || playerOnTurn >= numberOfPlayers) {
            throw new IllegalArgumentException("Invalid player index");
        }
        this.players = new ArrayList<>(numberOfPlayers);
        for(int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(Orientation.fromValue(i % Orientation.values().length), i));
        }
    }

    public int getPlayerOnTurn() {
        return playerOnTurn;
    }

    public void setPlayerOnTurn(int playerOnTurn) {
        this.playerOnTurn = playerOnTurn;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer() {
        if (playerOnTurn < 0 || playerOnTurn >= players.size()) {
            throw new IllegalArgumentException("Invalid player index");
        }
        return players.get(playerOnTurn);
    }

    public int getTurn() {
        return turnCount;
    }

    public void setLastTurn(Turn lastTurn) {
        if (lastTurn == null) {
            throw new IllegalArgumentException("Last turn cannot be null");
        }
        this.lastTurn = lastTurn;
    }

    public Turn getLastTurn() {
        return lastTurn;
    }

    public boolean evaluateTurn() {
        boolean result = false;
        for (Player player : players) {
            if (ruleResolver.resolveEndCondition( player.getNumber(), this)) {
                System.out.println("Player " + player.getNumber() + " has lost the game!");
                result = true;
            }
        }
        if (!result && playerOnTurn == players.size() - 1) {
            turnCount++;
        }
        return result;
    }

    public void makeMove(String move) {
        String[] split = move.split(Separators.SPACE_SEPARATOR);
        if (split.length < 2 || split.length > 3) {
            throw new IllegalArgumentException("Invalid move format");
        }
        Position from = new Position(split[0]);
        Position to = new Position(split[1]);
        String payload = split.length == 3 ? split[2] : null;
        if(ruleResolver.resolveMove(from, to, payload, this)) {
            playerOnTurn = ((playerOnTurn+1) % numberOfPlayers);
        }
    }
}
