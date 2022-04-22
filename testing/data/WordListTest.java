package data;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordListTest {

    private final String path = "../resources/common.txt";

    @Test
    public void getData() {
        WordList list = new WordList(path);
        assertEquals(2315, list.length);
    }

    @Test
    public void sort() {
        WordList list = new WordList(path);
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
        WordList list = new WordList(path);
        assertEquals("CIGAR", list.dataAt(0));
    }

    @Test
    public void searchTrue() {
        WordList list = new WordList(path);
        list.sort();
        assertTrue(list.search("SPOOK") >= 0);
    }

    @Test
    public void searchFalse() {
        WordList list = new WordList(path);
        assertFalse(list.search("SPOOKY") >= 0);
    }
}