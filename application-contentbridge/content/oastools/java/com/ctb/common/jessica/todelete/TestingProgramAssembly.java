package com.ctb.common.jessica.todelete;


import java.io.*;
import java.sql.*;


public class TestingProgramAssembly {
    static Connection conn;
    public static int testCatalogId = 0;
    public static int sequence = 0;
    public static int parentItemSetId = 0;
    public static int itemSetId = 0;
    public static int productId = 0;

    public static final int TEST_CATALOG_ADD = 1;
    public static final int TEST_CATALOG_EDIT = 2;
    public static final int TEST_CATALOG_VIEW = 3;
    public static final int ITEM_SET_ADD = 4;
    public static final int PRODUCT_SELECT = 5;
    // public static final String TEST_CATALOG_TYPE = "TC";
    // public static final String TEST_DELIVERABLE_TYPE = "TD";

    public TestingProgramAssembly() {}

    /* Open database connection */
    public boolean openDBconn(String hostname, String connSid, String user, String passwd) {
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

    /* Prompt the user for input */
    public void promptUser(int mode) throws Exception {
        String testCatAddPrompt = "Please view the list above and enter the ID of the Testing Program you want to assemble: ";
        String testCatViewPrompt = "Please view the list above and enter the ID of the Testing Program you want to view: ";
        String testCatEditPrompt = "** Note that deletion is final * Press CTRL-c to exit ** "
                + "\nPlease view the list above and enter the ID of the Testing Program you want to delete: ";
        String itemSetPrompt = "Please view the list above and enter the ID of the Item Set you want to add: ";
        
        String inputId = "";
        
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        if (mode == TEST_CATALOG_ADD) {
            System.out.println(testCatAddPrompt);
            System.out.flush(); // empties buffer, before you input text
            inputId = stdin.readLine();
            if (inputId.equals("")) {
                promptUser(TEST_CATALOG_ADD);
            } else {
                parentItemSetId = Integer.parseInt(inputId);
            }
        } else if (mode == TEST_CATALOG_EDIT) {
            System.out.println(testCatEditPrompt);
            System.out.flush(); // empties buffer, before you input text
            inputId = stdin.readLine();
            if (inputId.equals("")) {
                promptUser(TEST_CATALOG_EDIT);
            } else {
                parentItemSetId = Integer.parseInt(inputId);
            }
        } else if (mode == TEST_CATALOG_VIEW) {
            System.out.println(testCatViewPrompt);
            System.out.flush(); // empties buffer, before you input text
            inputId = stdin.readLine();
            if (inputId.equals("")) {
                promptUser(TEST_CATALOG_VIEW);
            } else {
                parentItemSetId = Integer.parseInt(inputId);
            }
        } else if (mode == ITEM_SET_ADD) {
            System.out.println(itemSetPrompt);
            System.out.flush(); // empties buffer, before you input text
            inputId = stdin.readLine();
            if (inputId.equals("")) {
                promptUser(ITEM_SET_ADD);
            } else {
                itemSetId = Integer.parseInt(inputId);
            }
        } else {
            System.out.println("Error: Prompt mode not valid.");
            System.exit(1);
        }
    }

    /* Retrieve lists to display for user selection */
    public void retrieveList(int mode) throws Exception {
        String productSql = "SELECT PRODUCT_ID, PRODUCT_NAME FROM PRODUCT WHERE ACTIVATION_STATUS = 'AC'";
        // String prodIdSql = "SELECT PRODUCT_ID FROM PRODUCT WHERE PRODUCT_NAME LIKE '%Scenario%'";
        // String testCatSql = "SELECT ITEM_SET_ID, ITEM_SET_DISPLAY_NAME FROM ITEM_SET WHERE UPPER(ITEM_SET_NAME) LIKE UPPER('%Scenario%') AND ACTIVATION_STATUS = 'AC' AND ITEM_SET_TYPE = 'TC'";
        String testCatSql = "SELECT ITEM_SET_ID, ITEM_SET_DISPLAY_NAME FROM ITEM_SET WHERE ITEM_SET_ID IN (SELECT ITEM_SET_ID FROM ITEM_SET_PRODUCT WHERE PRODUCT_ID = ?) AND ACTIVATION_STATUS = 'AC' AND ITEM_SET_TYPE = 'TC'";
        // String itemSetSql = "SELECT ITEM_SET_ID, ITEM_SET_DISPLAY_NAME FROM ITEM_SET WHERE UPPER(ITEM_SET_NAME) LIKE UPPER('%TABE%') AND ACTIVATION_STATUS = 'AC' AND ITEM_SET_TYPE = 'TD'";
        String itemSetSql = "SELECT ITEM_SET_ID, ITEM_SET_DISPLAY_NAME FROM ITEM_SET WHERE ITEM_SET_ID IN (SELECT ITEM_SET_ID FROM ITEM_SET_PRODUCT WHERE PRODUCT_ID = ?) AND ACTIVATION_STATUS = 'AC' AND ITEM_SET_TYPE = 'TD'";
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        PreparedStatement ps = null;
        // PreparedStatement ps2 = null;
        ResultSet rs = null;
        // ResultSet rs2 = null;
        String retrievedId = "";
        String displayName = "";
        String tempPrint = "";

        try {
            if (mode == TEST_CATALOG_ADD) {
                ps = conn.prepareStatement(testCatSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    retrievedId = rs.getString(1);
                    displayName = rs.getString(2);
                    tempPrint = retrievedId + "\t" + displayName;
                    System.out.println(tempPrint);
                }
                // promptUser(TEST_CATALOG_ADD);
            } else if (mode == TEST_CATALOG_EDIT) {
                ps = conn.prepareStatement(testCatSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    retrievedId = rs.getString(1);
                    displayName = rs.getString(2);
                    tempPrint = retrievedId + "\t" + displayName;
                    System.out.println(tempPrint);
                }
                // promptUser(TEST_CATALOG_EDIT);
            } else if (mode == TEST_CATALOG_VIEW) {
                ps = conn.prepareStatement(testCatSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    retrievedId = rs.getString(1);
                    displayName = rs.getString(2);
                    tempPrint = retrievedId + "\t" + displayName;
                    System.out.println(tempPrint);
                }
                // promptUser(TEST_CATALOG_VIEW);
            } else if (mode == ITEM_SET_ADD) {
                ps = conn.prepareStatement(itemSetSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    retrievedId = rs.getString(1);
                    displayName = rs.getString(2);
                    tempPrint = retrievedId + "\t" + displayName;
                    System.out.println(tempPrint);
                }
                // promptUser(ITEM_SET_ADD);
            } else if (mode == PRODUCT_SELECT) {
                ps = conn.prepareStatement(productSql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    retrievedId = rs.getString(1);
                    displayName = rs.getString(2);
                    tempPrint = retrievedId + "\t" + displayName;
                    System.out.println(tempPrint);
                }
            } else {
                System.out.println("Error: Retrieval mode not valid.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            // if (ps2 != null) ps2.close();
            // if (rs2 != null) rs2.close();
        }
    }

    /* Assembles the parent/child relationship for the Testing Program */
    public void assemble(int mode) throws Exception {
        int createdBy = 1;
        String checkExist = "SELECT * FROM ITEM_SET_PARENT WHERE PARENT_ITEM_SET_ID = ? AND ITEM_SET_TYPE = 'TD'";
        String insrtItemSetPrnt = "INSERT INTO ITEM_SET_PARENT(PARENT_ITEM_SET_ID, ITEM_SET_ID, ITEM_SET_SORT_ORDER, CREATED_BY, CREATED_DATE_TIME, ITEM_SET_TYPE, PARENT_ITEM_SET_TYPE) "
                + "VALUES (" + parentItemSetId + ", " + itemSetId + ", "
                + sequence + ", " + createdBy + ", SYSDATE, 'TD', 'TC')";
        // System.out.println(insrtItemSetPrnt);
        String deleteItemSetPrnt = "DELETE FROM ITEM_SET_PARENT WHERE PARENT_ITEM_SET_ID = ? AND ITEM_SET_TYPE <> 'SC'";
        String getItemSetPrnt = "SELECT ITEM_SET_ID FROM ITEM_SET_PARENT WHERE PARENT_ITEM_SET_ID = ? AND ITEM_SET_TYPE = 'TD' ORDER BY ITEM_SET_SORT_ORDER ASC";
        String getItemSetName = "SELECT ITEM_SET_DISPLAY_NAME FROM ITEM_SET WHERE ITEM_SET_ID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        int count = 0;
        String dsplyName = "";
        
        try {
            if (mode == TEST_CATALOG_ADD) {
                if (sequence == 1) {
                    ps2 = conn.prepareStatement(checkExist);
                    ps2.setInt(1, parentItemSetId);
                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        System.out.println("The selected Testing Program has already been assembled, you must delete all Item Sets from the Testing Program to continue.");
                        System.exit(1);
                    }
                }
                ps = conn.prepareStatement(insrtItemSetPrnt);
                ps.executeUpdate();
            } else if (mode == TEST_CATALOG_EDIT) {
                ps = conn.prepareStatement(deleteItemSetPrnt);
                ps.setInt(1, parentItemSetId);
                ps.executeUpdate();
            } else if (mode == TEST_CATALOG_VIEW) {
                ps = conn.prepareStatement(getItemSetPrnt);
                ps2 = conn.prepareStatement(getItemSetName);
                ps.setInt(1, parentItemSetId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    count++;
                    if (count == 1) {
                        System.out.println("\nThese are the Item Sets included in the selected Testing Program:");
                    }
                    itemSetId = rs.getInt(1);
                    ps2.setInt(1, itemSetId);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        dsplyName = rs2.getString(1);
                        System.out.println(dsplyName);
                    } 
                }
                if (count == 0) {
                    System.out.println("\nThere are no Item Sets included in the selected Testing Program.");
                }
            } else {
                System.out.println("Error: Assembly mode not valid.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (ps2 != null) {
                ps2.close();
            }
            if (rs2 != null) {
                rs2.close();
            }
        }
    }

    public static void main(String[] argv) throws Exception {
        if (argv.length < 0 && argv.length != 4) {
            System.out.println("Usage: java com.ctb.common.jessica.todelete.TestingProgramAssembly dbhost dbsid user passwd or java com.ctb.common.jessica.todelete.TestingProgramAssembly");
            System.exit(1);
        }

        String dbHost = "";
        String dbSid = "";
        String username = "";
        String password = "";
        String message = "";
        boolean testCatalogDone = false;
        boolean itemSetDone = false;
        boolean argsOnCmdLn = false;
        boolean addMode = false;
        boolean editMode = false;
        boolean viewMode = false;

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        TestingProgramAssembly tpa = new TestingProgramAssembly();

        if (argv.length == 4) {
            dbHost = argv[0];
            dbSid = argv[1];
            username = argv[2];
            password = argv[3];
            argsOnCmdLn = true;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.println("\nTesting Program Assembly Utility V1.0\n");
        System.out.println("--------------------------------------------------\n");

        if (!argsOnCmdLn || dbSid.equalsIgnoreCase("onad")
                || dbSid.equalsIgnoreCase("onap")) {
            System.out.println("\nPlease select an environment: ");
            System.out.println("1 -- ContentQA (contentqa.ctb.com) ");
            System.out.println("2 -- Staging (oasstaging.ctb.com) ");
            System.out.println("7 -- Production (testadministration.ctb.com) ");
            System.out.flush(); // empties buffer, before you input text
            message = stdin.readLine();
            if (message.equals("2")) {
                // the following four variables are for staging/appqa
                dbHost = "puppetteer";
                dbSid = "onad";
                username = "oasappqa";
                password = "cyhmn44";

                // the following four variables are for development
                /* dbHost = "corinth";
                 dbSid = "oasdev";
                 username = "jtrundy";
                 password = "jtrundy";*/

            } else if (message.equals("1")) {
                // the following four variables are for contentqa
                dbHost = "puppetteer";
                dbSid = "onad";
                username = "oas";
                password = "oas";
                
                // the following four variables are for testing
                /* dbHost = "corinth";
                 dbSid = "oasdev";
                 username = "oetest";
                 password = "oetest";*/

                // the following four variables are for development
                /* dbHost = "corinth";
                 dbSid = "oasdev";
                 username = "jtrundy";
                 password = "jtrundy";*/
            } else if (message.equals("7")) {
                // the following four variables are for production
                dbHost = "oasdb.eppg.com";
                dbSid = "onap";
                username = "oas";
                password = "wyslm64";

                // the following four variables are for development
                /* dbHost = "corinth";
                 dbSid = "oasdev";
                 username = "jtrundy";
                 password = "jtrundy";*/

            } else {
                System.err.println("Error: Invalid Menu Selection.");
                System.exit(1);
            }
        }
        System.out.println("\nWorking in : " + username + "@" + dbSid);
        System.out.println("\nPlease select from the following menu: ");
        System.out.println("1 -- Assemble a Testing Program.");
        System.out.println("2 -- Delete all Item Sets from a Testing Program.");
        System.out.println("3 -- View a Testing Program.");
        System.out.flush(); // empties buffer, before you input text
        message = stdin.readLine();

        if (message.equals("1")) {
            addMode = true;
        } else if (message.equals("2")) {
            editMode = true;
        } else if (message.equals("3")) {
            viewMode = true;
        } else {
            System.out.println("Error: Invalid menu selection.");
            System.exit(1);
        }

        System.out.println("");
        // open connection to the database
        if (tpa.openDBconn(dbHost, dbSid, username, password)) {
            System.out.println("");
            tpa.retrieveList(PRODUCT_SELECT);
            System.out.println("Please view the list above and enter the ID of the "
                    + "Product you are using:  ");
            System.out.flush();
            message = stdin.readLine();
            productId = Integer.parseInt(message);
            System.out.println("");
            
            if (addMode) {
                while (!testCatalogDone) {
                    tpa.retrieveList(TEST_CATALOG_ADD);
                    tpa.promptUser(TEST_CATALOG_ADD);
                    // tpa.retrieveId(TEST_CATALOG_ADD);
                    System.out.println("");
                    tpa.retrieveList(ITEM_SET_ADD);
                    while (!itemSetDone) {
                        tpa.promptUser(ITEM_SET_ADD);
                        // tpa.retrieveId(ITEM_SET_ADD);
                        sequence++;
                        tpa.assemble(TEST_CATALOG_ADD);

                        System.out.println("\nDo you need to add to this Testing Program? (y/n)");
                        System.out.flush(); // empties buffer, before you input text
                        message = stdin.readLine();

                        if (message.equals("n")) {
                            itemSetDone = true;
                        } else {
                            itemSetId = 0;
                            System.out.println("");
                            // itemSetName = "";
                        }
                    }

                    System.out.println("\nDo you need to assemble another Testing Program? (y/n)");
                    System.out.flush(); // empties buffer, before you input text
                    message = stdin.readLine();
                    if (message.equals("n")) {
                        testCatalogDone = true;
                    } else if (message.equals("y")) {
                        itemSetDone = false;
                        sequence = 0;
                        itemSetId = 0;
                        // itemSetName = "";
                        parentItemSetId = 0;
                        // testCatalogName = "";
                        System.out.println("");
                    }
                }
            } else if (editMode) {
                while (!testCatalogDone) {
                    tpa.retrieveList(TEST_CATALOG_EDIT);
                    tpa.promptUser(TEST_CATALOG_EDIT);
                    // tpa.retrieveId(TEST_CATALOG_EDIT);
                    tpa.assemble(TEST_CATALOG_EDIT);

                    System.out.println("\nDo you need to delete Item Sets from another Testing Program? (y/n)");
                    System.out.flush(); // empties buffer, before you input text
                    message = stdin.readLine();
                    if (message.equals("n")) {
                        testCatalogDone = true;
                    } else if (message.equals("y")) {
                        parentItemSetId = 0;
                        System.out.println("");
                        // testCatalogName = "";
                    }
                }
            } else if (viewMode) {
                while (!testCatalogDone) {
                    tpa.retrieveList(TEST_CATALOG_VIEW);
                    tpa.promptUser(TEST_CATALOG_VIEW);
                    tpa.assemble(TEST_CATALOG_VIEW);
                    // tpa.retrieveId(TEST_CATALOG_VIEW);

                    System.out.println("\nDo you need to view another Testing Program? (y/n)");
                    System.out.flush(); // empties buffer, before you input text
                    message = stdin.readLine();
                    if (message.equals("n")) {
                        testCatalogDone = true;
                    } else if (message.equals("y")) {
                        parentItemSetId = 0;
                        System.out.println("");
                        // testCatalogName = "";
                    }
                }
            } else {
                System.out.println("Error: Utility mode not valid.");
                System.exit(1);
            }
        } else {
            System.out.println("Error: Testing Program not assembled.");
            System.exit(1);
        }
    }
}
