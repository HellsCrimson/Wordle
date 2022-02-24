package ui.headerElements;

import javax.swing.*;
import java.awt.*;

public class Title extends JTextField {

    public Title(String title) {
        setText(title);
        setFocusable(false);
        setHorizontalAlignment(JTextField.CENTER);
        setBackground(null);
        setBorder(null);
        setFont(new Font(null, Font.PLAIN, 60));
        setEditable(false);
    }

    public Title(String title, int size) {
        setText(title);
        setFocusable(false);
        setHorizontalAlignment(JTextField.CENTER);
        setBackground(null);
        setBorder(null);
        setFont(new Font(null, Font.PLAIN, size));
        setEditable(false);
    }
}
