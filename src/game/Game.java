package src.game;

import java.util.ArrayList;
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
    }

    private void loadWords() {
        ArrayList<String> loadedWordList = loadFile("src/resources/common.txt");
        wordListAnswer = loadedWordList.toArray(new String[loadedWordList.size()]);
        sort(wordListAnswer);
        ArrayList<String> loadedPossibleWord = loadFile("src/resources/words.txt");
        possibleWord = loadedPossibleWord.toArray(new String[loadedPossibleWord.size()]);
        sort(possibleWord);
    }

    private ArrayList<String> loadFile(String path) {
        ArrayList<String> datas = new ArrayList<String>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                datas.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        return datas;
    }

    private void getRandom() {
        if (seed == -1) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }
    }

    private void sort(String[] arr) {
        // TODO
    }

    public boolean isValidWord(String str) {
        // TODO
        // Search in wordListAnswer and possibleWord
        return true;
    }

    public void correctLetters(String str) {
        // TODO
    }

    public boolean isCorrectWord(String str) {
        // TODO
        // Search in possibleWord
        return true;
    }
}
