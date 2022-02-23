package ui.header;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Statistics extends JButton {

    private int played;
    private int win;
    private int streak;
    private int maxStreak;
    private int[] guesses = new int[6];;

    public Statistics() {
        // Free Icon from Awesome Fonts
        Icon icon = new ImageIcon("resources/icons/square-poll-vertical-solid-resized.png", "Statistics");
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setFocusable(false);
        setIcon(icon);
        setPreferredSize(new Dimension(20, 23));
        Arrays.fill(guesses, 0);
        addActionListener((ActionEvent e) -> {
            getStatFromFile();
            JOptionPane.showMessageDialog(null, createMessage(), "Statistics", JOptionPane.PLAIN_MESSAGE);
        });
    }

    private JPanel createMessage() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;

        Title title = new Title("Statistics", 30);
        panel.add(title, constraints);
        constraints.gridy += 1;

        panel.add(getStats(), constraints);
        constraints.gridy += 1;

        panel.add(getGuessDistrib(), constraints);

        return panel;
    }

    private JPanel getStats() {
        JPanel stats = new JPanel();
        stats.setLayout(new GridBagLayout());
        stats.setPreferredSize(new Dimension(350, 50));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(0, 5, 0, 5);

        JTextField played = new JTextField("Played " + this.played);
        setParameter(played);
        stats.add(played, c);
        c.gridx++;

        JTextField winRate = new JTextField("Win Rate " + (int)(((double)this.win / (double)this.played) * 100) + "%");
        setParameter(winRate);
        stats.add(winRate, c);
        c.gridx++;

        JTextField streak = new JTextField("Streak " + this.streak);
        setParameter(streak);
        stats.add(streak, c);
        c.gridx++;

        JTextField maxStreak = new JTextField("Max Streak " + this.maxStreak);
        setParameter(maxStreak);
        stats.add(maxStreak, c);

        return stats;
    }

    private void setParameter(JTextField played) {
        played.setEditable(false);
        played.setBackground(null);
        played.setBorder(null);
    }

    private JPanel getGuessDistrib() {
        JPanel distrib = new JPanel();
        distrib.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        for (int guess : guesses) {
            JTextField textPane = new JTextField(String.valueOf(guess));
            textPane.setBorder(null);
            textPane.setEditable(false);
            textPane.setBackground(null);

            JProgressBar progessBar = new JProgressBar(0, win);
            progessBar.setValue(Integer.parseInt(textPane.getText()));

            c.gridx = 0;
            distrib.add(textPane, c);
            c.gridx = 1;
            distrib.add(progessBar, c);
            c.gridy += 1;
        }

        return distrib;
    }

    private void getStatFromFile() {
        try {
            File statFile = new File("resources/stats.txt");
            Scanner myReader = new Scanner(statFile);
            int index = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                switch (index) {
                    case 0 -> played = Integer.parseInt(data);
                    case 1 -> win = Integer.parseInt(data);
                    case 2 -> streak = Integer.parseInt(data);
                    case 3 -> maxStreak = Integer.parseInt(data);
                    default -> guesses[index - 4] = Integer.parseInt(data);
                }
                index++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

    }

    private void setStatInFile() {
        // TODO pass the model to add in the file the values
        try {
            FileWriter statFile = new FileWriter("resources/stats.txt");
            statFile.write(played + "\n");
            statFile.write(win + "\n");
            statFile.write(streak + "\n");
            statFile.write(maxStreak + "\n");
            for (int guess : guesses)
                statFile.write(guess + "\n");
            statFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing in the file.");
            e.printStackTrace();
        }
    }
}
