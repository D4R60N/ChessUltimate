package palecek.move.condition;

import palecek.Player;
import palecek.entity.Board;
import palecek.entity.Position;
import palecek.entity.Space;
import palecek.utils.booleantree.BooleanNode;
import palecek.utils.booleantree.BooleanTree;

import java.util.Map;
import java.util.Set;

public class PlayerNode implements BooleanNode {
    private PlayerType playerType;

    public PlayerNode(PlayerType playerType) {
        this.playerType = playerType;
    }

    @Override
    public boolean evaluate(Map<String, Object> context) {
        Board board = (Board) context.get("board");
        @SuppressWarnings("unchecked")
        Set<Position> expectedPositions = (Set<Position>) context.get("positions");
        Player player = (Player) context.get("player");

        if (board == null || expectedPositions == null || player == null) {
            return false;
        }

        for (Position position : expectedPositions) {
            Space space = board.getFromPosition(position);
            if (space != null) {
                if(space.getPlayer() == player.getNumber()) {
                    return playerType == PlayerType.ALLY;
                } else if (space.getPlayer() != -1) {
                    return playerType == PlayerType.ENEMY;
                }
            }
        }
        return false;
    }

    public enum PlayerType {
        ALLY, ENEMY
    }
}
