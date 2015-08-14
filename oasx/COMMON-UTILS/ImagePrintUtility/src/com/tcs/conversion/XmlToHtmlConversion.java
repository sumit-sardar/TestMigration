 package com.tcs.conversion;
 
 import com.lowagie.text.DocumentException;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.io.PrintStream;
 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.parsers.ParserConfigurationException;
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerConfigurationException;
 import javax.xml.transform.TransformerException;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.dom.DOMSource;
 import javax.xml.transform.stream.StreamResult;
 import javax.xml.transform.stream.StreamSource;
 import org.w3c.dom.Document;
 import org.xml.sax.SAXException;
 
 public class XmlToHtmlConversion
 {
   static Document document;
 
   public static InputStream getInputStreamFromString(String data)
   {
     InputStream is = new ByteArrayInputStream(data.getBytes());
 
     return is;
   }
 
   public static OutputStream getOutputStream() {
     OutputStream output = new OutputStream()
     {
       private StringBuilder string = new StringBuilder();
 
       public void write(int b) throws IOException {
         this.string.append((char)b);
       }
 
       public String toString()
       {
         return this.string.toString();
       }
     };
     return output;
   }
   public static String createHTMLFromXML(String xmlData) {
     String finalHtml = "";
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     try
     {
       File stylesheet = new File("StaticData\\xslt\\Conversion.xsl");
       File datafile = new File("StaticData\\xml\\Sample.xml");
 
       DocumentBuilder builder = factory.newDocumentBuilder();
       document = builder.parse(new FileInputStream(datafile));
 
       TransformerFactory tFactory = TransformerFactory.newInstance();
       StreamSource stylesource = new StreamSource(stylesheet);
       Transformer transformer = tFactory.newTransformer(stylesource);
 
       DOMSource source = new DOMSource(document);
       ByteArrayOutputStream stream = new ByteArrayOutputStream();
 
       StreamResult result = new StreamResult(getOutputStream());
       transformer.transform(source, result);
       finalHtml = result.getOutputStream().toString();
       finalHtml = finalHtml.replaceAll("&nbps;", "&#160;");
       finalHtml = finalHtml.replaceAll("&lt;", "<");
       finalHtml = finalHtml.replaceAll("&gt;", ">");
     }
     catch (TransformerConfigurationException tce)
     {
       System.out.println("\n** Transformer Factory error");
       System.out.println("   " + tce.getMessage());
 
       Throwable x = tce;
 
       if (tce.getException() != null) {
         x = tce.getException();
       }
 
       x.printStackTrace();
     }
     catch (TransformerException te) {
       System.out.println("\n** Transformation error");
       System.out.println("   " + te.getMessage());
 
       Throwable x = te;
 
       if (te.getException() != null) {
         x = te.getException();
       }
 
       x.printStackTrace();
     }
     catch (SAXException sxe)
     {
       Exception x = sxe;
 
       if (sxe.getException() != null) {
         x = sxe.getException();
       }
 
       x.printStackTrace();
     }
     catch (ParserConfigurationException pce) {
       pce.printStackTrace();
     }
     catch (IOException ioe) {
       ioe.printStackTrace();
     }
     return finalHtml;
   }
 
   public static void main(String[] a) throws DocumentException, IOException {
     XmlToHtmlConversion con = new XmlToHtmlConversion();
     HTMLToPDFConversion obj = new HTMLToPDFConversion();
   }
 }

