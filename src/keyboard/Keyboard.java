package src.keyboard;

import src.game.Game;

import java.util.Objects;

public class Keyboard {

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
            String userword = game.getCurrentUserWord();
            if (userword.length() == 5) {
                if (game.isValidWord(userword)) {
                    if (game.indexBuffer < 6)
                        game.indexBuffer += 1;

                    if (game.isCorrectWord(userword)) {
                        // TODO You won
                        game.setCurrentUserWord("");
                        System.out.println("You won");
                    } else {
                        String[] correctLetters = game.correctLetters(userword);
                        for (String correctLetter : correctLetters) {
                            changeKeyState(correctLetter, States.WRONG_PLACE);
                        }
                        correctLetters = game.correctPlaceLetters(userword);
                        for (String correctLetter : correctLetters) {
                            changeKeyState(correctLetter, States.RIGHT);
                        }
                        for (int i = 0; i < userword.length(); i++) {
                            for (Key[] rowKey : keys) {
                                for (Key currKey : rowKey) {
                                    if (currKey != null && Objects.equals(currKey.getLetter(), String.valueOf(userword.charAt(i))) && currKey.getState() == States.NOT_USED) {
                                        changeKeyState(String.valueOf(userword.charAt(i)), States.WRONG);
                                    }
                                }
                            }
                        }
                        game.setCurrentUserWord("");
                    }
                } else {
                    // TODO not a valid word
                    System.out.println("Not a valid word");
                }
            } else {
                // TODO you need to use 5 letters
                System.out.println("You need to use 5 letters");
            }
        } else if (Objects.equals(key.getLetter(), "DELETE")) {
            String userword = game.getCurrentUserWord();
            if (userword.length() > 0) {
                game.setCurrentUserWord(userword.substring(0, userword.length() - 1));
                game.wordsBuffer[game.indexBuffer] = userword.substring(0, userword.length() - 1);
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
                if (key != null && Objects.equals(key.getLetter(), character)) {
                    if (key.getState() != States.RIGHT)
                        key.changeState(newState);
                    return;
                }
            }
        }
    }

    public Keyboard() {
        initKeyboard();
    }
}
