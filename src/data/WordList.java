package src.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class WordList {

    public int length;
    private final String[] data;

    public WordList(String filePath) {
        ArrayList<String> tmpData = new ArrayList<>();
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                tmpData.add(scanner.nextLine().toUpperCase());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while loading file");
            e.printStackTrace();
        }
        data = tmpData.toArray(new String[0]);
        length = data.length;
    }

    public String[] getData() { return data; }

    public void sort() {
        Arrays.sort(data);
    }

    public String dataAt(int index) {
        return data[index];
    }

    public int search(String toFind) {
        return Arrays.binarySearch(data, toFind);
    }
}
