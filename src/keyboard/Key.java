package src.keyboard;

import javax.swing.*;
import java.awt.*;

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

