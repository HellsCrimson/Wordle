package src.game;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Game {

    private boolean fixedWord = false;

    private String[] wordListAnswer;
    private String[] possibleWord;
    private String wordToFind;
    private String currentUserWord;

    public String[] wordsBuffer;
    public int indexBuffer;

    private Random rng;
    private long seed = -1;

    public Game() {
        currentUserWord = "";
        wordsBuffer = new String[6];
        Arrays.fill(wordsBuffer, "");
        getRandom();
        seed = rng.nextLong();
        findNewWord();
    }

    public boolean getFixedWord() { return fixedWord; }

    public void setFixedWord(boolean state) { fixedWord = state; }

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
        sort(wordListAnswer);
        sort(possibleWord);
        wordToFind = wordListAnswer[rng.nextInt(wordListAnswer.length)];
    }

    public void changeWordToFind() {
        if (!fixedWord)
            wordToFind = wordListAnswer[rng.nextInt(wordListAnswer.length)];
    }

    private void loadWords() {
        ArrayList<String> loadedWordList = loadFile("src/resources/common.txt");
        wordListAnswer = loadedWordList.toArray(new String[0]);
        ArrayList<String> loadedPossibleWord = loadFile("src/resources/words.txt");
        possibleWord = loadedPossibleWord.toArray(new String[0]);
    }

    private ArrayList<String> loadFile(String path) {
        ArrayList<String> data = new ArrayList<String>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine().toUpperCase());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        return data;
    }

    private void getRandom() {
        rng = new Random();
    }

    private void sort(String[] arr) {
        Arrays.sort(arr);
    }

    public boolean isValidWord(String str) {
        // Search in wordListAnswer and possibleWord (binarySearch)
        return (Arrays.binarySearch(wordListAnswer, str) >= 0) || (Arrays.binarySearch(possibleWord, str) >= 0);
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
        return correct;
    }

    public String[] correctPlaceLetters(String str) {
        String[] correct = new String[wordToFind.length()];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == wordToFind.charAt(i)) {
                correct[i] = String.valueOf(str.charAt(i));
                break;
            }
        }
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
