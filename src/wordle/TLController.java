package src.wordle;

import src.keyboard.Key;

public class TLController {

    private TLModel model;
    private TLView view;

    public TLController(TLModel model) {
        this.model = model;
    }

    public void setView(TLView view) {
        this.view = view;
    }

    public void keyPressed(Key key) {
        //TODO
        model.getKeyboard().KeyPressed(key);
    }

    public void initialise() {
        model.initialise();
    }
}
