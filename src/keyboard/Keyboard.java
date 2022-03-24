package keyboard;

import wordle.TLModel;

public class Keyboard {

    private final TLModel model;
    private final String[][] keysAvalable = new String[][]{
            {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
            {"ENTER", "Z", "X", "C", "V", "B", "N", "M", "DELETE"}
    };
    public boolean needLetter = false;
    public boolean notValid = false;
    private Key[][] keys;
    ;

    public Keyboard(TLModel model) {
        initKeyboard();
        this.model = model;
    }

    public Key[][] getKeys() {
        return keys;
    }

    /**
     * Put all the keys in the correct state
     */
    private void initKeyboard() {
        keys = new Key[keysAvalable.length][getMaxRowKeys()];
        for (int y = 0; y < keysAvalable.length; y++) {
            for (int x = 0; x < keysAvalable[y].length; x++) {
                keys[y][x] = new Key(keysAvalable[y][x]);
                if (keys[y][x].getLetter().equals("ENTER") || keys[y][x].getLetter().equals("DELETE")) {
                    keys[y][x].changeState(States.SPECIAL);
                }
            }
        }
    }

    /**
     * Return the number of keys on the row with the most keys
     */
    public int getMaxRowKeys() {
        int max = 0;
        for (String[] strings : keysAvalable) {
            if (strings.length > max)
                max = strings.length;
        }
        return max;
    }

    public void KeyPressed(Key key) {
        if (key.getLetter().equals("ENTER")) {
            String userWord = model.getCurrentUserWord();
            if (userWord.length() == 5) {
                if (model.isValidWord(userWord) || !model.getNeedBeValid()) {
                    model.correctLetters(userWord);
                    String[] correctLetters = model.getCorrectLetters();
                    String[] correctPlaceLetters = model.getCorrectPlaceLetters();
                    for (String correctLetter : correctLetters) {
                        changeKeyState(correctLetter, States.WRONG_PLACE);
                    }
                    for (String correctLetter : correctPlaceLetters) {
                        changeKeyState(correctLetter, States.RIGHT);
                    }

                    setRemainingWrongs(correctLetters, correctPlaceLetters);

                    if (model.isCorrectWord(userWord)) {
                        model.setWon(true);
                        model.getStatWriter().addData(true, model.indexBuffer);
                    }

                    model.setCurrentUserWord("");
                    model.changeEnterPressed();
                    model.indexBuffer += 1;

                    if (!model.getWon() && model.indexBuffer == 6) {
                        model.getStatWriter().addData(false, -1);
                        model.setLost(true);
                    }
                } else {
                    // not a valid word
                    notValid = true;
                }
            } else {
                // need to use 5 letters
                needLetter = true;
            }
        } else if (key.getLetter().equals("DELETE")) {
            String userWord = model.getCurrentUserWord();
            if (userWord.length() > 0) {
                model.setCurrentUserWord(userWord.substring(0, userWord.length() - 1));
                model.getWordsBuffer()[model.indexBuffer] = model.getCurrentUserWord();
            }
        } else {
            if (model.getCurrentUserWord().length() < 5) {
                model.getWordsBuffer()[model.indexBuffer] = model.getCurrentUserWord() + key.getLetter();
                model.addLetter(key.getLetter());
            }
        }
    }

    private void setRemainingWrongs(String[] correctLetters, String[] correctPlaceLetters) {
        for (int i = 0; i < model.getCurrentUserWord().length(); i++) {
            String currLetter = String.valueOf(model.getCurrentUserWord().charAt(i));
            if (valueNotExist(correctLetters, currLetter) && valueNotExist(correctPlaceLetters, currLetter))
                changeKeyState(currLetter, States.WRONG);
        }
    }

    private boolean valueNotExist(String[] arr, String value) {
        for (String letter : arr) {
            if (letter != null && letter.equals(value))
                return false;
        }
        return true;
    }

    private void changeKeyState(String character, States newState) {
        if (character == null)
            return;
        for (Key[] rowKey : keys) {
            for (Key key : rowKey) {
                if (key != null && key.getLetter().equals(character)) {
                    if (key.getState() != States.SPECIAL)
                        key.changeState(newState);
                    return;
                }
            }
        }
    }

    /**
     * Put the keyboard like when it was created
     */
    public void resetKeyboard() {
        for (Key[] row : keys) {
            for (Key key : row) {
                if (key != null && key.getState() != States.SPECIAL) {
                    key.changeState(States.NOT_USED);
                }
            }
        }
    }
}
