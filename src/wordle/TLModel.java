package src.wordle;

import src.keyboard.Keyboard;

import java.util.Observable;
import java.util.Random;

public class TLModel extends Observable {

    private Keyboard keyboard;

    public Keyboard getKeyboard() { return keyboard; }

    public void initialise() {
        keyboard = new Keyboard();
        setChanged();
        notifyObservers();
    }

    public TLModel() {
        initialise();
    }
}
