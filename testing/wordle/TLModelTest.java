package wordle;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class TLModelTest {

    @Test
    public void testKeyPressed() {
    }

    @Test
    public void testRestartGame() {
        TLModel model = new TLModel();
        model.addWordBuffer("test1");
        model.addWordBuffer("test2");

        String[] expected = new String[model.getWordsBuffer().length];
        Arrays.fill(expected, "");

        model.restartGame();

        assertArrayEquals(model.getWordsBuffer(), expected);
    }

    @Test
    public void testGetWordsBuffer() {
        TLModel model = new TLModel();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(model.getWordsBuffer(), expected);
    }

    @Test
    public void testAddWordBuffer() {
        TLModel model = new TLModel();
        model.addWordBuffer("test");
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        expected[0] = "test";
        assertArrayEquals(model.getWordsBuffer(), expected);
    }

    @Test
    public void testChangeEnterPressed() {
    }

    @Test
    public void testCorrectLetters() {
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
        assertArrayEquals(answer, expected);
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
    public void testAddLetter() {
    }

    @Test
    public void testResetWordBuffer() {
        TLModel model = new TLModel();
        Arrays.fill(model.getWordsBuffer(), "test");
        model.resetWordBuffer();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(model.getWordsBuffer(), expected);
    }
}