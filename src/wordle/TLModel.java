package wordle;

import data.StatsWriter;
import data.WordList;
import keyboard.Key;
import keyboard.Keyboard;
import java.util.*;

public class TLModel extends Observable {

    private Keyboard keyboard;
    private StatsWriter statsWriter;

    private boolean needBeValid = true;
    private boolean showAnswer = false;
    private boolean isFixedWord = false;
    private static final String fixedWord = "ROGER";

    private WordList wordListAnswer;
    private WordList possibleWord;
    private String wordToFind;
    private String currentUserWord;

    private String[] wordsBuffer;
    public int indexBuffer;

    private String[] correctPlaceLetters;
    private String[] correctLetters;
    private boolean enterPressed = false;

    private Random rng;
    private long seed = -1;

    public boolean restarting = false;

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
        resetWordBuffer();
        getRandom();
        seed = rng.nextLong();
        findNewWord();
        statsWriter = new StatsWriter();
    }

    /** Send the key press event to the keyboard
     * notify the observers to call the update
     * @param key is a valid non-null key */
    public void keyPressed(Key key) {
        keyboard.KeyPressed(key);
        setChanged();
        notifyObservers();
    }

    /** Reset the attribute to be ready for another game */
    public void restartGame() {
        String oldWordToFind = getWordToFind();

        currentUserWord = "";
        changeWordToFind();
        resetWordBuffer();
        keyboard.resetKeyboard();
        restarting = true;

        assert !oldWordToFind.equals(wordToFind) || isFixedWord():"word must have changed or the word is set";

        setChanged();
        notifyObservers();
    }

    public void changeNeedBeValid() { needBeValid = !needBeValid; }

    public boolean getNeedBeValid() { return needBeValid; }

    public void changeShowAnswer() { showAnswer = !showAnswer; }

    public String[] getWordsBuffer() { return wordsBuffer; }

    public void addWordBuffer(String word) {
        wordsBuffer[indexBuffer] = word;
    }

    public Keyboard getKeyboard() { return keyboard; }

    public void changeEnterPressed() { enterPressed = !enterPressed; }

    public boolean getEnterPressed() { return enterPressed; }

    public String[] getCorrectPlaceLetters() { return correctPlaceLetters; }

    public String[] getCorrectLetters() { return correctLetters; }

    public boolean isFixedWord() { return isFixedWord; }

    public void setIsFixedWord(boolean state) { isFixedWord = state; }

    public String getWordToFind() {
        if (!isFixedWord)
            return wordToFind;
        return fixedWord;
    }

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
        wordToFind = wordListAnswer.dataAt(rng.nextInt(wordListAnswer.length));
    }

    private void loadWords() {
        wordListAnswer = new WordList("resources/common.txt");
        possibleWord = new WordList("resources/words.txt");

        assert wordListAnswer!=null && possibleWord!=null:"both data structure must be loaded (not null)";
    }

    /** @param str is a 5 capital letter word */
    public boolean isValidWord(String str) {
        assert str!=null:"the word to find must not be null";

        return (wordListAnswer.search(str) >= 0) || (possibleWord.search(str) >= 0);
    }

    /** Check the letter that are correct
     * Set an array with the letters that are correct or null
     * And Set an array with the letters that are in the correct place or null
     * The letters are in the place where they are in the correct word */
    public void correctLetters(String str) {
        assert str!=null:"string must not be null";

        String[] correct = new String[getWordToFind().length()];
        String[] correctPlace = new String[getWordToFind().length()];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == wordToFind.charAt(i))
                correctPlace[i] = String.valueOf(str.charAt(i));
            else {
                for (int j = 0; j < getWordToFind().length(); j++) {
                    if (str.charAt(i) == getWordToFind().charAt(j) && str.charAt(j) != getWordToFind().charAt(j)) {
                        correct[i] = String.valueOf(str.charAt(i));
                        break;
                    }
                }
            }
        }
        correctLetters = correct;
        correctPlaceLetters = correctPlace;
    }

    public boolean isCorrectWord(String str) { return str.equals(getWordToFind()); }

    public void addLetter(String character) { currentUserWord += character; }

    /** Reset the word buffer at the end of a game */
    public void resetWordBuffer() {
        assert wordsBuffer!=null:"word buffer must be initialised";
        Arrays.fill(wordsBuffer, "");
        indexBuffer = 0;
    }

    public StatsWriter getStatWriter() { return statsWriter; }
}
