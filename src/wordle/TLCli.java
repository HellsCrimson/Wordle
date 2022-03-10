package wordle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import cli.*;
import keyboard.Key;
import keyboard.States;

public class TLCli {

    private final TLModel model;

    private static final String GRID_COLOR = Colors.RED_BRIGHT;
    private static final String WRONG_COLOR = Colors.WHITE;
    private static final String CORRECT_PLACE_COLOR = Colors.GREEN;
    private static final String CORRECT_COLOR = Colors.YELLOW;

    private final ArrayList<String> notUsed = new ArrayList<>();
    private final ArrayList<String> wrong = new ArrayList<>();
    private final ArrayList<String> wrongPlace = new ArrayList<>();
    private final ArrayList<String> right = new ArrayList<>();

    private int[][] board;
    private int nbTries;

    private boolean ended = false;

    public TLCli(TLModel model) {
        this.model = model;
        board = new int[6][6];
        for (int[] row : board)
            Arrays.fill(row, -1);
        nbTries = 0;
    }

    public void startGame() {
        Scanner scan = new Scanner(System.in);
        String userWord = "";
        clearScreen();
        int titleIndex = Title.getTitleIndex();

        while (!model.getWon() && !model.getLost()) {
            printLine(Colors.RED + Title.getTitles()[titleIndex] + Colors.RESET);
            printGrid();
            printAvailableKeys();
            printLine("");
            if (model.isShowAnswer()) {
                printLine("Correct word: " + Colors.RED + model.getWordToFind() + Colors.RESET);
                printLine("");
            }

            do {
                print("Please enter a " + Colors.RED_UNDERLINED + "five" + Colors.RESET + " letter word: ");
                userWord = scan.next();
            } while (!userWord.matches("[a-zA-Z]{5}"));
            printLine("");

            if (userWord.equalsIgnoreCase("EPITA")) {
                printLine(Title.getEasterEgg());
                printLine("");
            }

            nbTries++;
            model.setCurrentUserWord(userWord.toUpperCase());
            model.addWordBuffer(model.getCurrentUserWord());
            model.keyPressed(new Key("ENTER", States.SPECIAL));
        }
        printLine(Colors.RED + Title.getTitles()[titleIndex] + Colors.RESET);
        printGrid();

        if (model.getWon()) {
            winScreen();
        } else {
            lostScreen();
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void print(String text) {
        System.out.print(text);
    }

    public void printLine(String text) {
        System.out.println(text);
    }

    public void printGrid() {
        String[] wordBuffer = model.getWordsBuffer();

        drawUnderline(21);
        for (int y = 0; y < wordBuffer.length; y++) {
            for (int x = 0; x < 5; x++) {
                String letter;
                try {
                    letter = getLetterColor(String.valueOf(wordBuffer[y].charAt(x)), y, x);
                } catch (Exception e) {
                    letter = " ";
                }
                print(GRID_COLOR + "| " + Colors.RESET + letter + " ");
            }
            print(GRID_COLOR + "|" + Colors.RESET);
            printLine("");
            drawUnderline(21);
        }
        printLine("");
    }

    private void drawUnderline(int size) {
        for (int i = 0; i < size; i++) {
            print(GRID_COLOR + "-" + Colors.RESET);
        }
        printLine("");
    }

    private void lostScreen() {
        printLine("");
        printLine("You lost! The correct word was: " + Colors.RED_UNDERLINED + model.getWordToFind() + Colors.RESET);
    }

    private void winScreen() {
        printLine("");
        printLine("You won! Using " + Colors.RED_UNDERLINED + model.indexBuffer + Colors.RESET + " attempt(s)");
    }

    private void printAvailableKeys() {
        printLine("");
        Key[][] keys = model.getKeyboard().getKeys();
        for (Key[] row : keys) {
            for (Key key : row) {
                if (key != null) {
                    switch (key.getState()) {
                        case NOT_USED:
                            if (!notUsed.contains(key.getLetter()))
                                notUsed.add(key.getLetter());
                            break;
                        case WRONG_PLACE:
                            if (!wrongPlace.contains(key.getLetter()))
                                wrongPlace.add(key.getLetter());
                            break;
                        case WRONG:
                            if (!wrong.contains(key.getLetter()))
                                wrong.add(key.getLetter());
                            break;
                        case RIGHT:
                            notUsed.remove(key.getLetter());
                            if (!right.contains(key.getLetter()))
                                right.add(key.getLetter());
                            wrongPlace.remove(key.getLetter());
                            break;
                        case SPECIAL:
                            break;
                    }
                }
            }
        }
        displayKeys(notUsed, "Not Used");
        displayKeys(right, "Right");
        displayKeys(wrongPlace, "Wrong Place");
        displayKeys(wrong, "Wrong");
    }

    private void displayKeys(ArrayList<String> list, String category) {
        Collections.sort(list);
        if (list.size() > 0) {
            drawUnderline(list.size() * 4 + category.length() + 2);
            print(category + " ");
            for (String letter : list) {
                print(GRID_COLOR + "| " + Colors.RESET + letter + " ");
            }
            printLine(GRID_COLOR + "|" + Colors.RESET);
            drawUnderline(list.size() * 4 + category.length() + 2);
        }
    }

    private String getLetterColor(String letter, int y, int x) {
        if (board[y][x] == -1) {
            if (model.getCorrectPlaceLetters()[x] != null) {
                letter = CORRECT_PLACE_COLOR + letter;
                board[y][x] = 2;
            } else if (model.getCorrectLetters()[x] != null) {
                letter = CORRECT_COLOR + letter;
                board[y][x] = 1;
            } else {
                letter = WRONG_COLOR + letter;
                board[y][x] = 0;
            }
        } else if (board[y][x] == 2) {
            letter = CORRECT_PLACE_COLOR + letter;
        } else if (board[y][x] == 1) {
            letter = CORRECT_COLOR + letter;
        } else {
            letter = WRONG_COLOR + letter;
        }
        return letter + Colors.RESET;
    }

    public void askFlags() {
        Scanner scan = new Scanner(System.in);
        model.setNeedBeValid(askFlag(scan, "Should a valid word be needed? (Yes/No) "));
        model.setShowAnswer(askFlag(scan, "Should the valid word be shown? (Yes/No) "));
        model.setIsFixedWord(askFlag(scan, "Should the word be set? (Yes/No) "));
    }

    private boolean askFlag(Scanner scan, String textAsked) {
        String response;
        while (true) {
            print(textAsked);
            response = scan.next();
            if ("YES".equalsIgnoreCase(response) ||
                    "Y".equalsIgnoreCase(response)) {
                return true;
            } else if ("NO".equalsIgnoreCase(response) ||
                    "N".equalsIgnoreCase(response)) {
                return false;
            }
        }
    }

    public boolean askRestart() {
        Scanner scan = new Scanner(System.in);
        String response;
        while (true) {
            print("Do you want to restart? (Yes/No) ");
            response = scan.next();
            if ("YES".equalsIgnoreCase(response) ||
                    "Y".equalsIgnoreCase(response)) {
                return true;
            } else if ("NO".equalsIgnoreCase(response) ||
                    "N".equalsIgnoreCase(response)) {
                return false;
            }
        }
    }

    public void restart() {
        for (int[] row : board)
            Arrays.fill(row, -1);
        nbTries = 0;
        notUsed.clear();
    }
}
