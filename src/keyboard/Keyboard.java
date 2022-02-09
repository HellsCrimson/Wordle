package src.keyboard;

import src.game.Game;
import src.wordle.TLModel;

import javax.swing.*;
import java.util.Objects;

public class Keyboard {

    private TLModel model;

    public boolean won = false;
    public boolean needLetter = false;
    public boolean notValid = false;

    public boolean needBeValid = true;

    private Key[][] keys;
    private String keysAvalable[][] = new String[][] {
        {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
        {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
        {"ENTER", "Z", "X", "C", "V", "B", "N", "M", "DELETE"}
    };

    public Key[][] getKeys() {
        return keys;
    }

    public String[][] getKeysAvalable() {
        return keysAvalable;
    }

    private void initKeyboard() {
        keys = new Key[keysAvalable.length][getMaxRowKeys()];
        for (int y = 0; y < keysAvalable.length; y++) {
            for (int x = 0; x < keysAvalable[y].length; x++) {
                keys[y][x] = new Key(keysAvalable[y][x]);
                if (keys[y][x].getLetter() == "ENTER" || keys[y][x].getLetter() == "DELETE") {
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

    public void KeyPressed(Key key, Game game) {
        if (Objects.equals(key.getLetter(), "ENTER")) {
            String userWord = game.getCurrentUserWord();
            if (userWord.length() == 5) {
                if (game.isValidWord(userWord) || !needBeValid) {
                    if (game.isCorrectWord(userWord)) {
                        // You won
                        // TODO put all letter in green
                        game.setCurrentUserWord("");
                        won = true;
                    } else {
                        String[] correctLetters = game.correctLetters(userWord);
                        for (String correctLetter : correctLetters) {
                            changeKeyState(correctLetter, States.WRONG_PLACE);
                            // TODO put correct letter in YELLOW
                        }
                        correctLetters = game.correctPlaceLetters(userWord);
                        for (String correctLetter : correctLetters) {
                            changeKeyState(correctLetter, States.RIGHT);
                            // TODO put correct letter in GREEN
                        }
                        for (int i = 0; i < userWord.length(); i++) {
                            for (Key[] rowKey : keys) {
                                for (Key currKey : rowKey) {
                                    if (currKey != null && Objects.equals(currKey.getLetter(), String.valueOf(userWord.charAt(i))) && currKey.getState() == States.NOT_USED) {
                                        changeKeyState(String.valueOf(userWord.charAt(i)), States.WRONG);
                                        // TODO put correct letter in DARK_GREY
                                    }
                                }
                            }
                        }
                        game.setCurrentUserWord("");
                    }

                    if (game.indexBuffer < 5)
                        game.indexBuffer += 1;
                    else {
                        // TODO lost
                        game.changeWordToFind();
                        game.resetWordBuffer();
                        resetKeyboard();
                        model.restartGame();
                        game.indexBuffer = 0;
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
            String userWord = game.getCurrentUserWord();
            if (userWord.length() > 0) {
                game.setCurrentUserWord(userWord.substring(0, userWord.length() - 1));
                game.wordsBuffer[game.indexBuffer] = game.getCurrentUserWord();
            }
        } else {
            if (game.getCurrentUserWord().length() < 5) {
                game.wordsBuffer[game.indexBuffer] = game.getCurrentUserWord() + key.getLetter();
                game.addLetter(key.getLetter());
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
