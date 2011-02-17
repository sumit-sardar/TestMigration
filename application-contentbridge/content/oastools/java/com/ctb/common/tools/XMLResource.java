package com.ctb.common.tools;


import java.io.*;

import org.xml.sax.*;

import com.ctb.util.*;


abstract public class XMLResource {
    private byte[] bytes;

    public void load(File file) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new SystemException("XML resource file not found "
                    + file.getName() + ":" + e.getMessage(),
                    e);
        }
        load(fis);
    }

    public void load(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Pipe pipe = new Pipe(is, os);

        pipe.run();
        this.bytes = os.toByteArray();
    }

    public void load(String findInLoaderOrFileSystem) {
        StreamResource res = new StreamResource();

        try {
            load(res.getStream(findInLoaderOrFileSystem));
        } catch (IOException e) {
            throw new SystemException("Could not locate "
                    + findInLoaderOrFileSystem + ":" + e.getMessage(),
                    e);
        }
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public InputStreamReader getInputStreamReader() {
        return new InputStreamReader(getInputStream());
    }

    public InputStreamReader getInputStreamReader(String encoding) {
        try {
            return new InputStreamReader(getInputStream(), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new SystemException("Specified Reader Encoding is unsupported: "
                    + e.getMessage(),
                    e);
        }
    }

    public InputSource getInputSource() {
        return new InputSource(getInputStreamReader());
    }

    public InputSource getInputSource(String encoding) {
        return new InputSource(getInputStreamReader(encoding));
    }

}
