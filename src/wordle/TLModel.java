package src.wordle;

import src.keyboard.Keyboard;

import java.util.Observable;
import java.util.Random;

public class TLModel extends Observable {

    private Keyboard keyboard;

    private Random rng;
    private String wordToFind;
    private long seed;

    public Keyboard getKeyboard() { return keyboard; }

    public void initialise() {
        keyboard = new Keyboard();
        setChanged();
        notifyObservers();
    }

    public TLModel() {
        initialise();
    }

    private void SelectWord() {
        // TODO
        if (seed == null) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }
    }
}
