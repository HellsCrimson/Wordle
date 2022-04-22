package keyboard;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeyTest {

    @Test
    public void getLetter() {
        Key key = new Key("A");
        assertEquals("A", key.getLetter());
    }

    @Test
    public void getState() {
        Key key = new Key("A", States.RIGHT);
        assertEquals(States.RIGHT, key.getState());
    }

    @Test
    public void changeState() {
        Key key = new Key("A");
        key.changeState(States.WRONG);
        assertEquals(States.WRONG, key.getState());
    }
}