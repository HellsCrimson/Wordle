package src.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Game {

    private String[] wordListAnswer;
    private String[] possibleWord;
    private String wordToFind;

    private Random rng;
    private long seed = -1;

    public Game() {
        getRandom();
        findNewWord();
    }

    private void findNewWord() {
        loadWords();
        sort(wordListAnswer);
        sort(possibleWord);
        wordToFind = wordListAnswer[rng.nextInt(wordListAnswer.length)];
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
        if (wordListAnswer[Arrays.binarySearch(wordListAnswer, str)] != str) {
            return possibleWord[Arrays.binarySearch(possibleWord, str)] == str;
        }
        return true;
    }

    public char[] correctLetters(String str) {
        char[] correct = new char[wordToFind.length()];
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < wordToFind.length(); j++) {
                if (str.charAt(i) == wordToFind.charAt(j)) {
                    correct[i] = str.charAt(i);
                    break;
                }
            }
        }
        return correct;
    }

    public boolean isCorrectWord(String str) {
        return str == wordToFind;
    }
}
