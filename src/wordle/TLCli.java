package src.wordle;

import java.util.Scanner;
import src.cli.*;

public class TLCli {

    private final TLModel model;
    private final TLController controller;

    public TLCli(TLModel model, TLController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void startGame() {
        Scanner scan = new Scanner(System.in);
        String userWord = "";
        clearScreen();
        printLine(Colors.RED + Title.getTitle() + Colors.RESET);

        do {
            print("Please enter a " + Colors.RED_UNDERLINED + "five" + Colors.RESET + " letter word: ");
            userWord = scan.next();
        } while (userWord.length() != 5);

        model.setCurrentUserWord(userWord.toUpperCase());
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
}
