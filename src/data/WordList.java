package data;

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

    /** Sort the array */
    public void sort() {
        Arrays.sort(data);
    }

    /** Get the data at a certain index */
    public String dataAt(int index) {
        return data[index];
    }

    /** Do a binary search to find the data
     * If the data is found the index is returned
     * Else the index where it is supposed to be is returned as a negative */
    public int search(String toFind) {
        return Arrays.binarySearch(data, toFind);
    }
}
