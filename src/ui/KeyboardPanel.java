package ui;

import keyboard.Keyboard;
import wordle.TLController;
import wordle.TLModel;
import wordle.TLView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class KeyboardPanel extends JPanel {

    private static final Dimension KEYBOARD_SIZE = new Dimension(800, 50);
    private static final int PADDING = 8;

    private final TLModel model;
    private final TLController controller;
    private final TLView view;

    private JPanel panelKeyboard10; // 10 keys row
    private JPanel panelKeyboard9; // 9 keys row

    private JButton[][] keyboard;

    public KeyboardPanel(TLModel model, TLController controller, TLView view) {
        this.model = model;
        this.controller = controller;
        this.view = view;
        createKeyboard();
        addKeyboardElements();
    }

    /** Add the two different panel of the keyboard together */
    private void addKeyboardElements() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        add(panelKeyboard10, constraints);
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.insets = new Insets(PADDING,0,0,0);
        add(panelKeyboard9, constraints);
    }

    /** Create the keyboard (visual) according to the keyboard (which has the states)
     * Each key has an action listener for the key press */
    private void createKeyboard() {
        panelKeyboard10 = new JPanel();
        panelKeyboard10.setLayout(new GridLayout(0,10, PADDING, PADDING));
        panelKeyboard10.setPreferredSize(KEYBOARD_SIZE);
        panelKeyboard9 = new JPanel();
        panelKeyboard9.setLayout(new GridLayout(0,9, PADDING, PADDING));
        panelKeyboard9.setPreferredSize(new Dimension(KEYBOARD_SIZE.width, KEYBOARD_SIZE.height * 2));

        Keyboard kb = model.getKeyboard();
        keyboard = new JButton[kb.getKeys().length][kb.getMaxRowKeys()];
        for (int y = 0; y < keyboard.length; y++) {
            for (int x = 0; x < keyboard[y].length; x++) {
                if (kb.getKeys()[y][x] != null) {
                    JButton button = new JButton(kb.getKeys()[y][x].getLetter());
                    button.setBorder(new EmptyBorder(0, 0, 0, 0));
                    keyboard[y][x] = button;
                    keyboard[y][x].setOpaque(true);
                    keyboard[y][x].setBackground(Color.WHITE);
                    keyboard[y][x].setFocusable(false);
                    int finalY = y;
                    int finalX = x;

                    keyboard[y][x].addActionListener((ActionEvent e) -> {
                        controller.keyPressed(kb.getKeys()[finalY][finalX]);
                        view.getDisplayPanel().displayWords();
                    });

                    if (y == 0) {
                        panelKeyboard10.add(keyboard[y][x]);
                    } else {
                        panelKeyboard9.add(keyboard[y][x]);
                    }
                }
            }
        }
    }

    /** Go through the keys to update the color of the keys */
    public void updateColorKeyboard() {
        for (int y = 0; y < keyboard.length; y++) {
            for (int x = 0; x < model.getKeyboard().getMaxRowKeys(); x++) {
                if (model.getKeyboard().getKeys()[y][x] != null) {
                    switch (model.getKeyboard().getKeys()[y][x].getState()) {
                        case RIGHT -> keyboard[y][x].setBackground(Color.GREEN);
                        case WRONG -> {
                            keyboard[y][x].setBackground(Color.DARK_GRAY);
                            keyboard[y][x].setEnabled(false);
                        }
                        case WRONG_PLACE -> keyboard[y][x].setBackground(Color.YELLOW);
                        default -> keyboard[y][x].setBackground(Color.WHITE);
                    }
                }
            }
        }
    }
}
