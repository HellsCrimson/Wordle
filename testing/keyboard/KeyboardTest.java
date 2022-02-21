package keyboard;

import org.junit.Test;
import wordle.TLModel;

import java.util.Objects;

import static org.junit.Assert.*;

public class KeyboardTest {

    @Test
    public void getKeys() {
        Keyboard kb = new Keyboard(new TLModel());
        assertNotNull(kb.getKeys());
    }

    @Test
    public void getMaxRowKeys() {
        Keyboard kb = new Keyboard(new TLModel());
        assertEquals(kb.getMaxRowKeys(), 10);
    }

    @Test
    public void keyPressed() {
        TLModel model = new TLModel();
        Keyboard kb = new Keyboard(model);
        kb.KeyPressed(new Key("Q"));
        assertTrue(model.getCurrentUserWord().length() > 0);
    }

    @Test
    public void keyPressedDelete() {
        TLModel model = new TLModel();
        Keyboard kb = new Keyboard(model);
        kb.KeyPressed(new Key("Q"));
        kb.KeyPressed(new Key("DELETE"));
        assertTrue(model.getCurrentUserWord().length() == 0);
    }

    @Test
    public void resetKeyboard() {
        TLModel model = new TLModel();
        Keyboard kb = new Keyboard(model);
        Key[][] expected = kb.getKeys().clone();
        for (int y = 0; y < kb.getKeys().length; y++) {
            for (int x = 0; x < kb.getKeys()[y].length; x++) {
                if (kb.getKeys()[y][x] != null && kb.getKeys()[y][x].getState() != States.SPECIAL)
                    kb.getKeys()[y][x].changeState(States.RIGHT);
            }
        }
        kb.resetKeyboard();

        assertArrayEquals(kb.getKeys(), expected);
    }
}