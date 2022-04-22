package wordle;

import data.StatsWriter;
import data.WordList;
import keyboard.Key;
import keyboard.Keyboard;

import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

public class TLModel extends Observable {

    private static final String fixedWord = "ROGER";
    private int indexBuffer;
    private boolean restarting = false;
    private Keyboard keyboard;
    private StatsWriter statsWriter;
    private boolean won = false;
    private boolean lost = false;
    private boolean needBeValid = true;
    private boolean showAnswer = false;
    private boolean isFixedWord = false;
    private WordList wordListAnswer;
    private WordList possibleWord;
    private String wordToFind;
    private String currentUserWord;
    private String[] wordsBuffer;
    private String[] correctPlaceLetters;
    private String[] correctLetters;
    private boolean enterPressed = false;
    private Random rng;

    public TLModel() {
        initialise();
        setChanged();
        notifyObservers();
    }

    private boolean invariant() {
        boolean invariant = !(won && lost);
        invariant &= currentUserWord.length() <= wordToFind.length();
        if (isFixedWord)
            invariant &= !wordToFind.equals(getWordToFind());
        return invariant;
    }

    /**
     * Initialise the model with the basic attributes
     *
     * @pre. model's field not initialised
     * @post. fields are initialised
     */
    private void initialise() {
        keyboard = new Keyboard(this);
        currentUserWord = "";
        wordsBuffer = new String[6];
        Arrays.fill(wordsBuffer, "");
        setIndexBuffer(0);
        getRandom();
        findNewWord();
        statsWriter = new StatsWriter();
    }

    /**
     * Send the key press event to the keyboard
     * notify the observers to call the update
     *
     * @pre. keyboard object is initialised
     * @post. is old keyboard with one key pressed
     */
    public void keyPressed(Key key) {
        assert invariant();
        assert keyboard != null;

        keyboard.KeyPressed(key);
        setChanged();
        notifyObservers();
    }

    /**
     * Reset the attribute to be ready for another game
     *
     * @pre. a game was already started
     * @post. the model like in the first init
     */
    public void restartGame() {
        assert invariant();

        String oldWordToFind = getWordToFind();

        currentUserWord = "";
        changeWordToFind();
        resetWordBuffer();
        keyboard.resetKeyboard();
        restarting = true;
        won = false;
        lost = false;

        assert !oldWordToFind.equals(wordToFind) || isFixedWord() : "word must have changed or the word is set";
        assert invariant();

        setChanged();
        notifyObservers();
    }

    public int getIndexBuffer() {
        return indexBuffer;
    }

    public void setIndexBuffer(int number) { indexBuffer = number; }

    public boolean getWon() {
        return won;
    }

    public void setWon(boolean state) {
        won = state;
    }

    public boolean getLost() {
        return lost;
    }

    public void setLost(boolean state) {
        lost = state;
    }

    public boolean isShowAnswer() {
        return showAnswer;
    }

    public void setShowAnswer(boolean showAnswer) {
        this.showAnswer = showAnswer;
    }

    public void changeNeedBeValid() {
        needBeValid = !needBeValid;
    }

    public boolean getNeedBeValid() {
        return needBeValid;
    }

    public void setNeedBeValid(boolean needBeValid) {
        this.needBeValid = needBeValid;
    }

    public void changeShowAnswer() {
        showAnswer = !showAnswer;
    }

    public String[] getWordsBuffer() {
        return wordsBuffer;
    }

    public void addWordBuffer(String word) {
        wordsBuffer[getIndexBuffer()] = word;
    }

    public Keyboard getKeyboard() {
        assert keyboard != null;

        return keyboard;
    }

    public void changeEnterPressed() {
        enterPressed = !enterPressed;
    }

    public boolean getEnterPressed() {
        return enterPressed;
    }

    public String[] getCorrectPlaceLetters() {
        return correctPlaceLetters;
    }

    public String[] getCorrectLetters() {
        return correctLetters;
    }

    public boolean isFixedWord() {
        return isFixedWord;
    }

    public void setIsFixedWord(boolean state) {
        isFixedWord = state;
    }

    public String getWordToFind() {
        if (!isFixedWord)
            return wordToFind;
        return fixedWord;
    }

