package src.keyboard;

import src.wordle.TLModel;

import java.util.Objects;

public class Keyboard {

    private TLModel model;

    public boolean won = false;
    public boolean lost = false;
    public boolean needLetter = false;
    public boolean notValid = false;

    public boolean needBeValid = true;

    private Key[][] keys;
    private final String[][] keysAvalable = new String[][] {
        {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
        {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
        {"ENTER", "Z", "X", "C", "V", "B", "N", "M", "DELETE"}
    };

    public Key[][] getKeys() {
        return keys;
    }

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

    public int getMaxRowKeys() {
        int max = 0;
        for (int i = 0; i < keysAvalable.length; i++) {
            if (keysAvalable[i].length > max) {
                max = keysAvalable[i].length;
            }
        }
        return max;
    }

    public void KeyPressed(Key key) {
        if (Objects.equals(key.getLetter(), "ENTER")) {
            String userWord = model.getCurrentUserWord();
            if (userWord.length() == 5) {
                if (model.isValidWord(userWord) || !needBeValid) {
                    String[] correctLetters = model.correctLetters(userWord);
                    for (String correctLetter : correctLetters) {
                        changeKeyState(correctLetter, States.WRONG_PLACE);
                    }
                    correctLetters = model.correctPlaceLetters(userWord);
                    for (String correctLetter : correctLetters) {
                        changeKeyState(correctLetter, States.RIGHT);
                    }
                    for (int i = 0; i < userWord.length(); i++) {
                        for (Key[] rowKey : keys) {
                            for (Key currKey : rowKey) {
                                if (currKey != null && currKey.getLetter().equals(String.valueOf(userWord.charAt(i))) && currKey.getState() == States.NOT_USED) {
                                    changeKeyState(String.valueOf(userWord.charAt(i)), States.WRONG);
                                }
                            }
                        }
                    }
                    if (model.isCorrectWord(userWord))
                        won = true;

                    model.setCurrentUserWord("");
                    model.changeEnterPressed();

                    if (model.indexBuffer <= 5)
                        model.indexBuffer += 1;
                    else if (!won) {
                        lost = true;
                        model.changeWordToFind();
                        model.resetWordBuffer();
                        resetKeyboard();
                        model.restartGame();
                        model.indexBuffer = 0;
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
                model.wordsBuffer[model.indexBuffer] = model.getCurrentUserWord();
            }
        } else {
            if (model.getCurrentUserWord().length() < 5) {
                model.wordsBuffer[model.indexBuffer] = model.getCurrentUserWord() + key.getLetter();
                model.addLetter(key.getLetter());
            }
        }
    }

    private void changeKeyState(String character, States newState) {
        if (character == null)
            return;
        for (Key[] rowKey : keys) {
            for (Key key : rowKey) {
                if (key != null && key.getLetter().equals(character)) {
                    if (key.getState() != States.RIGHT)
                        key.changeState(newState);
                    return;
                }
            }
        }
    }

    private void resetKeyboard() {
        for (Key[] row : keys) {
            for (Key key : row) {
                if (key != null && key.getState() != States.SPECIAL) {
                    key.changeState(States.NOT_USED);
                }
            }
        }
    }

    public Keyboard(TLModel model) {
        initKeyboard();
        this.model = model;
    }
}
