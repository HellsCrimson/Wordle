package src.game;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Game {

    private String[] wordListAnswer;
    private String[] possibleWord;
    private String wordToFind;
    private String currentUserWord;

    public String[] wordsBuffer;
    public int indexBuffer;

    private Random rng;
    private long seed = -1;

    public Game() {
        currentUserWord = new String("");
        wordsBuffer = new String[6];
        Arrays.fill(wordsBuffer, "");
        getRandom();
        findNewWord();
        System.out.println(wordToFind); // TODO to delete
    }

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
        wordToFind = wordListAnswer[rng.nextInt(wordListAnswer.length)].toUpperCase();
    }

    private void loadWords() {
        ArrayList<String> loadedWordList = loadFile("src/resources/common.txt");
        wordListAnswer = loadedWordList.toArray(new String[loadedWordList.size()]);
        ArrayList<String> loadedPossibleWord = loadFile("src/resources/words.txt");
        possibleWord = loadedPossibleWord.toArray(new String[loadedPossibleWord.size()]);
    }

    private ArrayList<String> loadFile(String path) {
        ArrayList<String> data = new ArrayList<String>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        return data;
    }

    private void getRandom() {
        if (seed == -1) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }
    }

    private void sort(String[] arr) {
        Arrays.sort(arr);
    }

    public boolean isValidWord(String str) {
        // Search in wordListAnswer and possibleWord (binarySearch)
        if (Arrays.binarySearch(wordListAnswer, str) >= 0) {
            return Arrays.binarySearch(possibleWord, str) >= 0;
        }
        return true;
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

    public boolean isCorrectWord(String str) {
        return Objects.equals(str, wordToFind);
    }

    public void addLetter(String character) {
        currentUserWord += character;
    }
}
