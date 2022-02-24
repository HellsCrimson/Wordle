package ui.headerElements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HowPlay extends JButton {

    private final String message = """
            Guess the WORDLE in six tries.

            Each guess must be a valid five-letter word (except if you deactivate the feature).
            Hit the enter button to submit.

            After each guess, the color of the tiles will change to show how close your guess was to the word.
            The letter in the correct spot is green.
            The letter in the wrong spot is yellow.
            Any other letter is gray.
            """;

    public HowPlay() {
        // Free Icon from Awesome Font
        Icon icon = new ImageIcon("resources/icons/circle-question-regular-resized.png", "Question mark");
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setIcon(icon);
        setFocusable(false);
        setPreferredSize(new Dimension(20, 20));
        addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(null, message, "How To Play", JOptionPane.PLAIN_MESSAGE);
        });
    }
}
