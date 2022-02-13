package src.wordle;

import src.game.Game;
import src.keyboard.Keyboard;
import src.keyboard.KeyboardListener;

import java.util.Observer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class TLView implements Observer {

    private static final Dimension PANEL_SIZE = new Dimension(800, 400);
    private static final Dimension KEYBOARD_SIZE = new Dimension(800, 50);
    private static final int PADDING = 8;

    private final TLModel model;
    private final TLController controller;

    private JFrame frame;
    private JPanel panelDisplay;
    private JPanel panelKeyboard10; // 10 keys row
    private JPanel panelKeyboard9; // 9 keys row
    private JPanel panelOption;

    private final JTextField[][] letterArea = new JTextField[6][5];
    private int letterXAxis = 0;
    private int letterYAxis = 0;

    private JButton[][] keyboard;

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

    public int getLetterXAxis() {
        return letterXAxis;
    }

    public void setLetterXAxis(int letterXAxis) {
        this.letterXAxis = letterXAxis;
    }

    public int getLetterYAxis() {
        return letterYAxis;
    }

    public void setLetterYAxis(int letterYAxis) {
        this.letterYAxis = letterYAxis;
    }

    public void createControls()
    {
        frame = new JFrame("Wordle by Matthias Rauline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KeyboardListener keyboardListener = new KeyboardListener(controller);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        createPanel();
        contentPane.add(panelDisplay);

        createOption(keyboardListener);
        contentPane.add(panelOption);

        createKeyboard();
        JPanel panelKeyboard = new JPanel();
        panelKeyboard.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        panelKeyboard.add(panelKeyboard10, constraints);
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.insets = new Insets(PADDING,0,0,0);
        panelKeyboard.add(panelKeyboard9, constraints);
        contentPane.add(panelKeyboard);


        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        checkWinner();
        checkNeedLetter();
        checkNotValid();

        updateColorKeyboard();
        if (model.getGame().getEnterPressed()) {
            colorGridBackground();
            model.getGame().changeEnterPressed();
        }
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
                    "You won using " + model.getGame().indexBuffer + " attempt(s)\n"
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

    private void updateColorKeyboard() {
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

    private void createOption(KeyboardListener keyboardListener) {
        panelOption = new JPanel();
        panelOption.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.ipadx = 40;

        //createSeedPanel();
        createFalseWordPanel(constraint, keyboardListener);
        constraint.gridx = 2;
        createAnswerPanel(constraint, keyboardListener);
        constraint.gridx = 4;
        createSetWordPanel(constraint, keyboardListener);
    }

    private void createSetWordPanel(GridBagConstraints constraints, KeyboardListener keyboardListener) {
        JCheckBox setWord = new JCheckBox("Set Answer");
        setWord.addActionListener((ActionEvent e) -> {
            model.getGame().setIsFixedWord(!model.getGame().isFixedWord());
        });
        setWord.addKeyListener(keyboardListener);
        setWord.setFocusTraversalKeysEnabled(false);
        panelOption.add(setWord, constraints);
    }

    private void createAnswerPanel(GridBagConstraints constraints, KeyboardListener keyboardListener) {
        JPanel answerPanel = new JPanel();

        JTextField answerField = new JTextField("");
        answerField.setHorizontalAlignment(SwingConstants.CENTER);
        answerField.setPreferredSize(new Dimension(60, 20));
        answerField.setEditable(false);
        answerField.setFocusable(false);

        JCheckBox showAnswer = new JCheckBox("Show answer");
        showAnswer.addActionListener((ActionEvent e) -> {
            if (answerField.getText().equals(""))
                answerField.setText(model.getGame().getWordToFind());
            else
                answerField.setText("");
        });
        showAnswer.addKeyListener(keyboardListener);
        showAnswer.setFocusTraversalKeysEnabled(false);
        answerPanel.add(showAnswer);
        answerField.addKeyListener(keyboardListener);
        answerField.setFocusTraversalKeysEnabled(false);
        answerPanel.add(answerField);

        panelOption.add(answerPanel, constraints);
    }

    private void createFalseWordPanel(GridBagConstraints constraints, KeyboardListener keyboardListener) {
        JCheckBox falseWord = new JCheckBox("Error On False Word");
        falseWord.setSelected(true);
        falseWord.addActionListener((ActionEvent e) -> {
            model.getKeyboard().needBeValid = !model.getKeyboard().needBeValid;
        });
        falseWord.addKeyListener(keyboardListener);
        falseWord.setFocusTraversalKeysEnabled(false);
        panelOption.add(falseWord, constraints);
    }

    private void createSeedPanel(GridBagConstraints constraints) {
        JPanel seedPanel = new JPanel();
        JTextField seedText = new JTextField("The seed is:");
        seedText.setEditable(false);
        seedText.setBorder(null);
        seedPanel.add(seedText);
        JTextField seedArea = new JTextField(String.valueOf(model.getGame().getSeed()));
        
        seedArea.addActionListener((ActionEvent e) -> {
            try {
                System.out.println(Integer.parseInt(seedArea.getText()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            // TODO set the new seed
        });
        seedPanel.add(seedArea);
        panelOption.add(seedPanel, constraints);
    }

    private void createPanel() {
        panelDisplay = new JPanel();
        panelDisplay.setLayout(new GridLayout(0,5, PADDING, PADDING));

        createGrid();

        panelDisplay.setPreferredSize(PANEL_SIZE);
    }

    private void createGrid() {
        for (int y = 0; y < letterArea.length; y++) {
            for (int x = 0; x < letterArea[y].length; x++) {
                letterArea[y][x] = new JTextField("");
                letterArea[y][x].setHorizontalAlignment(SwingConstants.CENTER);
                letterArea[y][x].setEditable(false);
                letterArea[y][x].setBackground(Color.WHITE);
                letterArea[y][x].setFocusable(false);
                panelDisplay.add(letterArea[y][x]);
            }
        }
    }

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
                        displayWords();
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

    public void displayWords() {
        Game game = model.getGame();
        for (int y = 0; y <= game.indexBuffer; y++) {
            for (int x = 0; x < game.wordsBuffer[y].length(); x++) {
                letterArea[y][x].setText(String.valueOf(game.wordsBuffer[y].charAt(x)));
            }
            for (int x = game.wordsBuffer[y].length(); x < 5; x++) {
                letterArea[y][x].setText("");
            }
        }
    }

    public void colorGridBackground() {
        String[] correctLetters = model.getGame().getCorrectLetters();
        String[] correctPlaceLetters = model.getGame().getCorrectPlaceLetters();

        int currIndex = model.getGame().indexBuffer - 1; // index already + 1 at this point

        for (int i = 0; i < letterArea[currIndex].length; i++) { // All line in gray
            letterArea[currIndex][i].setBackground(Color.GRAY);
        }

        colorLine(correctLetters, currIndex, Color.YELLOW);
        colorLine(correctPlaceLetters, currIndex, Color.GREEN);
    }

    private void colorLine(String[] correctLetters, int currIndex, Color color) {
        for (String letter : correctLetters) {
            if (letter != null) {
                for (int i = 0; i < letterArea[currIndex].length; i++) {
                    if (letterArea[currIndex][i].getText().equals(letter)) {
                        letterArea[currIndex][i].setBackground(color);
                        break;
                    }
                }
            }
        }
    }
}
