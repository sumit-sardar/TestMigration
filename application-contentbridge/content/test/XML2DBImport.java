package com.ctb.common.tools;


/*
 * XML2DBImport.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


// Import the W3C DOM classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;


//Import FILE and I/O classes
import java.io.*;
import java.lang.*;

// Import other Java classes
import java.net.URL;
import java.sql.*;
import java.text.*;
import oracle.sql.*;
import oracle.jdbc.*;


/*
 *@author Eddie Jun, Jessica Glissmann
 *@version $Revision$
 *@version $Revision$
 */
public class XML2DBImport
{

    private static final String ACTIVE_STATUS = "AC";
    private static final String INACTIVE_STATUS = "IN";
    private static final String DISCONTINUED = "DC";
    private static final String FALSE = "F";
    private static final String TRUE = "T";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final int DEFAULT_CREATED_BY = 2;
    private static final int INTERWOVEN_CREATED_BY = 2;
    private static final String SELECTED_RESPONSE = "SR";
    private static final String CONSTRUCTED_RESPONSE = "CR";
    private static final String SA_ITEM_TYPE = "SA";
    private int createdBy = 0;
    
    private static final String TABE_MEDIA_PATH = "/oastd-web/versions/tabe/1.0/";
    private static final String CAB_MEDIA_PATH = "/oastd-web/versions/cab/1.0/";
    private String mediaPath = "";
    
    private static final String SWF_MIME_TYPE = "application/x-shockwave-flash";
    private static final String XML_MIME_TYPE = "text/xml";
    private static final String PDF_MIME_TYPE = "application/pdf";

    private static final String CTB_ROOT_ID = "1";
    private static final String CTB_ID = "2";
    private static final String REPORTING = "RE";
    private static final String TEST_CATALOG = "TC";
    private static final String TEST_DELIVERABLE = "TD";
    
    private static final String XML_MEDIA = "IBXML";
    private static final String ITEM_BANK_MEDIA = "IBSWF";
    private static final String ANSWER_KEY_MEDIA = "AKSWF";
    private static final String PDF_MEDIA = "IBPDF";
    private static final String ANSWER_KEY_PDF_MEDIA = "AKPDF";

    private static boolean production = false;
    private static boolean tabeContent = false;
    private static boolean hasAssessment = false;
    private static boolean hasSubTest = false;
    private static boolean hasItemSet = false;
    private static boolean newVersion = false;
    private static boolean sampleSet = false;
    private static boolean suppressOutput = false;
    private static boolean interwoven = false;
    private static boolean allAccess = false;
    private static boolean crItem = false;

    //Test_Catalog table fields to update
    private String Test_Catalog_ID;
    private String Test_Catalog_Test_Name;
    private String Test_Catalog_Version;
    private String Test_Catalog_Subject;
    private String Test_Catalog_Test_Grade;
    private String Test_Catalog_Level;
    private String Test_Catalog_Test_Form;
    private String Test_Catalog_Test_Display_Name;

    //Item_Set Table fields to update
    private int Item_Set_ID;
    private String Item_Set_Ext_EMS_Item_Set_ID;
    private String Item_Set_Type;
    private int Item_Set_Sort_Order;
    private String Item_Set_Name;
    private String Item_Set_Display_Name;
    private String Item_Set_Version;
    private String Item_Set_Subject;
    private String Item_Set_Sample;
    private String Item_Set_Time_Limit;
    private String Item_Set_Activation_Status;
    private String Item_Set_Is_Finished;
    private int Item_Set_Created_By;
    private int Item_Set_Owner_Customer_ID;
    private String Item_Set_Media_Path;
    private String SubTest_Item_Set_ID;
    private String ItemSet_Item_Set_ID;
    private String ItemSet_Description;

    //Item Table fields to update
    private String Item_ID;
    private String Correct_Answer;
    private String Correct_Answer_ID;
    private String Item_Grade;
    private String Item_Type;
    private String Item_Field_Test;
    private String Item_Suppressed;
    private String Item_Sample;
    private String EMS_Item_ID;
    private int Item_Created_By;
    private String Item_History;
    private String Item_Description;
    private String Item_Ext_Stimulus_ID;
    private String Item_Ext_Stimulus_Title;
    private String Item_Version;
    private String Item_Template_ID;
    private String Item_Activation_Status;
    private String Item_Subject;
    private String Item_Max_Points;
    private String Item_Min_Points;

    //Item_Set_Item table fields to update
    private String Item_ObjectiveID;
    private int Item_Set_Item_Sort_Order;

    private Connection conn;
    private String Product_ID;

    private int old_test_catalog_id = 0;
    private static int Item_no = 0;
    private static int Choice_no = 1;
    private int timelimit_in_seconds = 0;
    
    private static String Answer_ID = "";
    private static String Answer_Type = "";
    private static String Hierarchy_Type = "";
    private static String Hierarchy_CurriculumID = "";
    private static String Hierarchy_Name = "";
  
    /** constructor */
    public XML2DBImport()
    {
    }

    /**
     * Determines whether current ITEM_SET_ITEM relationship exists.
     *
     * @param _emsItemId String
     * @param _itemSetId String
     * @return boolean
     */
    private boolean doesItemSetItemExist(String _emsItemId, String _itemSetId) throws SQLException
    {
        boolean found = false;

        String result = executeSqlWithReturn("SELECT COUNT(*) FROM ITEM_SET_ITEM WHERE "+
            "ITEM_ID = '"+_emsItemId+"' AND ITEM_SET_ID = "+_itemSetId);

        if(!result.equals("0"))
        {
            found = true;
        }      
        return found;

    }//public doesItemSetItemExist
    
    /**
     * Executes the passed in sql code.
     *
     * @param sql String
     * @return boolean
     */
    private boolean executeSqlStatement(String _sql) throws SQLException
    {

        Statement stmt = null;
        boolean returnValue = false;
        
        //System.out.println("SQL: "+_sql);

        try
        {
            stmt = conn.createStatement();
            //execute the passed in String as a SQL query
            stmt.executeUpdate(_sql);
            returnValue = true;
        }
        finally
        {
            if (stmt != null) stmt.close();
        }
        return returnValue;
    }//public executeSqlStatement
    
    /**
     * Executes a sql query that will only return one row.
     *
     * @param sql String
     * @return String
     */
    private String executeSqlWithReturn(String _sql) throws SQLException
    {

        ResultSet rslt = null;
        Statement stmt = null;
        String result = "";

        //System.out.println("SQL With Return: "+_sql);

        try
        {
            stmt = conn.createStatement();
            //execute the passed in String as a SQL query
            rslt = stmt.executeQuery(_sql);
            if(rslt.next())
            {
                result = rslt.getString(1);
            }
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (rslt != null) rslt.close();
        }
        return result;
    }//public executeSqlWithReturn
    