    public String getCurrentUserWord() {
        return currentUserWord;
    }

    public void setCurrentUserWord(String str) {
        assert str != null;

        currentUserWord = str.toUpperCase();
    }

    private void getRandom() {
        rng = new Random();
    }

    /**
     * Find a new word
     * Only call on startup cause call creation wordlist
     *
     * @pre. nothing
     * @post. two list loaded and word to find selected
     */
    private void findNewWord() {
        loadWords();
        wordListAnswer.sort();
        possibleWord.sort();
        wordToFind = wordListAnswer.dataAt(rng.nextInt(wordListAnswer.length));

        assert wordToFind != null;
    }

    /**
     * Find a new word
     * Only call when the game has already started
     *
     * @pre. two list of word already initialized
     * @post. old word to find != new word to find
     */
    private void changeWordToFind() {
        assert invariant();
        String oldWordToFind = wordToFind;

        do {
            wordToFind = wordListAnswer.dataAt(rng.nextInt(wordListAnswer.length));
        } while (oldWordToFind.equalsIgnoreCase(wordToFind));

        assert !oldWordToFind.equalsIgnoreCase(wordToFind);
    }

    /**
     * Load the words in WordList object
     *
     * @pre. nothing
     * @post. objects initialized
     */
    private void loadWords() {
        wordListAnswer = new WordList("../resources/common.txt");
        possibleWord = new WordList("../resources/words.txt");

        assert (wordListAnswer != null) && (possibleWord != null) : "both data structure must be loaded";
    }

    /**
     * Look if the word is in one of the list
     *
     * @pre. str is not null
     * @post. nothing
     */
    public boolean isValidWord(String str) {
        assert invariant();
        assert str != null : "the word to find must not be null";
        assert wordListAnswer != null && possibleWord != null;

        return (wordListAnswer.search(str) >= 0) || (possibleWord.search(str) >= 0);
    }

    /**
     * Check the letter that are correct thanks to a histogram
     * Set an array with the letters that are correct or null
     * And Set an array with the letters that are in the correct place or null
     * The letters are in the place where they are in the correct word
     *
     * @pre. str is not null
     * @post. the two array contain the letters && the letters are not at the same index
     */
    public void correctLetters(String str) {
        assert invariant();
        assert str != null;

        int[] histogram = new int[26];
        String[] correct = new String[getWordToFind().length()];
        String[] correctPlace = new String[getWordToFind().length()];

        for (int i = 0; i < getWordToFind().length(); i++) {
            histogram[getWordToFind().charAt(i) - 65]++;
        }
        for (int i = 0; i < str.length(); i++) {
            if (histogram[str.charAt(i) - 65] != 0) {
                if (str.charAt(i) == getWordToFind().charAt(i)) {
                    correctPlace[i] = String.valueOf(str.charAt(i));
                    histogram[str.charAt(i) - 65]--;
                }
            }
        }
        for (int i = 0; i < str.length(); i++) {
            if (histogram[str.charAt(i) - 65] != 0) {
                correct[i] = String.valueOf(str.charAt(i));
            }
        }

        correctLetters = correct;
        correctPlaceLetters = correctPlace;

        assert correctLetters != null;
        assert correctPlaceLetters != null;
    }

    public boolean isCorrectWord(String str) {
        assert str != null && getWordToFind() != null;

        return str.equals(getWordToFind());
    }

    public void addLetter(String character) {
        assert currentUserWord.length() < 6;

        currentUserWord += character;
    }

    /**
     * Reset the word buffer at the end of a game
     *
     * @pre. wordBuffer is already initialized
     * @post. wordBuffer full of empty word @@ index of buffer at 0
     */
    public void resetWordBuffer() {
        assert invariant();
        assert wordsBuffer != null : "word buffer must be initialised";

        Arrays.fill(wordsBuffer, "");
        setIndexBuffer(0);
    }

    public StatsWriter getStatWriter() {
        assert statsWriter != null;

        return statsWriter;
    }

    public boolean isRestarting() {
        return restarting;
    }

    public void setRestarting(boolean restarting) {
        this.restarting = restarting;
    }
}
