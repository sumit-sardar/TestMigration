package com.ctb.contentBridge.core.publish.tools;


import java.io.*;

import org.xml.sax.*;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.layout.Pipe;
import com.ctb.contentBridge.core.util.StreamResource;



abstract public class XMLResource {
    private byte[] bytes;

    public void load(File file) throws BusinessException {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new BusinessException("XML resource file not found "
                    + file.getName() + ":" + e.getMessage());
        }
        load(fis);
    }

    public void load(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Pipe pipe = new Pipe(is, os);

        pipe.run();
        this.bytes = os.toByteArray();
    }

    public void load(String findInLoaderOrFileSystem) throws BusinessException {
        StreamResource res = new StreamResource();

        try {
            load(res.getStream(findInLoaderOrFileSystem));
        } catch (IOException e) {
            throw new BusinessException("Could not locate "
                    + findInLoaderOrFileSystem + ":" + e.getMessage());
        }
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public InputStreamReader getInputStreamReader() {
        return new InputStreamReader(getInputStream());
    }

    public InputStreamReader getInputStreamReader(String encoding) throws BusinessException {
        try {
            return new InputStreamReader(getInputStream(), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("Specified Reader Encoding is unsupported: "
                    + e.getMessage());
        }
    }

    public InputSource getInputSource() {
        return new InputSource(getInputStreamReader());
    }

    public InputSource getInputSource(String encoding) throws BusinessException {
        return new InputSource(getInputStreamReader(encoding));
    }

}
