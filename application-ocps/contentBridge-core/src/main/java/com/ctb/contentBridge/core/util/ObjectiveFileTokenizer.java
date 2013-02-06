package com.ctb.contentBridge.core.util;


import java.io.*;
import java.util.*;


public class ObjectiveFileTokenizer {
    protected String text;
    protected static final String DELIMITER = "::";

    public ObjectiveFileTokenizer(String text) {
        this.text = text.trim();
    }

    public String nextToken() {
        if (text.length() == 0) {
            throw new NoSuchElementException();
        }

        int pos = text.indexOf("::");

        if (pos >= 0) {
            String token = StripQuotes(text.substring(0, pos));

            text = text.substring(pos + DELIMITER.length());
            return token;
        } else {
            String token = StripQuotes(text);

            text = "";
            return token;
        }
    }

    public boolean hasMoreTokens() {
        return (text.length() > 0);
    }

    private String StripQuotes(String s) {
        s = s.trim();
        if (s.startsWith("\"")) {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '\"') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static void main(String[] args) {
        try {
            File file = new File("../mappingdata/colorado/objectives.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    ObjectiveFileTokenizer toker = new ObjectiveFileTokenizer(line);

                    System.out.print(toker.nextToken());
                    System.out.print("\t" + toker.nextToken());
                    System.out.print("\t" + toker.nextToken());
                    System.out.println("\t" + toker.nextToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // To change body of catch statement use Options | File Templates.
        }
    }
}
