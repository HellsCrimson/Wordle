package wordle;

import cli.Colors;

import java.util.Scanner;

public class WordleCli {
    public static void main(String[] args) {
        TLModel model = new TLModel();
        TLCli cli = new TLCli(model);
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            cli.askFlags(scan);
            cli.startGame();
            model.restartGame();
            cli.restart();
        } while (cli.ask(scan, "Do you want to restart? (" + Colors.GREEN + "Yes" + Colors.RESET
                + "/" + Colors.RED + "No" + Colors.RESET + ") "));
        System.out.print("\033[H\033[2J"); // clear the terminal from the game
        System.out.flush();
    }
}
