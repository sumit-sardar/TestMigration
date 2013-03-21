package com.ctb.contentBridge.core.util;


import java.io.*;
import java.util.*;

import org.apache.commons.lang.*;


/**
 * utilities for creating diff reports from lists
 */
public class FileListUtils {

    public static void listToFile(List list, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                String item = (String) iterator.next();

                writer.println(item);
            }
            writer.flush();
            writer.close();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("unable to write list to fileName");
        }
    }

    public static List fileToList(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            List list = new ArrayList();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                line = StringUtils.chomp(line, "\u00A0");
                list.add(line);
            }
            reader.close();
            return list;
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("unable to build list from file");
        }
    }
}
