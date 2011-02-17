package com.ctb.common.tools;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.xpath.*;

import com.ctb.xmlProcessing.item.*;

public class OpenDeployWrapper implements OpenDeploy {
    private static Logger logger = Logger.getLogger(OpenDeployWrapper.class);
    public static final String EXECUTABLE =
        "/appl/OpenDeploy/OpenDeployNG/bin/iwodstart";
    static Set artMovedByThisJVM = new HashSet();

    /**
     * Temp directory for file containing list of images
     */
    public static final String IMAGE_FILE_TEMP_DIR =
        "/appl/TeamSite/iw-home/tmp";
    // this should be configurable
    public static final String DEPLOY_CONFIG_FILE = "tw_filelist_deploy";
    String targetImageArea;
    String localImageArea;
    //String environment;
    Element element;

    OpenDeployConfig config = null;

    public OpenDeployWrapper(
        String localImageArea,
        String targetImageArea,
        OpenDeployConfig config) {
        this.config = config;
        this.localImageArea = localImageArea;
        this.targetImageArea = targetImageArea;
    }
    
    public String createUniqueImageFile() throws IOException
    {
        String processID = null;
        boolean done = false;
        Random random = new Random();
        while( !done )
        {
            processID = String.valueOf(random.nextInt(99999));
            File inputFile = new File( config.getWorkingDirectory(),
                    "filelist_" + processID + ".txt" );
            if ( !inputFile.exists() )
            {
                inputFile.createNewFile();
                done = true;
            }
        }
		return processID;
    }

    public void run(Element itemElement) {
        this.element = itemElement;
        run();
    }

    public void run() {
        Collection fileNames = new HashSet();

        try {
            if (config.enabled()) {
                // get the file names
                fileNames = getFileNames(element);
                if (fileNames.isEmpty()) {
                    return;
                }
                // create the filelist file
                Random random = new Random();

 //               String processID = String.valueOf(random.nextInt(99999));
                String processID = createUniqueImageFile();
                File fileOfImages = createFileOfImages(processID, fileNames);

                String[] commandArray =
                    createCommand(
                        fileOfImages,
                        processID,
                        String.valueOf(random.nextInt(99999)));
                logger.info("Calling OpenDeploy:");
                execOpenDeploy(commandArray);
                logger.info("OpenDeploy complete.");
            }
        } catch (IOException ioEx) {
            throw new SystemException("Error executing OpenDeploy", ioEx);
        }
        // add copied files to the art moved during this JVM run
        for (Iterator iter = fileNames.iterator(); iter.hasNext();) {
            artMovedByThisJVM.add(iter.next());
        }

    }

    File createFileOfImages(String processID, Collection fileNames)
        throws IOException {
        File file =
            new File(
                config.getWorkingDirectory(),
                "filelist_" + processID + ".txt");
        PrintWriter writer =
            new PrintWriter(new BufferedWriter(new FileWriter(file)));

        for (Iterator iter = fileNames.iterator(); iter.hasNext();) {
            writer.println(iter.next());
        }
        writer.close();
        return file;
    }

    String[] createCommand(
        File fileOfImages,
        String processID,
        String randomID)
        throws IOException {

        String instance = processID + "-" + randomID;
        String fileName = "file_list=" + fileOfImages.getCanonicalPath();
        String[] commandArray =
            {
                config.getExecutable(),
                config.getOpenDeployConfig(),
                "-inst",
                instance,
                "-k",
                "srcarea=" + localImageArea,
                "-k",
                "destarea=" + targetImageArea,
                "-k",
                fileName };

        return commandArray;

    }

    IOUtils.ExecInfo execOpenDeploy(String[] cmdArray) {
        IOUtils.ExecInfo info;

        try {
            info = IOUtils.exec(cmdArray);
            if (info.exitValue != 0) {
                throw new SystemException(
                    "Error "
                        + info.exitValue
                        + " executing OpenDeploy:"
                        + info.toString());
            }
        } catch (InterruptedException e) {
            throw new SystemException("Operation interrupted: " + cmdArray[0]);
        } catch (IOException e) {
            throw new SystemException("Operation IO error: " + cmdArray[0]);
        }
        return info;
    }

    Collection getFileNames(Element element) {
        Collection fileNames = new TreeSet();

        try {
            getFileNamesUsingXPath(
                element,
                fileNames,
                "descendant::*/BMPPrint/@FileName");
            getFileNamesUsingXPath(
                element,
                fileNames,
                "descendant::*/EPSPrint/@FileName");
            getFileNamesUsingXPath(
                element,
                fileNames,
                "descendant::*/Flash/@FileName");
        } catch (JDOMException jdomEx) {
            throw new SystemException(
                "JDOM Exception while trying to find file names",
                jdomEx);
        }

        return removeFileNamesAlreadyCopied(fileNames);
    }

    private Collection removeFileNamesAlreadyCopied(Collection fileNames) {
        Collection outColl = new HashSet();

        for (Iterator iter = fileNames.iterator(); iter.hasNext();) {
            String currentName = (String) iter.next();

            if (!artMovedByThisJVM.contains(currentName)) {
                outColl.add(currentName);
            }
        }
        return outColl;
    }

    Collection getFileNamesUsingXPath(
        Element element,
        Collection fileNames,
        String xPathExpression)
        throws JDOMException {
        XPath xpath = XPath.newInstance(xPathExpression);
        List fileNodes = xpath.selectNodes(element);

        for (Iterator iter = fileNodes.iterator(); iter.hasNext();) {
            Object node = iter.next();

            if (!(node instanceof Attribute)) {
                continue;
            }
            String fileName = ((Attribute) node).getValue();

            fileName = getRelativeFileNameFromAttributeValue(fileName);
            // check for duplication
            if (!fileNames.contains(fileName)) {
                fileNames.add(fileName);
            }
        }
        return fileNames;
    }

    private String getRelativeFileNameFromAttributeValue(String fileName) {

        if ((fileName.startsWith(File.separator) || fileName.charAt(1) == 58)
            && !fileName.startsWith(targetImageArea)) {
            throw new SystemException(
                "Aborting OpenDeploy.\n\tSome FileName attributes in XML do not start with '"
                    + targetImageArea
                    + "' but are canonical filenames,'"
                    + fileName
                    + "'");
        }
        fileName = fileName.substring(targetImageArea.length() + 1);
        // the +1 is for removing the '/'
        return fileName;
    }

}
