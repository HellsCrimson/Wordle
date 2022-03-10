package wordle;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TLModelTest {

    @Test
    public void keyPressed() {
    }

    @Test
    public void restartGame() {
        TLModel model = new TLModel();
        model.addWordBuffer("test1");
        model.addWordBuffer("test2");

        String[] expected = new String[model.getWordsBuffer().length];
        Arrays.fill(expected, "");

        model.restartGame();

        assertArrayEquals(model.getWordsBuffer(), expected);
    }

    @Test
    public void getWordsBuffer() {
        TLModel model = new TLModel();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(model.getWordsBuffer(), expected);
    }

    @Test
    public void addWordBuffer() {
        TLModel model = new TLModel();
        model.addWordBuffer("test");
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        expected[0] = "test";
        assertArrayEquals(model.getWordsBuffer(), expected);
    }

    @Test
    public void changeEnterPressed() {
    }

    @Test
    public void correctLetters() {
    }

    @Test
    public void correctPlaceLettersAllTrue() {
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
    public void correctPlaceLettersAllFalse() {
        TLModel model = new TLModel();
        for (int i = 'a'; i <= 'z'; i++) {
            if (model.getWordToFind().charAt(0) != i) {
                String toTest = "";
                for (int j = 0; j < 5; j++)
                    toTest += String.valueOf((char)i);
                model.setCurrentUserWord(toTest);
                break;
            }
        }
        model.correctLetters(model.getCurrentUserWord());
        String[] answer = model.getCorrectPlaceLetters();

        String[] expected = new String[answer.length];
        assertArrayEquals(answer, expected);
    }

    @Test
    public void addLetter() {
    }

    @Test
    public void resetWordBuffer() {
        TLModel model = new TLModel();
        Arrays.fill(model.getWordsBuffer(), "test");
        model.resetWordBuffer();
        String[] expected = new String[6];
        Arrays.fill(expected, "");
        assertArrayEquals(model.getWordsBuffer(), expected);
    }
}