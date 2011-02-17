package com.ctb.util.executable;

import com.ctb.util.Pipe;
import com.ctb.common.tools.SystemException;

import java.io.*;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 11:31:21 AM
 * 
 *
 */
public class StreamSink implements Runnable {
        String name_;
        InputStream ins_;
        StringBuffer buf;

        StreamSink(String name, InputStream ins, StringBuffer buf) {
            name_ = name;
            ins_ = ins;
            this.buf = buf;

        }
        public void run() {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Pipe pipe = new Pipe(ins_,bos);
            pipe.run();
            buf.append(bos.toString());
            try {
                bos.close();
                ins_.close();
            } catch (IOException e) {
                throw new SystemException(e.getMessage(),e);
            }
        }
    }