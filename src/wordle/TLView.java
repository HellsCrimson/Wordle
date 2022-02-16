package src.wordle;

import src.keyboard.KeyboardListener;
import src.ui.GridPanel;
import src.ui.KeyboardPanel;
import src.ui.OptionPanel;
import src.ui.Title;

import java.util.Observer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TLView implements Observer {

    private final TLModel model;
    private final TLController controller;

    private JFrame frame;
    private GridPanel panelDisplay;
    private OptionPanel panelOption;
    private KeyboardPanel panelKeyboard;

    public GridPanel getDisplayPanel() {return panelDisplay; }

    public TLView(TLModel model, TLController controller)  {
        // Load the CrossPlatformLook on mac
        /*try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        this.model = model;
        this.controller = controller;
        createControls();
        controller.setView(this);
        model.addObserver(this);
        update(model, null);
    }

    public void createControls() {
        frame = new JFrame("Wordle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KeyboardListener keyboardListener = new KeyboardListener(controller);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        Title title = new Title();
        contentPane.add(title);

        panelDisplay = new GridPanel(model, controller, this);
        contentPane.add(panelDisplay);

        panelOption = new OptionPanel(model, controller, this, keyboardListener);
        contentPane.add(panelOption);

        panelKeyboard = new KeyboardPanel(model, controller, this);
        contentPane.add(panelKeyboard);


        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        panelKeyboard.updateColorKeyboard();

        checkNeedLetter();
        checkNotValid();

        if (model.getEnterPressed()) {
            panelDisplay.colorGridBackground();
            model.changeEnterPressed();
        }

        checkWinner();
        checkLost();

        frame.repaint();
    }

    private void checkNeedLetter() {
        if (model.getKeyboard().needLetter) {
            model.getKeyboard().needLetter = false;
            JOptionPane.showMessageDialog(frame,
                    "You need to enter a 5 letter word",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkNotValid() {
        if (model.getKeyboard().notValid) {
            model.getKeyboard().notValid = false;
            JOptionPane.showMessageDialog(frame,
                    "Your word is not valid",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkWinner() {
        if (model.getKeyboard().won) {
            model.getKeyboard().won = false;
            String[] optionButtons = {"Yes", "No"};
            int response = JOptionPane.showOptionDialog(frame,
                    "You won using " + model.indexBuffer + " attempt(s)\n"
                    + "Do you want to restart the game?",
                    "Winner",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    optionButtons,
                    optionButtons[0]);

            if (response == JOptionPane.NO_OPTION) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else {
                // TODO restart game
                // new word to find
                controller.changeWord();
            }
        }
    }

    private void checkLost() {
        if (model.getKeyboard().lost) {
            model.getKeyboard().lost = false;
            String[] optionButtons = {"Yes", "No"};
            int response = JOptionPane.showOptionDialog(frame,
                    "You lost. The correct word was: " + model.getWordToFind() + "\n"
                            + "Do you want to restart the game?",
                    "Lost",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    optionButtons,
                    optionButtons[0]);

            if (response == JOptionPane.NO_OPTION) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else {
                // TODO restart game
                // new word to find
                controller.changeWord();
            }
        }
    }
}
