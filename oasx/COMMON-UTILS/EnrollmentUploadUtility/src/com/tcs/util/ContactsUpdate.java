package com.tcs.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ctb.bean.Contects;
import com.tcs.dataaccess.AbstractConnectionManager;
import com.tcs.dataaccess.ConnectionManager;
import com.tcs.dataaccess.UpdateContactHelper;

public class ContactsUpdate {
	private static final Logger logger = Logger
	.getLogger(NetworkUpdate.class);
	private String databaseProp;
	private String csvFile;
	private String year;
	private String marketType;
	private int rtsCustomerId;
	private Connection connection;
	private String sql=" merge into site_survey t using (select 1 from DUAL) s on (t.SITE_SURVEY_ID = ?)  when matched then"+
	" update set t.SITE_ID = ?, t.SITE_TYPE = ? , t.TEST_COORD_FIRST = ? , t.TEST_COORD_LAST = ?,"+
	 " t.TEST_COORD_EMAIL = ?, t.TEST_COORD_PHONE= ?, t.TECH_COORD_FIRST = ?, t.TECH_COORD_LAST = ?, t.TECH_COORD_EMAIL = ?,"+
	 " t.TECH_COORD_PHONE = ? when not matched then insert  (SITE_SURVEY_ID, SITE_ID, SITE_TYPE , TEST_COORD_FIRST , TEST_COORD_LAST,"+
	 " TEST_COORD_EMAIL, TEST_COORD_PHONE, TECH_COORD_FIRST , TECH_COORD_LAST , TECH_COORD_EMAIL , TECH_COORD_PHONE)"+
	 " values (?, ?, ? , ? , ?,?, ?, ? , ? , ? , ?)";

	public ContactsUpdate(String dbprop, String csv, String year,
			String marketType) {
		this.databaseProp = dbprop;
		this.csvFile = csv;
		this.year = year;
		this.marketType = marketType;

	}

	public ContactsUpdate() {
		// TODO Auto-generated constructor stub
	}

	public void processContactForOkhlahama() {
		try {
			AbstractConnectionManager.processProperties(databaseProp);
			this.rtsCustomerId = Integer.parseInt(AbstractConnectionManager.getCustomerId());
			
			this.connection = ConnectionManager.getConnection();
			connection.setAutoCommit(false);			
			PreparedStatement preparedStatement=connection.prepareStatement(sql);			 
			List list = CSVFileReader.getFileContent(csvFile);
			DataFormater dataFormater = new DataFormater();
			List<Contects> record = dataFormater.getValueMap(list);
			processRecords(record,preparedStatement);
			connection.commit();

		} catch (Exception e) {
			logger.error(e);
		}
		finally{
			
			try {
				connection.close();
			} catch (SQLException e) {				
				logger.error(e);
			}
		}
	}

	private void processRecords(List<Contects> record,PreparedStatement preparedStatement) {
		Integer siteSurveyId=0;
		String customerId=null;
		boolean isSubject=false;
		String tableName="";
		String customerType=null;
	
		Map<String, String> properties=null;
		for (Contects indvRecord : record) {
			try {
				String schoolId="";				
				String districtId="";
				districtId=indvRecord.getDistrictNumber().trim();
				if(!indvRecord.getSchoolNumber().equals(""))
					 schoolId=indvRecord.getSchoolNumber().trim();
				siteSurveyId=CSVFileReader.getSiteSurveyId(this.rtsCustomerId, schoolId ,districtId, connection);
				switch (siteSurveyId) {
				case 0:
					logger.info("Given Contact Update combination does not matches in DB :district id [" +indvRecord.getDistrictNumber()+"], school id ["+indvRecord.getSchoolNumber()+"]. Skipping processing of this row.");
					break;

				default:			
						
							UpdateContactHelper contactHelpher=new UpdateContactHelper(preparedStatement);
							contactHelpher.processEachRecordForOkhlahama(siteSurveyId, indvRecord, customerId);											
					break;//this is switch case break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
}// end of class
