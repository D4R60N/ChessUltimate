package palecek;

import palecek.utils.Orientation;

import java.util.ArrayList;
import java.util.List;

public class Move {

    private List<MoveComponent> moveComponents;

    public Move() {
        this.moveComponents = new ArrayList<>();
    }

    public List<MoveComponent> getMoveComponents() {
        return moveComponents;
    }

    public void setMoveComponents(List<MoveComponent> moveComponents) {
        this.moveComponents = moveComponents;
    }

    public void addMoveComponent(MoveComponent moveComponent) {
        this.moveComponents.add(moveComponent);
    }



}
