package com.tcs;

import com.tcs.dataaccess.ConnectionManager;
import com.tcs.dataaccess.DataAccessManager;
import com.tcs.utility.Configuration;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
  public static String BASE_DIR;
  public static String CSS_BODY;
  public static String PRODUCT_ID;
  private static String CUSTOMER_ID;
  private static String DISTRICT_CODE;
  public static Integer THREAD_COUNT = Integer.valueOf(2);
  public static Integer STUDENT_COUNTER = Integer.valueOf(0);
  public static Map<String, String> commodityCodeMap = new HashMap();
  public static Set<String> corporationSet = new HashSet();
  public static final String COPYWRITE = "Copyright &#169; 2013 by CTB/McGraw-Hill LLC. All rights reserved. Only authorized customers may copy, download and/or print the document, located online at ctb.com. Any other use or reproduction of this document, in whole or in part, requires written permission of the publisher.";
  private ConnectionManager oasConnectionManager = null;
  private ConnectionManager adsConnectionManager = null;
  private static Logger slf4jLogger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    Main mainObject = new Main();
    mainObject.setupEnv(args);
    mainObject.generateStudentReports();
  }

  public void setupEnv(String[] args) throws Exception {
    if (args.length != 4)
    {
      slf4jLogger.info("Incorrect argument....");
      slf4jLogger.info("Please provide the argument with order: <OAS database properties file> <ADS database properties file> <CC_CODES.properties>");
      throw new Exception("Incorrect argument....");
    }

    this.oasConnectionManager = new ConnectionManager(args[0]);
    this.adsConnectionManager = new ConnectionManager(args[1]);
    processProperties(args[2]);
    Configuration.doConfiguration(args[2]);
    readConfigFile();
    readCommodityCodes(args[3]);
  }

  private void generateStudentReports()
    throws Exception
  {
    DataAccessManager mgr = new DataAccessManager();
    slf4jLogger.info("Product ID :: " + PRODUCT_ID);
    mgr.generateReports(this.oasConnectionManager, this.adsConnectionManager, PRODUCT_ID, CUSTOMER_ID, DISTRICT_CODE);
  }

  public static void processProperties(String fileName) throws Exception {
    Properties prop = new Properties();
    prop.load(new FileInputStream(fileName));
    BASE_DIR = prop.getProperty("BASE_DIR") != null ? prop.getProperty("BASE_DIR").trim() : null;
    if ((BASE_DIR == null) || (BASE_DIR.length() == 0)) {
      throw new Exception("Base directory present in config.properties file....");
    }
    PRODUCT_ID = prop.getProperty("PRODUCT_ID") != null ? prop.getProperty("PRODUCT_ID").trim() : null;
    if ((PRODUCT_ID == null) || (PRODUCT_ID.length() == 0))
      throw new Exception("Product ID not present in config.properties file....");
    CUSTOMER_ID = prop.getProperty("CUSTOMER_ID") != null ? prop.getProperty("CUSTOMER_ID").trim() : null;
    if ((CUSTOMER_ID == null) || (CUSTOMER_ID.length() == 0))
      throw new Exception("Customer ID not present in config.properties file....");
    DISTRICT_CODE = (prop.getProperty("DISTRICT_CODE") != null) && 
      (prop.getProperty("DISTRICT_CODE").trim().length() > 0) ? 
      prop.getProperty("DISTRICT_CODE").trim() : null;
  }

  public static void readConfigFile() throws IOException
  {
    BufferedReader reader = new BufferedReader(new FileReader(BASE_DIR + System.getProperty("file.separator") + "style.css"));
    String line = null;
    StringBuilder stringBuilder = new StringBuilder();
    String ls = System.getProperty("line.separator");

    while ((line = reader.readLine()) != null) {
      stringBuilder.append(line);
      stringBuilder.append(ls);
    }

    CSS_BODY = stringBuilder.toString();
  }

  public static void readCommodityCodes(String ccCodeFileName) throws IOException
  {
    Properties prop = new Properties();
    prop.load(new FileInputStream(ccCodeFileName));
    Set<Map.Entry<Object,Object>> ccCodes = prop.entrySet();
    for (Map.Entry entry : ccCodes)
      commodityCodeMap.put(entry.getKey().toString().trim(), entry.getValue().toString().trim());
  }

  public static synchronized int getStudentCounter()
  {
    return (Main.STUDENT_COUNTER = Integer.valueOf(STUDENT_COUNTER.intValue() + 1)).intValue();
  }
}