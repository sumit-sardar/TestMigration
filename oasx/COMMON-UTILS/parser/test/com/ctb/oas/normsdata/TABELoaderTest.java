package com.ctb.oas.normsdata;

import java.io.File;
import java.io.StringWriter;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TABELoaderTest extends NormsDataTestCase {
    protected File in_dir = new File("test/data/tabe");
    protected File tm_dir = new File("test/data/tabe/totma");
    private File eged_dir = new File("test/data/tabe/eged");
    private StringWriter writer = new StringWriter();
    private File ss_to_ge_dir = new File("test/data/tabe/ge");

    public void testCVSFileNameFilter() {
        final File[] files = in_dir.listFiles(new TABELoader.CSVFileNameFilter());
        assertEquals(4, files.length);
    }

    public void testLoadForIndividualSubTest() {
        runLoader(in_dir);
    }

    public void testLoadForTotalMath() {
        runLoader(tm_dir);
    }

    public void testLoadForEGED() {
        runLoader(eged_dir);
    }


    public void testLoadForSSToGE() {
        runLoader(ss_to_ge_dir);
    }

    private void runLoader(File dir) {
        assertTrue(dir.exists());
        TABELoader loader = new TABELoader(dir);
        writer = new StringWriter();
        loader.load(writer);
//        String output = writer.toString();
//        System.out.println(output);
    }
}