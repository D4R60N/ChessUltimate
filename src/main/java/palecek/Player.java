package palecek;

import palecek.utils.Orientation;

public class Player {
    private Orientation orientation;
    private int number;

    public Player(Orientation orientation, int number) {
        this.orientation = orientation;
        this.number = number;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
