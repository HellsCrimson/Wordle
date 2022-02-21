package ui;

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
}
