package src.keyboard;

public class Key {

    private final String letter;
    private States state;

    public String getLetter() {
        return letter;
    }

    public States getState() {
        return state;
    }

    public Key(String letter) {
        this.letter = letter;
        state = States.NOT_USED;
    }

    public void changeState(States newState) {
        state = newState;
    }
}

enum States {
    NOT_USED,
    USED_WRONG,
    WRONG_PLACE,
    RIGHT,
    SPECIAL;
}
