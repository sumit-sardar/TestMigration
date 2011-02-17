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
public class CumulativeReport {
    static Connection conn;
    private BufferedWriter fileOut = null;
    private File exportFile = null;

    // private int rowCount = 0;
    public CumulativeReport() {}

    // populate the export file
    public boolean createExport(int _custId, String _email)
        throws Exception {
        boolean returnValue = false;

        Statement stmtGetCust = null;
        Statement stmtGetTest = null;
        Statement stmtGetEndDate = null;

        ResultSet rsGetCust = null;
        ResultSet rsGetTest = null;
        ResultSet rsGetEndDate = null;

        String custRec = "CR";
        String custNm = "";

        String testRec = " TR";
        int testAdminId = 0;
        String testAdminNm = "";
        String form = "";
        String level = "";
        String grade = "AD";
        int tcItemSetId = 0;

        String endRec = "ER";
        java.sql.Date endDate = null;

        String getCust = "select customer_name from student_test_history where customer_id = "
                + _custId;
        String getTest = "select unique ta.test_admin_id, ta.test_admin_name, its.item_set_form, "
                + "its.item_set_level, its.item_set_id from student_test_history ta, item_set its where ta.test_item_set_id "
                + "= its.item_set_id and ta.customer_id = " + _custId
                + " and item_set_id in (select item_set_id from "
                + "item_set_product where product_id = 1009)";
        String getEndDate = "select sysdate from dual";

        try {
            exportFile = File.createTempFile("TABE_Export", ".txt");
            exportFile.deleteOnExit();
            fileOut = new BufferedWriter(new FileWriter(exportFile));

            stmtGetCust = conn.createStatement();
            rsGetCust = stmtGetCust.executeQuery(getCust);

            if (rsGetCust.next()) {
                custNm = rsGetCust.getString(1);
                printRow(custRec + " " + _custId + "," + custNm);

                stmtGetTest = conn.createStatement();
                rsGetTest = stmtGetTest.executeQuery(getTest);

                while (rsGetTest.next()) {
                    testAdminId = rsGetTest.getInt(1);
                    testAdminNm = rsGetTest.getString(2);
                    form = rsGetTest.getString(3);
                    level = rsGetTest.getString(4);
                    tcItemSetId = rsGetTest.getInt(5);
                    printRow(testRec + " " + testAdminId + "," + testAdminNm
                            + "," + form + "," + level + "," + grade);
                    getStudentData(_custId, testAdminId, tcItemSetId);
                }

                stmtGetEndDate = conn.createStatement();
                rsGetEndDate = stmtGetEndDate.executeQuery(getEndDate);

                if (rsGetEndDate.next()) {
                    endDate = rsGetEndDate.getDate(1);
                    printRow(endRec + " " + endDate.toString());
                } else {
                    System.out.println("Error: End Date Not Retrieved.");
                    System.exit(1);
                }

                fileOut.close();
                constructMessage(exportFile, _email);
                returnValue = true;
            } else {
                System.out.println("Error: Customer Data Not Retrieved.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException ie) {
            System.err.println(ie.getMessage());
            ie.printStackTrace();
        }
        finally {
            if (stmtGetCust != null) {
                stmtGetCust.close();
            }

            if (rsGetCust != null) {
                rsGetCust.close();
            }

            if (stmtGetTest != null) {
                stmtGetTest.close();
            }

            if (rsGetTest != null) {
                rsGetTest.close();
            }

            if (fileOut != null) {
                fileOut.close();
            }
        }

        return returnValue;
    }

    public static String usage() {
        return "com.ctb.common.jessica.todelete.CumulativeReport [no arguments]";
    }

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] argv) throws Exception {
        if (argv.length != 0) {
            System.out.println("Usage: java com.ctb.common.jessica.todelete.CumulativeReport");
            System.exit(1);
        }

        String osrHostname = "";
        String osrConnSid = "";
        String osrUser = "";
        String osrPasswd = "";
        String customerId = "";
        String emailAddress = "";

        String custList = "";

        String input = "";

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n--------------------------------------------------");
        System.out.println("\nTABE Export Utility V1.0\n");
        System.out.println("--------------------------------------------------\n");

        System.out.println("\nPlease select an environment: ");
        System.out.println("1 -- ContentQA (contentqa.ctb.com)");
        System.out.println("2 -- Staging (oasstaging.ctb.com)");
        System.out.println("7 -- Production(testadministration.ctb.com)");
        System.out.flush(); // empties buffer, before you input text
        input = stdin.readLine();

        if (input.equals("1")) {
            // the following four variables are for testing
            osrHostname = "puppetteer";
            osrConnSid = "osrd";
            osrUser = "osr";
            osrPasswd = "osr";
        } else if (input.equals("2")) {
            // the following four variables are for staging
            osrHostname = "puppetteer";
            osrConnSid = "osrd";
            osrUser = "osrappqa";
            osrPasswd = "cocobean71";
        } else if (input.equals("7")) {
            // the following four variables are for production
            osrHostname = "oasdb.eppg.com";
            osrConnSid = "osrp";
            osrUser = "osr";
            osrPasswd = "t1dpia";
        } else {
            System.err.println("Error: Invalid Menu Selection.");
            System.exit(1);
        }

        CumulativeReport cr = new CumulativeReport();

        String getCustList = "select distinct customer_id, customer_name from student_test_history "
                + "where product_id = 1009 order by customer_id asc";
        Statement stmtGetCustList = null;
        ResultSet rsGetCustList = null;

        try {
            // open connection to OSR
            if (cr.openDBconn(osrHostname, osrConnSid, osrUser, osrPasswd)) {
                stmtGetCustList = conn.createStatement();
                rsGetCustList = stmtGetCustList.executeQuery(getCustList);

                while (rsGetCustList.next()) {
                    if (!custList.equals("")) {
                        custList += "\n";
                    }

                    custList += (
                            rsGetCustList.getInt(1) + "\t \t"
                            + rsGetCustList.getString(2)
                            );
                }

                if (custList.equals("")) {
                    System.out.println("Error: Customer List Not Retrieved.");
                    System.exit(1);
                }

                System.out.println("\nCustomer ID \tCustomer Name");
                System.out.println(custList);
                System.out.println("Please Select a Customer ID from the List Above: ");

                System.out.flush();
                customerId = stdin.readLine();

                int custId = Integer.parseInt(customerId);

                System.out.println("\nPlease Enter the Complete Email Address Where the TABE Export Should be Sent: ");

                System.out.flush();
                emailAddress = stdin.readLine();

                System.out.println("\nGenerating Export...");

                // create and send the export
                if (!cr.createExport(custId, emailAddress)) {
                    System.out.println("Error: Export not created.");
                    System.exit(1);
                }

                System.out.println("\nTABE Export has been sent to: "
                        + emailAddress);
            }
        } catch (SQLException se) {
            System.err.println(se.getMessage());
            se.printStackTrace();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }
        finally {
            if (stmtGetCustList != null) {
                stmtGetCustList.close();
            }

            if (rsGetCustList != null) {
                rsGetCustList.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
    }

    /* Open database connection */
    public boolean openDBconn(String _hostname, String _connSid, String _user,
            String _passwd) {
        String connURL = "jdbc:oracle:thin:@" + _hostname + ":1521:" + _connSid;
        boolean returnValue = false;

        try {
            // System.out.println( "Attempting to open database connection...");
            if (conn == null) {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                conn = DriverManager.getConnection("jdbc:oracle:thin:@"
                        + _hostname + ":1521:" + _connSid + "",
                        _user, _passwd);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        if (conn == null) {
            System.out.println("Connection to database failed with: " + connURL
                    + " and user: " + _user);
        } else {
            returnValue = true;
        }

        return returnValue;
    }

    // get the data for the TD Item Sets
    private void getItemSetData(int _tcItemSetId, int _testRostId, int _valid)
        throws Exception {
        String testSec = "   TS";
        int itemSetId = 0;
        String secNm = "";
        String secType = "SR";
        int rawScore = 0;
        int suppress = 0;

        String respAr = "    RA";
        String respArray = "";

        String scorAr = "    SA";
        String scoreArray = "";

        String suppressed = "";
        String itemId = "";
        String correct = "0";
        String response = "";

        String getItemSets = "select isp.item_set_id, its.item_set_display_name from item_set_parent "
                + "isp, item_set its where isp.item_set_id = its.item_set_id and isp.parent_item_set_id = "
                + _tcItemSetId
                + " and its.item_set_type = 'TD' and its.sample = 'F' "
                + "order by isp.item_set_sort_order asc";
        Statement stmtGetItemSets = null;
        ResultSet rsGetItemSets = null;

        String getItems = "select isi.item_id, item.suppressed from item_set_item isi, item where "
                + "isi.item_id = item.item_id and isi.item_set_id = ? order by isi.item_sort_order asc";
        PreparedStatement psGetItems = null;
        ResultSet rsGetItems = null;

        String getScoredResponse = "select response, correct_answer from student_item_score where "
                + "item_id = ? and test_item_set_id = ? and student_test_history_id = (select student_test_history_id from "
                + "student_test_history where test_roster_id = ?)";
        PreparedStatement psGetScoredResponse = null;
        ResultSet rsGetScoredResponse = null;

        String getSurveyResponse = "select response from item_response where item_id = ? and "
                + "item_set_id = ? and test_roster_id = ? and response_seq_num = (select "
                + "max(response_seq_num) from item_response where item_id = ? and item_set_id = ? "
                + "and test_roster_id = ?)";
        PreparedStatement psGetSurveyResponse = null;
        ResultSet rsGetSurveyResponse = null;

        try {
            psGetItems = conn.prepareStatement(getItems);

            psGetScoredResponse = conn.prepareStatement(getScoredResponse);

            psGetSurveyResponse = conn.prepareStatement(getSurveyResponse);

            stmtGetItemSets = conn.createStatement();
            rsGetItemSets = stmtGetItemSets.executeQuery(getItemSets);

            while (rsGetItemSets.next()) {
                itemSetId = rsGetItemSets.getInt(1);
                secNm = rsGetItemSets.getString(2);

                rawScore = 0;
                respArray = "";
                scoreArray = "";

                psGetItems.setInt(1, itemSetId);
                rsGetItems = psGetItems.executeQuery();

                while (rsGetItems.next()) {
                    correct = "0";
                    response = "";
                    itemId = rsGetItems.getString(1);
                    suppressed = rsGetItems.getString(2);

                    if (suppressed.equals("T") || suppressed.equals("Y")) {
                        suppress = 1;
                        psGetSurveyResponse.setString(1, itemId);
                        psGetSurveyResponse.setInt(2, itemSetId);
                        psGetSurveyResponse.setInt(3, _testRostId);
                        psGetSurveyResponse.setString(4, itemId);
                        psGetSurveyResponse.setInt(5, itemSetId);
                        psGetSurveyResponse.setInt(6, _testRostId);
                        rsGetSurveyResponse = psGetSurveyResponse.executeQuery();

                        if (rsGetSurveyResponse.next()) {
                            response = rsGetSurveyResponse.getString(1);
                            correct = "*";
                        } else {
                            // System.out.println("Error: Survey Response Not Retrieved.");
                            response = "*";
                            correct = "*";
                        }
                    } else {
                        psGetScoredResponse.setString(1, itemId);
                        psGetScoredResponse.setInt(2, itemSetId);
                        psGetScoredResponse.setInt(3, _testRostId);
                        rsGetScoredResponse = psGetScoredResponse.executeQuery();

                        if (rsGetScoredResponse.next()) {
                            response = rsGetScoredResponse.getString(1);
                            correct = rsGetScoredResponse.getString(2);

                            if (response.equals(correct)) {
                                correct = "1";
                                rawScore++;
                            } else {
                                correct = "0";
                            }
                        } else {
                            // System.out.println("Error: Scored Response Not Retrieved.");
                            response = "*";
                        }
                    }

                    if (response.equals("-")) {
                        response = "*";
                    } else if (response.equals("A")) {
                        response = "1";
                    } else if (response.equals("B")) {
                        response = "2";
                    } else if (response.equals("C")) {
                        response = "3";
                    } else if (response.equals("D")) {
                        response = "4";
                    } else if (response.equals("E")) {
                        response = "5";
                    }

                    respArray += response;
                    scoreArray += correct;
                }

                printRow(testSec + " " + itemSetId + "," + secNm + "," + secType
                        + "," + rawScore + "," + _valid + "," + suppress);
                printRow(respAr + " " + respArray);
                printRow(scorAr + " " + scoreArray);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            if (stmtGetItemSets != null) {
                stmtGetItemSets.close();
            }

            if (rsGetItemSets != null) {
                rsGetItemSets.close();
            }

            if (psGetItems != null) {
                psGetItems.close();
            }

            if (rsGetItems != null) {
                rsGetItems.close();
            }

            if (psGetScoredResponse != null) {
                psGetScoredResponse.close();
            }

            if (rsGetScoredResponse != null) {
                rsGetScoredResponse.close();
            }

            if (psGetSurveyResponse != null) {
                psGetSurveyResponse.close();
            }

            if (rsGetSurveyResponse != null) {
                rsGetSurveyResponse.close();
            }
        }
    }

    // populate the student specific data
    private void getStudentData(int _custId, int _testAdminId, int _tcItemSetId)
        throws Exception {
        String studtRec = "  SR";
        int studentId = 0;
        String extStudentId = "";
        String lastNm = "";
        String firstNm = "";
        String middleNm = "";
        java.sql.Date birthdate = null;
        String gender = "";
        String ethnicity = "";
        String ged = "";
        String diploma = "";
        String program = "";
        String esl = "";
        String aa = "";
        String bb = "";
        String cc = "";
        String rdd = "";
        String mdd = "";

        int valid = 0;

        String validStat = "";
        int testRostId = 0;
        int countItems = 0;
        String[] items = null;
        int currentItem = 0;

        String getTestRost = "select student_id, test_roster_id, validation_status from "
                + "test_roster where test_admin_id = " + _testAdminId;
        Statement stmtGetTestRost = null;
        ResultSet rsGetTestRost = null;

        String getStudent = "select ext_pin1, last_name, first_name, middle_name, birthdate, "
                + "gender, ethnicity from student_test_history where student_id = ? and test_roster_id = ?";
        PreparedStatement psGetStudent = null;
        ResultSet rsGetStudent = null;

        String getCountScItems = "select count(item_id) from item_set_item where item_set_id in "
                + "(select item_set_id from item_set_parent where parent_item_set_id = "
                + _tcItemSetId
                + " and item_set_id in (select item_set_id from item_set where "
                + "item_set_type = 'SC')) order by item_sort_order asc";
        Statement stmtGetCountScItems = null;
        ResultSet rsGetCountScItems = null;

        String getScItems = "select item_id from item_set_item where item_set_id in "
                + "(select item_set_id from item_set_parent where parent_item_set_id = "
                + _tcItemSetId
                + " and item_set_id in (select item_set_id from item_set where "
                + "item_set_type = 'SC')) order by item_sort_order asc";
        Statement stmtGetScItems = null;
        ResultSet rsGetScItems = null;

        String getScResponse = "select response from aux_item_response where item_id = ? "
                + "and test_roster_id = ? order by response asc";
        PreparedStatement psGetScResponse = null;
        ResultSet rsGetScResponse = null;

        try {
            stmtGetCountScItems = conn.createStatement();
            rsGetCountScItems = stmtGetCountScItems.executeQuery(getCountScItems);

            if (rsGetCountScItems.next()) {
                countItems = rsGetCountScItems.getInt(1);
            } else {
                System.out.println("Error: Count of SC Items Not Retrieved.");
                System.exit(1);
            }

            items = new String[countItems];

            currentItem = 0;
            stmtGetScItems = conn.createStatement();
            rsGetScItems = stmtGetScItems.executeQuery(getScItems);

            while (rsGetScItems.next()) {
                items[currentItem] = rsGetScItems.getString(1);
                currentItem++;
            }

            stmtGetTestRost = conn.createStatement();
            rsGetTestRost = stmtGetTestRost.executeQuery(getTestRost);

            psGetStudent = conn.prepareStatement(getStudent);

            psGetScResponse = conn.prepareStatement(getScResponse);

            while (rsGetTestRost.next()) {
                studentId = rsGetTestRost.getInt(1);
                testRostId = rsGetTestRost.getInt(2);
                validStat = rsGetTestRost.getString(3);

                if (validStat.equals("IN")) {
                    valid = 1;
                }

                psGetStudent.setInt(1, studentId);
                psGetStudent.setInt(2, testRostId);
                rsGetStudent = psGetStudent.executeQuery();

                if (rsGetStudent.next()) {
                    extStudentId = rsGetStudent.getString(1);
                    lastNm = rsGetStudent.getString(2);
                    firstNm = rsGetStudent.getString(3);
                    middleNm = rsGetStudent.getString(4);
                    birthdate = rsGetStudent.getDate(5);
                    gender = rsGetStudent.getString(6);
                    ethnicity = rsGetStudent.getString(7);
                } else {
                    System.out.println("Error: Student Data Not Retrieved.");
                    System.exit(1);
                }

                ged = "";
                diploma = "";
                program = "";
                esl = "";
                aa = "";
                bb = "";
                cc = "";
                rdd = "";
                mdd = "";

                for (int i = 0; i < items.length; i++) {
                    psGetScResponse.setString(1, items[i]);
                    psGetScResponse.setInt(2, testRostId);
                    rsGetScResponse = psGetScResponse.executeQuery();

                    while (rsGetScResponse.next()) {
                        switch (i) {
                        case 0:
                            esl += rsGetScResponse.getString(1);

                            break;

                        case 1:
                            ged += rsGetScResponse.getString(1);

                            break;

                        case 2:
                            diploma += rsGetScResponse.getString(1);

                            break;

                        case 3:
                            program += rsGetScResponse.getString(1);

                            break;

                        case 4:
                            aa += rsGetScResponse.getString(1);

                            break;

                        case 5:
                            bb += rsGetScResponse.getString(1);

                            break;

                        case 6:
                            cc += rsGetScResponse.getString(1);

                            break;

                        case 7:
                            rdd += rsGetScResponse.getString(1);

                            break;

                        case 8:
                            mdd += rsGetScResponse.getString(1);

                            break;
                        }
                    }
                }

                if (extStudentId == null) {
                    extStudentId = "";
                }

                if (middleNm == null) {
                    middleNm = "";
                }

                if (ethnicity == null) {
                    ethnicity = "";
                }

                printRow(studtRec + " " + studentId + "," + extStudentId + ","
                        + lastNm + "," + firstNm + "," + middleNm + ","
                        + birthdate.toString() + "," + gender + "," + ethnicity
                        + "," + ged + "," + diploma + "," + program + "," + esl
                        + "," + aa + "," + bb + "," + cc + "," + rdd + "," + mdd);
                getItemSetData(_tcItemSetId, testRostId, valid);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            if (stmtGetTestRost != null) {
                stmtGetTestRost.close();
            }

            if (rsGetTestRost != null) {
                rsGetTestRost.close();
            }

            if (psGetStudent != null) {
                psGetStudent.close();
            }

            if (rsGetStudent != null) {
                rsGetStudent.close();
            }

            if (stmtGetCountScItems != null) {
                stmtGetCountScItems.close();
            }

            if (rsGetCountScItems != null) {
                rsGetCountScItems.close();
            }

            if (stmtGetScItems != null) {
                stmtGetScItems.close();
            }

            if (rsGetScItems != null) {
                rsGetScItems.close();
            }

            if (psGetScResponse != null) {
                psGetScResponse.close();
            }

            if (rsGetScResponse != null) {
                rsGetScResponse.close();
            }
        }
    }

    private void constructMessage(File _file, String email)
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
        message.setSubject("TABE Export");

        // Create the first part
        Multipart mailBody = new MimeMultipart();
        MimeBodyPart mainBody = new MimeBodyPart();

        mainBody.setText("The attached file contains the research data for the TABE Standardization Study. \n");
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

    // write to export file
    private void printRow(String _data) {
        try {
            fileOut.write(_data);
            fileOut.newLine();
        } catch (IOException ie) {
            System.err.println(ie.getMessage());
            ie.printStackTrace();
        }
    }
}
