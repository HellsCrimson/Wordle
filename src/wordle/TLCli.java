package src.wordle;

import java.util.Scanner;
import src.cli.*;
import src.keyboard.Key;
import src.keyboard.States;

public class TLCli {

    private final TLModel model;
    private final TLController controller;

    private final String GRID_COLOR = Colors.RED_BRIGHT;
    private final String WRONG_COLOR = Colors.WHITE;
    private final String CORRECT_PLACE_COLOR = Colors.GREEN;
    private final String CORRECT_COLOR = Colors.YELLOW;

    private boolean ended = false;

    public TLCli(TLModel model, TLController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void startGame() {
        Scanner scan = new Scanner(System.in);
        String userWord = "";
        clearScreen();
        int titleIndex = Title.getTitleIndex();

        while (!model.getKeyboard().won || !model.getKeyboard().lost) {
            printLine(Colors.RED + Title.getTitles()[titleIndex] + Colors.RESET);
            printGrid();

            do {
                print("Please enter a " + Colors.RED_UNDERLINED + "five" + Colors.RESET + " letter word: ");
                userWord = scan.next();
            } while (userWord.length() != 5);

            model.setCurrentUserWord(userWord.toUpperCase());
            model.addWordBuffer(model.getCurrentUserWord());
            controller.keyPressed(new Key("ENTER", States.SPECIAL));
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
            for (int i = 0; i < 5; i++) {
                String letter;
                try {
                    letter = String.valueOf(wordBuffer[y].charAt(i));
                } catch (Exception e) {
                    letter = " ";
                }
                print(GRID_COLOR + "| " + Colors.RESET + WRONG_COLOR + letter + " " + Colors.RESET);
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
}
