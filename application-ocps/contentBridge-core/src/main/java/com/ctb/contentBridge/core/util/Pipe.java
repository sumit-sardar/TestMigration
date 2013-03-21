package com.ctb.contentBridge.core.util;

import java.io.*;

/**
 * utility for mapping the contents of an input stream
 * into an output stream.  may or may not be used in a <code>Thread</code>
 */
public class Pipe implements Runnable {
    private InputStream in;
    private OutputStream out;

    private int bufferSize = 1024;

    /**
     * Constructor
     * @param in stream to read from
     * @param out stream to write to
     */
    public Pipe(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public Pipe(InputStream in, OutputStream out, int bufferSize) {
        this(in, out);
        this.bufferSize = bufferSize;
    }

    /**
     * start reading and writing
     */
    public void run() {
        try {
            byte[] buf = new byte[bufferSize];
            int pos;

            while ((pos = in.read(buf)) != -1) {
                out.write(buf, 0, pos);
                out.flush();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
