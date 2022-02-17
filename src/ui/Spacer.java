package src.ui;

import javax.swing.*;
import java.awt.*;

public class Spacer extends JTextField {

    public Spacer(int size) {
        setFocusable(false);
        setBackground(null);
        setBorder(null);
        setFont(new Font(null, Font.PLAIN, size));
        setEditable(false);
    }
}
