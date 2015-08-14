 package com.tcs.conversion;
 
 import java.io.ByteArrayInputStream;
 import java.io.InputStream;
 import java.util.ArrayList;
 import java.util.List;
 import org.jdom.Document;
 import org.jdom.Element;
 import org.jdom.input.SAXBuilder;
 
 public class XMLUtils
 {
   public static Document parse(String input, String encoding)
     throws Exception
   {
     SAXBuilder builder = new SAXBuilder();
     Document doc = null;
     InputStream is = new ByteArrayInputStream(input.getBytes(encoding));
     doc = builder.build(is);
     return doc;
   }
 
   public static Document parse(String input)
     throws Exception
   {
     SAXBuilder builder = new SAXBuilder();
     Document doc = null;
     InputStream is = new ByteArrayInputStream(input.getBytes());
     doc = builder.build(is);
     return doc;
   }
 
   public static String processHexString(String inputString)
   {
     StringBuilder outPutString = new StringBuilder("");
     String temp = "";
     int index = inputString.indexOf("%");
     while (index > -1) {
       temp = inputString.substring(index + 1, index + 3);
       outPutString = outPutString.append(inputString.substring(0, index))
         .append(convertHexToChar(temp));
       inputString = inputString.substring(index + 3);
       index = inputString.indexOf("%");
     }
     return outPutString.toString();
   }
 
   public static List<Element> extractAllElement(String pattern, Element element)
     throws Exception
   {
     ArrayList results = new ArrayList();
     List<Element> children = element.getChildren();
     for (Element elem : children) {
       if (pattern.equals(elem.getName())) {
         results.add(elem);
       }
       results.addAll(extractAllElement(pattern, elem));
     }
 
     return results;
   }
 
   private static String convertHexToChar(String hexchar)
   {
     try
     {
       int val = Integer.parseInt(hexchar, 16);
       Character charVal = Character.valueOf((char)val);
       return charVal.toString(); } catch (Exception e) {
     }
     return hexchar;
   }
 }

