package data;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class WordListTest {

    @Test
    public void getData() {
        WordList list = new WordList("src/resources/common.txt");
        assertEquals(2315, list.length);
    }

    @Test
    public void getDataError() {
        WordList list = new WordList("");
        assertEquals(list.length, 0);
    }

    @Test
    public void sort() {
        WordList list = new WordList("src/resources/common.txt");
        list.sort();
        boolean sorted = true;
        int i = 0;
        while (sorted && i < list.length - 1) {
            sorted = list.dataAt(i).compareTo(list.dataAt(i + 1)) < 0;
            i++;
        }
        assertTrue(sorted);
    }

    @Test
    public void dataAt() {
        WordList list = new WordList("src/resources/common.txt");
        assertEquals(list.dataAt(0), "CIGAR");
    }

    @Test
    public void searchTrue() {
        WordList list = new WordList("src/resources/common.txt");
        list.sort();
        assertTrue(list.search("SPOOK") >= 0);
    }

    @Test
    public void searchFalse() {
        WordList list = new WordList("src/resources/common.txt");
        assertFalse(list.search("SPOOKY") >= 0);
    }
}