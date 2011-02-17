package com.ctb.common.tools;


import java.io.*;

import org.apache.commons.lang.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 17, 2003
 * Time: 4:43:46 PM
 * To change this template use Options | File Templates.
 */
public class CrashingText {

    static public String fixText(String input) {

        StringBuffer outBuf = new StringBuffer();

        try {
            writeWordsToBufferWithExtraSpaces(input, outBuf);
        } catch (IOException e) {
            throw new SystemException("Could not parse element content: "
                    + input);
        }
        return outBuf.toString();
    }

    static public void writeWordsToBufferWithExtraSpaces(String words, StringBuffer buf) throws IOException {
        CharArrayReader reader = new CharArrayReader(words.toCharArray());

        while (reader.ready()) {
            String word = readUntilEndOfWord(reader, buf);
            int countOfMatches = 0;

            countOfMatches += StringUtils.countMatches(word, "'");
            countOfMatches += StringUtils.countMatches(word, "-");
            for (int i = 0; i < countOfMatches; i++) {
                buf.append(' ');
            }
        }

    }

    static String readUntilEndOfWord(Reader reader, StringBuffer buf) throws IOException {
        char myChar = 'a';
        StringBuffer wordBuffer = new StringBuffer();

        while (reader.ready() && myChar != ' ') {
            myChar = (char) reader.read();
            wordBuffer.append(myChar);
            buf.append(myChar);
        }
        return wordBuffer.toString();
    }

}
