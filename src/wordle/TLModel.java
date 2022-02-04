package src.wordle;

import src.game.Game;
import src.keyboard.Key;
import src.keyboard.Keyboard;

import java.util.Observable;
import java.util.Random;

public class TLModel extends Observable {

    private Keyboard keyboard;
    private Game game;

    public Keyboard getKeyboard() { return keyboard; }
    public Game getGame() { return game; }

    public void initialise() {
        keyboard = new Keyboard();
        game = new Game();
    }

    public void keyPressed(Key key) {
        keyboard.KeyPressed(key, game);
        setChanged();
        notifyObservers();
    }

    public TLModel() {
        initialise();
        setChanged();
        notifyObservers();
    }

    private void startGame() {
        // TODO
    }
}
