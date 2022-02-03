package src.wordle;

import src.keyboard.Keyboard;

import java.util.Observer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TLView implements Observer {

    private static final Dimension PANEL_SIZE = new Dimension(400, 400);

    private final TLModel model;
    private final TLController controller;
    private JFrame frame;
    private JPanel panelDisplay;
    private JPanel panelKeyboard10; // 10 keys row
    private JPanel panelKeyboard9; // 9 keys row

    private final JTextField letterArea[][] = new JTextField[6][5];
    private JButton keyboard[][];

    public TLView(TLModel model, TLController controller)  {
        this.model = model;
        model.addObserver(this);
        this.controller = controller;
        createControls();
        controller.setView(this);
        update(model, null);
    }

    public void createControls()
    {
        frame = new JFrame("Wordle by Matthias Rauline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        createPanel();
        contentPane.add(panelDisplay);
        createKeyboard();
        contentPane.add(panelKeyboard10);
        contentPane.add(panelKeyboard9);


        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        frame.repaint();
    }

    private void createPanel() {
        panelDisplay = new JPanel();
        panelDisplay.setLayout(new GridLayout(0,5));

        createGrid();

        panelDisplay.setPreferredSize(PANEL_SIZE);
    }

    private void createGrid() {
        for (int y = 0; y < letterArea.length; y++) {
            for (int x = 0; x < letterArea[y].length; x++) {
                letterArea[y][x] = new JTextField("");
                letterArea[y][x].setEditable(false);
                panelDisplay.add(letterArea[y][x]);
            }
        }
    }

    private void createKeyboard() {
        panelKeyboard10 = new JPanel();
        panelKeyboard10.setLayout(new GridLayout(0,10));
        panelKeyboard9 = new JPanel();
        panelKeyboard9.setLayout(new GridLayout(0,9));

        Keyboard kb = model.getKeyboard();
        keyboard = new JButton[kb.getKeys().length][kb.getMaxRowKeys()];
        for (int y = 0; y < keyboard.length; y++) {
            for (int x = 0; x < keyboard[y].length; x++) {
                if (kb.getKeys()[y][x] != null) {
                    keyboard[y][x] = new JButton(kb.getKeys()[y][x].getLetter());
                    int finalY = y;
                    int finalX = x;
                    keyboard[y][x].addActionListener((ActionEvent e) -> {
                        controller.keyPressed(kb.getKeys()[finalY][finalX]);
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
}
