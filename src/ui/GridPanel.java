package src.ui;

import src.wordle.TLController;
import src.wordle.TLModel;
import src.wordle.TLView;

import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {

    private static final Dimension PANEL_SIZE = new Dimension(800, 500);
    private static final Dimension SQUARE_SIZE = new Dimension(80, 80);

    private final TLModel model;
    private final TLController controller;
    private final TLView view;

    private final JTextField[][] letterArea = new JTextField[6][5];

    public GridPanel(TLModel model, TLController controller, TLView view) {
        this.model = model;
        this.controller = controller;
        this.view = view;

        setLayout(new GridBagLayout());

        createGrid();

        setPreferredSize(PANEL_SIZE);
    }

    private void createGrid() {
        GridBagConstraints constraints = new GridBagConstraints();
        for (int y = 0; y < letterArea.length; y++) {
            for (int x = 0; x < letterArea[y].length; x++) {
                constraints.gridx = x;
                constraints.gridy = y;

                letterArea[y][x] = new JTextField("");
                letterArea[y][x].setPreferredSize(SQUARE_SIZE);
                letterArea[y][x].setHorizontalAlignment(SwingConstants.CENTER);
                letterArea[y][x].setEditable(false);
                letterArea[y][x].setBackground(Color.WHITE);
                letterArea[y][x].setFocusable(false);
                add(letterArea[y][x], constraints);
            }
        }
    }

    public void displayWords() {
        int indexBuffer = model.indexBuffer + 1;
        if (indexBuffer > 6)
            indexBuffer = 6;
        for (int y = 0; y < indexBuffer; y++) {
            for (int x = 0; x < model.wordsBuffer[y].length(); x++) {
                letterArea[y][x].setText(String.valueOf(model.wordsBuffer[y].charAt(x)));
            }
            for (int x = model.wordsBuffer[y].length(); x < 5; x++) {
                letterArea[y][x].setText("");
            }
        }
    }

    public void colorGridBackground() {
        String[] correctLetters = model.getCorrectLetters();
        String[] correctPlaceLetters = model.getCorrectPlaceLetters();

        int currIndex = model.indexBuffer - 1; // index already + 1 at this point
        if (currIndex < 0)
            currIndex = 0;

        for (int i = 0; i < letterArea[currIndex].length; i++) { // All line in gray
            letterArea[currIndex][i].setBackground(Color.GRAY);
        }

        cleanArr(correctPlaceLetters, correctLetters);
        colorLine(correctLetters, currIndex, Color.YELLOW);
        colorLine(correctPlaceLetters, currIndex, Color.GREEN);
    }

    private void colorLine(String[] correctLetters, int index, Color color) {
        for (int i = 0; i < correctLetters.length; i++) {
            if (correctLetters[i] != null) {
                letterArea[index][i].setBackground(color);
            }
        }
    }

    private void cleanArr(String[] correctPlaceLetters, String[] correctLetters) {
        int[] indexLetter = new int[correctPlaceLetters.length];
        for (int i = 0; i < correctPlaceLetters.length; i++) {
            if (correctPlaceLetters[i] != null && correctLetters[i] != null) {
                indexLetter[i] = i;
            } else {
                indexLetter[i] = -1;
            }
        }
        for (int i : indexLetter) {
            if (i != -1) {
                for (int y = 0; y < correctLetters.length; y++) {
                    if (indexLetter[y] == -1 && correctLetters[y] != null && correctLetters[y].equals(correctPlaceLetters[i])) {
                        correctLetters[y] = null;
                    }
                }
            }
        }
    }
}