    /** finalize */
    protected void finalize() throws Throwable
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException ignore)
            {
            }
        }
        super.finalize();
    }//protected finalize

    /**
     * Replaces one apostrophe with two for sql inserts.
     *
     * @param _text String
     * @return String
     */
     private String formatText(String _text) throws Exception
     {
        //System.out.println("Within formatText.");
        int apostIndex = 0;
        int lastApostIndex = 0;
        boolean textEnd = false;
        String returnText = "";
        
        if(_text.indexOf("'", lastApostIndex) == -1)
        {
            returnText = _text;
        }
        else
        {
            while(!textEnd)
            {
                if(_text.indexOf("'", lastApostIndex) != -1)
                {          
                    apostIndex = _text.indexOf("'", lastApostIndex);
                    returnText = _text.substring(0, apostIndex+1) + "'" + _text.substring(apostIndex+1);
                    _text = returnText;
                    lastApostIndex = apostIndex + 2;
                }
                else
                {
                    textEnd = true;
                }
                System.out.println("returnText: "+returnText);
            }
        }
        return returnText;
     }
    
    /**
     * Retrieves Text from the child nodes and appends it to the Item Description.
     *
     * @param _node Node
     * @return void
     */
    private void getChildText(Node _node)
    {
        Node cldNd = null;
        Text txNd = null;
        int lngth = 0;
        //retrieve count of child nodes
        lngth = _node.getChildNodes().getLength();
        //loop for each child of the current node
        for(int ndNbr = 0; ndNbr < lngth; ndNbr++)
        {
            cldNd = _node.getChildNodes().item(ndNbr);
            //we are only looking for text nodes
            if(cldNd.getNodeType() == Node.TEXT_NODE)
            {
                txNd = (Text) cldNd;
                //append the text to the Item Description
                Item_Description += txNd.getNodeValue();
            }
            else
            {
                //if the child is not a text node we will get the text from its children
                getChildText(cldNd);
            }
        }
    }//public getChildText

    /**
     * Retrieves ITEM_SET_ID based on the passed in CMS Id.
     *
     * @param _emsItemSetId String
     * @return String
     */
    private String getItemSetIdByCmsItemSetId(String _cmsItemSetId) throws SQLException
    {
        String result = executeSqlWithReturn("SELECT ITEM_SET_ID FROM ITEM_SET WHERE EXT_CMS_ITEM_SET_ID = '"+
            _cmsItemSetId+"'");
        
        return result;

    }//public getItemSetIdByCmsItemSetId
    
    /**
     * Retrieves ITEM_SET_ID based on the passed in EMS Id.
     *
     * @param _emsItemSetId String
     * @return String
     */
    private String getItemSetIdByEmsItemSetId(String _emsItemSetId) throws SQLException
    {
        String result = executeSqlWithReturn("SELECT ITEM_SET_ID FROM ITEM_SET WHERE EXT_EMS_ITEM_SET_ID = '"+
            _emsItemSetId+"' AND ACTIVATION_STATUS = '"+ACTIVE_STATUS+"'");
        
        return result;

    }//public getItemSetIdByEmsItemSetId
    
    /**
     * Parses the xml to find the ItemSet ID.  
     * Returned boolean indicates success.
     *
     * @param _filename String
     * @return boolean
     */
    private boolean getItemSetIdFromXML(String _filename) 
        throws SAXException, ParserConfigurationException, IOException, Exception
    {
        Document doc = null;
        NodeIterator iter = null;
        TreeWalker walker = null;
        boolean returnValue = false;

        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new File(_filename));
            String sqlResult = "";
            
            if(tabeContent)
            {
                mediaPath = TABE_MEDIA_PATH;
            }
            else
            {
                mediaPath = CAB_MEDIA_PATH;
            }
            if(interwoven)
            {
                createdBy = INTERWOVEN_CREATED_BY;
            }
            else
            {
                createdBy = DEFAULT_CREATED_BY;
            }
            
            // Check if Traversals are supported
            if( doc.isSupported("Traversal", "2.0" ) )
            {

                // Set up a TreeWalker with an inline "accept up to Item Nodes" filter
                walker = ((DocumentTraversal)doc).createTreeWalker( doc,
                NodeFilter.SHOW_ALL, new ItemFilter(), true );
                Node n;
                // Stroll through the Nodes looking for Specific Elements
                while((n = walker.nextNode()) != null)
                {
                    Element e = (Element ) n;
                    String ElemName = n.getNodeName().trim();

                    //We are only retrieving the item set id                  
                    if (ElemName.equals("ItemSet"))
                    {
                        hasItemSet = true;
                        Item_Set_Ext_EMS_Item_Set_ID = e.getAttribute("ID");
                        if(Item_Set_Ext_EMS_Item_Set_ID.indexOf("IS") == -1)
                        {
                            Item_Set_Ext_EMS_Item_Set_ID = Test_Catalog_Test_Name;
                            tabeContent = true;
                        }
                        
                        if((getItemSetIdByEmsItemSetId(Item_Set_Ext_EMS_Item_Set_ID)).equals(""))                                                      
                        {                                
                            if(!suppressOutput)
                            {
                                System.out.println("Item Set not found. Please verify import file and try again.");
                            }
                            returnValue = false;
                        }
                        else
                        {
                            ItemSet_Item_Set_ID = sqlResult;
                            returnValue = true;
                        }                        
                    }
                    else if(ElemName.equals("SubTest"))
                    {
                        Test_Catalog_Test_Name = e.getAttribute ("Title");
                    }
                    else continue;
                }//while

            } //if (doc...
            else
            {
                if(!suppressOutput)
                {
                    System.out.println("Sorry but Traversal is not implemented");
                    System.out.println("Check CLASSPATH to include xerces.jar, version 1.3 or above.");
                }
                returnValue = false;
            }
        } //try
        catch (SAXException sxe)
        {
            // Error generated during parsing)
            Exception  x = sxe;
            if (sxe.getException() != null && !suppressOutput)
            {
                x = sxe.getException();
                x.printStackTrace();
            }
        }

        catch (ParserConfigurationException pce)
        {
            // Parser with specified options can't be built
            if(!suppressOutput)
            {
                pce.printStackTrace();
            }
        }

        catch (IOException ioe)
        {
            // I/O error
            if(!suppressOutput)
            {
                ioe.printStackTrace();
            }
        }
        catch ( Exception e )
        {
            String excpt = e.getMessage();
            if(excpt.indexOf("org.apache.xerces.dom.DeferredDocumentTypeImpl") != -1)
            {
                returnValue = true;
            }
            else
            {
                throw e;
            }
        }
        return returnValue;
    }//public getItemSetIdFromXML
    
     /**
     * Retrieves the next ITEM_SET_ID in sequence.
     *
     * @return String
     */
    private String getSeqItemSetId() throws SQLException
    {
        String result = "";
        String sql = "SELECT SEQ_ITEM_SET_ID.NEXTVAL FROM DUAL";
        
        result = executeSqlWithReturn(sql);
        return result;
    }//public getSeqItemSetId
    
    /**
     * Retrieves the next TEST_CATALOG_ID in sequence.
     *
     * @return String
     */
    private String getSeqTestCatalogId() throws SQLException
    {
        String result = "";
        String sql = "SELECT SEQ_TEST_CATALOG_ID.NEXTVAL FROM DUAL";

        result = executeSqlWithReturn(sql);
        return result;

    }//public getSeqTestCatalogId
    
     /**
     * Method inactivates the test with the passed in ID and its children.
     * This means that the Test Catalog and both TC and TD Item Sets are inactivated.
     *
     * @param _extEmsItemSetId String
     * @return void 
     */
    private void inactivateTest(String _extEmsItemSetId) throws SQLException
    {
        ResultSet rslt1 = null;
        ResultSet rslt2 = null;
        ResultSet rslt3 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps5 = null;
        PreparedStatement ps6 = null;
        int itemSetId = 0;
        int childItemSetId = 0;

        String invalidateTestCatalog = "UPDATE test_catalog set activation_status = '"+INACTIVE_STATUS+"', updated_date_time = SYSDATE where item_set_id = ? and activation_status = '"+ACTIVE_STATUS+"'";
        String selectTestCatalog = "SELECT test_catalog_id from test_catalog where item_set_id = ?";
        String invalidateItemSet = "UPDATE item_set set activation_status = '"+INACTIVE_STATUS+"', updated_date_time = SYSDATE where item_set_id = ? and activation_status = '"+ACTIVE_STATUS+"'";
        String selectItemSetId = "SELECT item_set_id from item_set where ext_ems_item_set_id = ?";
        String selectChildItemSetId = "SELECT item_set_id from item_set_parent where parent_item_set_id = ?";

        try
        {
          //retrieve the item_set_id
          ps1 = conn.prepareStatement(selectItemSetId);
          ps1.setString(1, _extEmsItemSetId);
          rslt1 = ps1.executeQuery();
          while(rslt1.next())
          {
              itemSetId = rslt1.getInt(1);
          }

          //inactivate the test catalog
          ps2 = conn.prepareStatement(invalidateTestCatalog);
          ps2.setInt(1, itemSetId);
          ps2.executeUpdate();

          //retrieve the test catalog ids
          ps3 = conn.prepareStatement(selectTestCatalog);
          ps3.setInt(1, itemSetId);
          rslt2 = ps3.executeQuery();
          while(rslt2.next())
          {
              old_test_catalog_id = 0;
              old_test_catalog_id = rslt2.getInt(1);
          }

          //inactivate the child item set
          ps5 = conn.prepareStatement(invalidateItemSet);
          ps6 = conn.prepareStatement(selectChildItemSetId);
          ps6.setInt(1, itemSetId);
          rslt3 = ps6.executeQuery();
          while(rslt3.next())
          {
              childItemSetId = 0;
              childItemSetId = rslt3.getInt(1);
              ps5.setInt(1, childItemSetId);
              ps5.executeUpdate();
          }

          //inactivate the item_set
          ps5.setInt(1, itemSetId);
          ps5.executeUpdate();

          conn.commit();
        }
        catch (SQLException e)
        {
            if(!suppressOutput)
            {
                System.err.println(e.getMessage());
            }
        }
        finally
        {
            if (ps1 != null) ps1.close();
            if (ps2 != null) ps2.close();
            if (ps3 != null) ps3.close();
            if (ps5 != null) ps5.close();
            if (ps6 != null) ps6.close();
            if (rslt1 != null) rslt1.close();
            if (rslt2 != null) rslt2.close();
            if (rslt3 != null) rslt3.close();
        }
    }//public inactivateTest
    
    /**
     * Method inserts the passed in BLOB into the appropriate database
     * field based upon the passed in flag.
     *
     * @param _lobFile String
     * @param _f File
     * @param _flag int
     * @return void 
     */
    private void insertBlob(String _lobFile, File _file, int _flag)
    {

        try
        {
            Statement stmt = null;
            BLOB blob = null;
            conn.setAutoCommit (false);
            stmt = conn.createStatement();
            String cmd = null;

            if(hasItemSet)
            {
                if (_flag == 1)
                {
                    
                    executeSqlStatement("UPDATE ITEM_SET_MEDIA SET MEDIA_BLOB = empty_blob() WHERE ITEM_SET_ID = '"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+ITEM_BANK_MEDIA+"'");
                    cmd = "SELECT ITEM_SET_ID, MEDIA_BLOB FROM ITEM_SET_MEDIA WHERE ITEM_SET_ID ='"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+ITEM_BANK_MEDIA+"' FOR UPDATE";
                }
                else if (_flag == 2)
                {
                    executeSqlStatement("UPDATE ITEM_SET_MEDIA SET MEDIA_BLOB = empty_blob() WHERE ITEM_SET_ID = '"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_MEDIA+"'");
                    cmd = "SELECT ITEM_SET_ID, MEDIA_BLOB FROM ITEM_SET_MEDIA WHERE ITEM_SET_ID ='"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_MEDIA+"' FOR UPDATE";
                }
                else if (_flag == 5)
                {
                    executeSqlStatement("UPDATE ITEM_SET_MEDIA SET MEDIA_BLOB = empty_blob() WHERE ITEM_SET_ID = '"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+PDF_MEDIA+"'");
                    cmd = "SELECT ITEM_SET_ID, MEDIA_BLOB FROM ITEM_SET_MEDIA WHERE ITEM_SET_ID ='"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+PDF_MEDIA+"' FOR UPDATE";
                }
            }
            else
            {
                if (_flag == 1)
                {
                    Statement stm = null;
                    ResultSet rs = null;
                    int count = 0;
                    cmd = "SELECT COUNT(*) FROM ITEM_MEDIA WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_MEDIA+"'";
                    stm = conn.createStatement();
                    rs = stm.executeQuery(cmd);
                    if(rs.next())
                    {   
                       count = rs.getInt(1);
                       if(count == 0)
                       {
                           executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_ID, MEDIA_PATH) VALUES ('"+ANSWER_KEY_MEDIA+"', empty_blob(), '"+SWF_MIME_TYPE+"', '"+EMS_Item_ID+"', '"+mediaPath+"')");
                       }                       
                       else
                       {
                           executeSqlStatement("UPDATE ITEM_MEDIA SET MEDIA_BLOB = empty_blob() WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_MEDIA+"'");
                       }
                       cmd = "SELECT ITEM_ID, MEDIA_BLOB FROM ITEM_MEDIA WHERE ITEM_ID ='"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_MEDIA+"' FOR UPDATE";
                    }
                    else
                    {
                        if(interwoven)
                        {    
                            System.out.println("1");
                        }
                        else
                        {
                            System.out.println("Error: BLOB not inserted.");
                        }
                        System.exit(1);
                    }
                }
                else if (_flag == 5)
                {
                    Statement stm = null;
                    ResultSet rs = null;
                    int count = 0;
                    cmd = "SELECT COUNT(*) FROM ITEM_MEDIA WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+PDF_MEDIA+"'";
                    stm = conn.createStatement();
                    rs = stm.executeQuery(cmd);
                    if(rs.next())
                    {   
                       count = rs.getInt(1);
                       if(count == 0)
                       {
                           executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_ID) VALUES ('"+PDF_MEDIA+"', empty_blob(), '"+PDF_MIME_TYPE+"', '"+EMS_Item_ID+"')");
                       }                       
                       else
                       {
                           executeSqlStatement("UPDATE ITEM_MEDIA SET MEDIA_BLOB = empty_blob() WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+PDF_MEDIA+"'");
                       }
                       cmd = "SELECT ITEM_ID, MEDIA_BLOB FROM ITEM_MEDIA WHERE ITEM_ID ='"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+PDF_MEDIA+"' FOR UPDATE";
                    }
                    else
                    {
                        if(interwoven)
                        {    
                            System.out.println("1");
                        }
                        else
                        {
                            System.out.println("Error: BLOB not inserted.");
                        }
                        System.exit(1);
                    }
                }
            //}
            else if (_flag == 6)
                {
                    Statement stm = null;
                    ResultSet rs = null;
                    int count = 0;
                    cmd = "SELECT COUNT(*) FROM ITEM_MEDIA WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_PDF_MEDIA+"'";
                    stm = conn.createStatement();
                    rs = stm.executeQuery(cmd);
                    if(rs.next())
                    {   
                       count = rs.getInt(1);
                       if(count == 0)
                       {
                           executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_ID) VALUES ('"+ANSWER_KEY_PDF_MEDIA+"', empty_blob(), '"+PDF_MIME_TYPE+"', '"+EMS_Item_ID+"')");
                       }                       
                       else
                       {
                           executeSqlStatement("UPDATE ITEM_MEDIA SET MEDIA_BLOB = empty_blob() WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_PDF_MEDIA+"'");
                       }
                       cmd = "SELECT ITEM_ID, MEDIA_BLOB FROM ITEM_MEDIA WHERE ITEM_ID ='"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+ANSWER_KEY_PDF_MEDIA+"' FOR UPDATE";
                    }
                    else
                    {
                        if(interwoven)
                        {    
                            System.out.println("1");
                        }
                        else
                        {
                            System.out.println("Error: BLOB not inserted.");
                        }
                        System.exit(1);
                    }
                }
            //}
            else if (_flag == 3)
            {
                cmd="UPDATE TUTORIAL SET MEDIA_BLOB = empty_blob() WHERE TUTORIAL_TYPE = 'ST'";
                ResultSet rset = stmt.executeQuery(cmd);
                cmd = "SELECT TUTORIAL_ID, MEDIA_BLOB FROM TUTORIAL WHERE TUTORIAL_TYPE ='ST' FOR UPDATE";
            }
            else if (_flag == 4)
            {
                cmd="UPDATE TUTORIAL SET MEDIA_BLOB = empty_blob() WHERE TUTORIAL_TYPE = 'TA'";
                ResultSet rset = stmt.executeQuery(cmd);
                cmd = "SELECT TUTORIAL_ID, MEDIA_BLOB FROM TUTORIAL WHERE TUTORIAL_TYPE ='TA' FOR UPDATE";
            }

            ResultSet rset = stmt.executeQuery(cmd);
            if (rset.next())
            {
                blob = ((OracleResultSet)rset).getBLOB(2);
            }
            else
            {
                if(interwoven)
                {    
                    System.out.println("1");
                }
                else
                {
                    System.out.println("Error: BLOB not inserted.");
                }
                System.exit(1);
                //return;
            }


            FileInputStream instream = new FileInputStream(_file);
            
            OutputStream outstream = blob.getBinaryOutputStream();

            int size = blob.getChunkSize();
            if(!suppressOutput)
            {
                System.out.print("\nStreaming file "+_lobFile+" at "+size+" byte segments.\n");
            }
            byte[] buffer = new byte[size];
            int length = -1;

            while ((length = instream.read(buffer)) != -1)
            {
                outstream.write(buffer, 0, length);
            }
            instream.close();
            outstream.close();
            stmt.execute("commit");
            conn.setAutoCommit (true);

        }
        }
        catch(Exception e)
        {
            if(interwoven)
            {
                System.out.println("1");
            }
            else
            {
                e.printStackTrace();
            }
            System.exit(1);
        }

    }//public insertBlob
    
    /**
     * Method inserts the passed in CLOB into the appropriate database field.
     *
     * @param _lobFile String
     * @param _f File
     * @param _flag int
     * @return void 
     */
    private void insertClob(String _lobFile, File _file)
    {
        byte[] tmp = new byte[1024];
        byte[] data = null;
        int sz = 0;
        int len = 0;
        Statement stmt = null;
        try
        {
            CLOB clob = null;
            conn.setAutoCommit (false);
            stmt = conn.createStatement();
            String cmd = "";
            
            if(hasItemSet)
            {
                executeSqlStatement("UPDATE ITEM_SET_MEDIA SET MEDIA_CLOB = empty_clob() WHERE ITEM_SET_ID = '"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+XML_MEDIA+"'");
                cmd = "SELECT ITEM_SET_ID, MEDIA_CLOB FROM ITEM_SET_MEDIA WHERE ITEM_SET_ID ='"+ItemSet_Item_Set_ID+"' AND MEDIA_TYPE ='"+XML_MEDIA+"' FOR UPDATE";
            }
            else
            {
                Statement stm = null;
                ResultSet rs = null;
                int count = 0;
                cmd = "SELECT COUNT(*) FROM ITEM_MEDIA WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+XML_MEDIA+"'";
                stm = conn.createStatement();
                rs = stm.executeQuery(cmd);
                if(rs.next())
                {   
                    count = rs.getInt(1);
                    if(count == 0)
                    {
                        executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MIME_TYPE, MEDIA_CLOB, ITEM_ID) VALUES ('"+XML_MEDIA+"', '"+XML_MIME_TYPE+"', empty_clob(), '"+EMS_Item_ID+"')");
                    }
                    else
                    {
                        executeSqlStatement("UPDATE ITEM_MEDIA SET MEDIA_CLOB = empty_clob() WHERE ITEM_ID = '"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+XML_MEDIA+"'");
                    }
                    cmd = "SELECT ITEM_ID, MEDIA_CLOB FROM ITEM_MEDIA WHERE ITEM_ID ='"+EMS_Item_ID+"' AND MEDIA_TYPE ='"+XML_MEDIA+"' FOR UPDATE";
                }
                else
                {
                    if(interwoven)
                    {    
                        System.out.println("1");
                    }
                    else
                    {
                        System.out.println("Error: BLOB not inserted.");
                    }
                    System.exit(1);
                }                
            }
            ResultSet rset = stmt.executeQuery(cmd);

            if (rset.next())
            {
                clob = ((OracleResultSet)rset).getCLOB(2);
            }
            else
            {
                if(interwoven)
                {    
                    System.out.println("1");
                }
                else
                {
                    System.out.println("Error: CLOB not inserted.");
                }
                System.exit(1);
                //return;
            }


            FileInputStream instream = new FileInputStream(_file);
            
            OutputStream outstream = clob.getAsciiOutputStream();

            int size = clob.getChunkSize();
            if(!suppressOutput)
            {
                System.out.print("\nStreaming file "+_lobFile+" at "+size+" byte segments.\n");
            }

            byte[] buffer = new byte[size];
            int length = -1;

            while ((length = instream.read(buffer)) != -1)
            {
                outstream.write(buffer, 0, length);
            }
            instream.close();
            outstream.close();
            stmt.execute("commit");
            conn.setAutoCommit (true);

        }
        catch(Exception e)
        {
            if(interwoven)
            {
                System.out.println("1");
            }
            else
            {
                e.printStackTrace();
            }
            System.exit(1);
        }

    }//public insertClob
    
    /**
     * Opens Connection to the database with the passed in parameters.
     *
     * @param _connSid String
     * @param _user String
     * @param _passwd String
     * @return boolean 
     */
    private boolean openDBConn(String _connSid, String _user, String _passwd)
    {
        String connURL= "jdbc:oracle:oci:@"+_connSid;
        boolean returnValue = false;

        try
        {
            if (conn == null)
            {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                conn = DriverManager.getConnection("jdbc:oracle:oci:@"+_connSid+"", _user, _passwd);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        if (conn == null)
        {
            if(!suppressOutput)
            {
                System.out.println("Connection to database failed with: "+connURL+" and user: "+_user);
            }
        }
        else
        {
            returnValue =  true;
        }
        return returnValue;
    }//public openDBConn
    
    /**
     * This method extracts information from the Hierarchy Element of an item.
     *
     * @return boolean 
     */
    private boolean processHierarchy() throws Exception
    {
        boolean returnValue = true;
        if (Hierarchy_Type.equals("Grade"))
        {
            Item_Grade = Hierarchy_CurriculumID;
        }
        else if (Hierarchy_Type.equals("Content Area"))
        {
            Item_Subject = Hierarchy_Name;
        }        
        return returnValue;
    }//public processHierarchy
    
    /**
     * This method processes the Item Element by inserting the item and 
     * setting up its media (where applicable).
     *
     * @return boolean 
     */
    private boolean processItem() throws Exception
    {
        //System.out.println("Within processItem.");        
        String sqlResult = "";
        String sqlResult2 = "";
        int recs;
        if(tabeContent)
        {
            mediaPath = TABE_MEDIA_PATH;
        }
        else
        {
            mediaPath = CAB_MEDIA_PATH;
        }
            
            if(Item_Description != null)
            {
                //database max length is 255
                if(Item_Description.length() > 255)
                {
                    Item_Description = Item_Description.substring(0, 254);
                }
                Item_Description = formatText(Item_Description);
            }
            
            Item_Ext_Stimulus_Title = formatText(Item_Ext_Stimulus_Title);
            if(!hasSubTest)
            {                
                Test_Catalog_Level = Item_Grade;
            }
            
            if(executeSqlWithReturn("SELECT COUNT(*) FROM ITEM WHERE ITEM_ID = '"+EMS_Item_ID+"'").equals("0"))
            {
                if(!crItem)
                {
                    if (!executeSqlStatement("INSERT INTO ITEM (ITEM_ID, "+
                        "CORRECT_ANSWER, CORRECT_ANSWER_ID, GRADE, ITEM_TYPE, FIELD_TEST, "+
                        "SUPPRESSED, CREATED_BY, ACTIVATION_STATUS, CREATED_DATE_TIME, "+
                        "TEMPLATE_ID, VERSION, DESCRIPTION, EXT_STIMULUS_ID, "+
                        "EXT_STIMULUS_TITLE, ITEM_LEVEL, ORIG_ITEM_ID) VALUES ('"+EMS_Item_ID+
                        "','"+Correct_Answer+"','"+Correct_Answer_ID+"','"+Item_Grade+"','"+
                        Item_Type+"','"+Item_Field_Test+"','"+Item_Suppressed+"',"+createdBy+
                        ",'"+ACTIVE_STATUS+"', SYSDATE, '"+Item_Template_ID+"','"+Item_Version+
                        "','"+Item_Description+"','"+Item_Ext_Stimulus_ID+"','"+
                        Item_Ext_Stimulus_Title+"','"+Test_Catalog_Level+"', '"+
                        Item_History+"')"))
                    {
                        if(!suppressOutput)
                        {
                            System.out.println("Error inserting to ITEM table Item ID: "+EMS_Item_ID);
                        }
                        return false;
                    }
                }
                else
                {
                    if (!executeSqlStatement("INSERT INTO ITEM (ITEM_ID, "+
                        "GRADE, ITEM_TYPE, FIELD_TEST, SUPPRESSED, CREATED_BY, "+
                        "ACTIVATION_STATUS, CREATED_DATE_TIME, TEMPLATE_ID, VERSION, "+
                        "DESCRIPTION, EXT_STIMULUS_ID, EXT_STIMULUS_TITLE, ITEM_LEVEL, "+
                        "ORIG_ITEM_ID, MAX_POINTS, MIN_POINTS) VALUES ('"+EMS_Item_ID+"','"+
                        Item_Grade+"','"+Item_Type+"','"+Item_Field_Test+"','"+Item_Suppressed+
                        "',"+createdBy+",'"+ACTIVE_STATUS+"', SYSDATE, '"+Item_Template_ID+
                        "','"+Item_Version+"','"+Item_Description+"','"+Item_Ext_Stimulus_ID+
                        "','"+Item_Ext_Stimulus_Title+"','"+Test_Catalog_Level+"', '"+
                        Item_History+"', '"+Item_Max_Points+"', '"+Item_Min_Points+"')"))
                    {
                        if(!suppressOutput)
                        {
                            System.out.println("Error inserting to ITEM table Item ID: "+EMS_Item_ID);
                        }
                        return false;
                    }
                }
                if(!hasItemSet)
                {
                    //executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_ID, MEDIA_PATH) VALUES ('"+ANSWER_KEY_MEDIA+"', empty_blob(), '"+SWF_MIME_TYPE+"', '"+EMS_Item_ID+"', '"+mediaPath+"')");
                    executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MIME_TYPE, MEDIA_CLOB, ITEM_ID) VALUES ('"+XML_MEDIA+"', '"+XML_MIME_TYPE+"', empty_clob(), '"+EMS_Item_ID+"')");
                }  
            }//if(!(executeSqlWithReturn("SELECT COUNT(*) FROM ITEM WHERE ITEM_ID = '"+EMS_Item_ID+"'").equals("0")))
            else
            {
                if(Item_Type.equals(SELECTED_RESPONSE))
                {
                    if (!executeSqlStatement("UPDATE ITEM SET ACTIVATION_STATUS = '"+
                        ACTIVE_STATUS+"', CORRECT_ANSWER = '"+Correct_Answer+"', "+
                        "CORRECT_ANSWER_ID = '"+Correct_Answer_ID+"', GRADE = '"+Item_Grade+
                        "', FIELD_TEST = '"+Item_Field_Test+"', SUPPRESSED = '"+
                        Item_Suppressed+"', ITEM_TYPE = '"+Item_Type+"', TEMPLATE_ID = '"+
                        Item_Template_ID+"', VERSION = '"+Item_Version+"', DESCRIPTION = '"+
                        Item_Description+"', UPDATED_DATE_TIME = SYSDATE, EXT_STIMULUS_ID = '"+
                        Item_Ext_Stimulus_ID+"', EXT_STIMULUS_TITLE = '"+
                        Item_Ext_Stimulus_Title+"', ITEM_LEVEL = '"+Test_Catalog_Level+
                        "', UPDATED_BY = "+createdBy+", ORIG_ITEM_ID = '"+Item_History+"' "+
                        "WHERE ITEM_ID = '"+EMS_Item_ID+"'"))
                    {
                        if(!suppressOutput)
                        {
                            System.out.println("Error updating ITEM table Item ID: "+EMS_Item_ID);
                        }
                        return false;
                    }
                }
                else if(Item_Type.equals(CONSTRUCTED_RESPONSE))
                {
                   if (!executeSqlStatement("UPDATE ITEM SET ACTIVATION_STATUS = '"+
                        ACTIVE_STATUS+"', GRADE = '"+Item_Grade+"', FIELD_TEST = '"+
                        Item_Field_Test+"', SUPPRESSED = '"+Item_Suppressed+"', ITEM_TYPE = '"+
                        Item_Type+"', TEMPLATE_ID = '"+Item_Template_ID+"', VERSION = '"+
                        Item_Version+"', DESCRIPTION = '"+Item_Description+"', "+
                        "UPDATED_DATE_TIME = SYSDATE, EXT_STIMULUS_ID = '"+
                        Item_Ext_Stimulus_ID+"', EXT_STIMULUS_TITLE = '"+
                        Item_Ext_Stimulus_Title+"', ITEM_LEVEL = '"+Test_Catalog_Level+"', "+
                        "UPDATED_BY = "+createdBy+", ORIG_ITEM_ID = '"+Item_History+"', "+
                        "MAX_POINTS = '"+Item_Max_Points+"', MIN_POINTS = '"+Item_Min_Points+
                        "' WHERE ITEM_ID = '"+EMS_Item_ID+"'"))
                    {
                        if(!suppressOutput)
                        {
                            System.out.println("Error updating ITEM table Item ID: "+EMS_Item_ID);
                        }
                        return false;
                    }
                }
                else
                {
                    if(!suppressOutput)
                    {
                        System.out.println("Error: Item Type not valid.");
                    }
                    return false;
                 }
                if(!hasItemSet)
                {
                    if((executeSqlWithReturn("SELECT COUNT(*) FROM ITEM_MEDIA WHERE ITEM_ID = '"+EMS_Item_ID+"'")).equals("0"))
                    {
                        executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_ID, MEDIA_PATH) VALUES ('"+ANSWER_KEY_MEDIA+"', empty_blob(), '"+SWF_MIME_TYPE+"', '"+EMS_Item_ID+"', '"+mediaPath+"')");
                        executeSqlStatement("INSERT INTO ITEM_MEDIA (MEDIA_TYPE, MIME_TYPE, MEDIA_CLOB, ITEM_ID) VALUES ('"+XML_MEDIA+"', '"+XML_MIME_TYPE+"', empty_clob(), '"+EMS_Item_ID+"')");
                    }
                }
            }

            //if(hasItemSet && !(getItemSetIdByEmsItemId(EMS_Item_ID).equals(ItemSet_Item_Set_ID)))
            if(hasItemSet && !(doesItemSetItemExist(EMS_Item_ID, ItemSet_Item_Set_ID)))
            {
                executeSqlStatement("INSERT INTO ITEM_SET_ITEM (ITEM_ID, ITEM_SET_ID, "+
                    "ITEM_SORT_ORDER, CREATED_BY) VALUES ('"+EMS_Item_ID+"',"+
                    ItemSet_Item_Set_ID+","+Item_no+",'"+createdBy+"')");
            }

            if(!(doesItemSetItemExist(EMS_Item_ID, getItemSetIdByCmsItemSetId(Item_ObjectiveID))))
            {
                executeSqlStatement("INSERT INTO ITEM_SET_ITEM (ITEM_ID, ITEM_SET_ID, "+
                    "ITEM_SORT_ORDER, CREATED_BY) VALUES ('"+EMS_Item_ID+"',"+
                    "(SELECT ITEM_SET_ID FROM ITEM_SET WHERE EXT_CMS_ITEM_SET_ID = '"+
                    Item_ObjectiveID+"'),0,'"+createdBy+"')");
            }

    //Choice_no++;
    //System.out.println("End processItem");
    return true;
    }//public processItem
    
    /**
     * This method processes the ItemSet Element by inserting the Item Set and 
     * setting up its media (where applicable).
     *
     * @return boolean 
     */
    private boolean processItemSet() throws Exception
    {
        //System.out.println("Within processItemSet.");
        hasItemSet = true;
        String sqlResult = "";
        //String mediaPath = "";
        if(tabeContent)
        {
            if(!suppressOutput)
            {
                System.out.println("\nProcessing ItemSet ID: "+ Item_Set_Ext_EMS_Item_Set_ID);
            }
            mediaPath = TABE_MEDIA_PATH;
        }
        else
        {
            mediaPath = CAB_MEDIA_PATH;
        }

        if((getItemSetIdByEmsItemSetId(Item_Set_Ext_EMS_Item_Set_ID)).equals("") || newVersion)
        {
            if(!sampleSet)
            {
                //System.out.println("Before insertTestDeliverable.");
                executeSqlStatement("INSERT INTO ITEM_SET (ITEM_SET_ID, ITEM_SET_TYPE, ITEM_SET_NAME, TIME_LIMIT, ITEM_SET_FORM, ITEM_SET_DISPLAY_NAME, ITEM_SET_DESCRIPTION, SAMPLE, EXT_EMS_ITEM_SET_ID, CREATED_BY, ACTIVATION_STATUS, MEDIA_PATH, VERSION, ITEM_SET_LEVEL, GRADE, SUBJECT) VALUES (SEQ_ITEM_SET_ID.NEXTVAL, '"+TEST_DELIVERABLE+"', '"+
                    Test_Catalog_Test_Name+"', "+timelimit_in_seconds+", '"+Test_Catalog_Test_Form+"', '"+Test_Catalog_Test_Name+"', '"+ItemSet_Description+"', '"+FALSE+"', '"+Item_Set_Ext_EMS_Item_Set_ID+"',  '"+createdBy+"','"+ACTIVE_STATUS+"','"+mediaPath+"','"+Item_Set_Version+"', '"+Test_Catalog_Level+"', '"+Test_Catalog_Test_Grade+"', '"+Test_Catalog_Subject+"')");
            }
            else
            {
                executeSqlStatement("INSERT INTO ITEM_SET (ITEM_SET_ID, ITEM_SET_TYPE, ITEM_SET_NAME, TIME_LIMIT, ITEM_SET_FORM, ITEM_SET_DISPLAY_NAME, ITEM_SET_DESCRIPTION, SAMPLE, EXT_EMS_ITEM_SET_ID, CREATED_BY, ACTIVATION_STATUS, MEDIA_PATH, VERSION, ITEM_SET_LEVEL, GRADE) VALUES (SEQ_ITEM_SET_ID.NEXTVAL, '"+TEST_DELIVERABLE+"', '"+
                    Test_Catalog_Test_Name+"', "+timelimit_in_seconds+", '"+Test_Catalog_Test_Form+"', '"+Test_Catalog_Test_Name+"', '"+ItemSet_Description+"', '"+TRUE+"', '"+Item_Set_Ext_EMS_Item_Set_ID+"',  '"+createdBy+"','"+ACTIVE_STATUS+"','"+mediaPath+"','"+Item_Set_Version+"', '"+Test_Catalog_Level+"', '"+Test_Catalog_Test_Grade+"', '"+Test_Catalog_Subject+"')");
                
            }
       
            ItemSet_Item_Set_ID = getItemSetIdByEmsItemSetId(Item_Set_Ext_EMS_Item_Set_ID);
            
            executeSqlStatement("INSERT INTO ITEM_SET_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_SET_ID, MEDIA_PATH) VALUES ('"+ITEM_BANK_MEDIA+"', empty_blob(), '"+SWF_MIME_TYPE+"', '"+ItemSet_Item_Set_ID+"', '"+mediaPath+"')");
            executeSqlStatement("INSERT INTO ITEM_SET_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_SET_ID, MEDIA_PATH) VALUES ('"+ANSWER_KEY_MEDIA+"', empty_blob(), '"+SWF_MIME_TYPE+"', '"+ItemSet_Item_Set_ID+"', '"+mediaPath+"')");
            executeSqlStatement("INSERT INTO ITEM_SET_MEDIA (MEDIA_TYPE, MIME_TYPE, MEDIA_CLOB, ITEM_SET_ID) VALUES ('"+XML_MEDIA+"', '"+XML_MIME_TYPE+"', empty_clob(), '"+ItemSet_Item_Set_ID+"')");
            executeSqlStatement("INSERT INTO ITEM_SET_MEDIA (MEDIA_TYPE, MEDIA_BLOB, MIME_TYPE, ITEM_SET_ID) VALUES ('"+PDF_MEDIA+"', empty_blob(), '"+PDF_MIME_TYPE+"', '"+ItemSet_Item_Set_ID+"')");
            
            //insert into Item_Set_Product
            executeSqlStatement("INSERT INTO ITEM_SET_PRODUCT (ITEM_SET_ID, PRODUCT_ID, CREATED_BY, CREATED_DATE_TIME) VALUES('"+ItemSet_Item_Set_ID+"', '"+Product_ID+"', '"+createdBy+"', SYSDATE)");
            
            if(!tabeContent)
            {
                //System.out.println("Before insertItemSetParent.");
                //System.out.println("SQL Statement: "+insertItemSetParent);
                executeSqlStatement("INSERT INTO ITEM_SET_PARENT (PARENT_ITEM_SET_ID, ITEM_SET_ID, CREATED_BY, ITEM_SET_TYPE, PARENT_ITEM_SET_TYPE, ITEM_SET_SORT_ORDER) VALUES ('" +
                    SubTest_Item_Set_ID+"', '"+ItemSet_Item_Set_ID+"', '"+createdBy+"', '"+TEST_DELIVERABLE+"', '"+TEST_CATALOG+"', 1)");
            }
        }
        else if (tabeContent || (!production && allAccess))
        {
            executeSqlStatement("UPDATE ITEM_SET SET ITEM_SET_NAME = '"+Test_Catalog_Test_Name+"', "+
                "TIME_LIMIT = "+timelimit_in_seconds+", ITEM_SET_FORM = '"+Test_Catalog_Test_Form+
                "', ITEM_SET_DISPLAY_NAME = '"+Test_Catalog_Test_Name+"', ITEM_SET_DESCRIPTION = '"+
                ItemSet_Description+"', UPDATED_DATE_TIME = SYSDATE, UPDATED_BY = '"+createdBy+
                "', MEDIA_PATH = '"+mediaPath+"' , VERSION = '"+Item_Set_Version+"', "+
                "ITEM_SET_LEVEL = '"+Test_Catalog_Level+"', SUBJECT = '"+Test_Catalog_Subject+
                "' WHERE EXT_EMS_ITEM_SET_ID = '"+Item_Set_Ext_EMS_Item_Set_ID+"'");
                
            ItemSet_Item_Set_ID = getItemSetIdByEmsItemSetId(Item_Set_Ext_EMS_Item_Set_ID);
                
            executeSqlStatement("DELETE FROM ITEM_SET_ITEM WHERE ITEM_SET_ID = "+ItemSet_Item_Set_ID);
        }
        else
        {
            if(!suppressOutput)
            {
                System.out.println("Error: Cannot update Item Set.");
            }
            return false;
        }
        //System.out.println("End processItemSet.");
        return true;
    }//public processItemSet
    
    /**
     * This method processes the SubTest Element by inserting the Item Set and 
     * Test Catalog information.
     *
     * @return boolean 
     */
    private boolean processSubTest() throws Exception
    {
        hasSubTest = true;
        BufferedReader stdin = new BufferedReader
            (new InputStreamReader(System.in));
        String message = ""; // Creates a varible called message for input
        String sqlResult = "";
        String sqlResult2 = "";
        if(!tabeContent && !suppressOutput)
        {
            System.out.println("\nProcessing Test ID: " + Item_Set_Ext_EMS_Item_Set_ID + ", CA: " +Test_Catalog_Subject + ", Grade: " + Test_Catalog_Test_Grade + ", Title: " +Test_Catalog_Test_Name + ", Version: " + Test_Catalog_Version);
        }
        
        if(!(getItemSetIdByEmsItemSetId(Item_Set_Ext_EMS_Item_Set_ID)).equals(""))
        {
            if(!production && !tabeContent && !suppressOutput && allAccess)
            {
                System.out.println("\nTest "+Item_Set_Ext_EMS_Item_Set_ID+" already exists in the database.");
                System.out.println("Should the existing test be updated? (y/n)");
                System.out.flush(); // empties buffer, before you input text
                message = stdin.readLine();
            }
            if((message.equals("y")) && !production && !tabeContent && !suppressOutput && allAccess)
            {
                //System.out.println("Test: "+Item_Set_Ext_EMS_Item_Set_ID+" is being updated.");
                executeSqlStatement("UPDATE ITEM_SET SET ITEM_SET_NAME = '"+Test_Catalog_Test_Name+"', TIME_LIMIT = "+timelimit_in_seconds+", ITEM_SET_FORM = '"+
                    Test_Catalog_Test_Form+"', ITEM_SET_DISPLAY_NAME = '"+Test_Catalog_Test_Name+"', ITEM_SET_DESCRIPTION = '"+ItemSet_Description+"', VERSION = '"+Test_Catalog_Version+"', ITEM_SET_LEVEL = '"+Test_Catalog_Level+"', GRADE = '"+Test_Catalog_Test_Grade+"', UPDATED_DATE_TIME = SYSDATE, UPDATED_BY = '"+createdBy+"', SUBJECT = '"+Test_Catalog_Subject+"', MEDIA_PATH = '"+mediaPath+"' WHERE EXT_EMS_ITEM_SET_ID = '"+Item_Set_Ext_EMS_Item_Set_ID+"'");
            }
            else
            {                
                if(!tabeContent)
                {
                    newVersion = true;
                    //System.out.println("Within not TABE block.");
                    inactivateTest(Item_Set_Ext_EMS_Item_Set_ID);

                    //Get Next Test Catalog ID from sequence
                    Test_Catalog_ID = getSeqTestCatalogId();

                    //Get Next Item Set ID from sequence
                    SubTest_Item_Set_ID = getSeqItemSetId();

                    //Insert Item_Set Table with SubTest Information
                    executeSqlStatement("INSERT INTO ITEM_SET (ITEM_SET_ID, ITEM_SET_TYPE, ITEM_SET_NAME, TIME_LIMIT, ITEM_SET_FORM, ITEM_SET_DISPLAY_NAME, ITEM_SET_DESCRIPTION, SAMPLE, EXT_EMS_ITEM_SET_ID, CREATED_BY, ACTIVATION_STATUS, VERSION, ITEM_SET_LEVEL, GRADE, SUBJECT, MEDIA_PATH) VALUES ("+SubTest_Item_Set_ID+", '"+TEST_CATALOG+"', '"+
                        Test_Catalog_Test_Name+"', "+timelimit_in_seconds+", '"+Test_Catalog_Test_Form+"', '"+Test_Catalog_Test_Name+"', '"+ItemSet_Description+"', '"+FALSE+"', '"+Item_Set_Ext_EMS_Item_Set_ID+"',  "+createdBy+",'"+ACTIVE_STATUS+"', '"+Test_Catalog_Version+"', '"+Test_Catalog_Level+"', '"+Test_Catalog_Test_Grade+"', '"+Test_Catalog_Subject+"', '"+mediaPath+"')");

                    //Insert into Test_Catalog Table
                    executeSqlStatement("INSERT INTO TEST_CATALOG (TEST_CATALOG_ID, PRODUCT_ID, TEST_NAME, VERSION, TEST_DISPLAY_NAME, ITEM_SET_ID, SUBJECT, TEST_GRADE, TEST_LEVEL, TEST_FORM, CREATED_BY, ACTIVATION_STATUS)"+
                        "VALUES ("+Test_Catalog_ID+","+Product_ID+", '"+Test_Catalog_Test_Name+"', '"+Test_Catalog_Version+"', '"+Test_Catalog_Test_Name+"', '"+
                        SubTest_Item_Set_ID+"', '"+Test_Catalog_Subject+"', '"+Test_Catalog_Test_Grade+"', '"+Test_Catalog_Level+"', '"+Test_Catalog_Test_Form+"',"+createdBy+", '"+ACTIVE_STATUS+"')");
                        
                    //insert into Item_Set_Product
                    executeSqlStatement("INSERT INTO ITEM_SET_PRODUCT (ITEM_SET_ID, PRODUCT_ID, CREATED_BY, CREATED_DATE_TIME) VALUES('"+SubTest_Item_Set_ID+"', '"+Product_ID+"', '"+createdBy+"', SYSDATE)");

                    updateTestCatalogReferences();
                }
            }
        }//if (sqlResult.compareTo("empty") != 0)
        else
        {
            if(!tabeContent)
            {
                //Get Next Test Catalog ID from sequence
                Test_Catalog_ID=getSeqTestCatalogId();

                //Get Next Item Set ID from sequence
                SubTest_Item_Set_ID = getSeqItemSetId();

                //Insert Item_Set Table with SubTest Information
                executeSqlStatement("INSERT INTO ITEM_SET (ITEM_SET_ID, ITEM_SET_TYPE, ITEM_SET_NAME, TIME_LIMIT, ITEM_SET_FORM, ITEM_SET_DISPLAY_NAME, ITEM_SET_DESCRIPTION, SAMPLE, EXT_EMS_ITEM_SET_ID, CREATED_BY, ACTIVATION_STATUS, VERSION, ITEM_SET_LEVEL, GRADE, SUBJECT, MEDIA_PATH) VALUES ("+SubTest_Item_Set_ID+", '"+TEST_CATALOG+"', '"+
                    Test_Catalog_Test_Name+"', "+timelimit_in_seconds+", '"+Test_Catalog_Test_Form+"', '"+Test_Catalog_Test_Name+"', '"+ItemSet_Description+"', '"+FALSE+"', '"+Item_Set_Ext_EMS_Item_Set_ID+"',  "+DEFAULT_CREATED_BY+",'"+ACTIVE_STATUS+"', '"+Test_Catalog_Version+"', '"+Test_Catalog_Level+"', '"+Test_Catalog_Test_Grade+"', '"+Test_Catalog_Subject+"', '"+mediaPath+"')");

                //Insert into Test_Catalog Table
                executeSqlStatement("INSERT INTO TEST_CATALOG (TEST_CATALOG_ID, PRODUCT_ID, TEST_NAME, VERSION, TEST_DISPLAY_NAME, ITEM_SET_ID, SUBJECT, TEST_GRADE, TEST_LEVEL, TEST_FORM, CREATED_BY, ACTIVATION_STATUS)"+
                    "VALUES ("+Test_Catalog_ID+","+Product_ID+", '"+Test_Catalog_Test_Name+"', '"+Test_Catalog_Version+"', '"+Test_Catalog_Test_Name+"', '"+
                    SubTest_Item_Set_ID+"', '"+Test_Catalog_Subject+"', '"+Test_Catalog_Test_Grade+"', '"+Test_Catalog_Level+"', '"+Test_Catalog_Test_Form+"',"+createdBy+", '"+ACTIVE_STATUS+"')");

                executeSqlStatement("INSERT INTO ORG_NODE_TEST_CATALOG (TEST_CATALOG_ID, ORG_NODE_ID, CUSTOMER_ID, PRODUCT_ID, CREATED_BY, ACTIVATION_STATUS, ITEM_SET_ID)"+
                    "VALUES ("+Test_Catalog_ID+", "+CTB_ROOT_ID+", "+CTB_ROOT_ID+", "+Product_ID+", '"+createdBy+"', '"+ACTIVE_STATUS+"', "+SubTest_Item_Set_ID+")");
                executeSqlStatement("INSERT INTO ORG_NODE_TEST_CATALOG (TEST_CATALOG_ID, ORG_NODE_ID, CUSTOMER_ID, PRODUCT_ID, CREATED_BY, ACTIVATION_STATUS, ITEM_SET_ID)"+
                    "VALUES ("+Test_Catalog_ID+", "+CTB_ID+", "+CTB_ID+", "+Product_ID+", '"+createdBy+"', '"+ACTIVE_STATUS+"', "+SubTest_Item_Set_ID+")");
                
                //insert into Item_Set_Product
                executeSqlStatement("INSERT INTO ITEM_SET_PRODUCT (ITEM_SET_ID, PRODUCT_ID, CREATED_BY, CREATED_DATE_TIME) VALUES('"+SubTest_Item_Set_ID+"', '"+Product_ID+"', '"+createdBy+"', SYSDATE)");
            }
        }
        return true;
    }//public processSubTest
        
    /**
     * This method parses the XML document and retrieves all necessary data.
     *
     * @return boolean 
     */
    private boolean processXML(String filename)
    {
        // Defines the standard input stream
        BufferedReader stdin = new BufferedReader
            (new InputStreamReader(System.in));
        String message = ""; // Creates a varible called message for input
        Document doc = null;
        NodeIterator iter = null;
        TreeWalker walker = null;

        try
        {
            if(tabeContent)
            {
                mediaPath = TABE_MEDIA_PATH;
            }
            else
            {
                mediaPath = CAB_MEDIA_PATH;
            }
            if(interwoven)
            {
                createdBy = INTERWOVEN_CREATED_BY;
            }
            else
            {
                createdBy = DEFAULT_CREATED_BY;
            }
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new File(filename));

            //int Inserted_Items = 0;
            String sqlResult = "";
            String sqlResult2 = "";
            String temp = "";
            int startIndex = 0;
            int endIndex = 0;
            String begin = "";
            String end = "";
            Node chNd = null;
            Text tn = null;
            int nmbrNds = 0;
            int length = 0;
            int ansrChoices = 0;
            int totalAnsrChoices = 0;
            String strTotalAnsrChoices = "";
            
            // Check if Traversals are supported
            if( doc.isSupported("Traversal", "2.0" ) )
            {
                //Set up a TreeWalker with an inline "accept up to Item Nodes" filter
                walker = ((DocumentTraversal)doc).createTreeWalker( doc,
                NodeFilter.SHOW_ALL, new ItemFilter(), true );
                Node n;
                // Stroll through the Nodes looking for Specific Elements
                while((n = walker.nextNode()) != null)
                {
                    //System.out.println("Within while");
                    
                    Element e = (Element ) n;
                    String ElemName = n.getNodeName().trim();

                    //For each element type, process and insert into database
                    if (ElemName.equals("Hierarchy"))
                    {
                        //System.out.println("Within hierarchy.");
                        Hierarchy_Type = e.getAttribute("Type");
                        Hierarchy_Name = e.getAttribute("Name");
                        Hierarchy_CurriculumID = e.getAttribute("CurriculumID");
                        if(!processHierarchy())
                        {
                            break;
                        }                           
                    }//if (ElemName.indexOf("Hierarchy") != -1)
                    
                    else if (ElemName.equals("Stimulus"))
                    {
                        //System.out.println("Within Stimulus");
                        if(Item_Subject.indexOf("Math") != 1)
                        {
                            Item_Ext_Stimulus_ID = e.getAttribute("ID");
                        }
                        Item_Ext_Stimulus_Title = e.getAttribute("ID");                          
                    }//if (ElemName.indexOf("Stimulus") != -1)
                    
                    else if (ElemName.equals("Passage"))
                    {
                        //System.out.println("Within Passage");
                        if(Item_Subject.indexOf("Read") != 1)
                        {
                            Item_Ext_Stimulus_ID = e.getAttribute("ID");
                        }                          
                    }//if (ElemName.indexOf("Passage") != -1)
                    else if (ElemName.equals("AnswerChoice"))
                    {
                        //System.out.println("Within AnswerChoice");
                        ansrChoices++;
                        Answer_ID = e.getAttribute("ID");
                        Answer_Type = e.getAttribute("Type");
                        if (Answer_Type == null)
                        {
                            if(!suppressOutput)
                            {
                                System.out.println("No Type field for AnswerChoice: "+Answer_ID);
                                System.out.println("Please verify input file and try again.");
                            }
                            return false;
                        }//if (Answer_Type == null)
                        if (Answer_Type.equals("Correct"))
                        {
                            Correct_Answer_ID = Answer_ID;
                            switch(Choice_no)
                            {
                                case 1: Correct_Answer = "A"; break;
                                case 2: Correct_Answer = "B"; break;
                                case 3: Correct_Answer = "C"; break;
                                case 4: Correct_Answer = "D"; break;
                                case 5: Correct_Answer = "E"; break;
                                case 6: Correct_Answer = "F"; break;
                                case 7: Correct_Answer = "G"; break;
                                case 8: Correct_Answer = "H"; break;
                                case 9: Correct_Answer = "I"; break;
                                case 10: Correct_Answer = "J"; break;
                            }//switch(Choice_no)
                           }
                           
                            if(ansrChoices == totalAnsrChoices)
                            {
                                if(!processItem())
                                {
                                    break;
                                }
                           }
                           Choice_no++;
                    }//if (ElemName.indexOf("AnswerChoice") != -1)
                    
                    else if (ElemName.equals("Stem"))
                    {
                        Item_Description = "";
                        chNd = null;
                        tn = null;
                        nmbrNds = 0;
                        length = 0;
                        length = e.getChildNodes().getLength();
                        for(nmbrNds = 0; nmbrNds < length; nmbrNds++)
                        {
                            chNd = e.getChildNodes().item(nmbrNds);
                            if(chNd.getNodeType() == Node.TEXT_NODE)
                            {
                                tn = (Text) chNd;
                                Item_Description += tn.getNodeValue();
                            }
                            else
                            {
                                getChildText(chNd);
                            }
                        }
                                             
                    }//if (ElemName.indexOf("Stem") != -1)
                    else if (ElemName.equals("SelectedResponse"))
                    {
                        //System.out.println("Within SelectedResponse");
                        if (Item_Sample.equals(FALSE))
                        {
                            Item_Type = SELECTED_RESPONSE;
                        }
                        else
                        {
                            Item_Type = SA_ITEM_TYPE;
                        }
                        strTotalAnsrChoices = e.getAttribute("NumberAnswerChoices");
                        totalAnsrChoices = Integer.parseInt(strTotalAnsrChoices);
                    }//if (ElemName.indexOf("SelectedResponse") != -1)
                    
                    else if (ElemName.equals("ConstructedResponse"))
                    {
                        if (Item_Sample.equals(FALSE))
                        {
                            Item_Type = CONSTRUCTED_RESPONSE;
                        }
                        else
                        {
                            Item_Type = SA_ITEM_TYPE;
                        }
                        crItem = true;
                        Item_Max_Points = e.getAttribute("MaxScorePts");
                        Item_Min_Points = e.getAttribute("MinScorePts");
                        if(!processItem())
                        {
                            break;
                        }
                    }//if (ElemName.indexOf("ConstructedResponse") != -1)
                    else if (ElemName.equals("Item"))
                    {
                        //System.out.println("Within Item");
                        ansrChoices = 0;
                        Choice_no = 1;
                        Item_no++;
                        Item_Template_ID = e.getAttribute("Template");
                        Item_Version = e.getAttribute("Version");
                        Item_History = e.getAttribute("ItemHistory");
                        EMS_Item_ID = e.getAttribute("ID");
                        if(EMS_Item_ID.indexOf("EMS_") != -1)
                        {
                            temp = EMS_Item_ID.substring(4);
                            EMS_Item_ID = temp;
                        }
                        
                        executeSqlStatement("DELETE FROM ITEM_SET_ITEM WHERE ITEM_ID = '"+
                            EMS_Item_ID+"' AND ITEM_SET_ID IN (SELECT ITEM_SET_ID FROM "+
                            "ITEM_SET WHERE ITEM_SET_TYPE = '"+REPORTING+"')");                        

                        if ((Item_Field_Test = e.getAttribute("FieldTest")) == "")
                        {
                            Item_Field_Test = FALSE;
                        }
                        if ((Item_Suppressed = e.getAttribute("SuppressScore")) == "")
                        {
                            Item_Suppressed = FALSE;
                        }
                        if ((Item_Sample = e.getAttribute("Sample")) == "")
                        {
                            Item_Sample = FALSE;
                        }
                        if(sampleSet)
                        {
                            Item_Suppressed = TRUE;
                            Item_Sample = TRUE;
                        }
                        Item_Suppressed = Item_Suppressed.substring(0,1);
                        if(Item_Suppressed.equals(NO))
                        {
                            Item_Suppressed = FALSE;
                        }
                        else if(Item_Suppressed.equals(YES))
                        {
                            Item_Suppressed = TRUE;
                        }
                        Item_Sample = Item_Sample.substring(0,1);
                        if(Item_Sample.equals(NO))
                        {
                            Item_Sample = FALSE;
                        }
                        else if(Item_Sample.equals(YES))
                        {
                            Item_Sample = TRUE;
                        }
                        Item_Field_Test = Item_Field_Test.substring(0,1);
                        if(Item_Field_Test.equals(NO))
                        {
                            Item_Field_Test = FALSE;
                        }
                        else if(Item_Field_Test.equals(YES))
                        {
                            Item_Field_Test = TRUE;
                        }
                        Item_ObjectiveID = e.getAttribute("ObjectiveID");
                        Item_Set_Item_Sort_Order = Item_no;
                    }

                    else if (ElemName.equals("ItemSet"))
                    {
                        sampleSet = false;
                        Item_Set_Version = e.getAttribute("Version");
                        if(!tabeContent)
                        {
                            Item_Set_Ext_EMS_Item_Set_ID = e.getAttribute("ID");
                        }
                        else
                        {
                            Item_Set_Ext_EMS_Item_Set_ID = Test_Catalog_Test_Name;
                        }
                        if(!processItemSet())
                        {
                            break;
                        }
                    }

                    else if (ElemName.equals("SampleSet"))
                    {
                        //System.out.println("Within SampleSet");
                        sampleSet = true;
                        Item_Set_Version = e.getAttribute("Version");
                        if(!tabeContent)
                        {
                            Item_Set_Ext_EMS_Item_Set_ID = e.getAttribute("ID");
                        }
                        else
                        {
                            Item_Set_Ext_EMS_Item_Set_ID = Test_Catalog_Test_Name;
                        }
                        if(!processItemSet())
                        {
                            break;
                        }
                    }

                    else if(ElemName.equals("SubTest"))
                    {
                        //System.out.println("Within SubTest");
                        Item_Set_Ext_EMS_Item_Set_ID = e.getAttribute("ID");
                        Test_Catalog_Subject = e.getAttribute ("ContentArea");
                        Test_Catalog_Test_Grade = e.getAttribute ("Grade");
                        Test_Catalog_Test_Name = e.getAttribute ("Title");
                        Test_Catalog_Level = e.getAttribute("Level");
                        Test_Catalog_Test_Form = e.getAttribute("Form");
                        Test_Catalog_Version = e.getAttribute ("Version");

                        Item_Set_Time_Limit=e.getAttribute ("TimeLimit");
                        timelimit_in_seconds = Integer.parseInt(Item_Set_Time_Limit) * 60;

                        if(!hasAssessment)
                        {
                            Product_ID = executeSqlWithReturn("SELECT PRODUCT_ID FROM PRODUCT WHERE UPPER(SUBSTR(PRODUCT_NAME,1,4)) = UPPER(SUBSTR('"+Test_Catalog_Subject+"',1,4))");
                        }

                        if ( (ItemSet_Description = e.getAttribute("description")) == "")
                        {
                            ItemSet_Description = executeSqlWithReturn("SELECT PRODUCT_DESCRIPTION FROM PRODUCT WHERE PRODUCT_ID = "+Product_ID);
                        }
                        if(!tabeContent)
                        {
                            if(!processSubTest())
                            {
                                break;
                            }
                        }
                    }//else if( ElemName.indexOf("SubTest") != -1 )
                    else if (ElemName.equals("Assessment"))
                    {
                        Product_ID = e.getAttribute("ProductID");
                        hasAssessment = true;
                    }//else if (ElemName.equals("Assessment"))
                    else continue;
                }//while
                //System.out.println("outside while");
                return true;
            } //if (doc...
            else
            {
                if(!suppressOutput)
                {
                    System.out.println("Sorry but Traversal is not implemented");
                    System.out.println("Check CLASSPATH to include xerces.jar, version 1.3 or above.");
                }
                return false;
            }
        } //try
        catch (SAXException sxe)
        {
            // Error generated during parsing)
            Exception  x = sxe;
            if (sxe.getException() != null && !suppressOutput)
            {
                x = sxe.getException();
                x.printStackTrace();
            }
        }

        catch (ParserConfigurationException pce)
        {
            // Parser with specified options can't be built
            if(!suppressOutput)
            {
                pce.printStackTrace();
            }
        }

        catch (IOException ioe)
        {
            // I/O error
            if(!suppressOutput)
            {               
                ioe.printStackTrace();
            }
        }
        catch ( Exception e )
        {
            // We should really have more Exception types here
            String excpt = e.getMessage();
            if(excpt.indexOf("org.apache.xerces.dom.DeferredDocumentTypeImpl") != -1)
            {
                return true;
            }
            else
            {
                if(!suppressOutput)
                {
                    System.out.println( "Exception: "+excpt );
                }
            }
        }
        catch(Throwable t)
        {
        }
        //System.out.println("End processXML.");
        return false;
    }//public processXML

    /**
     * This method updates the activation status of reporting Item Sets
     * based on whether they have any active child Items or Item Sets.
     *
     * @return void 
     */
     private void updateReItemSetActivationStatus() throws SQLException
     {
        //inactivate all RE item_sets that do not contain active items
        executeSqlStatement("update item_set set activation_status = 'IN' where "+
            "activation_status <> 'IN' and item_set_type = 'RE' and item_set_id "+
            "not in (select item_set_id from item_set_item where item_id in "+
            "(select item_id from item where activation_status = 'AC'))");

        //activate all RE item_sets that contain active items
        executeSqlStatement("update item_set set activation_status = 'AC' where "+
            "activation_status <> 'AC' and item_set_type = 'RE' and item_set_id "+
            "in (select item_set_id from item_set_item where item_id in "+
            "(select item_id from item where activation_status = 'AC'))");

        //activate all RE item_set whose children contain active items
        executeSqlStatement("update item_set set activation_status = 'AC' where "+
            "item_set_type = 'RE' and activation_status <> 'AC' and item_set_id "+
            "in (select ancestor_item_set_id from item_set_ancestor where item_set_id in "+
            "(select item_set_id from item_set where item_set_type = 'RE' and activation_status = 'AC'))");
     }

    /**
     * This method updates all references to a Test Catalog that has been
     * inactivated.  This involves updating both Test Admins and Org Node
     * Test Catalogs.
     *
     * @return void 
     */
    private void updateTestCatalogReferences() throws SQLException
    {
        
        String redirectFuTestAdministrations = "UPDATE test_admin set test_catalog_id = "+
            Test_Catalog_ID+", item_set_id = "+SubTest_Item_Set_ID+", updated_date_time = "+
            "SYSDATE where test_catalog_id = "+old_test_catalog_id+" and test_admin_status = 'FU' ";
        String redirectCuTestAdministrations = "UPDATE test_admin set test_catalog_id = "+
            Test_Catalog_ID+", item_set_id = "+SubTest_Item_Set_ID+", updated_date_time = "+
            "SYSDATE where test_catalog_id = "+old_test_catalog_id+" and test_admin_status "+
            "= 'CU' and test_admin_id not in (select test_admin from test_roster where "+
            "test_roster_id in (select test_roster_id from student_item_set_status))";
        String redirectOrgNodeTestCatalogs = "UPDATE org_node_test_catalog set test_catalog_id = "+
            Test_Catalog_ID+", item_set_id = "+SubTest_Item_Set_ID+", updated_date_time = "+
            "SYSDATE where test_catalog_id = "+old_test_catalog_id+"";

            //point Current Test Administrations, that no students have taken, to the new test catalog
            executeSqlStatement(redirectCuTestAdministrations);
            
            //point Future Test Administrations to the new test catalog
            executeSqlStatement(redirectFuTestAdministrations);
            
            //point the org_node_test_catalog rows to the new test catalog
            executeSqlStatement(redirectOrgNodeTestCatalogs);
    }
       
    /**
     * This method recieves all input parameters (through arguments or menu options)
     * and call the appropriate methods for processing.
     *
     * @return void 
     */
    public static void main( String argv[] ) throws Exception, Throwable
    {
        int length = 0;
        length = argv.length;
        // Defines the standard input stream
        BufferedReader stdin = new BufferedReader
            (new InputStreamReader(System.in));
        String message; // Creates a varible called message for input
        String inFile = null;
        String inPath = "";
        int startFilename = 0;
        int endFilename = 0;
        int tutorial = 0;
        int tabeTutorial = 0;
        int swfUpdate = 0;
        int testOnly = 0;
        int answerKeyOnly = 0;
        int totalUpdate = 0;
        int multipleUpdate = 0;
        int itemOnly = 0;
        int mediaOnly = 0;
        int pdfOnly = 0;
        boolean argsOnCmdLn = false;
        boolean skipMenus = false;
        String dbSid = null;
        String username = null;
        String password = null;
        String swfOne = null;
        String swfTwo = null;
        String pdfFile = null;
        String itemMediaOne = null;
        String itemMediaTwo = null;
        String coreFilename = null;
        File f = null;
        String source = "";
        String environment = "";
        String prodType = "";
        String dataType = "";
        
        //process the command line parameters
        if(length > 0)
        {
            source = argv[0];
            if(source.equalsIgnoreCase("admin"))
            {
                //check for the right number of input parameters
                if(length != 1 && length != 4 && length != 7 && length != 8 && length != 9 && length != 10)
                {
                    System.out.println("Error: Incorrect number or parameters supplied.");
                    System.exit(1);
                }
                else
                {
                    allAccess = true;
                    if(length == 4)
                    {
                        dbSid = argv[1];
                        username = argv[2];
                        password = argv[3];
                        argsOnCmdLn = true;
                    }
                    else if(length != 1)
                    {
                        prodType = argv[1];
                        if(prodType.equalsIgnoreCase("cab"))
                        {
                            dataType = argv[2];
                            if(dataType.equalsIgnoreCase("item"))
                            {
                                if(length == 9)
                                {
                                    dbSid = argv[3];
                                    username = argv[4];
                                    password = argv[5];
                                    inFile = argv[6];
                                    itemMediaOne = argv[7];
                                    itemMediaTwo = argv[8];
                                    skipMenus = true;
                                    argsOnCmdLn = true;
                                    itemOnly = 1;
                                }
                                else
                                {
                                    System.out.println("Error: Incorrect number or parameters supplied.");
                                    System.exit(1);
                                }
                            }
                            else if(dataType.equalsIgnoreCase("tutorial"))
                            {
                                if(length == 7)
                                {    
                                    dbSid = argv[3];
                                    username = argv[4];
                                    password = argv[5];
                                    inFile = argv[6];
                                    argsOnCmdLn = true;
                                    skipMenus = true;
                                    tutorial = 1;
                                }
                                else
                                {
                                    System.out.println("Error: Incorrect number or parameters supplied.");
                                    System.exit(1);
                                }
                            }
                            else if(dataType.equalsIgnoreCase("assessment") || dataType.equalsIgnoreCase("subtest"))
                            {
                                if(length == 10)
                                {
                                    dbSid = argv[3];
                                    username = argv[4];
                                    password = argv[5];
                                    inFile = argv[6];
                                    swfOne = argv[7];
                                    swfTwo = argv[8];
                                    pdfFile = argv[9];
                                    skipMenus = true;
                                    argsOnCmdLn = true;
                                    totalUpdate = 1;
                                }
                                else
                                {
                                    System.out.println("Error: Incorrect number or parameters supplied.");
                                    System.exit(1);
                                }
                            }
                            else
                            {
                                System.out.println("Error: Invalid Data Type.");
                                System.exit(1);
                            }
                        }
                        else if(prodType.equalsIgnoreCase("tabe"))
                        {
                            tabeContent = true;
                            dataType = argv[2];
                            if(dataType.equalsIgnoreCase("itemset") || dataType.equalsIgnoreCase("sampleset"))
                            {
                                if(length == 9)
                                {
                                    dbSid = argv[3];
                                    username = argv[4];
                                    password = argv[5];
                                    inFile = argv[6];
                                    swfOne = argv[7];
                                    swfTwo = argv[8];
                                    skipMenus = true;
                                    argsOnCmdLn = true;
                                    totalUpdate = 1;
                                }
                                else
                                {
                                    System.out.println("Error: Incorrect number or parameters supplied.");
                                    System.exit(1);
                                }
                            }
                            else if(dataType.equalsIgnoreCase("tutorial"))
                            {
                                if(length == 7)
                                {    
                                    dbSid = argv[3];
                                    username = argv[4];
                                    password = argv[5];
                                    inFile = argv[6];
                                    argsOnCmdLn = true;
                                    skipMenus = true;
                                    tabeTutorial = 1;
                                }
                                else
                                {
                                    System.out.println("Error: Incorrect number or parameters supplied.");
                                    System.exit(1);
                                }
                            }
                            else
                            {
                                System.out.println("Error: Invalid Data Type.");
                                System.exit(1);
                            }
                        }
                        else
                        {
                            System.out.println("Error: Invalid Product Type.");
                            System.exit(1);
                        }
                    }
                }
            }
            else if(source.equalsIgnoreCase("interwoven"))
            {
                //check for the right number of input parameters
                if(length != 5 && length != 7 && length != 8)
                {
                    System.out.println("1");
                    System.exit(1);
                }
                else
                {
                    interwoven = true;
                    suppressOutput = true;
                    prodType = argv[1];
                    if(prodType.equalsIgnoreCase("cab"))
                    {
                        dataType = argv[2];
                        if(dataType.equalsIgnoreCase("assessment") || dataType.equalsIgnoreCase("subtest"))
                        {
                            environment = argv[3];
                            if(environment.equalsIgnoreCase("test") || environment.equalsIgnoreCase("staging") || environment.equalsIgnoreCase("production"))
                            {
                                if(length == 8)
                                {
                                    inFile = argv[4];
                                    swfOne = argv[5];
                                    swfTwo = argv[6];
                                    pdfFile = argv[7];
                                    skipMenus = true;
                                    argsOnCmdLn = true;
                                    totalUpdate = 1;
                                }
                                else
                                {
                                    System.out.println("1");
                                    System.exit(1);
                                }
                            }
                            else
                            {
                                System.out.println("1");
                                System.exit(1);
                            }
                        }
                        else if(dataType.equalsIgnoreCase("item"))
                        {
                            environment = argv[3];
                            if(environment.equalsIgnoreCase("test") || environment.equalsIgnoreCase("staging") || environment.equalsIgnoreCase("production"))
                            {
                                if(length == 7)
                                {
                                    inFile = argv[4];
                                    itemMediaOne = argv[5];
                                    itemMediaTwo = argv[6];
                                    skipMenus = true;
                                    argsOnCmdLn = true;
                                    itemOnly = 1;
                                }
                            }
                            else
                            {
                                System.out.println("1");
                                System.exit(1);
                            }
                        }
                        else if(dataType.equalsIgnoreCase("tutorial"))
                        {
                            environment = argv[3];
                            if(environment.equalsIgnoreCase("test") || environment.equalsIgnoreCase("staging") || environment.equalsIgnoreCase("production"))
                            {                        
                                if(length == 5)
                                {
                                    inFile = argv[4];
                                    argsOnCmdLn = true;
                                    skipMenus = true;
                                    tutorial = 1;
                                }
                                else
                                {
                                    System.out.println("1");
                                    System.exit(1);
                                }
                            }
                            else
                            {
                                System.out.println("1");
                                System.exit(1);
                            }
                        }
                        else
                        {
                            System.out.println("1");
                            System.exit(1);
                        }
                    }
                    else if(prodType.equalsIgnoreCase("tabe"))
                    {
                        tabeContent = true;
                        dataType = argv[2];
                        if(dataType.equalsIgnoreCase("itemset") || dataType.equalsIgnoreCase("sampleset"))
                        {
                            environment = argv[3];
                            if(environment.equalsIgnoreCase("test") || environment.equalsIgnoreCase("staging") || environment.equalsIgnoreCase("production"))
                            {
                                if(length == 7)
                                {
                                    inFile = argv[4];
                                    swfOne = argv[5];
                                    swfTwo = argv[6];
                                    skipMenus = true;
                                    argsOnCmdLn = true;
                                    totalUpdate = 1;
                                }
                                else
                                {
                                    System.out.println("1");
                                    System.exit(1);
                                }
                            }
                            else
                            {
                                System.out.println("1");
                                System.exit(1);
                            }
                        }
                        else if(dataType.equalsIgnoreCase("tutorial"))
                        {
                            environment = argv[3];
                            if(environment.equalsIgnoreCase("test") || environment.equalsIgnoreCase("staging") || environment.equalsIgnoreCase("production"))
                            {
                                if(length == 5)
                                {
                                    inFile = argv[4];
                                    argsOnCmdLn = true;
                                    skipMenus = true;
                                    tabeTutorial = 1;
                                }
                            }
                            else
                            {
                                System.out.println("1");
                                System.exit(1);
                            }
                        }
                        else
                        {
                            System.out.println("1");
                            System.exit(1);
                        }
                    }
                    else
                    {
                        System.out.println("1");
                        System.exit(1);
                    }
                }
            }
            else
            {
                System.out.println("Error: Invalid Source.");
                System.exit(1);
            }
        }
    
        //evaluate the environment parameter
        if (!environment.equals(""))
        {
            if (environment.equalsIgnoreCase("staging"))
            {                
                //staging
                dbSid = "onad";
                username = "oas";
                password = "oas";
            }
            else if (environment.equalsIgnoreCase("test"))
            {
                //testing
                dbSid = "oasdev";
                username = "jtrundy";
                password = "jtrundy";
            }
            else if (environment.equalsIgnoreCase("production"))
            {
                
                //production
                dbSid = "onap";
                username = "oas";
                password = "oasprod";
                
            }
            else
            {
                if(interwoven)
                {
                    System.out.println("1");
                }
                else
                {
                    System.out.println("Error: Invalid Environment.");
                }
                System.exit(1);
            }
        }
        
        if(!suppressOutput)
        {
            System.out.println("\n--------------------------------------------------");
            System.out.println("\nContent Import Module V2.0\n");
            System.out.println("--------------------------------------------------\n");
        }
        
        if(!argsOnCmdLn && !skipMenus)
        {
            System.out.println("\nPlease select an environment to import into: ");
            System.out.println("1 -- Staging");
            System.out.println("2 -- Testing");
            System.out.println("7 -- Production");
            System.out.flush(); // empties buffer, before you input text
            message = stdin.readLine();
            if(message.equals("1"))
            {
                
                //staging
                dbSid = "onad";
                username = "oas";
                password = "oas";
            }
            else if(message.equals("2"))
            {
                //testing
                dbSid = "oasdev";
                username = "jtrundy";
                password = "jtrundy";
            }
            else if(message.equals("7"))
            {                
                //production
                dbSid = "onap";
                username = "oas";
                password = "oasprod";

                production = true;
            }
            else
            {
                if(interwoven)
                {
                    System.out.println("1");
                }
                else
                {
                    System.err.println("Error: Invalid Menu Selection.");
                }
                System.exit(1);
            }
        }
        if(!suppressOutput)
        {
            System.out.println("\nImporting into : "+username+"@"+dbSid);
        }
        if(!skipMenus)
        {
            System.out.println("\nPlease select an import mode: ");
            if(allAccess)
            {
                if(production)
                {
                    System.out.println("1 -- Import Standard Assessment.");
                    System.out.println("2 -- Import TABE Item Set.");                
                    System.out.println("3 -- Import Item.");
                    System.out.println("4 -- Import Standard Tutorial.");
                    System.out.println("5 -- Import TABE Tutorial.");
                }
                else
                {
                    System.out.println("1 -- Import Standard Assessment.");
                    System.out.println("2 -- Import TABE Item Set.");                
                    System.out.println("3 -- Import Test swf file for an Item Set.");
                    System.out.println("4 -- Import Answer Key swf file for an Item Set.");
                    System.out.println("5 -- Import Test pdf file for an Item Set.");
                    System.out.println("6 -- Import all media for an Item Set.");                
                    System.out.println("7 -- Import Item.");
                    System.out.println("8 -- Import Standard Tutorial.");
                    System.out.println("9 -- Import TABE Tutorial.");
                }
            }
            else
            {
            	System.out.println("1 -- Import TABE Item Set.");
                System.out.println("2 -- Import TABE Tutorial.");
            }
            System.out.flush(); // empties buffer, before you input text
            message = stdin.readLine();
            if(message.equals("1"))
            {
            	if(!allAccess)
                {
                    tabeContent = true;
                }

                totalUpdate = 1;
                System.out.println("\nPlease enter the path to the xml file: ");
            	System.out.flush(); // empties buffer, before you input text
            	inFile = stdin.readLine();
            }
            else if(message.equals("2"))
            {
                if(allAccess)
            	{
                    totalUpdate = 1;
                    tabeContent = true;
                    System.out.println("\nPlease enter the path to the xml file: ");
            	}
            	else
            	{
                    tabeTutorial = 1;
                    System.out.println("\nPlease enter the path to the tutorial: ");
            	}
            	System.out.flush(); // empties buffer, before you input text
            	inFile = stdin.readLine();
            }
            else if(message.equals("3"))
            {
                if(allAccess)
            	{
                    if(!production)
                    {
                        swfUpdate = 1;
                        testOnly = 1;                        
                    }
                    else
                    {
                        itemOnly = 1;
                    }
                    System.out.println("\nPlease enter the path to the xml file: ");
                    System.out.flush(); // empties buffer, before you input text
                    inFile = stdin.readLine();
                    
            	}
            	else
            	{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: Invalid menu selection.");
                    }
                    System.exit(1);
            	}
            }
            else if(message.equals("4"))
            {
                if(allAccess)
                {
                    if(!production)
                    {
                        swfUpdate = 1;
                        answerKeyOnly = 1;
                        System.out.println("\nPlease enter the path to the xml file: ");
                    }
                    else
                    {
                        tutorial = 1;
                        System.out.println("\nPlease enter the path to the tutorial: ");
                    }
                    
                    System.out.flush(); // empties buffer, before you input text
                    inFile = stdin.readLine();
                } 
                else
            	{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: Invalid menu selection.");
                    }
                    System.exit(1);
            	}   
            }
            else if(message.equals("5"))
            {
                if(allAccess)
                {
                    if(!production)
                    {
                        pdfOnly = 1;
                        System.out.println("\nPlease enter the path to the xml file: ");
                    }
                    else
                    {
                        tabeTutorial = 1;
                        System.out.println("\nPlease enter the path to the tutorial: ");
                    }
                    
                    System.out.flush(); // empties buffer, before you input text
                    inFile = stdin.readLine();
                } 
                else
            	{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: Invalid menu selection.");
                    }
                    System.exit(1);
            	}   
            }
            else if(message.equals("6"))
            {
                if(allAccess && !production)
                {
                    mediaOnly = 1;
                    System.out.println("\nPlease enter the path to the xml file: ");
                    System.out.flush(); // empties buffer, before you input text
                    inFile = stdin.readLine();
                }
                else
            	{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: Invalid menu selection.");
                    }
                    System.exit(1);
            	}   
            }
            else if(message.equals("7"))
            {
            	if(allAccess && !production)
                {
                    itemOnly = 1;
                    System.out.println("\nPlease enter the path to the xml file: ");
                    System.out.flush(); // empties buffer, before you input text
                    inFile = stdin.readLine();
                }
                else
            	{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: Invalid menu selection.");
                    }
                    System.exit(1);
            	}
            }
            else if(message.equals("8"))
            {
            	if(allAccess && !production)
                {
                    tutorial = 1;
                    System.out.println("\nPlease enter the path to the tutorial: ");
                    System.out.flush(); // empties buffer, before you input text
                    inFile = stdin.readLine();
                }
                else
            	{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: Invalid menu selection.");
                    }
                    System.exit(1);
            	}
            }
            else if(message.equals("9"))
            {
            	if(allAccess && !production)
            	{
                    tabeTutorial = 1;
                    System.out.println("\nPlease enter the path to the tutorial: ");
                    System.out.flush(); // empties buffer, before you input text
                    inFile = stdin.readLine();
            	}
            	else
            	{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: Invalid menu selection.");
                    }
                    System.exit(1);
            	}
            }
            else
            {
                if(interwoven)
                {
                    System.out.println("1");
                }
                else
                {
                    System.err.println("Error: Invalid menu selection.");
                }
            	System.exit(1);
            }
        }

        //if(multipleUpdate == 0)
        //{               
        f = new File(inFile);

        if (!f.exists())
        {
            if(interwoven)
            {
                System.out.println("1");
            }
            else
            {
                System.err.println("Error: File "+inFile+" does not exist!\n");
            }
            System.exit(1);
        }
        if (tabeTutorial == 0 && tutorial == 0 && !skipMenus)
        {
            startFilename = inFile.lastIndexOf("/");
            endFilename = inFile.lastIndexOf(".");
            if(startFilename > 0)
            {
                inPath = inFile.substring(0, startFilename+1);
            }
                
            coreFilename = inFile.substring(startFilename+5, endFilename);
                
            if(answerKeyOnly == 1)
            {
                swfOne = inPath+"ak_"+coreFilename+".swf";
            }
            else if(testOnly == 1)
            {
                swfOne = inPath+"ib_"+coreFilename+".swf";
            }
            else if(itemOnly == 1)
            {
                swfOne = inPath+"ib_"+coreFilename+".swf";
            }
            else if(pdfOnly == 1)
            {
                pdfFile = inPath+"ib_"+coreFilename+".pdf";
            }
            else if(totalUpdate == 1 || mediaOnly ==1)
            {
                swfOne = inPath+"ib_"+coreFilename+".swf";
                swfTwo = inPath+"ak_"+coreFilename+".swf";
                if(!tabeContent)
                {
                    pdfFile = inPath+"ib_"+coreFilename+".pdf";
                }
            }
            else
            {
                if(interwoven)
                {
                    System.out.println("1");
                }
                else
                {
                    System.err.println("Error: Import mode not set!\n");
                }
                System.exit(1);
            }
        }
        //}

        XML2DBImport nsd = new XML2DBImport();

        //if db connection was successful, continue
        if ( nsd.openDBConn (dbSid, username, password))
        {
            if ( tutorial == 1)
            {
                nsd.insertBlob(inFile, f, 3);
            }//if ( tutorial == 1)
            else if ( tabeTutorial == 1)
            {
                nsd.insertBlob(inFile, f, 4);
            }//if ( tabeTutorial == 1)
            else if (swfUpdate == 1 || mediaOnly == 1 || pdfOnly == 1)
            {
                if(nsd.getItemSetIdFromXML(inFile))
        	{
                    File f2;
                    if(pdfOnly == 0)
                    {
                        f2 = new File(swfOne);
                        if (!f2.exists())
                        {
                            if(interwoven)
                            {
                                System.out.println("1");
                            }
                            else
                            {
                                System.err.println("Error: File "+swfOne+" does not exist!\n");
                            }
                            System.exit(1);
                        }
                    }
                    else
                    {
                        f2 = new File(pdfFile);
                        if (!f2.exists())
                        {
                            if(interwoven)
                            {
                                System.out.println("1");
                            }
                            else
                            {
                                System.err.println("Error: File "+pdfFile+" does not exist!\n");
                            }
                            System.exit(1);
                        }
                    }
                    if ( testOnly == 1)
                    {
                        nsd.insertBlob(swfOne, f2, 1);
                    }
                    else if ( answerKeyOnly == 1)
                    {
                        nsd.insertBlob(swfOne, f2, 2);
                    }
                    else if ( pdfOnly == 1)
                    {
                        nsd.insertBlob(pdfFile, f2, 5);
                    }
                    else if (mediaOnly == 1)
                    {
                        File f3 = new File(swfTwo);
                        if (!f3.exists())
			{
                            if(interwoven)
                            {
                                System.out.println("1");
                            }
                            else
                            { 
                                System.err.println("Error: File "+swfTwo+" does not exist!\n");
                            }
                            System.exit(1);
                        }
                        File f4 = null;
                        if(!tabeContent)
                        {
                            f4 = new File(pdfFile);
                            if (!f4.exists())
                            {
                                if(interwoven)
                                {
                                    System.out.println("1");
                                }
                                else
                                {
                                    System.err.println("Error: File "+pdfFile+" does not exist!\n");
                                }
                                System.exit(1);
                            }
                        }
                        nsd.insertClob(inFile, f);
                        nsd.insertBlob(swfOne, f2, 1);
                        nsd.insertBlob(swfTwo, f3, 2);
                        if(!tabeContent)
                        {
                            nsd.insertBlob(pdfFile, f4, 5);
                        }
                    }
        	}//if(nsd.getItemSetIdFromXML(inFile))
        	else
		{
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: File "+inFile+" could not be processed!\n");
                    }
                    System.exit(1);
                }
            }//else if (swfUpdate == 1)
            else if(totalUpdate == 1)
            {
                if (nsd.processXML(inFile))
                {
                    nsd.insertClob(inFile, f);

                    File f2 = new File(swfOne);
                    if (!f2.exists())
                    {
                        if(interwoven)
                        {
                            System.out.println("1");
                        }
                        else
                        {
                            System.err.println("Error: File "+swfOne+" does not exist!\n");
                        }
                        System.exit(1);
                    }
                    else
                    {
                        nsd.insertBlob(swfOne, f2, 1);
                        File f3 = new File(swfTwo);
                        if (!f3.exists())
                        {
                            if(interwoven)
                            {
                                System.out.println("1");
                            }
                            else
                            {   
                                System.err.println("Error: File "+swfTwo+" does not exist!\n");
                            }
                                System.exit(1);
                        }
                        else
                        {
                            nsd.insertBlob(swfTwo, f3, 2);
                            if(!tabeContent)
                            {
                                File f4 = new File(pdfFile);
                                if (!f4.exists())
                                {
                                    if(interwoven)
                                    {
                                        System.out.println("1");
                                    }
                                    else
                                    {
                                        System.err.println("Error: File "+pdfFile+" does not exist!\n");
                                    }       
                                        System.exit(1);
                                }
                                else
                                {
                                    nsd.insertBlob(pdfFile, f4, 5);
                                }
                            }
                        }
                    }
                        
                }
                else
                {
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: File "+inFile+" could not be processed!\n");
                    }
                    System.exit(1);
		}
            }
            else if(itemOnly == 1)
            {
                if (nsd.processXML(inFile))
                {
                    nsd.insertClob(inFile, f);

                    File f2 = new File(itemMediaOne);
                    if (!f2.exists())
                    {
                        if(interwoven)
                        {
                            System.out.println("1");
                        }
                        else
                        {
                            System.err.println("Error: File "+itemMediaOne+" does not exist!\n");
                        }
                        System.exit(1);
                    }
                    File f3 = new File(itemMediaTwo);
                    if (!f3.exists())
                    {
                        if(interwoven)
                        {
                            System.out.println("1");
                        }
                        else
                        {
                            System.err.println("Error: File "+itemMediaTwo+" does not exist!\n");
                        }
                        System.exit(1);
                    }
                    else
                    {
                        if(!crItem)
                        {
                            nsd.insertBlob(itemMediaOne, f2, 1);
                            nsd.insertBlob(itemMediaTwo, f3, 5);
                        }
                        else
                        {
                            nsd.insertBlob(itemMediaOne, f2, 5);
                            nsd.insertBlob(itemMediaTwo, f3, 6);
                        }
                    }
                    
                }
                else
                {
                    if(interwoven)
                    {
                        System.out.println("1");
                    }
                    else
                    {
                        System.err.println("Error: File "+inFile+" could not be processed!\n");
                    }
                    System.exit(1);
		}
            }
            else
            {
                if(interwoven)
                {
                    System.out.println("1");
                }
                else
                {
                    System.err.println("Error: Import mode not set!\n");
                }
                System.exit(1);
            }
            
            nsd.updateReItemSetActivationStatus();

            nsd.finalize();
        }//if ( nsd.openDBConn )
        else
        {
            if(interwoven)
            {
                System.out.println("1");
            }
            else
            {
                System.err.println("Error: Connection to database not established!\n");
            }
            System.exit(1);
        }
        if(interwoven)
        {
            System.out.println("0");
        }
    }//public main


    private class ItemFilter implements NodeFilter
    {
        public short acceptNode(Node n)
        {
            if( n.hasChildNodes() || n.getNodeName().equals("Item"))
            {
                return FILTER_ACCEPT;
            }
            else
            {
                return FILTER_SKIP;
            }
        }
    }//private ItemFilter

}//public class XML2DBImport

