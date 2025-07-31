package palecek.move;

import palecek.utils.SpecialMovePosition;

public class SpecialMoveComponent {
    private SpecialMovePosition position;
    private int from, to;

    public SpecialMoveComponent(SpecialMovePosition position, int from, int to) {
        this.position = position;
        this.from = from;
        this.to = to;
    }

    public SpecialMovePosition getPosition() {
        return position;
    }

    public void setPosition(SpecialMovePosition position) {
        this.position = position;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
