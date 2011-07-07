package com.ctb.utils.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TerraNovaRSExport
{
    static Connection conn;
    private BufferedWriter fileOut = null;
    private File exportFile = null;
    private ArrayList productList;
    private HashMap customerList;
    private String fileName;
    private java.sql.Date endDate;

    public TerraNovaRSExport() {}
   
    public void getToday() throws Exception
    {
        Statement stmtGetEndDate = null;
        ResultSet rsGetEndDate = null;
        String getEndDate = "select sysdate from dual";
    	stmtGetEndDate = conn.createStatement();
        rsGetEndDate = stmtGetEndDate.executeQuery( getEndDate );
        if ( rsGetEndDate.next() )
            endDate = rsGetEndDate.getDate( 1 );
        else
            throw new RuntimeException( "Error: End Date Not Retrieved." );
        rsGetEndDate.close();
        stmtGetEndDate.close();
    }
    
    public boolean createReport() throws Exception
    {
    	boolean returnValue = true;
    	String endRec = "ER";
    	exportFile = new File( "./" + getOutFileName() );
		fileOut = new BufferedWriter( new FileWriter( exportFile ));
    	try
		{
    		Iterator it = customerList.keySet().iterator();
            while ( it.hasNext() )
            {
            	Integer keyName = ( Integer ) it.next();
                ArrayList value = (ArrayList) customerList.get( keyName );
                createExport( keyName.intValue(), value );
            }
            printRow(endRec + " " + dateToYYYYDDMM( endDate ));
		}
		finally
		{
			if ( fileOut != null )
	            fileOut.close();
		}
    	return returnValue;
    }
    
    public String getOutFileName()
    {
    	if ( fileName == null )
    		fileName = "Export.txt";
    	return fileName;
    }
    
    public boolean createExport(int _custId, ArrayList customerProductList )
                         throws Exception
    {
        boolean returnValue = false;

        Statement stmtGetCust = null;
        Statement stmtGetTest = null;

        ResultSet rsGetCust = null;
        ResultSet rsGetTest = null;

        String custRec = "CR";
        String custNm = "";

        String testRec = " TR";
        int testAdminId = 0;
        String testAdminNm = "";
        String form = "";
        String level = "";
        String grade = "";
        int tcItemSetId = 0;

        String getCust =
            "select customer_name from customer where customer_id = " +
            _custId;
        String getTest =
            "select ta.test_admin_id, ta.test_admin_name, its.item_set_form, " +
                        "its.item_set_level, its.item_set_id, its.grade, its.item_set_name " +
						"from test_admin ta, item_set its " + 
                        "where ta.customer_id = " + _custId +
                        " and ta.item_set_id = its.item_set_id and its.item_set_type = 'TC' " +
						"and exists ( select * from test_roster tr " +
						"where ta.test_admin_id = tr.test_admin_id )";
        int productSize = customerProductList.size();
        for ( int i = 0; i < productSize; i++ )
        {
        	if ( i == 0 )
        		getTest = getTest + " and ta.product_id in ( ";
        	getTest = getTest + ( Integer )customerProductList.get( i ) + ",";
        }
        if ( productSize > 0 )
        	getTest = getTest + "2) ";
        try
        {			
            stmtGetCust = conn.createStatement();
            rsGetCust = stmtGetCust.executeQuery(getCust);

            if (rsGetCust.next())
            {
                custNm = rsGetCust.getString(1);
                printRow(custRec + " " + _custId + "," + custNm);

                stmtGetTest = conn.createStatement();
                rsGetTest = stmtGetTest.executeQuery(getTest);

                while (rsGetTest.next())
                {
                    testAdminId = rsGetTest.getInt(1);
                    testAdminNm = rsGetTest.getString(2);
                    form = rsGetTest.getString(3);
                    if ( form == null )
                    	form = new String( "" );
                    level = rsGetTest.getString(4);
                    if ( level == null )
                    	level = new String( "" );
                    tcItemSetId = rsGetTest.getInt(5);
					grade = rsGetTest.getString(6);
					if ( grade == null || grade.equals( "0" ))
						grade = new String( "" );
					String testName = rsGetTest.getString(7);
                    printRow(testRec + " " + testAdminId + "," + testAdminNm +
                             "," + testName + "," + level + "," + form + "," + grade);
                    getStudentData(_custId, testAdminId, tcItemSetId);
                }
                returnValue = true;
            }
            else
            {
                throw new RuntimeException( "Error: Customer Data Not Retrieved." );
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        catch (IOException ie)
        {
            System.err.println(ie.getMessage());
            ie.printStackTrace();
        }
        finally
        {
            if (stmtGetCust != null)
            {
                stmtGetCust.close();
            }

            if (rsGetCust != null)
            {
                rsGetCust.close();
            }

            if (stmtGetTest != null)
            {
                stmtGetTest.close();
            }

            if (rsGetTest != null)
            {
                rsGetTest.close();
            }
        }
        return returnValue;
    }
	
    public String dateToYYYYDDMM( java.sql.Date date ) throws Exception
    {
    	String returnValue;
		returnValue = new SimpleDateFormat("yyyy-dd-MM").format( date );
		return returnValue;
    }
    
    public void processArgs( String[] argv ) throws Exception
	{
    	HashMap aHashMap = new HashMap();
    	for ( int i = 0; i < argv.length; i++ )
    	{
    		String param = argv[ i ];
    		processSingleArg( param, aHashMap );
    	}
    	Integer frameworkId = ( Integer )aHashMap.get( "framework_id" );
    	Integer productId = ( Integer )aHashMap.get( "product_id" );
    	if ( frameworkId != null && productId != null )
    		throw new RuntimeException( "Error argument: product_id and framework_id should not be used together." );
     	productList = new ArrayList();
    	customerList = new HashMap();
		if ( productId != null )
			productList.add( productId );
		else
		{
			if ( frameworkId != null )
			{
				getFrameworkProducts( frameworkId, productList );
			}
		}
		fileName = "EXP";
		Integer customerId = ( Integer )aHashMap.get( "customer_id" );
		if ( customerId != null )
		{
			customerList.put( customerId, productList );
			fileName = fileName + "_C" + customerId;
		}
		else if ( productList.size() == 0 )
		{
			throw new RuntimeException( "Error argument: framework_id is invalid." );
		}
		else
		{
			getProductCustomers( customerList, productList );
		}
		if ( frameworkId != null )
			fileName = fileName + "_F" + frameworkId;
		if ( productId != null )
			fileName = fileName + "_P" + productId;
	    getToday();
	    fileName = fileName + "_" + dateToYYYYDDMM( endDate ) + ".dat";
	}
    
    public void getFrameworkProducts( Integer frameworkId, ArrayList productList ) throws Exception
	{
    	String sql = "select product_id from product pd "
    				+ "where parent_product_id = " + frameworkId 
					+ "and activation_status = 'AC' ";
    	PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.prepareStatement( sql );
			rs = stmt.executeQuery();
			while ( rs.next() )
			{
				Integer productId = new Integer( rs.getInt( 1 )); 
				productList.add( productId );
			}
			rs.close();
		}
		catch ( SQLException e )
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
	}
    
    public void getProductCustomers( HashMap customerList, ArrayList productList ) throws Exception
	{
    	String sql = "select distinct customer_id from test_admin where customer_id > 2 "
			+ "and product_id in ( ";
    	for ( int i = 0; i < productList.size(); i++ )
    	{
    		sql = sql + ( Integer )productList.get( i ) + ",";
    	}
    	sql = sql + "1 )";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.prepareStatement( sql );
			rs = stmt.executeQuery();
			while ( rs.next() )
			{
				Integer customerId = new Integer( rs.getInt( 1 )); 
				customerList.put( customerId, productList );
			}
			rs.close();
		}
		catch ( SQLException e )
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
	}
    
    public boolean isValidArg( String key )
    {
    	boolean result = false;
    	if ( key.equalsIgnoreCase( "framework_id" ) 
    			|| key.equalsIgnoreCase( "customer_id" )
				|| key.equalsIgnoreCase( "product_id" ) )
    		result = true;
    	return result;
    }
    
    public void processSingleArg( String argv, HashMap aHashMap ) throws Exception
	{
    	int index = argv.indexOf( "=" );
    	if ( index > 0 )
    	{
    		String key = argv.substring( 0, index );
    		if ( !isValidArg( key ) )
    			throw new RuntimeException( "Error argument: " + key );
    		String value = argv.substring( index + 1 );
    		Integer intValue = Integer.valueOf( value );
    		aHashMap.put( key.toLowerCase(), intValue );
    	}
    	else
    	{
    		throw new RuntimeException( "Error argument: " + argv );
    	}
	}
    
    public static void showUsage()
    {
    	System.out.println("\n--------------------------------------------------");
        System.out.println("\nResearch Export Utility V1.0\n");
        System.out.println("--------------------------------------------------\n");
        System.out.println("-- Parameter List---------------------------------\n");
        System.out.println("\nframework_id=?       -- optional ");
        System.out.println("\ncustomer_id=?       -- optional");
        System.out.println("\nproduct_id=?       -- optional");
        System.out.println("\n\n*** At least one of the parameter should be specifed.");
        System.out.println("Please don't specify framework_id and product_id at the same time - don't make sense.");
        System.out.flush();
    }

    public void generateFriendlyMessage()
    {
    	System.out.println();
    	System.out.println( fileName + " file is generated." );
    }
    
    public static void main(String[] argv) throws Exception
    {
        if (argv.length == 0)
        {
        	TerraNovaRSExport.showUsage();
            System.exit(1);
        }

  /*      String osrHostname = "oasdb.eppg.com";
        String osrConnSid = "atspprod";
        String osrUser = "ats";
        String osrPasswd = "atsprod"; */
        String osrHostname = "nj09mhe0343-vip.edmz.mcgraw-hill.com";
        String osrConnSid = "oasr5p1";
        String osrUser = "oas";
        String osrPasswd = "oaspr5r";
 //       String customerId = "2458";
        // product_id = 2112
        TerraNovaRSExport aResearchExport = new TerraNovaRSExport();
        try
        {
            if ( aResearchExport.openDBconn(osrHostname, osrConnSid, osrUser, osrPasswd))
            {
            	aResearchExport.processArgs( argv );
                System.out.println("\nGenerating Export...");
                if ( !aResearchExport.createReport())
                    System.out.println("Error: Export not created.");
                else
                	aResearchExport.generateFriendlyMessage();
            }
        }
        catch ( SQLException se )
        {
            System.err.println(se.getMessage());
            se.printStackTrace();
        }
        catch ( IOException ioe )
        {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
    }

    public boolean openDBconn(String _hostname, String _connSid, String _user,
                              String _passwd)
    {
        String connURL = "jdbc:oracle:thin:@" + _hostname + ":1521:" +
                         _connSid;
        boolean returnValue = false;

        try
        {
            if (conn == null)
            {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                conn =
                        DriverManager.getConnection("jdbc:oracle:thin:@" +
                                                    _hostname + ":1521:" +
                                                    _connSid + "", _user,
                                                    _passwd);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        if (conn == null)
        {
            System.out.println("Connection to database failed with: " +
                               connURL + " and user: " + _user);
        }
        else
        {
            returnValue = true;
        }

        return returnValue;
    }

    private void getItemSetData(int _tcItemSetId, int _testRostId, int _valid)
                         throws Exception
    {
        String testSec = "   TS";
        int itemSetId = 0;
        String secNm = "";
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
        String completionStatus;
        String level;
        String form;

        String getItemSets = "select siss.item_set_id, decode( completion_status, 'CO', 'Completed', 'SC', 'Not taken', 'Incomplete'), its.item_set_display_name, its.item_set_level, its.item_set_form "
        					+ "from student_item_set_status siss, item_set its "
        					+ "where siss.test_roster_id = " + _testRostId
							+ " and siss.item_set_id = its.item_set_id "
							+ "and its.item_set_type = 'TD' and its.sample = 'F' order by siss.item_set_order";
        Statement stmtGetItemSets = null;
        ResultSet rsGetItemSets = null;

        String getItems =
                        "select isi.item_id, isi.suppressed from item_set_item isi, item where " +
                        "isi.item_id = item.item_id and isi.item_set_id = ? order by isi.item_sort_order asc";
        PreparedStatement psGetItems = null;
        ResultSet rsGetItems = null;

        String getScoredResponse =
                        "select it.item_type, ir.response, it.correct_answer, irp.points, " +
						"decode( irp.condition_code_id, 1003, 'A', 1004, 'B', 1005, 'C', null ) as Code " +
						"from item_response ir, item_response_points irp, item it " +
						"where ir.item_id = ? and ir.item_set_id = ? and ir.test_roster_id = ? " +
						"and ir.response_seq_num = (select max( ir1.response_seq_num) from item_response ir1 where ir1.item_id = ? " +
						"and ir1.item_set_id = ? and ir1.test_roster_id = ? ) and it.item_id = ir.item_id " +
						"and ir.item_response_id = irp.item_response_id ( + )";
        PreparedStatement psGetScoredResponse = null;
        ResultSet rsGetScoredResponse = null;

        String getSurveyResponse =
                        "select response from item_response where item_id = ? and " +
                        "item_set_id = ? and test_roster_id = ? and response_seq_num = (select " +
                        "max(response_seq_num) from item_response where item_id = ? and item_set_id = ? " +
                        "and test_roster_id = ?)";
        PreparedStatement psGetSurveyResponse = null;
        ResultSet rsGetSurveyResponse = null;

        try
        {
            psGetItems = conn.prepareStatement(getItems);

            psGetScoredResponse = conn.prepareStatement(getScoredResponse);

            psGetSurveyResponse = conn.prepareStatement(getSurveyResponse);

            stmtGetItemSets = conn.createStatement();
            rsGetItemSets = stmtGetItemSets.executeQuery(getItemSets);

            while (rsGetItemSets.next())
            {
                itemSetId = rsGetItemSets.getInt(1);
                completionStatus = rsGetItemSets.getString(2);
                secNm = rsGetItemSets.getString(3);
                level = rsGetItemSets.getString(4);
                form = rsGetItemSets.getString(5);
                rawScore = 0;
                respArray = "";
                scoreArray = "";
                psGetItems.setInt(1, itemSetId);
                rsGetItems = psGetItems.executeQuery();

                while (rsGetItems.next())
                {
                    correct = "0";
                    response = "";
                    itemId = rsGetItems.getString(1);
                    suppressed = rsGetItems.getString(2);
                    boolean srItemType = true;
                    if (suppressed.equals("T") || suppressed.equals("Y"))
                    {
                        suppress = 1;
                        psGetSurveyResponse.setString(1, itemId);
                        psGetSurveyResponse.setInt(2, itemSetId);
                        psGetSurveyResponse.setInt(3, _testRostId);
                        psGetSurveyResponse.setString(4, itemId);
                        psGetSurveyResponse.setInt(5, itemSetId);
                        psGetSurveyResponse.setInt(6, _testRostId);
                        rsGetSurveyResponse = psGetSurveyResponse.executeQuery();
                        if (rsGetSurveyResponse.next())
                        {
                            response = rsGetSurveyResponse.getString(1);
                            correct = "*";
                        }
                        else
                        {
                            response = " ";
                            correct = "*";
                        }
                    }
                    else
                    {
                        psGetScoredResponse.setString(1, itemId);
                        psGetScoredResponse.setInt(2, itemSetId);
                        psGetScoredResponse.setInt(3, _testRostId);
                        psGetScoredResponse.setString(4, itemId);
                        psGetScoredResponse.setInt(5, itemSetId);
                        psGetScoredResponse.setInt(6, _testRostId);
                        rsGetScoredResponse = psGetScoredResponse.executeQuery();
                        if (rsGetScoredResponse.next())
                        {// select item_type, response, correct_answer, points
                        	if ( rsGetScoredResponse.getString(1).equals( "SR" ) )
                        	{
	                            response = rsGetScoredResponse.getString( 2 );
	                            if ( response == null )
	                            	response = new String( " " );
	                            correct = rsGetScoredResponse.getString( 3 );	
	                            if (response.equals(correct))
	                            {
	                                correct = "1";
	                                rawScore++;
	                            }
	                            else
	                            {
	                                correct = "0";
	                            }
                        	}
                        	else
                        	{
                        		srItemType = false;
                        		String points = rsGetScoredResponse.getString( 4 );	
                        		if ( points != null )
                        		{
                        			rawScore += Integer.valueOf( points ).intValue();
                        			response = points;
                        			correct = points;
                        		}
                        		else
                        		{
                        			String CRCode = rsGetScoredResponse.getString( 5 );	
                        			if ( CRCode == null )
                        				CRCode = new String( " " );
                        			response = CRCode;
                        			correct = "0";
                        		}
                        	}
                        }
                        else
                        {
                            response = " ";
                        }
                    }
                    if ( !srItemType )
                    {
                    	// CR item, don't do anything because it can be condition code
                    }
                    else if (response.equals("-"))
                    {
                        response = " ";
                    }
                    else if (response.equals("A"))
                    {
                        response = "1";
                    }
                    else if (response.equals("B"))
                    {
                        response = "2";
                    }
                    else if (response.equals("C"))
                    {
                        response = "3";
                    }
                    else if (response.equals("D"))
                    {
                        response = "4";
                    }
                    else if (response.equals("E"))
                    {
                        response = "5";
                    }
                    respArray += response;
                    scoreArray += correct;
                }
                printRow(testSec + " " + itemSetId + "," + secNm + "," + level + "," + form + "," + completionStatus + "," + rawScore + "," + _valid + "," +
                         suppress);
                printRow(respAr + " " + respArray);
                printRow(scorAr + " " + scoreArray);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            if (stmtGetItemSets != null)
            {
                stmtGetItemSets.close();
            }

            if (rsGetItemSets != null)
            {
                rsGetItemSets.close();
            }

            if (psGetItems != null)
            {
                psGetItems.close();
            }

            if (rsGetItems != null)
            {
                rsGetItems.close();
            }

            if (psGetScoredResponse != null)
            {
                psGetScoredResponse.close();
            }

            if (rsGetScoredResponse != null)
            {
                rsGetScoredResponse.close();
            }

            if (psGetSurveyResponse != null)
            {
                psGetSurveyResponse.close();
            }

            if (rsGetSurveyResponse != null)
            {
                rsGetSurveyResponse.close();
            }
        }
    }

	
	private String getStudentDemoData( int studentId ) throws Exception
	{
		String sql = "select DATA_KEY, DATA_VALUE from STUDENT_RESEARCH_DATA " +
								"where STUDENT_ID = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		String key;
		String value;
		String key_value;
		try
		{
			stmt = conn.prepareStatement( sql);
			stmt.setInt( 1, studentId );
			rs = stmt.executeQuery();
			while ( rs.next() )
			{
				key = rs.getString( 1 ); 
				value = rs.getString( 2 ); 
				key_value = key + "=" + value;
				if ( result == null )
					result	= key_value;
				else
				{
					result = result + "," + key_value;				
				}
			}
			rs.close();
		}
		catch ( SQLException e )
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
		return result;
		
	}
	
	private String getStudentRearchDemo( String key_, int studentId )
	                                    throws Exception
	{
		String sql = "select DATA_VALUE from STUDENT_RESEARCH_DATA " +
	                        "where DATA_KEY = ? and STUDENT_ID = ?";
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
		String result = null;
		try
	    {
	        stmt = conn.prepareStatement( sql);
			stmt.setString( 1, key_ );
			stmt.setInt( 2, studentId );
	        rs = stmt.executeQuery();
	        if ( rs.next() )
	        {
	           result = rs.getString( 1 ); 
	        }
	        else
	        {
	            result = "";
	        }
			rs.close();
		}
		catch ( SQLException e )
	    {
	        System.err.println(e.getMessage());
	        e.printStackTrace();
	    }
	    finally
	    {
	        if (stmt != null)
	        {
	            stmt.close();
	        }
		}
		result = key_ + "=" + result;
		return result;
	}

	private String getStudentSchool( Integer studOrgNodeId ) throws Exception
	{
		String result = null;
		if ( studOrgNodeId == null )
			return result;
		String sql = "select orn.org_node_name from org_node orn, org_node_category onc " +
						"where orn.org_node_id in ( select ancestor_org_node_id from org_node_ancestor " +
						"where org_node_id = ? ) and orn.org_node_category_id = onc.org_node_category_id " +
						"and upper( onc.category_name ) = 'SCHOOL'";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.prepareStatement( sql );
			stmt.setInt( 1, studOrgNodeId.intValue() );
			rs = stmt.executeQuery();
			if ( rs.next() )
			{
			   result = rs.getString( 1 ); 
			}
			else
			{
				result = "";
			}
			rs.close();
		}
		catch ( SQLException e )
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
		return result;	
	}

	private String getStudentClass( Integer studOrgNodeId ) throws Exception
	{
		String sql = "select orn.org_node_name from org_node orn, org_node_category onc " +
						"where orn.org_node_id in ( select ancestor_org_node_id from org_node_ancestor " +
						"where org_node_id = ? ) and orn.org_node_category_id = onc.org_node_category_id " +
						"and upper( onc.category_name ) = 'CLASS'";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		try
		{
			stmt = conn.prepareStatement( sql );
			stmt.setInt( 1, studOrgNodeId.intValue() );
			rs = stmt.executeQuery();
			if ( rs.next() )
			{
			   result = rs.getString( 1 ); 
			}
			else
			{
				result = "";
			}
			rs.close();
		}
		catch ( SQLException e )
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
		return result;	
	}

	private String getStudentOrgs( int studentId ) throws Exception
	{
		String hierachySql = "select ogn.org_node_id from org_node ogn, org_node_student ons "
									+ ", org_node_category onc "
									+ "where ons.student_id = ? "
									+ "and ons.activation_status = 'AC' "
									+ "and ons.org_node_id = ogn.org_node_id "
									+ "and ogn.activation_status = 'AC' "
									+ "and ogn.org_node_category_id = onc.org_node_category_id "
									+ "and onc.category_level > 1 "
									+ "and onc.is_group = 'F' "
									+ "order by ons.created_date_time desc";
		ArrayList returnArray = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement( hierachySql );
		ps.setInt( 1, studentId );
		rs = ps.executeQuery();
		Integer orgNodeId = null;
		while ( rs.next() )
		{
			orgNodeId = new Integer( rs.getInt( 1 ) );
			returnArray.add( orgNodeId );
		}		
		rs.close();
		ps.close();
		String result = "";
		int i, size = returnArray.size();
		for ( i = 0; i < size; i++ )
		{
			if ( i > 0 )
				result = result + ";";	
			Integer orgNode = ( Integer )returnArray.get( i );
			result = result + getStudentSchool( orgNode ) + "::";
			result = result + getStudentClass( orgNode );
		}
		return result;
	}

    private void getStudentData(int _custId, int _testAdminId, int _tcItemSetId)
                         throws Exception
    {
        String studtRec = "  SR";
        int studentId = 0;
        String extStudentId = "";
        String lastNm = "";
        String firstNm = "";
        String middleNm = "";
        java.sql.Date birthdate = null;
        String gender = "";
        String ethnicity = "";
		String demo_data;
		String school;
		String studClass;
		String school_class;

        int valid = 0;

        String validStat = "";
        int testRostId = 0;

        String getTestRost =
                        "select student_id, test_roster_id, validation_status from " +
                        "test_roster where test_admin_id = " + _testAdminId;
        Statement stmtGetTestRost = null;
        ResultSet rsGetTestRost = null;

        String getStudent =
                        "select ext_pin1, last_name, first_name, middle_name, birthdate, " +
                        "gender, ethnicity from student where student_id = ?";
        PreparedStatement psGetStudent = null;
        ResultSet rsGetStudent = null;

        try
        {
            stmtGetTestRost = conn.createStatement();
            rsGetTestRost = stmtGetTestRost.executeQuery(getTestRost);
            psGetStudent = conn.prepareStatement(getStudent);
            while (rsGetTestRost.next())
            {
                studentId = rsGetTestRost.getInt(1);
                testRostId = rsGetTestRost.getInt(2);
                validStat = rsGetTestRost.getString(3);

                if (validStat.equals("IN"))
                {
                    valid = 1;
                }

                psGetStudent.setInt(1, studentId);
                rsGetStudent = psGetStudent.executeQuery();

                if (rsGetStudent.next())
                {
                    extStudentId = rsGetStudent.getString(1);
                    lastNm = rsGetStudent.getString(2);
                    firstNm = rsGetStudent.getString(3);
                    middleNm = rsGetStudent.getString(4);
                    birthdate = rsGetStudent.getDate(5);
                    gender = rsGetStudent.getString(6);
                    ethnicity = rsGetStudent.getString(7);
                }
                else
                {
                    // System.out.println("Error: Student Data Not Retrieved.");
                    // System.exit(1);
                	rsGetStudent.close();
                    rsGetStudent = null;
                	continue;
                }
                rsGetStudent.close();
                rsGetStudent = null;
                if (extStudentId == null)
                {
                    extStudentId = "";
                }
                if (middleNm == null)
                {
                    middleNm = "";
                }
                if (ethnicity == null)
                {
                    ethnicity = "";
                }		
				// school_class = getStudentOrgs( studentId );
                String studentHierarchyPath = getStudentHierachy( studentId );
				demo_data = getStudentDemoData( studentId );
				if ( demo_data == null )
					demo_data = "";
				else
					demo_data = "," + demo_data;
                printRow(	studtRec + " " + studentId + "," + extStudentId + "," +
                         	lastNm + "," + firstNm + "," + middleNm + "," +
							dateToYYYYDDMM( birthdate ) + "," + gender + "," + ethnicity +
                         	"," + studentHierarchyPath + demo_data );
                getItemSetData(_tcItemSetId, testRostId, valid);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            if (stmtGetTestRost != null)
            {
                stmtGetTestRost.close();
            }

            if (rsGetTestRost != null)
            {
                rsGetTestRost.close();
            }

            if (psGetStudent != null)
            {
                psGetStudent.close();
            }

            if (rsGetStudent != null)
            {
                rsGetStudent.close();
            }
        }
    }

    public String getStudentHierachy( int student_id ) throws Exception
    {
        String result = "";
		String studentAtSql = "select ond.org_node_name from org_node_ancestor ona, org_node ond, "
								+ "org_node_category onc where ona.org_node_id = ? "
								+ "and ona.ancestor_org_node_id = ond.org_node_id "
								+ "and ond.org_node_category_id = onc.org_node_category_id "
								+ "and onc.category_level > 1 order by ona.number_of_levels desc";
								
        String hierachySql = "select ogn.org_node_id from org_node ogn, org_node_student ons "
        					+ ", org_node_category onc "
        					+ "where ons.student_id = ? "
        					+ "and ons.activation_status = 'AC' "
        					+ "and ons.org_node_id = ogn.org_node_id "
        					+ "and ogn.activation_status = 'AC' "
        					+ "and ogn.org_node_category_id = onc.org_node_category_id "
        					+ "and onc.category_level > 1 "
        					+ "and onc.is_group = 'F' "
        					+ "order by ons.created_date_time desc";
        PreparedStatement ps = null;
        ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		ps1 = conn.prepareStatement( hierachySql );
		ps1.setInt( 1, student_id );
		rs1 = ps1.executeQuery();
		Integer orgNodeId = null;
		if ( rs1.next() )
			orgNodeId = new Integer( rs1.getInt( 1 ) );
		rs1.close();
		ps1.close();
		if ( orgNodeId != null )
		{
	        ps = conn.prepareStatement( studentAtSql );
	        ps.setInt( 1, orgNodeId.intValue() );
	        rs = ps.executeQuery();
	        while ( rs.next() )
	        {
	            if ( result.length() > 0 )
	                result = result + "::";
	            result = result + rs.getString( 1 );
	        }
	        rs.close();
	        ps.close();
		}
        return result;
    }
    
    private void printRow(String _data)
    {
        try
        {
        	System.out.println(_data);
            fileOut.write(_data);
            fileOut.newLine();
            fileOut.flush();
        }
        catch (IOException ie)
        {
            System.err.println(ie.getMessage());
            ie.printStackTrace();
        }
    }
}
