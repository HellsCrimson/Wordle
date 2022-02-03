package src.keyboard;

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

    public void KeyPressed(Key key) {
        key.changeState(States.RIGHT); // TODO
        // check what is new state
    }

    public Keyboard() {
        initKeyboard();
    }
}
