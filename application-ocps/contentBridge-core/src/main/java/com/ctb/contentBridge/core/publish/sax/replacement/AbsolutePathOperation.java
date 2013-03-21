/*
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 15, 2003
 * Time: 4:01:21 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.ctb.contentBridge.core.publish.sax.replacement;


import java.io.*;
import java.util.*;

import org.xml.sax.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.sax.element.FieldOperation;


public class AbsolutePathOperation implements FieldOperation {

    private com.ctb.contentBridge.core.util.ParseFilterProperties pathProperties = null;
    private String basepath = null;
    private Set fileSet = new HashSet();
    public AbsolutePathOperation() {}

    public AbsolutePathOperation(String imageAreaPath) throws SystemException {
        this(new File(imageAreaPath));
    }

    public AbsolutePathOperation(File imageAreaPath) throws SystemException {
        try {
            basepath = imageAreaPath.getCanonicalPath();
        } catch (IOException e) {
            throw new SystemException("Folder " + imageAreaPath.getPath()
                    + " does not exist");
        }
        validateBasePath();
    }

    public AbsolutePathOperation(com.ctb.contentBridge.core.util.ParseFilterProperties props) throws SystemException {
        pathProperties = props;
        basepath = pathProperties.getImagesBaseCanonicalPath();
        validateBasePath();
    }

    public String process(String input) throws SAXException {

        String output = null;

        if (basepath == null) {
            output = input;
        } else {
            output = getCanonicalFilePath(input);
        }
        addToFilenames(output);
        return output;
    }

    public Set getFilenames() {
        return fileSet;
    }

    public boolean allFilesExist() {
        return (invalidFiles().size() == 0);
    }

    public Set invalidFiles() {
        Set missingFiles = new HashSet();

        for (Iterator iter = fileSet.iterator(); iter.hasNext();) {
            String currentFileName = (String) iter.next();
            File file = new File(currentFileName);

            if (!file.exists()) {
                missingFiles.add(currentFileName);
            }
        }
        return missingFiles;
    }

    private void validateBasePath() throws SystemException {
        if (basepath == null) {
            return;
        }
        File file = new File(basepath);

        if (!file.isDirectory()) {
            throw new SystemException("Image area must point to a valid folder:"
                    + basepath);
        }
    }

    private void addToFilenames(String fileName) {
        fileSet.add(fileName);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("MISSING ART:\n");
        for (Iterator iter = invalidFiles().iterator(); iter.hasNext();) {
            buffer.append("\tFILE:" + iter.next() + "\n");
        }
        return buffer.toString();
    }

    private String getCanonicalFilePath(String input) {

        try {
            File baseDir = new File(basepath);

            if (!baseDir.exists()) {
                return input;
            }
            StringBuffer buffer = new StringBuffer();

            buffer.append(input);
            buffer.reverse();
            StringTokenizer revTokenizer = new StringTokenizer(buffer.toString(),
                    ":/");
            String outString = "";

            while (revTokenizer.hasMoreTokens()) {
                String token = revTokenizer.nextToken();
                StringBuffer buf = new StringBuffer();

                buf.append(token);
                buf.reverse();
                outString = buf.toString() + outString;
                File file = new File(baseDir, outString);

                if (file.exists()) {
                    return file.getCanonicalPath();
                }
                outString = "/" + outString;
            }
        } catch (IOException e) {
            e.printStackTrace(); // Swallowing exceptions, this operation shouldnt fail if files arent found
        }
        return input;
    }
}
