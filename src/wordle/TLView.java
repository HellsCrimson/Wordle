package wordle;

import keyboard.KeyboardListener;
import ui.*;

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

    /** Create the different panels and add them to the frame
     * as well as create the KeyboardListener to listen the keystrokes*/
    public void createControls() {
        frame = new JFrame("Wordle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KeyboardListener keyboardListener = new KeyboardListener(controller);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(new Spacer(10));

        Header header = new Header("WORDLE", model);
        contentPane.add(header);

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

    /** Update called each time a key is pressed
     * call for the keyboard and grid color update
     * and call a check for the different rules*/
    public void update(java.util.Observable o, Object arg) {
        panelKeyboard.updateColorKeyboard();

        checkNeedLetter();
        checkNotValid();

        if (model.getEnterPressed()) {
            panelDisplay.colorGridBackground();
            model.changeEnterPressed();
            panelOption.setRestartButton(true);
        }

        checkWinner();
        checkLost();

        if (model.restarting) {
            panelDisplay.resetGrid();
            panelOption.refreshWord();
            panelOption.setRestartButton(false);
            model.restarting = false;
        }

        frame.repaint();
    }

    /** Display a message dialog if there are less than 5 letters */
    private void checkNeedLetter() {
        if (model.getKeyboard().needLetter) {
            model.getKeyboard().needLetter = false;
            JOptionPane.showMessageDialog(frame,
                    "You need to enter a 5 letter word",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Display a message dialog if the word is not valid */
    private void checkNotValid() {
        if (model.getKeyboard().notValid) {
            model.getKeyboard().notValid = false;
            JOptionPane.showMessageDialog(frame,
                    "Your word is not valid",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Display a message dialog if the player won
     * Ask if the player want to do another game
     * else close the game */
    private void checkWinner() {
        if (model.getWon()) {
            model.setWon(false);
            String[] optionButtons = {"Yes", "No"};
            int response = JOptionPane.showOptionDialog(frame,
                    "You won using " + model.indexBuffer + " attempt(s)\n"
                    + "Do you want to do another game?",
                    "Winner",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    optionButtons,
                    optionButtons[0]);

            if (response == JOptionPane.NO_OPTION) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else {
                controller.restartGame();
            }
        }
    }

    /** Display a message dialog if the player lost
     * Display what was the correct word to find
     * Ask if the player want to do another game
     * else close the game */
    private void checkLost() {
        if (model.getLost()) {
            model.setLost(false);
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
                controller.restartGame();
            }
        }
    }
}
