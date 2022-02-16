package src.wordle;

public class WordleCLI {
    public static void main(String[] args) {
        TLModel model = new TLModel();
        TLController controller = new TLController(model);
        TLCli cli = new TLCli(model, controller);
        cli.startGame();
    }
}
