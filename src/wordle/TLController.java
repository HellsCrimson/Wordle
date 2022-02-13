package src.wordle;

import src.keyboard.Key;
import src.keyboard.States;

public class TLController {

    private TLModel model;
    private TLView view;

    public TLController(TLModel model) {
        this.model = model;
    }

    public void setView(TLView view) {
        this.view = view;
    }

    public void keyPressed(Key key) {
        model.keyPressed(key);
    }

    public void keyKeyboardPressed(int characterCode) {
        if (characterCode == 8 || characterCode == 10) { // 8 DELETE; 10 ENTER
            findSpecialKey(characterCode);
        } else {
            findLetter((char)characterCode);
        }
        view.displayWords();
    }

    private void findSpecialKey(int keyCode) {
        String toFind;
        if (keyCode == 8)
            toFind = "DELETE";
        else
            toFind = "ENTER";
        for (Key[] row : model.getKeyboard().getKeys()) {
            for (Key key : row) {
                if (key != null && key.getState() == States.SPECIAL && key.getLetter().equals(toFind)) {
                    model.keyPressed(key);
                }
            }
        }
    }

    private void findLetter(char character) {
        for (Key[] row : model.getKeyboard().getKeys()) {
            for (Key key : row) {
                if (key != null && key.getLetter().equals(String.valueOf(character).toUpperCase()) && key.getState() != States.WRONG) {
                    model.keyPressed(key);
                }
            }
        }
    }

    public void initialise() {
        model.initialise();
    }

    public void changeWord() {
        // model.changeWord();
    }
}
