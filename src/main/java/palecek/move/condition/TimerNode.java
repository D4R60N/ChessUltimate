package palecek.move.condition;

import palecek.utils.Separators;
import palecek.utils.booleantree.BooleanNode;

import java.util.Map;

public class TimerNode implements BooleanNode {
    private int turn;
    private boolean isRepeting;
    private int offset;

    public TimerNode(String value) {
        String[] split = value.split(Separators.DIRECTION_SEPARATOR);
        if (split.length == 1) {
            this.turn = Integer.parseInt(split[0]);
            this.isRepeting = false;
            this.offset = 0;
        } else if (split.length == 2) {
            this.turn = Integer.parseInt(split[0]);
            this.isRepeting = true;
            this.offset = Integer.parseInt(split[1]);
        } else {
            throw new IllegalArgumentException("Invalid TimerNode value: " + value);
        }
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        int currentTurn = (int) context.get("turn");

        if(currentTurn < turn) {
            return false;
        }
        if (isRepeting) {
            int turnDifference = currentTurn - turn;
            return turnDifference % offset == 0;
        }
        return currentTurn == turn;
    }
}
