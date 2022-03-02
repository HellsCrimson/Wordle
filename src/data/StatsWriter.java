package data;

import wordle.TLModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class StatsWriter {

    private int played;
    private int win;
    private int streak;
    private int maxStreak;
    private int[] guesses;

    public int getPlayed() {
        return played;
    }

    public int getWin() {
        return win;
    }

    public int getStreak() {
        return streak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public int[] getGuesses() {
        return guesses;
    }

    public StatsWriter() {
        guesses = new int[6];
        Arrays.fill(guesses, 0);
        getStatFromFile();
    }

    private void setStatInFile() {
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

    public void getStatFromFile() {
        try {
            File statFile = new File("resources/stats.txt");

            if (statFile.createNewFile()) {
                setup();
            }

            Scanner myReader = new Scanner(statFile);
            int index = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                try {
                    switch (index) {
                        case 0 -> played = Integer.parseInt(data);
                        case 1 -> win = Integer.parseInt(data);
                        case 2 -> streak = Integer.parseInt(data);
                        case 3 -> maxStreak = Integer.parseInt(data);
                        default -> guesses[index - 4] = Integer.parseInt(data);
                    }
                } catch (Exception e) {
                    resetStats();
                    return;
                }
                index++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addData(boolean won, int nbTries) {
        if (won) {
            win++;
            streak++;
            if (streak > maxStreak)
                maxStreak = streak;
            guesses[nbTries] += 1;
        } else {
            streak = 0;
        }
        played++;
        setStatInFile();
    }

    private void setup() {
        try {
            FileWriter statFile = new FileWriter("resources/stats.txt");
            statFile.write(0 + "\n");
            statFile.write(0 + "\n");
            statFile.write(0 + "\n");
            statFile.write(0 + "\n");
            for (int i = 0; i < guesses.length; i++)
                statFile.write(0 + "\n");
            statFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing in the file.");
            e.printStackTrace();
        }
    }

    private void resetStats() {
        played = 0;
        win = 0;
        streak = 0;
        maxStreak = 0;
        Arrays.fill(guesses, 0);
        setStatInFile();
        getStatFromFile();
    }
}
