package src.wordle;

import java.util.Scanner;

public class TLCli {

    private final TLModel model;
    private final TLController controller;

    public TLCli(TLModel model, TLController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void startGame() {
        Scanner scan = new Scanner(System.in);
        clearScreen();
        System.out.println("test");
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
