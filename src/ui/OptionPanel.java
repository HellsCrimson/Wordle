package ui;

import keyboard.KeyboardListener;
import wordle.TLController;
import wordle.TLModel;
import wordle.TLView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OptionPanel extends JPanel {

    private final TLModel model;
    private final TLController controller;
    private final TLView view;

    private JTextField answerField;
    private JButton restartButton;

    public OptionPanel(TLModel model, TLController controller, TLView view, KeyboardListener keyboardListener) {
        this.model = model;
        this.controller = controller;
        this.view = view;
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.ipadx = 40;

        //createSeedPanel();
        createFalseWordPanel(constraint, keyboardListener);
        constraint.gridx = 2;
        createAnswerPanel(constraint, keyboardListener);
        constraint.gridx = 4;
        createSetWordPanel(constraint, keyboardListener);
        constraint.gridx = 6;
        createRestartButton(constraint, keyboardListener);
    }

    private void createSetWordPanel(GridBagConstraints constraints, KeyboardListener keyboardListener) {
        JCheckBox setWord = new JCheckBox("Random Answer");
        setWord.setSelected(true);
        setWord.addActionListener((ActionEvent e) -> {
            model.setIsFixedWord(!model.isFixedWord());
            refreshWord();
        });
        setWord.addKeyListener(keyboardListener);
        setWord.setFocusTraversalKeysEnabled(false);
        add(setWord, constraints);
    }

    private void createAnswerPanel(GridBagConstraints constraints, KeyboardListener keyboardListener) {
        JPanel answerPanel = new JPanel();

        answerField = new JTextField("");
        answerField.setHorizontalAlignment(SwingConstants.CENTER);
        answerField.setPreferredSize(new Dimension(60, 20));
        answerField.setEditable(false);
        answerField.setFocusable(false);

        JCheckBox showAnswer = new JCheckBox("Show answer");
        showAnswer.addActionListener((ActionEvent e) -> {
            if (answerField.getText().equals(""))
                answerField.setText(model.getWordToFind());
            else
                answerField.setText("");
            model.changeShowAnswer();
        });
        showAnswer.addKeyListener(keyboardListener);
        showAnswer.setFocusTraversalKeysEnabled(false);
        answerPanel.add(showAnswer);
        answerField.addKeyListener(keyboardListener);
        answerField.setFocusTraversalKeysEnabled(false);
        answerPanel.add(answerField);

        add(answerPanel, constraints);
    }

    private void createFalseWordPanel(GridBagConstraints constraints, KeyboardListener keyboardListener) {
        JCheckBox falseWord = new JCheckBox("Error On False Word");
        falseWord.setSelected(true);
        falseWord.addActionListener((ActionEvent e) -> {
            model.changeNeedBeValid();
        });
        falseWord.addKeyListener(keyboardListener);
        falseWord.setFocusTraversalKeysEnabled(false);
        add(falseWord, constraints);
    }

    private void createRestartButton(GridBagConstraints constraints, KeyboardListener keyboardListener) {
        restartButton = new JButton("Restart");
        restartButton.setEnabled(false);
        restartButton.addActionListener((ActionEvent e) -> {
            model.restartGame();
        });
        restartButton.addKeyListener(keyboardListener);
        restartButton.setFocusTraversalKeysEnabled(false);
        add(restartButton, constraints);
    }

    public void setRestartButton(boolean state) {
        restartButton.setEnabled(state);
    }

    public void refreshWord() {
        answerField.setText(model.getWordToFind());
    }
}
