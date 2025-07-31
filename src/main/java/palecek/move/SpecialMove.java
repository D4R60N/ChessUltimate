package palecek.move;

import palecek.utils.SpecialMovePosition;

public class SpecialMove {
    private boolean isRank;
    private SpecialMovePosition position;
    private int offset;
    private SpecialMoveComponent specialMoveComponent;

    public SpecialMove(boolean isRank, SpecialMovePosition position, int offset, SpecialMoveComponent specialMoveComponent) {
        this.isRank = isRank;
        this.position = position;
        this.offset = offset;
        this.specialMoveComponent = specialMoveComponent;
    }

    public static SpecialMove getFromString(String component, int offset, SpecialMoveComponent specialMoveComponent) {
        boolean isRank = component.startsWith("R");
        SpecialMovePosition position = SpecialMovePosition.fromString(component.substring(1, 2));
        return new SpecialMove(isRank, position, offset, specialMoveComponent);
    }

    public boolean isRank() {
        return isRank;
    }

    public SpecialMovePosition getPosition() {
        return position;
    }

    public int getOffset() {
        return offset;
    }

    public SpecialMoveComponent getSpecialMoveComponent() {
        return specialMoveComponent;
    }

    public void setSpecialMoveComponent(SpecialMoveComponent specialMoveComponent) {
        this.specialMoveComponent = specialMoveComponent;
    }
}
