package wordle;

public class WordleCli {
    public static void main(String[] args) {
        TLModel model = new TLModel();
        TLCli cli = new TLCli(model);
        cli.startGame();
    }
}
