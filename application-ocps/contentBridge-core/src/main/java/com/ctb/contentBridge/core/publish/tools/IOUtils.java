package com.ctb.contentBridge.core.publish.tools;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.*;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.parsers.SAXParser;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.xml.sax.*;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlOutputter;
import com.ctb.contentBridge.core.util.StreamResource;


public class IOUtils {
    public static final String DTD_NAME = "R2_Flash_UI_SOFA.dtd";
    private static Random random = new Random();

    public static File copyFile(File source, File target) throws BusinessException {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(source));
            out = new BufferedOutputStream(new FileOutputStream(target));
            int ch;

            while ((ch = in.read()) != -1) {
                out.write(ch);
            }
            out.flush(); // just in case
        } catch (IOException fnfe) {
            new BusinessException(fnfe.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
        }
        if (source.length() != target.length()) {
            throw new BusinessException(
                "Error in copying -- " + source + " to " + target);
        }
        return target;
    }

    public static Document loadXmlDocument(File xmlFile, boolean validate) throws BusinessException {
        return loadXmlDocument(xmlFile, validate, null);
    }

    public static Document loadXmlDocument(InputStream xmlIs, boolean validate)
        throws JDOMException, IOException {
        return loadXmlDocument(xmlIs, validate, null);
    }

    public static Document loadXmlDocument(
        File xmlFile,
        boolean validate,
        XMLFilter filter) throws BusinessException {

        try {
            return loadXmlDocument(
                new FileInputStream(xmlFile),
                validate,
                filter);
        } catch (JDOMException e) {
            throw new BusinessException(e.getMessage());
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public static Document loadXmlDocument(
        InputStream xmlIs,
        boolean validate,
        XMLFilter filter)
        throws JDOMException, IOException {
        org.jdom.input.SAXBuilder builder = builder(validate, filter);

        org.jdom.Document doc = builder.build(new InputStreamReader(xmlIs));

        return doc;
    }

    private static SAXBuilder builder(boolean validate, XMLFilter filter) {
        SAXBuilder builder =
            new SAXBuilder(SAXParser.class.getName(), validate);

        if (filter != null) {
            builder.setXMLFilter(filter);
        }
        builder.setEntityResolver(new CtbEntityResolver());
        return builder;
    }

    public static Document loadXmlDocument(String xml, boolean validate)
        throws JDOMException, IOException {
        CharArrayReader charArrayReader =
            new CharArrayReader(xml.toCharArray());

        return loadXmlDocument(charArrayReader, validate);
    }

    public static Document loadXmlDocument(Reader xmlReader, boolean validate)
        throws JDOMException, IOException {
        org.jdom.input.SAXBuilder builder = builder(validate, null);

        return builder.build(xmlReader);
    }

    public static File createCopyWithDoctype(File originalFile)
        throws IOException, JDOMException, BusinessException {
        Document doc = loadXmlDocument(originalFile, false);

        setR2FlashUiDoctype(doc);

        XMLOutputter outputter = new XMLOutputter();

        //Sudha outputter.setNewlines(false);
        //Sudha outputter.setTrimAllWhite(true);

        File temp =
            File.createTempFile(
                "temp",
                ".xml",
                originalFile.getCanonicalFile().getParentFile());

        temp.deleteOnExit();

        OutputStreamWriter out = createUtf8FileWriter(temp);

        outputter.output(doc, out);
        out.close();
        return temp;
    }

    // todo: unit test me
    public static void setR2FlashUiDoctype(Document doc) {
        DocType doctype = doc.getDocType();

        if (doctype == null || doctype.getSystemID() == null) {
            doctype =
                new DocType(doc.getRootElement().getName(), IOUtils.DTD_NAME);
            doc.setDocType(doctype);
        }
    }

    private static OutputStreamWriter createUtf8FileWriter(File file)
        throws UnsupportedEncodingException, FileNotFoundException {
        return new OutputStreamWriter(new FileOutputStream(file), "UTF8");
    }

    public static InputStreamReader createUtf8FileReader(File file)
        throws UnsupportedEncodingException, FileNotFoundException {
        return new InputStreamReader(new FileInputStream(file), "UTF8");
    }

    public static byte[] loadBytes(InputStream inputStream)
        throws IOException {
        BufferedInputStream in = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[(int) inputStream.available()];
        int b;
        int i = 0;

        while ((b = in.read()) != -1) {
            bytes[i++] = (byte) b;
        }
        in.close();
        return bytes;
    }

    public static byte[] loadBytes(File file) throws IOException, BusinessException {
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                "A file resource too long:" + file);
        }
        return loadBytes(new StreamResource().getStream(file));
    }

    public static byte[] loadBytes(String resourceName) throws IOException, BusinessException {
        InputStream is = new StreamResource().getStream(resourceName);

        if (is.available() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                "A resource is too large:" + resourceName);
        }
        return loadBytes(is);
    }

    static public EntityResolver getCtbEntityResolver() {
        return new CtbEntityResolver();
    }

    public static char[] loadChars(File file) throws IOException {
        return loadChars(new FileInputStream(file));
    }

    public static char[] loadChars(InputStream is) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        long length = (long) is.available();

        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("InputStream too long");
        }
        StringBuffer buf = new StringBuffer((int) length);
        int b;

        while ((b = in.read()) != -1) {
            buf.append((char) b);
        }
        in.close();
        return buf.toString().toCharArray();
    }

    public static void writeFile(File file, char[] chars) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        writer.write(chars);
        writer.close();
    }

    public static void writeFile(File file, byte[] bytes) throws IOException {
        BufferedOutputStream writer =
            new BufferedOutputStream(new FileOutputStream(file));

        writer.write(bytes);
        writer.close();
    }

    public static File writeFileUtf8(File file, String contents)
        throws IOException {
        PrintWriter printWriter = new PrintWriter(createUtf8FileWriter(file));

        printWriter.print(contents);
        printWriter.close();
        return file;
    }

    public static File writeFileUtf8(File file, Document contents)
        throws IOException {
        return writeFileUtf8(file, xmlToString(contents));
    }

    public static File writeFileUtf8(File file, Element contents)
        throws IOException {
        return writeFileUtf8(file, xmlToString(contents));
    }

    public static File createRandomDirectory(File parentDir, String prefix) {
        File dir = null;

        do {
            dir = new File(parentDir, prefix + random.nextInt());
        } while (dir.exists());
        dir.mkdirs();
        return dir;
    }

    /**
     * @param input stream to read
     * @return String containing contents of file
     **/
    public static String readStream(InputStream input) throws IOException {
        return readReader(new InputStreamReader(input));
    }

    /**
     * @param input stream to read
     * @return String containing contents of file
     **/
    public static String readReader(Reader input) throws IOException {
        try {
            StringBuffer buf = new StringBuffer();
            BufferedReader in = new BufferedReader(input);
            int ch;

            while ((ch = in.read()) != -1) {
                buf.append((char) ch);
            }
            return buf.toString();
        } finally {
            input.close();
        }
    }

    static String cleanPathString(File file) {
        String absolutePath = file.getAbsolutePath();
        String pathSeparator = File.separator;

        absolutePath =
            StringUtils.replace(absolutePath, pathSeparator + ".", "");
        return absolutePath;
    }

    public static class ExecInfo {
        public int exitValue;
        public String stdout;
        public String stderr;

        public void print() {
            System.out.println("process.exitValue() = " + exitValue);
            System.out.println("stdout = " + stdout);
            System.out.println("stderr = " + stderr);
        }

        public String toString() {
            return (
                "process.exitValue() = "
                    + exitValue
                    + "\n"
                    + "stdout = "
                    + stdout
                    + "\n"
                    + "stderr = "
                    + stderr);
        }
    }

    public static ExecInfo exec(String command)
        throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        // System.out.println("Executing " + command);
        Process process = rt.exec(command);

        process.waitFor();
        ExecInfo info = new ExecInfo();

        info.exitValue = process.exitValue();
        info.stdout = IOUtils.readStream(process.getInputStream());
        info.stderr = IOUtils.readStream(process.getErrorStream());
        return info;
    }

    public static ExecInfo exec(String command, File dir)
        throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        // System.out.println("Executing " + command);
        Process process = rt.exec(command, null, dir);

        process.waitFor();
        ExecInfo info = new ExecInfo();

        info.exitValue = process.exitValue();
        info.stdout = IOUtils.readStream(process.getInputStream());
        info.stderr = IOUtils.readStream(process.getErrorStream());
        return info;
    }

    public static ExecInfo exec(String[] commandArray)
        throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        Process process = rt.exec(commandArray);

        process.waitFor();
        ExecInfo info = new ExecInfo();

        info.exitValue = process.exitValue();
        info.stdout = IOUtils.readStream(process.getInputStream());
        info.stderr = IOUtils.readStream(process.getErrorStream());
        return info;
    }

    public static String xmlToString(Document document) throws IOException {
        return xmlToStringInternal(document, null);
    }

    public static String xmlToString(Element element) throws IOException {
        return xmlToStringInternal(null, element);
    }

    public static String xmlToStringWithoutHeader(Document xml)
        throws IOException {
        return xmlToString(xml.getRootElement());
    }

    private static String xmlToStringInternal(
        Document document,
        Element element)
        throws IOException {
        StringWriter out = new StringWriter();
        XMLOutputter outputter = xmlOutputter();

        if (document == null) {
            outputter.output(element, out);
        } else if (element == null) {
            outputter.output(document, out);
        } else {
            throw new RuntimeException("Null parameter passed to xmlToString");
        }
        String s = out.toString();

        return s;
    }

    public static XMLOutputter xmlOutputter() {
        return new R2XmlOutputter();
    }

    public static void prettyXmlOutput(File file, Element element)
        throws IOException {
        XMLOutputter outputter = xmlOutputter();

        //Sudha outputter.setNewlines(true);
        //Sudha outputter.setLineSeparator(System.getProperty("line.separator"));
        //Sudha outputter.setIndent("  ");
        StringWriter out = new StringWriter();

        outputter.output(element, out);
        IOUtils.writeFileUtf8(file, out.toString());
    }

    public static org.w3c.dom.Document loadW3CXmlDocument(
        File file,
        boolean validate)
        throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);
        factory.setValidating(validate);
        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setEntityResolver(new CtbEntityResolver());
        org.w3c.dom.Document document =
            builder.parse(new InputSource(new FileReader(file)));

        document.normalize();
        return document;
    }

    /* **************  Z I P   F I L E S  *****************/
    public static File createZip(List files) throws Exception {
        File zipFile = File.createTempFile(IOUtils.class.getName(), "zip");
        ZipOutputStream out =
            new ZipOutputStream(new FileOutputStream(zipFile));
        int sz = files.size();

        for (int i = 0; i < sz; i++) {
            File rptFile = (File) files.get(i);

            rptFile.deleteOnExit();
            addFileToZip(out, rptFile);
        }
        out.flush();
        out.close();
        return zipFile;
    }

    private static void addFileToZip(ZipOutputStream out, File f) throws Exception {
        int BUFFER = 1024;
        byte data[] = new byte[BUFFER];
        FileInputStream fi = new FileInputStream(f);
        BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
        ZipEntry entry = new ZipEntry(f.getName());

        out.putNextEntry(entry);
        int count;

        while ((count = origin.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        origin.close();
    }
}
