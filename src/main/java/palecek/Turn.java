package palecek;

import palecek.entity.Position;

public class Turn {
    private Position from;
    private Position to;
    private int playerIndex;
    private int turnNumber;
    private String payload;
    private String piece;

    public Turn(Position from, Position to, int playerIndex, int turnNumber, String payload, String piece) {
        this.from = from;
        this.to = to;
        this.playerIndex = playerIndex;
        this.turnNumber = turnNumber;
        this.payload = payload;
        this.piece = piece;
    }
    public Turn() {
        this.from = null;
        this.to = null;
        this.playerIndex = -1;
        this.turnNumber = -1;
        this.payload = null;
        this.piece = null;
    }

    public Position getFrom() {
        return from;
    }

    public void setFrom(Position from) {
        this.from = from;
    }

    public Position getTo() {
        return to;
    }

    public void setTo(Position to) {
        this.to = to;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }
}
