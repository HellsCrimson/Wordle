package wordle;

public class TLController {

    private TLModel model;
    private TLView view;

    public TLController(TLModel model) {
        this.model = model;
    }

    public void setView(TLView view) {
        this.view = view;
    }

    public void keyPressed() { //TODO
        model.getKeyboard().KeyPreesed();
    }

    public void initialise() {
        model.initialise();
    }
}
