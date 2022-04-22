package wordle;

import keyboard.Key;
import keyboard.States;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TLModelTest {

    @Test
    public void testRestartGame() {
        TLModel model = new TLModel();
        model.addWordBuffer("test1");
        model.addWordBuffer("test2");

        String[] expected = new String[model.getWordsBuffer().length];
        Arrays.fill(expected, "");

        model.restartGame();

        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testRestartGameBuffFull() {
        TLModel model = new TLModel();
        for (int i = 0; i < 6; i++)
            model.addWordBuffer("test1");

        String[] expected = new String[model.getWordsBuffer().length];
        Arrays.fill(expected, "");

        model.restartGame();

        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testRestartGameBuffEmpty() {
        TLModel model = new TLModel();

        String[] expected = new String[model.getWordsBuffer().length];
        Arrays.fill(expected, "");

        model.restartGame();

        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testGetWordsBuffer() {
        TLModel model = new TLModel();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testAddWordBuffer() {
        TLModel model = new TLModel();
        model.addWordBuffer("test");
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        expected[0] = "test";
        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testAddWordBufferFull() {
        TLModel model = new TLModel();
        model.setNeedBeValid(false);
        for (int i = 0; i < 6; i++) {
            model.addWordBuffer("tests");
            model.setIndexBuffer(model.getIndexBuffer() + 1);
        }
        String[] expected = new String[6];
        Arrays.fill(expected, "tests");
        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testCorrectLetters() {
        TLModel model = new TLModel();
        model.setCurrentUserWord(model.getWordToFind().replaceFirst("[a-zA-Z]", "Z"));
        model.correctLetters(model.getWordToFind());
        boolean notAllTrue = false;
        for (String letter : model.getCorrectLetters()) {
            if (letter == null) {
                notAllTrue = true;
                break;
            }
        }
        assertTrue(notAllTrue);
    }

    @Test
    public void testCorrectPlaceLettersAllTrue() {
        TLModel model = new TLModel();
        model.setCurrentUserWord(model.getWordToFind());
        model.correctLetters(model.getWordToFind());
        String[] answer = model.getCorrectPlaceLetters();

        String[] expected = new String[answer.length];
        for (int i = 0; i < expected.length; i++)
            expected[i] = String.valueOf(model.getCurrentUserWord().charAt(i));
        assertArrayEquals(expected, answer);
    }

    @Test
    public void testCorrectPlaceLettersAllTrueWordFixed() {
        TLModel model = new TLModel();
        model.setIsFixedWord(true);
        model.setCurrentUserWord(model.getWordToFind());
        model.correctLetters(model.getWordToFind());
        String[] answer = model.getCorrectPlaceLetters();

        String[] expected = new String[answer.length];
        for (int i = 0; i < expected.length; i++)
            expected[i] = String.valueOf(model.getCurrentUserWord().charAt(i));
        assertArrayEquals(expected, answer);
    }

    @Test
    public void testCorrectPlaceLettersAllFalse() {
        TLModel model = new TLModel();
        String toTest = "";
        for (int j = 0; j < model.getWordToFind().length(); j++) {
            for (int i = 'A'; i <= 'Z'; i++) {
                if (model.getWordToFind().charAt(j) != i) {
                    toTest += String.valueOf((char) i);
                    break;
                }
            }
        }
        model.setCurrentUserWord(toTest);
        model.correctLetters(model.getCurrentUserWord());
        String[] answer = model.getCorrectPlaceLetters();

        String[] expected = new String[answer.length];
        assertArrayEquals(expected, answer);
    }

    @Test
    public void testResetWordBufferEmpty() {
        TLModel model = new TLModel();
        model.resetWordBuffer();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testResetWordBufferPartial() {
        TLModel model = new TLModel();
        model.addWordBuffer("test");
        model.addWordBuffer("test");
        model.resetWordBuffer();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testResetWordBufferFull() {
        TLModel model = new TLModel();
        Arrays.fill(model.getWordsBuffer(), "test");
        model.resetWordBuffer();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(expected, model.getWordsBuffer());
    }

    @Test
    public void testChangeIndexBuffer() {
        TLModel model = new TLModel();
        model.setCurrentUserWord("tests");
        model.keyPressed(new Key("ENTER", States.SPECIAL));
        assertEquals(1, model.getIndexBuffer());
    }

    @Test
    public void testChangeIndexBuffer3Times() {
        TLModel model = new TLModel();
        model.setCurrentUserWord("tests");
        model.keyPressed(new Key("ENTER", States.SPECIAL));
        model.setCurrentUserWord("tests");
        model.keyPressed(new Key("ENTER", States.SPECIAL));
        model.setCurrentUserWord("tests");
        model.keyPressed(new Key("ENTER", States.SPECIAL));
        assertEquals(3, model.getIndexBuffer());
    }

    @Test
    public void testAddLetter() {
        TLModel model = new TLModel();
        assertEquals("", model.getCurrentUserWord());
    }

    @Test
    public void testAddLetterOnce() {
        TLModel model = new TLModel();
        model.addLetter("A");
        assertEquals("A", model.getCurrentUserWord());
    }

    @Test
    public void testAddLetter5Times() {
        TLModel model = new TLModel();
        for (int i = 0; i < 5; i++)
            model.addLetter("A");
        assertEquals("AAAAA", model.getCurrentUserWord());
    }
}