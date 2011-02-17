package com.ctb.common.tools;

import junit.framework.Assert;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import org.apache.commons.lang.StringUtils;

/**
 * User: mwshort
 * Date: Dec 17, 2003
 * Time: 9:45:00 AM
 * 
 *
 */
public class UnitTestUtils {


    public static final File runtimeTestDir = new File("build/test");
    public static final File masterTestDir = new File("testdata");
    public static final File dtdMasterDir = new File("etc/");
    public static final String dtd = IOUtils.DTD_NAME;
    public static final String entityLat1 = "xhtml-lat1.ent";
    public static final String entitySpecial = "xhtml-special.ent";
    public static final String entitySymbol = "xhtml-symbol.ent";
    public static Random random = new Random();

    public static void assertEquals(String expected, String actual) {
        if (expected == null || actual == null) {
            Assert.assertEquals(expected, actual);
        }
        if (!expected.equals(actual)) {
            int at = StringUtils.differenceAt(expected, actual);
            String expectedWindow = StringUtils.abbreviate
                    (expected, at - 10, 60);
            String actualWindow = StringUtils.abbreviate(actual, at - 10, 60);

            Assert.fail("Expected <" + expected + "> but was <" + actual + ">"
                    + "\nExpected: " + StringUtils.escape(expectedWindow)
                    + "\nActual  : " + StringUtils.escape(actualWindow));
        }
    }

    public static File copyFileToRuntimeTestDir(File source) {
        return copyFileToDir(source, runtimeTestDir);
    }

    public static File copyFileToDir(File source, File targetDir)  {
        targetDir.mkdirs();
        File target = new File(targetDir, source.getName());

        return copyFile(source, target);
    }

    public static File copyFile(File source, File target)  {
        return IOUtils.copyFile(source, target);
    }

    public static void runAllInDir(File dir, Runner runner) throws Exception {
        Map failures = new TreeMap();
        List fileList = allXmlFiles(dir);

        for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
            File file = (File) iterator.next();

            try {
                runner.run(file);
            } catch (Throwable e) {
                System.err.println("Error in file " + dir + ": "
                        + e.getMessage());
                e.printStackTrace();
                failures.put(file, e);
            }
        }

        Set failedFiles = failures.keySet();

        if (!failedFiles.isEmpty()) {
            System.out.println("" + failedFiles.size() + " Failures");
        }
        for (Iterator iterator = failedFiles.iterator(); iterator.hasNext();) {
            File file = (File) iterator.next();
            Exception e = (Exception) failures.get(file);

            System.out.println("" + file + ":" + e.getMessage());
        }
        Assert.assertTrue(failures.isEmpty());
    }

    public static List allXmlFiles(File dir) throws Exception {
        List fileList = new ArrayList();

        return allXmlFiles(dir, fileList);
    }

    public static List allXmlFiles(File dir, List fileList) throws Exception {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory()
                        || name.endsWith(".xml");
            }
        };
        File[] files = dir.listFiles(filter);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                if (file.isDirectory()) {
                    allXmlFiles(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    public static void assertEquals(byte[] expected, byte[] actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals("byte[] lengths not equal", expected.length, actual.length);

        for (int i = 0; i < actual.length; i++) {
            byte original = expected[i];
            byte read = actual[i];

            Assert.assertEquals(original, read);
        }
    }

    public static void assertEquals(char[] expectedChars, char[] actualChars) {
        Assert.assertNotNull(actualChars);
        Assert.assertEquals(expectedChars.length, actualChars.length);

        for (int i = 0; i < actualChars.length; i++) {

            char expected = expectedChars[i];
            char actual = actualChars[i];

            assertEquals("position " + i, expected, actual);
        }
    }

    public static void assertEquals(String message, char expected, char actual) {
        if (expected != actual) {
            Assert.fail(message + ": Expected " + charString(expected) + " but was "
                    + charString(actual));
        }
    }

    public static String charString(char ch) {
        return "" + (int) ch + ":" + StringUtils.escape("" + ch);
    }

    public interface Runner {
        void run(File file) throws Throwable;
    }
}
