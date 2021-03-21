package com.domain.evernet;

import com.domain.evernet.model.FileManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests of the class FileManager
 */
public class FileManagerTest {

    @Test
    public void intToStringSmallIntTest() {
        FileManager f = new FileManager();
        String s = f.intToString(1);
        assertEquals("01", s);
    }

    @Test
    public void intToStringBigIntTest() {
        FileManager f = new FileManager();
        String s = f.intToString(123456789);
        assertEquals("123456789", s);
    }

    @Test
    public void getNextFragmentTest() {
        FileManager f = new FileManager();
        f.setImageString(
                "lzaiohvnklgpjogenjp48a5fzagatg456t1+zzfe45a6gafz5e4f6z4g54u5è(à_(èr_(urjszirhfàhnhozefnfzeifnzenfziofoze8az8d4");
        String s = f.getNextFragment();
        assertEquals("lzaiohvnklgpjogenjp48a5fzagatg456t1+zzfe45a6gafz5e4f6z4g54u5è(à_(èr_(urjszirhfàh", s);
    }
}