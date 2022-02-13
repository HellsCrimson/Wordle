package src.wordle;

import src.game.Game;
import src.keyboard.Key;
import src.keyboard.Keyboard;
import src.keyboard.KeyboardListener;

import java.util.Observable;
import java.util.Random;

public class TLModel extends Observable {

    private Keyboard keyboard;
    private Game game;

    public Keyboard getKeyboard() { return keyboard; }
    public Game getGame() { return game; }

    public void initialise() {
        keyboard = new Keyboard(this);
        game = new Game();
    }

    public void keyPressed(Key key) {
        keyboard.KeyPressed(key, game);
        setChanged();
        notifyObservers();
    }

    public void restartGame() {
        // TODO restart the game in the model and view
        setChanged();
        notifyObservers();
    }

    public TLModel() {
        initialise();
        setChanged();
        notifyObservers();
    }
}
