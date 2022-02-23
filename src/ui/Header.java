package ui;

import ui.header.HowPlay;
import ui.header.Statistics;
import ui.header.Title;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {

    public Header(String titleName) {
        setLayout(new FlowLayout());

        Title title = new Title(titleName);
        HowPlay howPlay = new HowPlay();
        Statistics stat = new Statistics();
        add(howPlay);
        add(Box.createHorizontalStrut(180));
        add(title);
        add(Box.createHorizontalStrut(180));
        add(stat);
    }
}
