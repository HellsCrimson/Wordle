package wordle;

import keyboard.Keyboard;

import java.util.Observable;

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
