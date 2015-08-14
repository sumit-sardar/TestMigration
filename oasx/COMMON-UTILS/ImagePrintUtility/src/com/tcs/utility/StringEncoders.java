 package com.tcs.utility;
 
 import java.io.PrintStream;
 
 public class StringEncoders
 {
   static final CharEncoder hexUrlEncoder = new CharEncoder("%", "", 16);
   static final CharEncoder hexHtmlEncoder = new CharEncoder("&#x", ";", 16);
   static final CharEncoder decimalHtmlEncoder = new CharEncoder("&#", ";", 10);
 
   public static void main(String[] args)
   {
     System.out.println("hex-url: " + hexUrlEncode("a"));
     System.out.println("hex-html: " + hexHtmlEncode("a"));
     System.out.println("decimal-html: " + decimalHtmlEncode("a"));
   }
   public static String hexUrlEncode(String str) {
     return encode(str, hexUrlEncoder);
   }
   public static String hexHtmlEncode(String str) {
     return encode(str, hexHtmlEncoder);
   }
   public static String decimalHtmlEncode(String str) {
     return encode(str, decimalHtmlEncoder);
   }
 
   private static String encode(String str, CharEncoder encoder) {
     StringBuilder buff = new StringBuilder();
/* 23 */     for (int i = 0; i < str.length(); i++)
/* 24 */       encoder.encode(str.charAt(i), buff);
/* 25 */     return buff.toString(); } 
   private static class CharEncoder { String prefix;
     String suffix;
     int radix;
 
/* 32 */     public CharEncoder(String prefix, String suffix, int radix) { this.prefix = prefix;
/* 33 */       this.suffix = suffix;
/* 34 */       this.radix = radix; }
 
     void encode(char c, StringBuilder buff) {
/* 37 */       buff.append(this.prefix).append(Integer.toString(c, this.radix)).append(this.suffix);
     }
   }
 }

/* Location:           C:\Users\522912.INDIA\Desktop\reports.jar
 * Qualified Name:     com.tcs.utility.StringEncoders
 * JD-Core Version:    0.6.0
 */