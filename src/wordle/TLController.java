package wordle;

import keyboard.Key;
import keyboard.States;

public class TLController {

    private TLModel model;
    private TLView view;

    public TLController(TLModel model) {
        this.model = model;
    }

    public void setView(TLView view) {
        this.view = view;
    }

    /**
     * Send key press for the visual keyboard
     */
    public void keyPressed(Key key) {
        model.keyPressed(key);
    }

    /**
     * Send key press for the physical keyboard
     */
    public void keyKeyboardPressed(int characterCode) {
        if (characterCode == 8 || characterCode == 10) { // 8 DELETE; 10 ENTER
            findSpecialKey(characterCode);
        } else {
            findLetter((char) characterCode);
        }
        view.getDisplayPanel().displayWords();
    }

    /**
     * Only in case of special keys
     * Search the keyboard (where the states of the keys are) for the correct key
     * and sent the key press
     */
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

    /**
     * For any key other than the special keys
     * Search the keyboard (where the states of the keys are) for the correct key
     * and sent the key press
     */
    private void findLetter(char character) {
        for (Key[] row : model.getKeyboard().getKeys()) {
            for (Key key : row) {
                if (key != null && key.getLetter().equals(String.valueOf(character).toUpperCase()) && key.getState() != States.WRONG) {
                    model.keyPressed(key);
                }
            }
        }
    }

    public void restartGame() {
        model.restartGame();
    }
}
