package wordle;

import java.util.Arrays;
import java.util.Scanner;
import cli.*;
import keyboard.Key;
import keyboard.States;

public class TLCli {

    private final TLModel model;

    private final String GRID_COLOR = Colors.RED_BRIGHT;
    private final String WRONG_COLOR = Colors.WHITE;
    private final String CORRECT_PLACE_COLOR = Colors.GREEN;
    private final String CORRECT_COLOR = Colors.YELLOW;

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

        while (!model.getKeyboard().won || !model.getKeyboard().lost) {
            printLine(Colors.RED + Title.getTitles()[titleIndex] + Colors.RESET);
            printGrid();
            printAvailableKeys();
            printLine(model.getWordToFind()); // TODO delete

            do {
                print("Please enter a " + Colors.RED_UNDERLINED + "five" + Colors.RESET + " letter word: ");
                userWord = scan.next();
            } while (userWord.length() != 5);

            model.setCurrentUserWord(userWord.toUpperCase());
            model.addWordBuffer(model.getCurrentUserWord());
            model.keyPressed(new Key("ENTER", States.SPECIAL));
        }
        if (model.getKeyboard().won) {
            winScreen();
        } else {
            lostScreen();
        }
    }

    public static void clearScreen() {
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

        drawUnderline();
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
            drawUnderline();
        }
        printLine("");
    }

    private void drawUnderline() {
        for (int i = 0; i < 21; i++) {
            print(GRID_COLOR + "-" + Colors.RESET);
        }
        printLine("");
    }

    private void lostScreen() {
        clearScreen();
        printLine("You lost! The correct word was: " + Colors.RED_UNDERLINED + model.getWordToFind() + Colors.RESET);
    }

    private void winScreen() {
        clearScreen();
        printLine("You won! Using " + Colors.RED_UNDERLINED + model.indexBuffer + Colors.RESET + " attempt(s)");
    }

    private void printAvailableKeys() {
    }

    private String getLetterColor(String letter, int y, int x) {
        if (board[y][x] == -1) {
            if (model.getCorrectPlaceLetters()[x] != null) {
                letter = Colors.GREEN + letter;
                board[y][x] = 2;
            } else if (model.getCorrectLetters()[x] != null) {
                letter = Colors.YELLOW + letter;
                board[y][x] = 1;
            } else {
                letter = Colors.RED + letter;
                board[y][x] = 0;
            }
        } else if (board[y][x] == 2) {
            letter = Colors.GREEN + letter;
        } else if (board[y][x] == 1) {
            letter = Colors.YELLOW + letter;
        } else {
            letter = Colors.RED + letter;
        }
        return letter + Colors.RESET;
    }
}
