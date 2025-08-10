package palecek.action;

import palecek.Player;
import palecek.entity.Board;
import palecek.entity.Position;
import palecek.utils.Orientation;

public interface Action {

    /**
     * Performs the action based on the given parameters. Returns true if action overrides the turn,
     * @param from
     * @param to
     * @param orientation
     * @param board
     * @param player
     * @param turn
     * @param payload
     * @return
     */
    void performAction(Position from, Position to, Orientation orientation, Board board, Player player, int turn, String payload);

}
