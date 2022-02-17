package src.wordle;

import src.data.WordList;
import src.keyboard.Key;
import src.keyboard.Keyboard;
import src.keyboard.KeyboardListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TLModel extends Observable {

    private Keyboard keyboard;

    private boolean isFixedWord = false;
    private final String fixedWord = "ROGER";

    private WordList wordListAnswer;
    private WordList possibleWord;
    private String wordToFind;
    private String currentUserWord;

    public String[] wordsBuffer;
    public int indexBuffer;

    private String[] correctPlaceLetters;
    private String[] correctLetters;
    private boolean enterPressed = false;

    private Random rng;
    private long seed = -1;

    public TLModel() {
        initialise();
        setChanged();
        notifyObservers();
    }

    /** Initialise the model with the basic attributes */
    private void initialise() {
        keyboard = new Keyboard(this);
        currentUserWord = "";
        wordsBuffer = new String[6];
        Arrays.fill(wordsBuffer, "");
        getRandom();
        seed = rng.nextLong();
        findNewWord();
    }

    /** Send the key press event to the keyboard
     * notify the observers to call the update */
    public void keyPressed(Key key) {
        keyboard.KeyPressed(key);
        setChanged();
        notifyObservers();
    }

    /** Reset the attribute to be ready for another game */
    public void restartGame() {
        // TODO restart the game in the model
        setChanged();
        notifyObservers();
    }

    public Keyboard getKeyboard() { return keyboard; }

    public void changeEnterPressed() { enterPressed = !enterPressed; }

    public boolean getEnterPressed() { return enterPressed; }

    public String[] getCorrectPlaceLetters() { return correctPlaceLetters; }

    public String[] getCorrectLetters() { return correctLetters; }

    public boolean isFixedWord() { return isFixedWord; }

    public String getFixedWord() { return fixedWord; }

    public void setIsFixedWord(boolean state) { isFixedWord = state; }

    public String getWordToFind() { return wordToFind; }

    public long getSeed() { return seed; }

    public String getCurrentUserWord() { return currentUserWord; }

    public void setCurrentUserWord(String str) { currentUserWord = str.toUpperCase(); }

    private void getRandom() { rng = new Random(); }

    /** Find a new word
     * Only call on startup cause call creation wordlist */
    private void findNewWord() {
        loadWords();
        wordListAnswer.sort();
        possibleWord.sort();
        wordToFind = wordListAnswer.dataAt(rng.nextInt(wordListAnswer.length));
    }

    /** Find a new word
     * Only call when the game has already started */
    public void changeWordToFind() {
        if (!isFixedWord)
            wordToFind = wordListAnswer.dataAt(rng.nextInt(wordListAnswer.length));
    }

    private void loadWords() {
        wordListAnswer = new WordList("src/resources/common.txt");
        possibleWord = new WordList("src/resources/words.txt");
    }

    public boolean isValidWord(String str) {
        return (wordListAnswer.search(str) >= 0) || (possibleWord.search(str) >= 0);
    }

    /** Check the letter that are correct but can be in the wrong place
     * Return an array with the letters that are correct or null
     * The letters are in the place where they are in the correct word */
    public String[] correctLetters(String str) {
        String[] correct = new String[wordToFind.length()];
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < wordToFind.length(); j++) {
                if (str.charAt(i) == wordToFind.charAt(j)) {
                    correct[i] = String.valueOf(str.charAt(i));
                    break;
                }
            }
        }
        correctLetters = correct;
        return correct;
    }

    /** Check the letter that are correct and in the correct place
     * Return an array with the letters that are correct or null */
    public String[] correctPlaceLetters(String str) {
        String[] correct = new String[wordToFind.length()];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == wordToFind.charAt(i)) {
                correct[i] = String.valueOf(str.charAt(i));
            }
        }
        correctPlaceLetters = correct;
        return correct;
    }

    public boolean isCorrectWord(String str) { return str.equals(wordToFind); }

    public void addLetter(String character) { currentUserWord += character; }

    /** Reset the word buffer at the end of a game */
    public void resetWordBuffer() {
        for (String row : wordsBuffer) {
            row = "";
        }
    }
}
