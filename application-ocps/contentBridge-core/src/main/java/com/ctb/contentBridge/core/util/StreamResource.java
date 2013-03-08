package com.ctb.contentBridge.core.util;


// import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;

import com.ctb.contentBridge.core.exception.SystemException;



/**
 * Stream resource searches URL's, classpaths, and filesystems for an InputStream
 */
public class StreamResource {
    // private static final Logger LOG = Logger.getLogger(StreamResource.class);
    private InputStream is;

    public StreamResource() {}

    public InputStream getStream(URL resourceLocator) throws IOException, SystemException {
        synchronized (this) {
            try {
                return getFromUrl(resourceLocator);
            } catch (Exception e) {
                cleanUp();
                throw new SystemException("Could not locate resource at URL");
            }

        }
    }

    public InputStream getStream(File resourceLocator) throws IOException, SystemException {
        synchronized (this) {
            try {
                return getFromFile(resourceLocator);
            } catch (Exception e) {
                cleanUp();
                throw new SystemException("Could not locate resource in file");
            }

        }
    }

    public InputStream getStream(String resourceLocator) throws IOException, SystemException {

        synchronized (this) {
            try {
                // LOG.debug("trying class loader");
                return getFromClassLoader(resourceLocator);
            } catch (Exception e) {
            }
            try {
                // LOG.debug("trying system class loader");
                return getFromSystemClassLoader(resourceLocator);
            } catch (Exception e) {
            }

            // try and get off file
            try {
                // LOG.debug("trying file loader");
                return getFromFile(resourceLocator);
            } catch (Exception e) {
            }

            // try and get off url

            try {
                // LOG.debug("trying url loader");
                return getFromUrl(resourceLocator);
            } catch (Exception e) {
            }

            cleanUp();
            // try and get off resource bundle
            throw new SystemException("Resource " + resourceLocator
                    + " not found or inaccessible at File, Classloader, AND URL");

        }

    }

    private InputStream getFromClassLoader(String resourceLocator) throws IOException {

        is = StreamResource.class.getResourceAsStream(resourceLocator);
        if (is == null) {
            throw new IOException("could not get resource from cp");
        }
        return is;

    }

    private InputStream getFromSystemClassLoader(String resourceLocator) throws IOException {

        is = ClassLoader.getSystemResourceAsStream(resourceLocator);
        if (is == null) {
            throw new IOException("could not get system resource from cp");
        }
        return is;
    }

    private InputStream getFromFile(String resourceLocator) throws FileNotFoundException, IOException {

        is = getFromFile(new File(resourceLocator));
        return is;
    }

    private InputStream getFromFile(File resourceLocator) throws FileNotFoundException, IOException {

        is = new FileInputStream(resourceLocator);
        return is;
    }

    private InputStream getFromUrl(String resourceLocator) throws MalformedURLException, IOException {

        is = getFromUrl(new URL(resourceLocator));
        return is;
    }

    private InputStream getFromUrl(URL url) throws IOException {

        is = url.openStream();
        return is;
    }

    private void cleanUp() {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {// e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }

}
