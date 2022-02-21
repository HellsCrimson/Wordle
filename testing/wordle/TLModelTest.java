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
    }

    @Test
    public void getKeyboard() {
    }

    @Test
    public void changeEnterPressed() {
    }

    @Test
    public void getEnterPressed() {
    }

    @Test
    public void getCorrectPlaceLetters() {
    }

    @Test
    public void getCorrectLetters() {
    }

    @Test
    public void isFixedWord() {
    }

    @Test
    public void setIsFixedWord() {
    }

    @Test
    public void getWordToFind() {
    }

    @Test
    public void getSeed() {
    }

    @Test
    public void getCurrentUserWord() {
    }

    @Test
    public void setCurrentUserWord() {
    }

    @Test
    public void changeWordToFind() {
    }

    @Test
    public void isValidWord() {
    }

    @Test
    public void correctLetters() {
    }

    @Test
    public void correctPlaceLetters() {
    }

    @Test
    public void isCorrectWord() {
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