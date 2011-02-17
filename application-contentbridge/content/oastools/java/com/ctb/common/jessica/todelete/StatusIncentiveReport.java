package com.ctb.common.jessica.todelete;


import java.io.*;
import java.sql.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class StatusIncentiveReport {
    static Connection conn;

    /**
     * Creates a new com.ctb.common.jessica.todelete.StatusIncentiveReport object.
     */
    public StatusIncentiveReport() {}

    /**
     * DOCUMENT ME!
     *
     * @param _file DOCUMENT ME!
     * @param email DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void constructMessage(File _file, String email)
        throws Exception {
        String smtpHost = "vought";

        String from = "jessica.r.glissmann@accenture.com";

        // Start a session
        java.util.Properties properties = System.getProperties();
        Session session = Session.getInstance(properties, null);

        // Construct a message
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(email));
        message.setSubject("TABE Status Report");

        // Create the first part
        Multipart mailBody = new MimeMultipart();
        MimeBodyPart mainBody = new MimeBodyPart();

        mainBody.setText("The attached file contains the current status of the TABE Standardization Study. \n");
        mailBody.addBodyPart(mainBody);

        // Connect to the transport
        Transport transport = session.getTransport("smtp");

        transport.connect(smtpHost, "", "");

        // create the attachment
        FileDataSource fds1 = new FileDataSource(_file);
        MimeBodyPart mimeAttach1 = new MimeBodyPart();

        mimeAttach1.setDataHandler(new DataHandler(fds1));
        mimeAttach1.setFileName(fds1.getName());
        mailBody.addBodyPart(mimeAttach1);

        message.setContent(mailBody);

        // Send the message and close the connection
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] argv) throws Exception {
        if (argv.length != 5) {
            System.out.println("Usage: java com.ctb.common.jessica.todelete.StatusIncentiveReport dbHost dbSid dbUser dbPsswd emailAddr");
            System.exit(1);
        }

        String dbHostname = argv[0];
        String dbConnSid = argv[1];
        String dbUser = argv[2];
        String dbPasswd = argv[3];
        String emailAddress = argv[4];

        String testCatName = "";
        int testCatId = 0;
        String data = null;
        int custId = 1047;
        String orgNdName = "";
        int orgNdId = 0;
        int prntOrgNdId = 0;
        String prntOrgNdName = "";
        String testCatForm = "";
        String testCatLevel = "";
        String orgNodePrefix = "";
        int countCO = 0;
        int countIC = 0;
        int countIP = 0;
        int countIN = 0;
        int countIS = 0;
        int countNT = 0;
        int countSC = 0;
        int countAll = 0;

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        StatusIncentiveReport sir = new StatusIncentiveReport();

        // open connection to OSR
        if (sir.openDBconn(dbHostname, dbConnSid, dbUser, dbPasswd)) {
            // System.out.println("Within openDBconn");
            File exportFile = File.createTempFile("TABE_Status", ".csv");

            exportFile.deleteOnExit();

            BufferedWriter fileOut = new BufferedWriter(new FileWriter(exportFile));

            // String slctCustId = "SELECT MAX(CUSTOMER_ID) FROM CUSTOMER WHERE CUSTOMER_NAME = 'CTB' AND ACTIVATION_STATUS = 'AC'";
            // String slctCustId =
            // "SELECT MAX(CUSTOMER_ID) FROM CUSTOMER WHERE CUSTOMER_NAME LIKE 'TABE%' AND ACTIVATION_STATUS = 'AC'";
            // ResultSet rsSlctCustId = null;
            // PreparedStatement psSlctCustId = null;

            // String slctTestCats = "SELECT TEST_DISPLAY_NAME, TEST_CATALOG_ID FROM TEST_CATALOG WHERE PRODUCT_ID = (SELECT PRODUCT_ID FROM PRODUCT WHERE PRODUCT_NAME = 'READING' AND ACTIVATION_STATUS = 'AC') AND ACTIVATION_STATUS = 'AC'";
            // String slctTestCats =
            // "SELECT TEST_DISPLAY_NAME, TEST_CATALOG_ID FROM TEST_CATALOG WHERE PRODUCT_ID = (SELECT PRODUCT_ID FROM PRODUCT WHERE PRODUCT_NAME LIKE 'TABE%' AND ACTIVATION_STATUS = 'AC') AND ACTIVATION_STATUS = 'AC'";
            String slctTestCats = "SELECT TEST_DISPLAY_NAME, TEST_CATALOG_ID, TEST_FORM, TEST_LEVEL FROM TEST_CATALOG WHERE PRODUCT_ID = 1009 AND ACTIVATION_STATUS = 'AC'";
            ResultSet rsSlctTestCats = null;
            PreparedStatement psSlctTestCats = null;

            String slctOrgNdIds = "SELECT ORG_NODE_ID, ORG_NODE_NAME FROM ORG_NODE WHERE CUSTOMER_ID = ? AND ORG_NODE_CATEGORY_ID = (SELECT ORG_NODE_CATEGORY_ID FROM ORG_NODE_CATEGORY WHERE CUSTOMER_ID = ? AND CATEGORY_LEVEL = (SELECT MAX(CATEGORY_LEVEL)-1 FROM ORG_NODE_CATEGORY WHERE CUSTOMER_ID = ?) AND ACTIVATION_STATUS = 'AC') AND ACTIVATION_STATUS = 'AC' AND EXISTS (SELECT * FROM ORG_NODE_TEST_CATALOG ONTC WHERE TEST_CATALOG_ID = ? AND ONTC.ORG_NODE_ID = ORG_NODE.ORG_NODE_ID)";
            ResultSet rsSlctOrgNdIds = null;
            PreparedStatement psSlctOrgNdIds = null;

            String slctOrgNdChld = "SELECT ORG_NODE_NAME, ORG_NODE_ID FROM ORG_NODE WHERE ORG_NODE_NAME LIKE ? AND ORG_NODE_ID IN (SELECT ORG_NODE_ID FROM ORG_NODE_PARENT WHERE PARENT_ORG_NODE_ID = ? AND CUSTOMER_ID = ?)";
            ResultSet rsSlctOrgNdChld = null;
            PreparedStatement psSlctOrgNdChld = null;

            String slctTestRstrs = "SELECT COUNT(DISTINCT STUDENT_ID) FROM TEST_ROSTER TR WHERE EXISTS (SELECT * FROM ORG_NODE_STUDENT ONS WHERE ONS.ORG_NODE_ID = ? AND ONS.STUDENT_ID = TR.STUDENT_ID AND TR.TEST_COMPLETION_STATUS LIKE ? AND EXISTS (SELECT * FROM TEST_ADMIN TA WHERE TA.TEST_CATALOG_ID = ? AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID))";
            ResultSet rsSlctTestRstrs = null;
            PreparedStatement psSlctTestRstrs = null;

            // String slctOrgNdPrnt =
            // "SELECT ORG_NODE_NAME FROM ORG_NODE WHERE ORG_NODE_ID = (SELECT PARENT_ORG_NODE_ID FROM ORG_NODE_PARENT WHERE ORG_NODE_ID = ? AND CUSTOMER_ID = ?)";
            // ResultSet rsSlctOrgNdPrnt = null;
            // PreparedStatement psSlctOrgNdPrnt = null;

            try {
                // System.out.println("Within try");
                // psSlctCustId = conn.prepareStatement(slctCustId);
                psSlctTestCats = conn.prepareStatement(slctTestCats);
                psSlctOrgNdIds = conn.prepareStatement(slctOrgNdIds);
                psSlctTestRstrs = conn.prepareStatement(slctTestRstrs);
                psSlctOrgNdChld = conn.prepareStatement(slctOrgNdChld);

                // query for TABE customer ID
                // rsSlctCustId = psSlctCustId.executeQuery();

                // if (rsSlctCustId.next())
                // {
                // System.out.println("Within rsSlctCustId.next()");
                // custId = rsSlctCustId.getInt(1);

                // query for the TABE Test Catalogs
                rsSlctTestCats = psSlctTestCats.executeQuery();

                int count = 0;

                while (rsSlctTestCats.next()) {
                    testCatName = rsSlctTestCats.getString(1);
                    testCatId = rsSlctTestCats.getInt(2);
                    testCatForm = rsSlctTestCats.getString(3);
                    testCatLevel = rsSlctTestCats.getString(4);
                    orgNodePrefix = testCatForm.trim() + testCatLevel.trim()
                            + "%";

                    // get Org Node Ids for TABE customers with a subscription to the current test catalog
                    psSlctOrgNdIds.setInt(1, custId);
                    psSlctOrgNdIds.setInt(2, custId);
                    psSlctOrgNdIds.setInt(3, custId);
                    psSlctOrgNdIds.setInt(4, testCatId);
                    rsSlctOrgNdIds = psSlctOrgNdIds.executeQuery();

                    while (rsSlctOrgNdIds.next()) {
                        // System.out.println("Within rsSlctOrgNdIds.next()");
                        prntOrgNdName = rsSlctOrgNdIds.getString(2);
                        prntOrgNdId = rsSlctOrgNdIds.getInt(1);

                        // query for child org node
                        psSlctOrgNdChld.setString(1, orgNodePrefix);
                        psSlctOrgNdChld.setInt(2, prntOrgNdId);
                        psSlctOrgNdChld.setInt(3, custId);
                        rsSlctOrgNdChld = psSlctOrgNdChld.executeQuery();

                        while (rsSlctOrgNdChld.next()) {
                            orgNdName = rsSlctOrgNdChld.getString(1);
                            orgNdId = rsSlctOrgNdChld.getInt(2);
                            // query for all Test Rosters
                            psSlctTestRstrs.setInt(1, orgNdId);
                            psSlctTestRstrs.setString(2, "%");
                            psSlctTestRstrs.setInt(3, testCatId);
                            rsSlctTestRstrs = psSlctTestRstrs.executeQuery();
								
                            if (rsSlctTestRstrs.next()) {
                                countAll = rsSlctTestRstrs.getInt(1);
                            }
								
                            if (countAll != 0) {

                                // query for Test Rosters for the current org node
                                // query for CO test rosters
                                psSlctTestRstrs.setInt(1, orgNdId);
                                psSlctTestRstrs.setString(2, "CO");
                                psSlctTestRstrs.setInt(3, testCatId);
                                rsSlctTestRstrs = psSlctTestRstrs.executeQuery();

                                if (rsSlctTestRstrs.next()) {
                                    countCO = rsSlctTestRstrs.getInt(1);
                                }

                                // query for IC test rosters
                                psSlctTestRstrs.setInt(1, orgNdId);
                                psSlctTestRstrs.setString(2, "IC");
                                psSlctTestRstrs.setInt(3, testCatId);
                                rsSlctTestRstrs = psSlctTestRstrs.executeQuery();

                                if (rsSlctTestRstrs.next()) {
                                    countIC = rsSlctTestRstrs.getInt(1);
                                }

                                // query for IP test rosters
                                psSlctTestRstrs.setInt(1, orgNdId);
                                psSlctTestRstrs.setString(2, "IP");
                                psSlctTestRstrs.setInt(3, testCatId);
                                rsSlctTestRstrs = psSlctTestRstrs.executeQuery();

                                if (rsSlctTestRstrs.next()) {
                                    countIP = rsSlctTestRstrs.getInt(1);
                                }

                                // query for IN test rosters
                                psSlctTestRstrs.setInt(1, orgNdId);
                                psSlctTestRstrs.setString(2, "IN");
                                psSlctTestRstrs.setInt(3, testCatId);
                                rsSlctTestRstrs = psSlctTestRstrs.executeQuery();

                                if (rsSlctTestRstrs.next()) {
                                    countIN = rsSlctTestRstrs.getInt(1);
                                }

                                // query for IS test rosters
                                psSlctTestRstrs.setInt(1, orgNdId);
                                psSlctTestRstrs.setString(2, "IS");
                                psSlctTestRstrs.setInt(3, testCatId);
                                rsSlctTestRstrs = psSlctTestRstrs.executeQuery();

                                if (rsSlctTestRstrs.next()) {
                                    countIS = rsSlctTestRstrs.getInt(1);
                                }

                                // query for NT test rosters
                                psSlctTestRstrs.setInt(1, orgNdId);
                                psSlctTestRstrs.setString(2, "NT");
                                psSlctTestRstrs.setInt(3, testCatId);
                                rsSlctTestRstrs = psSlctTestRstrs.executeQuery();

                                if (rsSlctTestRstrs.next()) {
                                    countNT = rsSlctTestRstrs.getInt(1);
                                }

                                // query for SC test rosters
                                psSlctTestRstrs.setInt(1, orgNdId);
                                psSlctTestRstrs.setString(2, "SC");
                                psSlctTestRstrs.setInt(3, testCatId);
                                rsSlctTestRstrs = psSlctTestRstrs.executeQuery();

                                if (rsSlctTestRstrs.next()) {
                                    countSC = rsSlctTestRstrs.getInt(1);
                                }

                                // query for parent org node
                                /* psSlctOrgNdPrnt.setInt(1, orgNdId);
                                 psSlctOrgNdPrnt.setInt(2, custId);
                                 rsSlctOrgNdPrnt = psSlctOrgNdPrnt.executeQuery();

                                 if (rsSlctOrgNdPrnt.next())
                                 {
                                 prntOrgNdName = rsSlctOrgNdPrnt.getString(1);
                                 }*/

                                if (count == 0) {
                                    // System.out.println("Within count == 0");
                                    // print header to csv file
                                    data = "Scenario,Site,Classroom,Status,Count";

                                    fileOut.write(data);
                                    fileOut.newLine();
                                    count++;
                                }

                                // print CO data to csv file
                                data = testCatName + "," + prntOrgNdName + ","
                                        + orgNdName + ",CO," + countCO;
                                fileOut.write(data);
                                fileOut.newLine();

                                // print IC data to csv file
                                data = testCatName + "," + prntOrgNdName + ","
                                        + orgNdName + ",IC," + countIC;
                                fileOut.write(data);
                                fileOut.newLine();

                                // print IP data to csv file
                                data = testCatName + "," + prntOrgNdName + ","
                                        + orgNdName + ",IP," + countIP;
                                fileOut.write(data);
                                fileOut.newLine();

                                // print IN data to csv file
                                data = testCatName + "," + prntOrgNdName + ","
                                        + orgNdName + ",IN," + countIN;
                                fileOut.write(data);
                                fileOut.newLine();

                                // print IS data to csv file
                                data = testCatName + "," + prntOrgNdName + ","
                                        + orgNdName + ",IS," + countIS;
                                fileOut.write(data);
                                fileOut.newLine();

                                // print NT data to csv file
                                data = testCatName + "," + prntOrgNdName + ","
                                        + orgNdName + ",NT," + countNT;
                                fileOut.write(data);
                                fileOut.newLine();

                                // print SC data to csv file
                                data = testCatName + "," + prntOrgNdName + ","
                                        + orgNdName + ",SC," + countSC;
                                fileOut.write(data);
                                fileOut.newLine();
                            }
                        }
                    }
                }

                fileOut.close();
                // }
                // else
                // {
                // System.out.println("Error: Customer ID not retrieved.");
                // System.exit(1);
                // }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }

            finally {
                // if (psSlctCustId != null)
                // {
                // psSlctCustId.close();
                // }

                // if (rsSlctCustId != null)
                // {
                // rsSlctCustId.close();
                // }

                if (psSlctTestCats != null) {
                    psSlctTestCats.close();
                }

                if (rsSlctTestCats != null) {
                    rsSlctTestCats.close();
                }

                if (psSlctOrgNdIds != null) {
                    psSlctOrgNdIds.close();
                }

                if (rsSlctOrgNdIds != null) {
                    rsSlctOrgNdIds.close();
                }

                if (psSlctTestRstrs != null) {
                    psSlctTestRstrs.close();
                }

                if (rsSlctTestRstrs != null) {
                    rsSlctTestRstrs.close();
                }

                if (psSlctOrgNdChld != null) {
                    psSlctOrgNdChld.close();
                }

                if (rsSlctOrgNdChld != null) {
                    rsSlctOrgNdChld.close();
                }

                if (conn != null) {
                    conn.close();
                }
            }

            sir.constructMessage(exportFile, emailAddress);
        } else {
            System.out.println("Error: Status not sent.");
            System.exit(1);
        }
    }

    /* Open database connection */
    public boolean openDBconn(String hostname, String connSid, String user,
            String passwd) {
        String connURL = "jdbc:oracle:thin:@" + hostname + ":1521:" + connSid;

        // System.out.println ("db conn: "+connURL);
        try {
            // System.out.println( "Attempting to open database connection...");
            if (conn == null) {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                conn = DriverManager.getConnection("jdbc:oracle:thin:@"
                        + hostname + ":1521:" + connSid + "",
                        user, passwd);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        if (conn == null) {
            System.out.println("Connection to database failed with: " + connURL
                    + " and user: " + user);

            return false;
        } else {
            // System.out.println("Connected to database SID: "+connSid+"\n");
            return true;
        }
    }
}
