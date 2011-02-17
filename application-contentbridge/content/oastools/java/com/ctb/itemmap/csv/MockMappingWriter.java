package com.ctb.itemmap.csv;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


public class MockMappingWriter extends TestCase implements MappingWriter {

    int currentLine = 0;
    List expectedEntries = new ArrayList();
    List actualEntries = new ArrayList();

    public void setExpectedEntry(String entry) {
        expectedEntries.add(entry);
    }

    public void verify() {
        assertEquals(expectedEntries.size(), actualEntries.size());

        for (int idx = 0; idx < expectedEntries.size(); idx++) {
            String expectedEntry = (String) expectedEntries.get(idx);
            String actualEntry = (String) actualEntries.get(idx);
            
            assertEquals(expectedEntry, actualEntry);
        }
    }

    public void writeLine(String line) {
        actualEntries.add(line);
    }

    public void close() {
    	// do nothing
    }

}
