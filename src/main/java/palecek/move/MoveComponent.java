package palecek.move;

import palecek.utils.Direction;

public class MoveComponent {

    private Direction direction;
    private int from;
    private int to;
    private boolean isRepeting;
    private int spacing;

    public MoveComponent(Direction direction, int from, int to, boolean isRepeting, int spacing) {
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.isRepeting = isRepeting;
        this.spacing = spacing;
    }

    public MoveComponent(Direction direction, int from, int to) {
        this(direction, from, to, false, 0);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public boolean isRepeting() {
        return isRepeting;
    }
    public void setRepeting(boolean repeting) {
        isRepeting = repeting;
    }

    public int getSpacing() {
        return spacing;
    }
}
