package src.wordle;

import src.game.Game;
import src.keyboard.Keyboard;

import java.util.Observable;
import java.util.Random;

public class TLModel extends Observable {

    private Keyboard keyboard;
    private Game game;

    private int nbTries; // To delete ?

    public Keyboard getKeyboard() { return keyboard; }

    public void initialise() {
        keyboard = new Keyboard();
        game = new Game();
        setChanged();
        notifyObservers();
    }

    public TLModel() {
        initialise();
    }

    private void startGame() {
        // TODO
    }
}
