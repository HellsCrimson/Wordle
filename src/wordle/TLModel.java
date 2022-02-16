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

    public Keyboard getKeyboard() { return keyboard; }

    public void initialise() {
        keyboard = new Keyboard(this);
        currentUserWord = "";
        wordsBuffer = new String[6];
        Arrays.fill(wordsBuffer, "");
        getRandom();
        seed = rng.nextLong();
        findNewWord();
    }

    public void keyPressed(Key key) {
        keyboard.KeyPressed(key);
        setChanged();
        notifyObservers();
    }

    public void restartGame() {
        // TODO restart the game in the model
        setChanged();
        notifyObservers();
    }

    public TLModel() {
        initialise();
        setChanged();
        notifyObservers();
    }

    public void changeEnterPressed() { enterPressed = !enterPressed; }

    public boolean getEnterPressed() { return enterPressed; }

    public String[] getCorrectPlaceLetters() { return correctPlaceLetters; }

    public String[] getCorrectLetters() { return correctLetters; }

    public boolean isFixedWord() { return isFixedWord; }

    public String getFixedWord() { return fixedWord; }

    public void setIsFixedWord(boolean state) { isFixedWord = state; }

    public String getWordToFind() { return wordToFind; }

    public long getSeed() { return seed; }

    public String getCurrentUserWord() {
        return currentUserWord;
    }

    public void setCurrentUserWord(String str) {
        currentUserWord = str.toUpperCase();
    }

    private void findNewWord() {
        loadWords();
        wordListAnswer.sort();
        possibleWord.sort();
        wordToFind = wordListAnswer.dataAt(rng.nextInt(wordListAnswer.length));
    }

    public void changeWordToFind() {
        if (!isFixedWord)
            wordToFind = wordListAnswer.dataAt(rng.nextInt(wordListAnswer.length));
    }

    private void loadWords() {
        wordListAnswer = new WordList("src/resources/common.txt");
        possibleWord = new WordList("src/resources/words.txt");
    }

    private void getRandom() {
        rng = new Random();
    }

    public boolean isValidWord(String str) {
        // Search in wordListAnswer and possibleWord (binarySearch)
        return (wordListAnswer.search(str) >= 0) || (possibleWord.search(str) >= 0);
    }

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

    public void addLetter(String character) {
        currentUserWord += character;
    }

    public void resetWordBuffer() {
        for (String row : wordsBuffer) {
            row = "";
        }
    }
}
