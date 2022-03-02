package ui.headerElements;

import data.StatsWriter;
import wordle.TLModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Statistics extends JButton {

    private TLModel model;

    private int played;
    private int win;
    private int streak;
    private int maxStreak;
    private int[] guesses = new int[6];;

    public Statistics(TLModel model) {
        this.model = model;
        // Free Icon from Awesome Fonts
        Icon icon = new ImageIcon("resources/icons/square-poll-vertical-solid-resized.png", "Statistics");
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setFocusable(false);
        setIcon(icon);
        setPreferredSize(new Dimension(20, 23));
        Arrays.fill(guesses, 0);
        addActionListener((ActionEvent e) -> {
            model.getStatWriter().getStatFromFile();
            loadData();
            JOptionPane.showMessageDialog(null, createMessage(), "Statistics", JOptionPane.PLAIN_MESSAGE);
        });
    }

    private JPanel createMessage() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;

        Title title = new Title("STATISTICS", 20);
        panel.add(title, constraints);
        constraints.gridy += 1;

        panel.add(getStats(), constraints);
        constraints.gridy += 1;

        panel.add(new Title("GUESS DISTRIBUTION", 20), constraints);
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

        JTextField winRate = new JTextField("Win Rate " + (int)(safeDiv(this.win, this.played) * 100) + "%");
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

    private double safeDiv(double a, double b) {
        if (b == 0)
            return 0;
        return a / b;
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

        for (int i = 0; i < guesses.length; i++) {
            JTextField nbGuess = new JTextField((i + 1) + ": ");
            nbGuess.setBorder(null);
            nbGuess.setEditable(false);
            nbGuess.setBackground(null);

            JTextField textPane = new JTextField(String.valueOf(guesses[i]));
            textPane.setBorder(null);
            textPane.setEditable(false);
            textPane.setBackground(null);

            JProgressBar progessBar = new JProgressBar(0, win);
            progessBar.setValue(Integer.parseInt(textPane.getText()));

            c.gridx = 0;
            distrib.add(nbGuess, c);
            c.gridx = 1;
            distrib.add(textPane, c);
            c.gridx = 2;
            distrib.add(progessBar, c);
            c.gridy += 1;
        }

        return distrib;
    }

    private void loadData() {
        StatsWriter sw = model.getStatWriter();
        played = sw.getPlayed();
        win = sw.getWin();
        streak = sw.getStreak();
        maxStreak = sw.getMaxStreak();
        guesses = sw.getGuesses();
    }
}
