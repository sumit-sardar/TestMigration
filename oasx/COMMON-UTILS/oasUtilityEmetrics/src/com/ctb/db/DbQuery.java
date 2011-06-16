package com.ctb.db;




import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ctb.dto.TestAndHierarchy;


//Referenced classes of package com.stgglobal.ads.common:
//AdsCommon, DbConnection, ExceptionHandler, XMLParsing

public class DbQuery
{

	DbConnection dbConnection;
	Connection adsConnection;
	Connection oasConnection;


	public DbQuery()
	throws Exception
	{
		dbConnection = null;
		adsConnection = null;
		dbConnection = new DbConnection();
		try
		{
			adsConnection = dbConnection.getConnection();
			oasConnection = dbConnection.getOasConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	

/*	public List getDummyData() throws Exception{
	
		ResultSetMetaData rsmd = null;
		List data = new ArrayList();
		Statement st = oasConnection.createStatement();
		ResultSet rs = st.executeQuery("Select * from sano_dummy"); 
		while (rs.next()) {
			String MODEL_LEVEL = rs.getString("MODEL_LEVEL");
			String ORGANIZATION_ID = rs.getString("ORGANIZATION_ID");
			String ELEMENT_NAME = rs.getString("ELEMENT_NAME");
			String ELEMENT_STRUCTURE_LEVEL = rs.getString("ELEMENT_STRUCTURE_LEVEL");
			data.add(MODEL_LEVEL + " " + ORGANIZATION_ID + " " + ELEMENT_NAME + " " + ELEMENT_STRUCTURE_LEVEL);
		}  
		st.close();
		rs.close();
		return data;

	}*/
	
	public List<TestAndHierarchy> getDummyData() throws Exception{
		
		ResultSetMetaData rsmd = null;
		List data = new ArrayList();
		Statement st = oasConnection.createStatement();
		ResultSet rs = st.executeQuery("Select * from sano_dummy");
		List<TestAndHierarchy> myList = new ArrayList<TestAndHierarchy>();
		
		while (rs.next()) {
			TestAndHierarchy th = new TestAndHierarchy();
			th.setModelLevel(rs.getString("MODEL_LEVEL"));
		    th.setOrganizationId(rs.getString("ORGANIZATION_ID"));
			th.setElementNameA(rs.getString("ELEMENT_NAME"));
			th.setElementStructureLevelA(rs.getString("ELEMENT_STRUCTURE_LEVEL"));
			th.setElementNumberA(rs.getString("ELEMENT_NUMBER"));
			th.setElementSpecialCodesA(rs.getString("ELEMENT_SPECIAL_CODES"));
			myList.add(th);
			
		}  
		
		st.close();
		rs.close();
		return myList;

	}


	public void cleanUpAdsConnection()
	{
		try
		{	
			if(adsConnection!= null && !adsConnection.isClosed())
			{
				adsConnection.rollback();
				adsConnection.close();
			}
		}
		catch(SQLException sqlexception) { }
	}
	public void cleanUpOasConnection()
	{
		try
		{	
			if(oasConnection!= null && !oasConnection.isClosed())
			{
				oasConnection.rollback();
				oasConnection.close();
			}
		}
		catch(SQLException sqlexception) { }
	}

/*	public String [] getItemId() throws Exception
	{
		Statement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData rsmd = null;

		try
		{
			statement = oasConnection.createStatement();
			resultSet = statement.executeQuery("select AA_ITEM_ID from AA_ITEM_DECRYPTED_DUPLICATE");
			rsmd = resultSet.getMetaData();

			int noOfColumns = rsmd.getColumnCount();
			String record [] = null ;
			System.out.println("NoOfColumns: "  +  noOfColumns);
			int i = 0;
			List<String> myList = new ArrayList<String>();
			while(resultSet.next()) { 
				myList.add(resultSet.getString(1));
			}



			record = (String[]) myList.toArray(new String[0]);
			System.out.println("MyList" + record.length);


			//	resultSet.getString(1);
			statement.close();
			resultSet.close();
			return record;

			//}

		}
		catch(Exception exception)
		{	
			System.err.println("Exception occured in getItemId()");
			exception.printStackTrace();
			statement.close();		
			resultSet.close();
			return null;
		}

	}


	public ItemData getItemXml(String itemId) throws Exception
	{
		Statement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData rsmd = null;
		ItemData item = new ItemData();
		Blob itemXmlBlob = null;

		try
		{
			statement = oasConnection.createStatement();
			resultSet = statement.executeQuery("select ITEM_RENDITION_XML as itemXml, CREATED_DATE_TIME as createdDateTime from AA_ITEM_DECRYPTED_DUPLICATE	where AA_ITEM_ID= '" + itemId + "'");



			while(resultSet.next()) { 

				itemXmlBlob = resultSet.getBlob(1);
				item.setCreatedDateTime(resultSet.getDate(2));
			}
			statement.close();
			resultSet.close();
			item.setItem(convertBlob(itemXmlBlob));

			return item;


		}
		catch(Exception exception)
		{	System.err.println("Exception occured in getItemXml()");
			exception.printStackTrace();
			statement.close();
			resultSet.close();
			return null;
			
		}

	}
*/

	

}


