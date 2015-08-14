 package com.tcs.parser;
 
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.io.Reader;
 import javax.xml.parsers.SAXParser;
 import javax.xml.parsers.SAXParserFactory;
 import org.xml.sax.InputSource;
 
 public class SaxConversion
 {
   public static void main(String[] strings)
     throws Exception
   {
     SAXParserFactory factory = SAXParserFactory.newInstance();
     SAXParser saxParser = factory.newSAXParser();
     File file = new File("StaticData\\xml\\a.xml");
     InputStream inputStream = new FileInputStream(file);
     Reader reader = new InputStreamReader(inputStream, "ISO-8859-1");
 
     InputSource is = new InputSource(reader);
     is.setEncoding("ISO-8859-1");
 
     SaxParserHandler handler = new SaxParserHandler();
     saxParser.parse(is, handler);
 
     System.out.println(handler.htmlString);
   }
 }

