package com.ctb.contentBridge.core.publish.tools;


import java.io.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.util.Pipe;



/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Aug 6, 2003
 * Time: 5:06:46 PM
 * To change this template use Options | File Templates.
 */
public class JGenWrapper {

    public String pathToJGen = "";
    final String LOCAL_SWT = "local.swt";
    final String LOCAL_XML = "compdisp.xml";
    final String LOCAL_MOVIE = "movie.swf";

    public JGenWrapper() {
        // todo - use properties file
        String os = System.getProperty("os.name");

        if (!os.startsWith("Windows")) {
            pathToJGen = "/appl/publishing/jgenerator/2.1/bin/";
        }
    }

    public byte[] generateFlashMovie(byte[] xmlBytes, byte[] templateBytes) {
        File tempDir = null;
        byte[] outBytes;

        try {
            tempDir = createTempDir();
            IOUtils.writeFile(new File(tempDir, LOCAL_XML), xmlBytes);
            IOUtils.writeFile(new File(tempDir, LOCAL_SWT), templateBytes);
            IOUtils.ExecInfo info = IOUtils.exec(buildCommand(LOCAL_MOVIE,
                    LOCAL_SWT),
                    tempDir);

            if (info.exitValue != 0) {
                throw new SystemException("Error " + info.exitValue
                        + " executing JGen: " + info.toString());
            }
            outBytes = IOUtils.loadBytes(new File(tempDir, LOCAL_MOVIE));
        } catch (IOException e) {
            throw new SystemException("FLASH ERROR");
        } catch (InterruptedException e) {
            throw new SystemException("FLASH ERROR");
        }
        finally {
            if (tempDir != null) {
                File[] files = tempDir.listFiles();

                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
                tempDir.delete();
            }

        }
        return outBytes;
    }

    public byte[] generateFlashMovie(InputStream xmlIs, InputStream templateIs) {

        ByteArrayOutputStream xmlOs = new ByteArrayOutputStream();
        ByteArrayOutputStream templateOs = new ByteArrayOutputStream();
        Pipe xmlPipe = new Pipe(xmlIs, xmlOs);
        Pipe templatePipe = new Pipe(templateIs, templateOs);

        xmlPipe.run();
        templatePipe.run();
        return generateFlashMovie(xmlOs.toByteArray(), templateOs.toByteArray());
    }

    private String buildCommand(String movie, String localTemplateName) {
        return pathToJGen + "jgenerate -log -swf " + movie + " "
                + localTemplateName;
    }

    private File createTempDir() {
        File parent = new File("tempdir");

        if (!parent.exists()) {
            parent.mkdir();
        }
        try {
            File tempFilePath = File.createTempFile("JGEN", "", parent);

            String dirName = tempFilePath.getAbsolutePath();

            tempFilePath.delete();
            File tempDir = new File(dirName);

            if (!tempDir.mkdirs()) {
                throw new SystemException("FLASH GENERATION ERROR - Failed to create temp directory:"
                        + dirName);
            }
            return tempFilePath;
        } catch (IOException e) {
            throw new SystemException("FLASH GENERATION ERROR - Failed to create temp directory:");
        }
    }
}
