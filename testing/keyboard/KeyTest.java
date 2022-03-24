package keyboard;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeyTest {

    @Test
    public void getLetter() {
        Key key = new Key("A");
        assertEquals(key.getLetter(), "A");
    }

    @Test
    public void getState() {
        Key key = new Key("A", States.RIGHT);
        assertEquals(key.getState(), States.RIGHT);
    }

    @Test
    public void changeState() {
        Key key = new Key("A");
        key.changeState(States.WRONG);
        assertEquals(key.getState(), States.WRONG);
    }
}