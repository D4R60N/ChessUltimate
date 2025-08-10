package palecek.action;

import palecek.Player;
import palecek.entity.Board;
import palecek.entity.Position;
import palecek.utils.Orientation;

public interface Action {

    void performAction(Position from, Position to, Orientation orientation, Board board, Player player, int turn, String payload);

}
